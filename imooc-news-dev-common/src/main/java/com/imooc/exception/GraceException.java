package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;

/**
 * 优雅处理异常，同一封装
 */
public class GraceException {

    public static void display(ResponseStatusEnum smsCodeError) {
        throw new MyCustomException(smsCodeError);
    }
}
