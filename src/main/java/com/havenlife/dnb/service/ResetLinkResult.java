package com.havenlife.dnb.service;

public class ResetLinkResult {
    private Integer id;
    private String errorMsg;

    public ResetLinkResult(Integer id, String errorMsg) {
        this.id = id;
        this.errorMsg = errorMsg;
    }

    public Integer getId() {
        return id;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
