package com.lx.potato.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lixiang on 2017/10/29.
 */
public class Gank {
    @SerializedName("_id")
    public String id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public Object who;
}
