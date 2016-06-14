package cn.guoyukun.leman.config;

import org.w3c.dom.Node;

import java.util.List;

/**
 * Created by guoyukun on 2016/4/5.
 */
public class DomUtil {

    public static void removeAll(Node node) {
        while (node.hasChildNodes()) {
            node.removeChild(node.getFirstChild());
        }
    }

    public static void addAll(Node parent, List<Node> append) {
        for (Node node : append) {
            parent.appendChild(node);
        }
    }
}
