package com.guoyukun.leman.config.guo;

import com.guoyukun.leman.config.AbstractLemanBeanDefinitionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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

		addValue(builder, element, "var", true);
		addValue(builder, element, "from", true);
		addValue(builder, element, "to", true);
		addValue(builder, element, "desc", true);
		NodeList nodeList = element.getChildNodes();


		String var = element.getAttribute("var");
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
				Element beanElement = (Element) deepReplaceAttr(node, index);
				// 使用Spring默认的方式解析替换过的bean标签
				BeanDefinitionHolder bdh =parserContext.getDelegate().parseBeanDefinitionElement(beanElement);
				registerBeanDefinition(bdh, parserContext.getRegistry());
				beanNames.add(bdh.getBeanName());
			}
		}
		addValue(builder, "beanNames", beanNames);
		LOG.info("doParse...,{}", element);
	}

	private Node replaceAllAttr(Node node, int index){
		NamedNodeMap attrs = node.getAttributes();
		if(attrs!=null){
			int attrLen = attrs.getLength();
			for (int j=0;j < attrLen; j++){
				Attr attr = (Attr)attrs.item(j);
				String attrVal = attr.getValue();
				if(! attrVal.contains("{{index}}")){
					continue;
				}
				String newVal = attrVal.replace("{{index}}", ""+index);
				LOG.info("{} --> {}",attrVal, newVal);
				attr.setValue(newVal);
			}
		}
		NodeList childList = node.getChildNodes();
		int childLen = childList.getLength();
		for(int i=0;i<childLen;i++){
			Node child = childList.item(i);
			replaceAllAttr(child, index);
		}

		return node;
	}

	private Node deepReplaceAttr(Node node, int index) {
		if(Node.ELEMENT_NODE!= node.getNodeType()){
			return node;
		}
		// 必须复制出来一个节点，否则解析器会缓存，后续循环变量修改无法反映到节点上
		Element beanElement = (Element) replaceAllAttr(node.cloneNode(true), index);
		LOG.info("\n@@ {}\n {}",beanElement.getNodeName(),  getNodeString(beanElement));
		return beanElement;
	}

	String getNodeString(Node node) {
		try {
			StringWriter writer = new StringWriter();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(new DOMSource(node), new StreamResult(writer));
			String output = writer.toString();
			return output.substring(output.indexOf("?>") + 2);//remove <?xml version="1.0" encoding="UTF-8"?>
		} catch (TransformerException e) {
			LOG.error("", e);
		}
		return node.getTextContent();
	}
	
	

}
