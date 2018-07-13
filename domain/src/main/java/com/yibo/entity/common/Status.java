package com.yibo.entity.common;

/**
 * 返回错误状态码以及信息，作为HttpStatus的补充
 * 后期根据业务需求扩充
 *
 * @author yibo
 */
public enum Status {
    NOT_VALID_SQL(22222, "SQL语法错误"),
    NULL_POINTER_EXCEPTION(33333, "空指针异常");

    private final int code;
    private final String reasonPhrase;

    Status(Integer code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}