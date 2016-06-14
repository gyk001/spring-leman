package cn.guoyukun.leman.config.guo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.Map;

/**
 *
 */
public class LemanGuoMapBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final String SPRING_UTIL_URI = "http://www.springframework.org/schema/util";
    private static final String SCOPE_ATTRIBUTE = "scope";
    private static final Logger LOG = LoggerFactory.getLogger(LemanGuoMapBeanDefinitionParser.class);

    @Override
    protected Class getBeanClass(Element element) {
        return MapFactoryBean.class;
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        ForElementExpandUtil.replaceForTagElements(element, parserContext);
        // 使用原来spring util标签解析的方式解析替换过的标签
        parseMap(element, parserContext, builder);

        if (LOG.isDebugEnabled()) {
            String e = ForElementExpandUtil.getNodeString(element);
            LOG.debug("-------------------------\n {} ---------------------\n", e);
        }
    }

    /**
     * @param element
     * @param parserContext
     * @param builder
     * @see org.springframework.beans.factory.xml.UtilNamespaceHandler.MapBeanDefinitionParser
     */
    private void parseMap(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String mapClass = element.getAttribute("map-class");
        Map parsedMap = parserContext.getDelegate().parseMapElement(element, builder.getRawBeanDefinition());
        builder.addPropertyValue("sourceMap", parsedMap);
        if (StringUtils.hasText(mapClass)) {
            builder.addPropertyValue("targetMapClass", mapClass);
        }
        String scope = element.getAttribute(SCOPE_ATTRIBUTE);
        if (StringUtils.hasLength(scope)) {
            builder.setScope(scope);
        }
    }


}
