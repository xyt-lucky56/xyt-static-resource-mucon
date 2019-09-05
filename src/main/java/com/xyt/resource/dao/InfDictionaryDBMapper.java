package com.xyt.resource.dao;

import com.xyt.rescource.model.InfDictionary;
import com.xyt.rescource.model.MongodbTestModel;
import model.CriterialFilter;
import model.MongodbSelect;
import myenum.CriteriaOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import tool.ChangeAction;
import tool.SelectAction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class InfDictionaryDBMapper {
    @Autowired
    private MongoTemplate mongoTemplate;
    private SelectAction selectAction;
    private ChangeAction changeAction;
    private  String collectName;
    public void setCollectName(String collectName) {
        this.collectName = collectName;
    }
    public InfDictionaryDBMapper(){
        super();
        setCollectName("inf_dictionary_collect");
        selectAction = new SelectAction(collectName);
        changeAction = new ChangeAction (collectName);
    }

    public List<InfDictionary> getListOfInfDictionaryBySignName(String signName) throws ClassNotFoundException, ParseException {
        selectAction.setMongoTemplate (mongoTemplate);//设置db模板
        MongodbSelect mongodbSelect =new MongodbSelect (InfDictionary.class);//查询初始化
        CriterialFilter criterialFilter = new CriterialFilter();
        criterialFilter.setCriteriaOperate(CriteriaOperate.IS);
        criterialFilter.setFieldName("signName");
        criterialFilter.setFieldValue(signName);
        mongodbSelect.setCollectName(this.collectName);
        mongodbSelect.setDefaultPageParam(false);//不分页
        List<CriterialFilter> criteriaFilters = new ArrayList<> ();
        criteriaFilters.add (criterialFilter);
        mongodbSelect.setCriterialFilterList (criteriaFilters);
        List<InfDictionary> mongodbListNew = selectAction.getMongodbList(InfDictionary.class, mongodbSelect);
        return mongodbListNew;
    }

    public  void saveInfDictionary(List<InfDictionary> list){
        //mongoTemplate.insert (list, collectName);

        for (InfDictionary infDictionary : list) {
            saveInfDictionary(infDictionary);
        }
    }

    public void saveInfDictionary(InfDictionary inf){
        try {
            Thread.sleep (2000);
            System.out.println("------当前线程休眠2秒------");
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        changeAction.setMongoTemplate (mongoTemplate);
        changeAction.savaData (inf, collectName);
    }

    public int  deleteInfDictionary (String _id) throws ParseException {
        changeAction.setMongoTemplate (mongoTemplate);
        //changeAction.deleteData (, );
        CriterialFilter criterialFilter = new CriterialFilter();
        criterialFilter.setCriteriaOperate(CriteriaOperate.IS);
        criterialFilter.setFieldName("_id");
        criterialFilter.setFieldValue(_id);
        List<CriterialFilter> criteriaFilters = new ArrayList<> ();
        criteriaFilters.add (criterialFilter);
      return  changeAction.deleteDataByCondition (criteriaFilters, collectName);
    }

    public int updateInfDictionary(InfDictionary inf) throws ParseException {
        changeAction.setMongoTemplate (mongoTemplate);
        CriterialFilter criterialFilter = new CriterialFilter();
        criterialFilter.setCriteriaOperate(CriteriaOperate.IS);
        criterialFilter.setFieldName("_id");
        criterialFilter.setFieldValue(inf.get_id ());
        List<CriterialFilter> criterialFilterList = new ArrayList<> ();
        criterialFilterList.add (criterialFilter);
        List<CriterialFilter> keyAndValueList=new ArrayList <> ();//列值
        CriterialFilter  valueFilter =new CriterialFilter ();
        valueFilter.setFieldName ("contentName");
        valueFilter.setFieldValue (inf.getContentName ());
        CriterialFilter  valueFilter2 =new CriterialFilter ();
        valueFilter2.setFieldName ("signName");
        valueFilter2.setFieldValue (inf.getSignName ());
        CriterialFilter  valueFilter3 =new CriterialFilter ();
        valueFilter3.setFieldName ("remark");
        valueFilter3.setFieldValue (inf.getRemark ());
        keyAndValueList.add (valueFilter);
        keyAndValueList.add (valueFilter2);
        keyAndValueList.add (valueFilter3);
        return  changeAction.updateFirst (criterialFilterList, keyAndValueList, collectName);
    }




}
