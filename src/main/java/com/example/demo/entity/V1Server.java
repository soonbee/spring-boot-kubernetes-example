package com.example.demo.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class V1Server {
    @SerializedName("port")
    private V1Port port = null;

    @SerializedName("hosts")
    private List<String> hosts = null;

    public V1Port getPort() {
        return port;
    }

    public void setPort(V1Port port) {
        this.port = port;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public void setHosts(String host) {
        if (this.hosts == null) {
            this.hosts = new ArrayList<String>();
        }
        this.hosts.add(host);
    }
}
