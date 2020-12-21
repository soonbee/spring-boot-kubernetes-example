package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;

public class V1ProfileSpec {
    @SerializedName("owner")
    private V1Owner owner;

    public V1ProfileSpec owner(V1Owner owner) {
        this.owner = owner;
        return this;
    }
}
