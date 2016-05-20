package com.guoyukun.leman.config.guo;


import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParserDelegate;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

public class LemanGuoCondBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * Default placeholder prefix: "${"
     */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /**
     * Default placeholder suffix: "}"
     */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /**
     * Parse the "cond" element and check the mandatory "test" attribute. If
     * the system property named by test is null or empty (i.e. not defined)
     * then return null, which is the same as not defining the bean.
     */
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String ok = parserContext.getDelegate().getEnvironment().getProperty("ok");
        BeanDefinition bd = parserContext.getRegistry().getBeanDefinition("props");
        ;
        System.out.println(">>>>>>>>>>>>>>"+ bd.getAttribute("locations"));
        if (DomUtils.nodeNameEquals(element, "cond")) {
            String test = element.getAttribute("test");
            if (!StringUtils.hasText(getProperty(test)) ) {
                Element beanElement = DomUtils.getChildElementByTagName(element, "bean");
                return parseAndRegisterBean(beanElement, parserContext);
            }
        }

        return null;
    }

    /**
     * Get the value of a named system property (it may not be defined).
     *
     * @param strVal The name of a system property. The property may
     *               optionally be surrounded in Ant/EL-style brackets. e.g. "${propertyname}"
     * @return
     */
    private String getProperty(String strVal) {
        if (StringUtils.hasText(strVal)) {
            return null;
        }
        if (strVal.startsWith(DEFAULT_PLACEHOLDER_PREFIX)) {
            if (strVal.endsWith(DEFAULT_PLACEHOLDER_SUFFIX)) {
                return System.getProperty(
                        strVal.substring(DEFAULT_PLACEHOLDER_PREFIX.length(),
                                strVal.length() - DEFAULT_PLACEHOLDER_SUFFIX.length()));
            }
        }
        return System.getProperty(strVal);
    }

    private BeanDefinition parseAndRegisterBean(Element element, ParserContext parserContext) {
        BeanDefinitionParserDelegate delegate = parserContext.getDelegate();
        BeanDefinitionHolder holder = delegate.parseBeanDefinitionElement(element);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, parserContext.getRegistry());

        return holder.getBeanDefinition();
    }
}
