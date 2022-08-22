package com.shwlong.qsn.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 全局自定义异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QsnException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public QsnException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public QsnException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public QsnException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public QsnException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
