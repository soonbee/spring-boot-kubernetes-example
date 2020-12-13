package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;
import io.kubernetes.client.openapi.models.V1ObjectMeta;

public class V1VirtualService {
    @SerializedName("apiVersion")
    private String apiVersion = "networking.istio.io/v1alpha3";

    @SerializedName("kind")
    private String kind = "VirtualService";

    @SerializedName("metadata")
    private V1ObjectMeta metadata = null;

    @SerializedName("spec")
    private V1VirtualServiceSpec spec = null;
}
