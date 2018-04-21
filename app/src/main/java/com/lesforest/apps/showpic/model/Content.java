package com.lesforest.apps.showpic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

public class Content {

    @Attribute(name = "src")
    public String src;

    @Override
    public String toString() {
        return "Content{" +
                "src='" + src + '\'' +
                '}';
    }
}
