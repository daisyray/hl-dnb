package com.havenlife.dnb.models;

import java.util.Map;

public class Output {
    private final byte[] bytes;
    private final Map<String, String> headers;

    public Output(byte[] bytes, Map<String, String> headers) {
        this.bytes = bytes;
        this.headers = headers;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
