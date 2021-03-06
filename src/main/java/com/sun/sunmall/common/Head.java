package com.sun.sunmall.common;

import lombok.Data;

/**
 * @author twc
 */
@Data
public class Head {

    /**
     * 状态码
     */
    private String statusCode;

    /**
     * 状态信息
     */
    private String statusMessage;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
