package com.wangyin.test.fund.tool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author yangjun3
 * @date 2015年9月16日 下午2:17:13
 * @description: 请求异常，主要是参数校验异常
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "内部异常")
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = -803991871601441523L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
