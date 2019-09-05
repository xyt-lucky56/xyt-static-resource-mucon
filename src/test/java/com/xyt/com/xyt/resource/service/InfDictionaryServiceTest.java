package com.xyt.com.xyt.resource.service;

import com.xyt.StaticResourceMuconApplicationTests;
import com.xyt.rescource.model.InfDictionary;
import com.xyt.resource.service.InfDictionaryService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

public class InfDictionaryServiceTest extends StaticResourceMuconApplicationTests {
    @Autowired
    private InfDictionaryService infDictionaryService;
    @Test
    public void  getListOfInfDictionaryTest(){
        try {
            List<InfDictionary> list =infDictionaryService.getListOfInfDictionary ("AAB");
            if(list !=null && list.size ()>0){
                System.out.println("---------------查到数据---------------");
            }else{
                System.out.println("-------没有数据-------");

            }

        } catch (ParseException e) {
            e.printStackTrace ();
        } catch (ClassNotFoundException e) {
            e.printStackTrace ();
        }


    }
    @Test
   public void saveInfDictionaryTest(){
        InfDictionary infDictionary= new  InfDictionary();
        infDictionary.set_id ("7");
        infDictionary.setContentName ("运输中");
        infDictionary.setSignName ("AAG");
        infDictionary.setSortNo (7);
        infDictionary.setRemark ("订单类型");
        infDictionary.setDefaultSelect (0);
        infDictionary.setStopSign (1);//不停用
        int size=infDictionaryService.saveInfDictionary (infDictionary);
        if(size>0){
            System.out.println("---------------保存成功---------------");
        }else{
            System.out.println("---------------保存失败---------------");
        }

   }


}
