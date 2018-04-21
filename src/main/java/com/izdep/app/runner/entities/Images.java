package com.izdep.app.runner.entities;

import java.io.Serializable;

public class Images implements Serializable {

    private int id;
    private String url;

    public Images(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
