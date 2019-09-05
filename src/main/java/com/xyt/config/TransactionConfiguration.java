/*
package com.xyt.config;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;


@Configuration
public class TransactionConfiguration {
															
    public static final String AOP_POINTCUT_EXPRESSION  = "execution(* com.xyt.resource.**.service.impl.*ServiceImpl.*(..))";
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    */
/**
     * 获取事务配置
     * 
     * @return
     *//*

    public static Properties getAttrubites() {
        Properties attributes = new Properties();
        
        //查询
        attributes.setProperty("get*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("query*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("find*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("select*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("load*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        attributes.setProperty("search*", "PROPAGATION_REQUIRED,-Throwable,readOnly");
        
        //添加
        attributes.setProperty("add*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("save*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("create*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("deploy*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("start*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("complete*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("convert*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("commit*", "PROPAGATION_REQUIRED,-Exception");

        //更新
        attributes.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("modify*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("complete*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("audit*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("refuse*", "PROPAGATION_REQUIRED,-Exception");
        
        //删除
        attributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
        attributes.setProperty("remove*", "PROPAGATION_REQUIRED,-Exception");
        
        
        //新事务
        //attributes.setProperty("save*Log*", "PROPAGATION_REQUIRES_NEW");
        
        //非事务
        attributes.setProperty("*WithoutTx", "PROPAGATION_NOT_SUPPORTED");
        attributes.setProperty("*NonTx", "PROPAGATION_NOT_SUPPORTED");
        
        //attributes.setProperty("*", "PROPAGATION_REQUIRED,-Exception");
        return attributes;

    }

    @Bean
    public TransactionInterceptor transactionInterceptor() {
    	 // 事物拦截器
        TransactionInterceptor txInterceptor = new TransactionInterceptor();
        txInterceptor.setTransactionManager(platformTransactionManager);
        txInterceptor.setTransactionAttributes(getAttrubites());
        
        return txInterceptor;
    }
    
    @Bean
    public DefaultPointcutAdvisor  defaultPointcutAdvisor(TransactionInterceptor ti){
    	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, ti);
    }
}*/
