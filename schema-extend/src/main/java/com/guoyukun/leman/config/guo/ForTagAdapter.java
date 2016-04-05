package com.guoyukun.leman.config.guo;

import java.util.List;

/**
 * Created by guoyukun on 2016/3/29.
 */
public class ForTagAdapter {
    private int from;
    private int to;
    private String placeholder;
    private String desc;
    private List<String> beanNames;

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<String> getBeanNames() {
        return beanNames;
    }

    public void setBeanNames(List<String> beanNames) {
        this.beanNames = beanNames;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String toString() {
        return "ForTagAdapter{" +
                "from=" + from +
                ", to=" + to +
                ", placeholder='" + placeholder + '\'' +
                ", desc='" + desc + '\'' +
                ", beanNames=" + beanNames +
                '}';
    }
}
