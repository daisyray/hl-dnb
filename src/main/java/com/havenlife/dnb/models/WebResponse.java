package com.havenlife.dnb.models;

import java.util.List;

public class WebResponse {
    Integer id;
    List<String> errors;

    public WebResponse(Integer id) {
        this.id = id;
    }

    public WebResponse(List<String> errors) {
        this.errors = errors;
    }

    public WebResponse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "WebResponse{" +
                "id=" + id +
                ", errors=" + errors +
                '}';
    }
}
