package com.lesforest.apps.showpic.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

import java.util.List;
import java.util.Map;

@Root(name = "entry")
public class Entry {

    @Element(name="title")
    private String title;

    @ElementArray
    private Img img;
//    private Link[] links;

//
//    @ElementMap(name = "link",value = "href",key = "rel")
//    Map<String,String> map;

//    @Namespace(reference="http://www.blah.com/ns/a")
//    @Element
//    private Content content;

    @Element
    private Content content;

    @Override
    public String toString() {
        return "Entry{" +
                "title='" + title + '\'' +
//                ", links=" + links +
                ", img=" + img + '}';
    }
}
