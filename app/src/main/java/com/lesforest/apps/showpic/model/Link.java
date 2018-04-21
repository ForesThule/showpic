package com.lesforest.apps.showpic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

public class Link {

    @Attribute(name = "href")
    String href;

    @Override
    public String toString() {
        return "Link{" +
                "href='" + href + '\'' +
                '}';
    }
}
