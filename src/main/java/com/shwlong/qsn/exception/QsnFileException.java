package com.shwlong.qsn.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QsnFileException extends Exception {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public QsnFileException(String msg) {
        super(msg);
        this.msg = msg;
    }

}
