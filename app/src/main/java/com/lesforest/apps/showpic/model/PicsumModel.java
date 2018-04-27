package com.lesforest.apps.showpic.model;

public class PicsumModel {

    String format;
    long width;
    long height;
    String filename;
    long id;
    String author;
    String author_url;
    public String post_url;

    @Override
    public String toString() {
        return "PicsumModel{" +
                "format='" + format + '\n' +
                ", width=" + width + '\n' +
                ", height=" + height + '\n' +
                ", filename='" + filename + '\n' +
                ", id=" + id + '\n' +
                ", author='" + author + '\n' +
                ", author_url='" + author_url + '\n' +
                ", post_url='" + post_url + '\n' +
                '}';
    }
}
