package com.xyt.resource.service.impl;

import com.xyt.rescource.model.InfDictionary;
import com.xyt.resource.dao.InfDictionaryDBMapper;
import com.xyt.resource.dao.InfDictionaryMapper;
import com.xyt.resource.service.InfDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Comparator;
import java.util.List;

/**
 * @author ：xxj
 * @create 2019-08-26 09:08
 * @function
 * @editLog
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class InfDictionaryServiceImpl implements InfDictionaryService {
    @Autowired
    InfDictionaryMapper infDictionaryMapper;
    @Autowired
    InfDictionaryDBMapper infDictionaryDBMapper;
   private static final Logger logger = LoggerFactory.getLogger (InfDictionaryServiceImpl.class);
    /**
     * 根据标识查询主数据
     *
     * @param signName 标识
     * @return 主数据集合
     */
    @Override
    public List<InfDictionary> getListOfInfDictionary(String signName) throws ParseException, ClassNotFoundException {

       List<InfDictionary> infDictionaries= infDictionaryDBMapper.getListOfInfDictionaryBySignName (signName);
       if(infDictionaries !=null && infDictionaries.size ()>0){
           return infDictionaries;
       }else {
          infDictionaries =infDictionaryMapper.getListOfInfDictionary(signName);
          if(infDictionaries !=null && infDictionaries.size ()>0){
              //说明数据库有该数据,另开启一个线程同步数据至MongoDB
              logger.info ("说明数据库有该数据,另开启一个线程同步数据至MongoDB");
              List <InfDictionary> finalInfDictionaries = infDictionaries;
              new Thread (() -> infDictionaryDBMapper.saveInfDictionary (finalInfDictionaries)).start ();

             return infDictionaries;
          }

       }
        return null;
    }


   public int  saveInfDictionary(InfDictionary infDictionary){
       InfDictionary inf2  = getInfDictionaryByJointCondition(infDictionary);
       int size=0;
       if(inf2 ==null){
           size=infDictionaryMapper.saveInfDictionary (infDictionary);
           //new Thread (() -> infDictionaryDBMapper.saveInfDictionary (infDictionary)).start ();
           new Thread ( () -> infDictionaryDBMapper.saveInfDictionary (infDictionary)).start ();
                   logger.info ("同步数据成功");

       }else{
           logger.info ("该数据已添加");

       }

       return  size;

   }

   private  InfDictionary getInfDictionaryByJointCondition(InfDictionary infDictionary){

        return infDictionaryMapper.selInfDictionaryByJointCondition (infDictionary.getSignName (), infDictionary.getContentName ());
   }


    @Override
    public int deleteInfDitionary(String _id) {
       int size=  infDictionaryMapper.updateInfDictionaryToDisable (_id, 1);
        new Thread (()-> {
            try {
                infDictionaryDBMapper.deleteInfDictionary (_id);
            } catch (ParseException e) {
                logger.info ("MongoDB数据删除异常");
                logger.info (e.getMessage ());
                e.printStackTrace ();
            }
        }).start ();

        return size;
    }

    @Override
    public int updateInfDitionary(InfDictionary infDictionary) {
        int size=0;
        if(getInfDictionaryByJointCondition(infDictionary)==null){
             size= infDictionaryMapper.updateInfDictionaryByPrimaryKey (infDictionary);
            new Thread (()-> {
                try {
                    infDictionaryDBMapper.updateInfDictionary (infDictionary);
                } catch (ParseException e) {
                    logger.info ("MongoDB数据异常");
                    logger.info (e.getMessage ());
                    e.printStackTrace ();
                }
            });

        }else{
            logger.info ("该数据已添加");

        }
        return size;
    }

}
