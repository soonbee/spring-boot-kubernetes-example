package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

public class V1Gateway {
    @SerializedName("apiVersion")
    private String apiVersion;

    @SerializedName("kind")
    private String kind = "Gateway";

    @SerializedName("metadata")
    private V1ObjectMeta metadata = null;

    @SerializedName("spec")
    private V1GatewaySpec spec = null;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public V1ObjectMeta getMetadata() {
        return metadata;
    }

    public void setMetadata(V1ObjectMeta metadata) {
        this.metadata = metadata;
    }

    public V1GatewaySpec getSpec() {
        return spec;
    }

    public void setSpec(V1GatewaySpec spec) {
        this.spec = spec;
    }
}
