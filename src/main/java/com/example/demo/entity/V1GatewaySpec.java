package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;
import io.kubernetes.client.openapi.models.V1LabelSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class V1GatewaySpec {
    @SerializedName("selector")
    private V1LabelSelector selector = null;

    @SerializedName("servers")
    private List<V1Server> servers = null;

    public V1LabelSelector getSelector() {
        return selector;
    }

    public void setSelector(V1LabelSelector selector) {
        this.selector = selector;
    }

    public List<V1Server> getServers() {
        return servers;
    }

    public void setServers(List<V1Server> servers) {
        this.servers = servers;
    }

    public void setServers(V1Server server) {
        if (servers == null ) {
            this.servers = new ArrayList<V1Server>();
        }
        this.servers.add(server);
    }
}
