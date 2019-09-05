package com.xyt.com.xyt.resource.dao;

import com.xyt.StaticResourceMuconApplicationTests;
import com.xyt.rescource.model.MongodbTestModel;
import com.xyt.resource.dao.MongoDBUsedTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

public class MongoDBUsedTemplateTest extends StaticResourceMuconApplicationTests {
    @Autowired
    private MongoDBUsedTemplate mongoDBUsedTemplate;
    @Test
    public void  Test() throws ParseException, ClassNotFoundException {
          List<MongodbTestModel> list =mongoDBUsedTemplate.getResouceList (1, 10);
          if(list !=null){
              System.out.println(list.size ());
          }

    }
    @Test
    public void saveTest(){
        MongodbTestModel mongodbTestModel=new MongodbTestModel ();
        mongodbTestModel.setSortNo (51);
        mongodbTestModel.setTestContent ("测试db插入");
        mongoDBUsedTemplate.save (mongodbTestModel);


    }
   @Test
   public void updateTest() throws ParseException {
       int size=mongoDBUsedTemplate.update ();
          if(size>0){
              System.out.println("----------更新成功-----------");
          }else{
              System.out.println("----------更新失败-----------");
          }


  }
  @Test
  public  void deleteTest(){
      MongodbTestModel mongodbTestModel=new MongodbTestModel ();
      mongodbTestModel.set_id ("5d5f9ab696642b19b4bb652e");
      mongodbTestModel.setSortNo (51);
      mongodbTestModel.setTestContent ("测试db插入");
     int size= mongoDBUsedTemplate.delete (mongodbTestModel);
        if(size>0){
            System.out.println("----------删除成功-----------");
        }

  }
  @Test
  public void getOneTest(){
      MongodbTestModel mongodbTestModel= mongoDBUsedTemplate.getOne ();
      System.out.println("----------sortNo-------------:"+mongodbTestModel.getSortNo ());

  }

}
