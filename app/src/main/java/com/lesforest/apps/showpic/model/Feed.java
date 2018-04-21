package com.lesforest.apps.showpic.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "feed")
public class Feed {

    @Element(name="title")
    private String title;

    @ElementList
    private List<Entry> entries;

    @Override
    public String toString() {
        return "Feed{" +
                "title='" + title + '\'' +
                ", entries =" + entries+
                '}';
    }
}
