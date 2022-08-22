package com.shwlong.qsn.exception;

import com.shwlong.qsn.util.QsnEnum;
import com.shwlong.qsn.util.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class QsnExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(QsnException.class)
    public R handleUserExistException(QsnException e) {
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(IOException.class)
    public R handleIOException(IOException e) {
        return R.error(500, QsnEnum.FILE_OPTION_ERROR.getMsg());
    }

    @ExceptionHandler(QsnFileException.class)
    public R handleUserExistException(QsnFileException e) {
        return R.error(500, QsnEnum.FILE_UPLOAD_FORMAT_ERROR.getMsg());
    }

}
