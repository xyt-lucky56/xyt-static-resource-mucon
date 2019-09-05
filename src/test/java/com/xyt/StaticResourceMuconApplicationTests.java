package com.xyt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
//由于是web项目,Junit需要模拟SerletContext,因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class StaticResourceMuconApplicationTests {

    @Test
    public void contextLoads() {
    }
    @Before
    public void  init(){
        System.out.println("-------------开始测试----------");

    }
    @After
    public void after(){
        System.out.println("--------结束测试----------");

    }
}
