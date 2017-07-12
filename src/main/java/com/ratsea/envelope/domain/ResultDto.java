package com.ratsea.envelope.domain;

import java.io.Serializable;

/**
 * Created by Ratsea on 2017/7/2.
 */
public class ResultDto implements Serializable {

    private int code;

    private String mgs;

    private String message;

    private Object obj;

    public ResultDto() {
    }

    public ResultDto(int code, String mgs, String message) {
        this.code = code;
        this.mgs = mgs;
        this.message = message;
    }

    public ResultDto(int code, String mgs, String message, Object obj) {
        this.code = code;
        this.mgs = mgs;
        this.message = message;
        this.obj = obj;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMgs() {
        return mgs;
    }

    public void setMgs(String mgs) {
        this.mgs = mgs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "ResultDto{" +
                "code=" + code +
                ", mgs='" + mgs + '\'' +
                ", message='" + message + '\'' +
                ", obj=" + obj +
                '}';
    }
}
