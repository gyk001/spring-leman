package com.guoyukun.leman.config.guo;

import com.guoyukun.leman.config.AbstractLemanBeanDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LemanGuoForBeanDefinitionParser extends AbstractLemanBeanDefinitionParser {
	private static final Logger LOG = LoggerFactory.getLogger(LemanGuoForBeanDefinitionParser.class);
	@Override
	public String getDefaultId() {
		return "leman-guo-for";
	}

	@Override
	protected Class<?> getBeanClass(Element element) {
		return ForTagAdapter.class;
	}

	@Override
	protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {

		addValue(builder, element, "placeholder", true);
		addValue(builder, element, "from", true);
		addValue(builder, element, "to", true);
		addValue(builder, element, "desc", false);
		NodeList nodeList = element.getChildNodes();


		String placeholder = element.getAttribute("placeholder");
		String fromVal = element.getAttribute("from");
		String toVal = element.getAttribute("to");

		int from = Integer.valueOf(fromVal);
		int to = Integer.valueOf(toVal);

		List<String> beanNames = new ArrayList<String>();

		for(int index=from; index <to; index++ ){

			// 依次解析for标签内的全部标签
			for(int i=0, length = nodeList.getLength(); i<length; i++){
				Node node = nodeList.item(i);
				// 忽略所有的非标签节点（文本节点等）
				if(Node.ELEMENT_NODE != node.getNodeType()){
					continue;
				}
				// 替换所有变量
				Element beanElement = (Element) ForElementExpandUtil.deepReplaceAttr(node, index, placeholder);
				// 使用Spring默认的方式解析替换过的bean标签
				BeanDefinitionHolder bdh =parserContext.getDelegate().parseBeanDefinitionElement(beanElement);
				registerBeanDefinition(bdh, parserContext.getRegistry());
				beanNames.add(bdh.getBeanName());
			}
		}
		addValue(builder, "beanNames", beanNames);
		LOG.info("doParse...,{}", element);
	}

}
