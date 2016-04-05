package cn.guoyukun.leman.config.guo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.ListFactoryBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.List;
import java.util.UUID;

/**
 *
 */
public class LemanGuoListBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String SCOPE_ATTRIBUTE = "scope";
	private static final Logger LOG = LoggerFactory.getLogger(LemanGuoListBeanDefinitionParser.class);

	@Override
	protected Class getBeanClass(Element element) {
		return ListFactoryBean.class;
	}

	@Override
	protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
		String id = super.resolveId(element, definition, parserContext);
		if(StringUtils.hasText(id)){
			return id;
		}
		return "list-"+ UUID.randomUUID().toString();
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		ForElementExpandUtil.replaceForTagElements(element, parserContext);
		// 使用原来spring util标签解析的方式解析替换过的标签
		parseList(element, parserContext, builder);
		if(LOG.isDebugEnabled()){
			String e = ForElementExpandUtil.getNodeString(element);
			LOG.debug("-------------------------\n {} ---------------------\n", e);
		}
	}

	/**
	 * @see org.springframework.beans.factory.xml.UtilNamespaceHandler.ListBeanDefinitionParser
	 * @param element
	 * @param parserContext
	 * @param builder
     */
	protected void parseList(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
		String listClass = element.getAttribute("list-class");
		List parsedList = parserContext.getDelegate().parseListElement(element, builder.getRawBeanDefinition());
		builder.addPropertyValue("sourceList", parsedList);
		if (StringUtils.hasText(listClass)) {
			builder.addPropertyValue("targetListClass", listClass);
		}
		String scope = element.getAttribute(SCOPE_ATTRIBUTE);
		if (StringUtils.hasLength(scope)) {
			builder.setScope(scope);
		}
	}

}
