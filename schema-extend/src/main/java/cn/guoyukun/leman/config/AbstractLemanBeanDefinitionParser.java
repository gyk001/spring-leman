package cn.guoyukun.leman.config;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public abstract class AbstractLemanBeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {
    /**
     * 获取bean的默认 id
     *
     * @return
     */
    public abstract String getDefaultId();

    /**
     * 为构造方法添加值
     *
     * @param builder 对象构建器
     * @param value   值
     */
    protected void addConstructorArgVal(BeanDefinitionBuilder builder, Object value) {
        builder.addConstructorArgValue(value);
    }

    /**
     * 为构造方法添加值
     *
     * @param builder  对象构建器
     * @param element  元素
     * @param property 属性
     */
    protected void addConstructorArgVal(BeanDefinitionBuilder builder, Element element, String property) {
        addConstructorArgVal(builder, element, property, null);
    }

    /**
     * 为构造方法添加值
     *
     * @param builder      对象构建器
     * @param element      元素
     * @param property     属性
     * @param defaultValue 默认值
     */
    protected void addConstructorArgVal(BeanDefinitionBuilder builder, Element element, String property,
                                        Object defaultValue) {
        String value = element.getAttribute(property);
        if (StringUtils.hasLength(value)) {
            builder.addConstructorArgValue(value);
        } else if (defaultValue != null) {
            builder.addConstructorArgValue(defaultValue);
        } else {
            throw new NsException(property + " of " + getDefaultId() + " must be configured!");
        }
    }

    /**
     * 为构造函数添加引用
     *
     * @param builder   对象构建器
     * @param reference 引用
     */
    protected void addConstructorArgRef(BeanDefinitionBuilder builder, String reference) {
        if (StringUtils.hasLength(reference)) {
            builder.addConstructorArgReference(reference);
        } else {
            throw new NsException("Constructor of " + getDefaultId() + "error!");
        }

    }

    /**
     * 添加构造函数参数引用
     *
     * @param builder  对象构建器
     * @param property 属性
     */
    protected void addConstructorArgRef(BeanDefinitionBuilder builder, Element element, String property) {
        this.addConstructorArgRef(builder, element, property, true);
    }

    /**
     * 添加构造函数参数引用
     *
     * @param builder  对象构建器
     * @param element  元素
     * @param property 属性
     * @param required 是否必须
     */
    protected void addConstructorArgRef(BeanDefinitionBuilder builder, Element element, String property,
                                        boolean required) {
        String value = element.getAttribute(property);
        if (StringUtils.hasLength(value)) {
            builder.addConstructorArgReference(value);
        } else {
            if (required) {
                throw new NsException(property + " of " + getDefaultId() + " must be configured!");
            }
        }
    }

    /**
     * 为引用类型的属性赋值
     *
     * @param builder  对象构建器
     * @param element  元素
     * @param property 属性
     * @param required 是否必须
     */

    protected void addReference(BeanDefinitionBuilder builder, Element element, String property, boolean required) {
        String ref = element.getAttribute(property);
        if (!StringUtils.hasLength(ref) && required) {
            throw new NsException(property + " of " + getDefaultId() + " must be configured!");
        }
        if (StringUtils.hasLength(ref)) {
            builder.addPropertyReference(property, ref);
        }
    }

    /**
     * 属性赋值
     *
     * @param builder  对象构建器
     * @param element  元素
     * @param property 属性
     * @param required 是否必须
     */

    protected void addValue(BeanDefinitionBuilder builder, Element element, String property, boolean required) {
        String val = element.getAttribute(property);
        if (!StringUtils.hasLength(val) && required) {
            throw new NsException(property + " of " + getDefaultId() + " must be configured!");
        }
        if (StringUtils.hasLength(val)) {
            builder.addPropertyValue(property, val);
        }
    }

    /**
     * 属性赋值
     *
     * @param builder  对象构建器
     * @param property 属性
     * @param value    值
     */

    protected void addValue(BeanDefinitionBuilder builder, String property, Object value) {
        builder.addPropertyValue(property, value);
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws
            BeanDefinitionStoreException {
        String id = element.getAttribute(ID_ATTRIBUTE);
        if (StringUtils.hasText(id)) {
            return super.resolveId(element, definition, parserContext);
        } else {
            return this.getDefaultId();
        }
    }
}
