package com.lesforest.apps.showpic.model;

import com.google.gson.annotations.SerializedName;
import com.lesforest.apps.showpic.model.img.Pic;



public class Img {

    @SerializedName("M")
    private Pic m;

    @Override
    public String toString() {
        return "Img{" +
                "m='" + m + '\'' +
                '}';
    }

}
