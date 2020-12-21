package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;

public class V1Owner {
    @SerializedName("kind")
    private String kind;

    @SerializedName("name")
    private String name;

    public V1Owner kind(String kind) {
        this.kind = kind;
        return this;
    }

    public V1Owner name(String name) {
        this.name = name;
        return this;
    }
}
