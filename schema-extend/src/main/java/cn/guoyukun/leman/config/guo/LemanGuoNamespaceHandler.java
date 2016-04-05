package cn.guoyukun.leman.config.guo;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LemanGuoNamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		registerBeanDefinitionParser("for", new LemanGuoForBeanDefinitionParser());
		registerBeanDefinitionParser("map", new LemanGuoMapBeanDefinitionParser());
		registerBeanDefinitionParser("list", new LemanGuoListBeanDefinitionParser());
	}
}
