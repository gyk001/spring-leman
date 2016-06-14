package cn.guoyukun.leman.config.guo;

import cn.guoyukun.leman.config.DomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * Created by guoyukun on 2016/4/5.
 */
public class ForElementExpandUtil {
    private static final String SPRING_UTIL_URI = "http://www.springframework.org/schema/util";
    private static final Logger LOG = LoggerFactory.getLogger(ForElementExpandUtil.class);

    public static void replaceForTagElements(Element element, ParserContext parserContext) {
        element.getOwnerDocument().renameNode(element, SPRING_UTIL_URI, element.getLocalName());
        NodeList childNodes = element.getChildNodes();
        int childLen = childNodes.getLength();
        List<Node> allReplacedEntrys = new ArrayList<Node>();
        // 遍历map节点的所有子节点
        for (int i = 0; i < childLen; i++) {
            Node childNode = childNodes.item(i);
            // 子节点如果为guo:for
            if (Node.ELEMENT_NODE == childNode.getNodeType() && "for".equals(childNode.getLocalName())) {
                List<Element> replacedElements = ForElementExpandUtil.getExpandElement((Element) childNode, parserContext);
                // 不能用展开后的xml替换for节点，替换过程中childNodes会动态变换出现问题
                allReplacedEntrys.addAll(replacedElements);
            } else {
                allReplacedEntrys.add(childNode);
            }
        }
        // 先移除所有的，再添加全部
        DomUtil.removeAll(element);
        DomUtil.addAll(element, allReplacedEntrys);
    }

    public static List<Element> getExpandElement(Element forEle, ParserContext parserContext) {
        String fromVal = forEle.getAttribute("from");
        String toVal = forEle.getAttribute("to");
        String placeholder = forEle.getAttribute("placeholder");

        int from = Integer.valueOf(fromVal);
        int to = Integer.valueOf(toVal);

        List<Element> expandElements = new ArrayList<Element>();

        NodeList nodeList = forEle.getChildNodes();
        for (int index = from; index < to; index++) {
            // 依次解析for标签内的全部标签
            for (int i = 0, length = nodeList.getLength(); i < length; i++) {
                Node node = nodeList.item(i);
                // 忽略所有的非标签节点（文本节点等）
                if (Node.ELEMENT_NODE != node.getNodeType()) {
                    continue;
                }
                // 替换所有变量
                Element beanElement = (Element) deepReplaceAttr(node, index, placeholder);
                expandElements.add(beanElement);
            }
        }
        return expandElements;
    }


    public static Node replaceAllAttr(Node node, int index, String placeholder) {

        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
            int attrLen = attrs.getLength();
            for (int j = 0; j < attrLen; j++) {
                Attr attr = (Attr) attrs.item(j);
                String attrVal = attr.getValue();
                if (!attrVal.contains("{{" + placeholder + "}}")) {
                    continue;
                }
                String newVal = attrVal.replace("{{" + placeholder + "}}", "" + index);
                LOG.info("{} --> {}", attrVal, newVal);
                attr.setValue(newVal);
            }
        }
        NodeList childList = node.getChildNodes();
        int childLen = childList.getLength();
        for (int i = 0; i < childLen; i++) {
            Node child = childList.item(i);
            replaceAllAttr(child, index, placeholder);
        }

        return node;
    }

    public static Node deepReplaceAttr(Node node, int index, String placeholder) {
        if (Node.ELEMENT_NODE != node.getNodeType()) {
            return node;
        }
        // 必须复制出来一个节点，否则解析器会缓存，后续循环变量修改无法反映到节点上
        Element beanElement = (Element) replaceAllAttr(node.cloneNode(true), index, placeholder);
        LOG.info("\n@@ {}\n {}", beanElement.getNodeName(), getNodeString(beanElement));
        return beanElement;
    }

    public static String getNodeString(Node node) {
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
