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
@ContextConfiguration(locations = {"classpath:/spring-guo-for.xml"})
public class ForTest {
private static final Logger LOG = LoggerFactory.getLogger(ForTest.class);
    @Resource
    private ApplicationContext ctx;

    @Test
    public void testFor(){
        Object o =ctx.getBean("xxxww");
        LOG.info(">> {}", o);

        Object inner2 =ctx.getBean("SymbolMap-2");
        Object inner3 =ctx.getBean("SymbolMap-3");
        Object inner22 =ctx.getBean("SymbolMap-2");
        Object inner33 =ctx.getBean("SymbolMap-3");

        LOG.info(">> {}", inner22);
        LOG.info(">> {}", inner33);
        LOG.info(">> {}", inner2);
        LOG.info(">> {}", inner3);
    }


}
