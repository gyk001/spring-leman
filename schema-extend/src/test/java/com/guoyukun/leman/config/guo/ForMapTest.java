package com.guoyukun.leman.config.guo;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by guoyukun on 2016/3/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring-guo-for-entry.xml"})
public class ForMapTest {
private static final Logger LOG = LoggerFactory.getLogger(ForMapTest.class);
    @Resource
    private ApplicationContext ctx;

    @Test
    public void testFor(){
        Map o = ctx.getBean("indenpentMap", Map.class);
        LOG.info(">> {}", ToStringBuilder.reflectionToString(o));
        MapUtils.debugPrint(System.out, null, o);
        LOG.info("key1 {}", o.get("k-1"));
//
//        Object inner2 =ctx.getBean("SymbolMap-2");
//        Object inner3 =ctx.getBean("SymbolMap-3");
//        Object inner22 =ctx.getBean("SymbolMap-2");
//        Object inner33 =ctx.getBean("SymbolMap-3");
//
//        LOG.info(">> {}", inner22);
//        LOG.info(">> {}", inner33);
//        LOG.info(">> {}", inner2);
//        LOG.info(">> {}", inner3);
    }


}
