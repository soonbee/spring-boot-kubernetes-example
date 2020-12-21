package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

public class V1Profile {
    @SerializedName("apiVersion")
    private String apiVersion;

    @SerializedName("kind")
    private String kind = "Profile";

    @SerializedName("metadata")
    private V1ObjectMeta metadata = null;

    @SerializedName("spec")
    private V1ProfileSpec spec = null;

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setMetadata(V1ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public void setSpec(V1ProfileSpec spec) {
        this.spec = spec;
    }
}
