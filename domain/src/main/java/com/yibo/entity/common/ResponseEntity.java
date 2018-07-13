package com.yibo.entity.common;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

import static org.springframework.http.HttpStatus.OK;

/**
 * @param <T> 返回的类型
 * @author Yibo
 */
public class ResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = -5950487642515880219L;
    private Date timestamp = new Date();
    private Integer status;
    private String message;
    private T data;

    public ResponseEntity() {
        this.status = OK.value();
        this.message = OK.getReasonPhrase();
    }

    public ResponseEntity(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseEntity(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResponseEntity ofSuccessMessage(String message) {
        return new ResponseEntity<>(OK.value(), message, null);
    }

    public static ResponseEntity ofSuccessData(Object data) {
        return new ResponseEntity<>(OK.value(), OK.getReasonPhrase(), data);
    }

    public static ResponseEntity ofStatus(Status status) {
        return new ResponseEntity<>(status.value(), status.getReasonPhrase(), null);
    }

    public static ResponseEntity ofHttpStatus(HttpStatus httpStatus) {
        return new ResponseEntity<>(httpStatus.value(), httpStatus.getReasonPhrase(), null);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}