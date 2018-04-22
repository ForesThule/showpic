package com.lesforest.apps.showpic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

public class Link {

    public String next;

    @Override
    public String toString() {
        return "Link{" +
                "next='" + next + '\'' +
                '}';
    }
}
