package com.xyt.resource.dao;

import com.xyt.rescource.model.MongodbTestModel;
import model.CriterialFilter;
import model.MongodbSelect;
import model.SortModel;
import myenum.CriteriaOperate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import tool.ChangeAction;
import tool.SelectAction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MongoDBUsedTemplate {
  @Autowired
  private MongoTemplate mongoTemplate;
  private SelectAction selectAction;
  private ChangeAction changeAction;
  private  String collectName;
  public void setCollectName(String collectName) {
    this.collectName = collectName;
  }
  public MongoDBUsedTemplate(){
    super();
    setCollectName("test_Collect");
    selectAction = new SelectAction(collectName);
    changeAction = new ChangeAction (collectName);
  }

  public  List<MongodbTestModel> getResouceList(Integer pageNum,Integer pageSize) throws ClassNotFoundException, ParseException {

        selectAction.setMongoTemplate (mongoTemplate);//设置db模板
        MongodbSelect mongodbSelect =new MongodbSelect (MongodbTestModel.class);//查询初始化
        mongodbSelect.setPageNumber(pageNum);//设置分页
        mongodbSelect.setPageSize(pageSize);
        List<SortModel> sortModels = new ArrayList<> ();//设置排序,支持多字段
        SortModel sortModel = new SortModel();
        sortModel.setFieldName("createData");
        sortModel.setAscSign(false);//设置排序规则<升、降序>
        sortModels.add(sortModel);
        mongodbSelect.setSortModels(sortModels);
        mongodbSelect.setCollectName(this.collectName);
        mongodbSelect.setDefaultPageParam(true);

        CriterialFilter criterialFilter = new CriterialFilter();
        criterialFilter.setCriteriaOperate(CriteriaOperate.IS);
        criterialFilter.setFieldName("stopSign");
        criterialFilter.setFieldValue(true);

        CriterialFilter criterialFilter2 = new CriterialFilter();
        criterialFilter2.setCriteriaOperate(CriteriaOperate.GTE);
        criterialFilter2.setFieldName("createData");
        String startDate = "2019-07-08 09:54:10";
        criterialFilter2.setFieldValue(startDate);

        CriterialFilter criterialFilter3 = new CriterialFilter();
        criterialFilter3.setCriteriaOperate(CriteriaOperate.LLIKE);
        criterialFilter3.setFieldName("testContent");
        criterialFilter3.setFieldValue("testContent");

        CriterialFilter criterialFilter4 = new CriterialFilter();
        criterialFilter4.setCriteriaOperate(CriteriaOperate.MOD);
        criterialFilter4.setFieldName("sortNo");//除以3，余数为1
        criterialFilter4.setFieldValue(3);
        criterialFilter4.setFieldValueEnd(1);

        List<CriterialFilter> criteriaFilters = new ArrayList<>();
        criteriaFilters.add(criterialFilter);
    //        criteriaFilters.add(criterialFilter2);
    //        criteriaFilters.add(criterialFilter3);
        criteriaFilters.add(criterialFilter4);
        //设置条件过滤、setCriterialFilterList并，setOrCriterialFilterList或
        mongodbSelect.setCriterialFilterList(criteriaFilters);//and
        mongodbSelect.setOrCriterialFilterList (criteriaFilters);//or
        List<MongodbTestModel> mongodbListNew = selectAction.getMongodbList(MongodbTestModel.class, mongodbSelect);


    return  mongodbListNew;
  }


  public void  save(MongodbTestModel mongodbTestModel){
    changeAction.setMongoTemplate (mongoTemplate);
    changeAction.saveData (mongodbTestModel);
  }

  public int update() throws ParseException {
    changeAction.setMongoTemplate (mongoTemplate);
    //方式1
    //changeAction.saveData (mongodbTestModel);
    //List<CriterialFilter> criterialFilterList, List<CriterialFilter> keyAndValueList,
    List<CriterialFilter> criterialFilterList=new ArrayList <> ();
    CriterialFilter criterialFilter =new CriterialFilter ();
    criterialFilter.setCriteriaOperate (CriteriaOperate.IS);
    criterialFilter.setFieldName ("sortNo");
    criterialFilter.setFieldValue (2);
    criterialFilterList.add (criterialFilter);
    List<CriterialFilter> keyAndValueList=new ArrayList <> ();//列值
    CriterialFilter  valueFilter =new CriterialFilter ();
    valueFilter.setFieldName ("testContent");
    valueFilter.setFieldValue ("测试222222222");
    CriterialFilter  valueFilter2 =new CriterialFilter ();
    valueFilter2.setFieldName ("testOrContent");
    valueFilter2.setFieldValue ("测试s");
    keyAndValueList.add (valueFilter);
    keyAndValueList.add (valueFilter2);
    //方式2
    int size=changeAction.updateFirst (criterialFilterList, keyAndValueList, collectName);
    System.out.println(size);
    return size ;
  }

public int  delete(MongodbTestModel mongodbTestModel){
  changeAction.setMongoTemplate (mongoTemplate);
 // int size=changeAction.deleteData (mongodbTestModel);
  int size=changeAction.deleteData (mongodbTestModel, collectName);

 return size;
}

public  MongodbTestModel getOne(){
  //selectAction.setMongoTemplate (mongoTemplate);
  Query query=new Query (Criteria.where ("sortNo").is (1));
  MongodbTestModel model= mongoTemplate.findOne (query, MongodbTestModel.class, collectName);
  return model;
}

}
