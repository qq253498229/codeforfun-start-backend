package com.example.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangbin
 */
public class Services {
    private String name;
    private String key;
    private Integer port;
    private String serviceName;

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("%%name%%", this.name);
        map.put("%%key%%", this.key);
        map.put("%%port%%", String.valueOf(this.port));
        map.put("%%serviceName%%", this.serviceName);
        return map;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
