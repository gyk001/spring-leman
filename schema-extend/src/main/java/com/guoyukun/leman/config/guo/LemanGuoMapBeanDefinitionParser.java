package com.guoyukun.leman.config.guo;

import com.guoyukun.leman.config.DomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.MapFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
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
		// 替换命名空间为spring util
		element.getOwnerDocument().renameNode(element, SPRING_UTIL_URI, element.getLocalName());
		NodeList childNodes = element.getChildNodes();
		int childLen = childNodes.getLength();
		List<Node> allReplacedEntrys = new ArrayList<Node>();
		Node forNode = null;
		// 遍历map节点的所有子节点
		for(int i=0; i<childLen; i++){
			Node childNode = childNodes.item(i);
			// 子节点如果为guo:for
			if(Node.ELEMENT_NODE == childNode.getNodeType()){
				if("for".equals(childNode.getLocalName())){
					forNode = childNode;
					// 不能用展开后的xml替换for节点，替换过程中childNodes会动态变换出现问题
					allReplacedEntrys.addAll(ForElementExpandUtil.getExpandElement((Element) forNode, parserContext));
				}
			}else{
				allReplacedEntrys.add(childNode);
			}
		}
		// 先移除所有的，再添加全部
		DomUtil.removeAll(element);
		DomUtil.addAll(element, allReplacedEntrys);
		// 使用原来spring util标签解析的方式解析替换过的map标签
		parseMap(element, parserContext, builder);

		if(LOG.isDebugEnabled()){
			String e = ForElementExpandUtil.getNodeString(element);
			LOG.debug("-------------------------\n {} ---------------------\n", e);
		}
	}

	/**
	 * @see org.springframework.beans.factory.xml.UtilNamespaceHandler.MapBeanDefinitionParser
	 * @param element
	 * @param parserContext
	 * @param builder
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
