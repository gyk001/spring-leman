package com.guoyukun.leman.config.guo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by guoyukun on 2016/3/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-guo-cond.xml"})
public class CondTest {
private static final Logger LOG = LoggerFactory.getLogger(CondTest.class);
    @Resource
    private ApplicationContext ctx;

    @Test
    public void testFor(){
        ctx.getBean("testOk");
    }


}
