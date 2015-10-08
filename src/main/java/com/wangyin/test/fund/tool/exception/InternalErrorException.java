package com.wangyin.test.fund.tool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author yangjun3
 * @date 2015年9月24日 下午3:03:43
 * @description: 内部异常，一般是500类型的
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "内部异常")
public class InternalErrorException extends RuntimeException {
    private static final long serialVersionUID = 4419640710144242183L;

    public InternalErrorException() {
        super();
    }

    public InternalErrorException(String msg) {
        super(msg);
    }

    public InternalErrorException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
