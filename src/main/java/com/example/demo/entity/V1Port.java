package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;

public class V1Port {
    @SerializedName("number")
    private Integer number = null;

    @SerializedName("name")
    private String name = null;

    @SerializedName("protocol")
    private String protocol = null;

    public V1Port(Integer number, String name, String protocol) {
        this.number = number;
        this.name = name;
        this.protocol = protocol;
    }
}
