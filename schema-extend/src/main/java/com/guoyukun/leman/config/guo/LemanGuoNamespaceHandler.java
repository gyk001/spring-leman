package com.guoyukun.leman.config.guo;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class LemanGuoNamespaceHandler extends NamespaceHandlerSupport {
	public void init() {
		registerBeanDefinitionParser("for", new LemanGuoForBeanDefinitionParser());
		registerBeanDefinitionParser("map", new LemanGuoMapBeanDefinitionParser());
	}
}
