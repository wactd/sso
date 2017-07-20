package com.dongly.web.model;

/**
 * Created by tiger on 2017/7/19.
 */
public class ResultModel<T> {

    private Integer status;
    private String message;
    private T data;

    public ResultModel() {
    }

    public ResultModel(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultModel<T> ok(T data) {
        return new ResultModel<>(200, "SUCCESS", data);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
