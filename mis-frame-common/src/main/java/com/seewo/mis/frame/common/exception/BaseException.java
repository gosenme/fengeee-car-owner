package com.seewo.mis.frame.common.exception;

import com.seewo.mis.frame.constant.ErrorsEnum;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 基础异常类
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Setter
@Getter
public class BaseException extends RuntimeException {

    private final String errorCode;

    /**
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     */
    public BaseException(@NotNull String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }

    /**
     * @param errorsEnum 错误枚举
     */
    public BaseException(@NotNull ErrorsEnum errorsEnum) {
        super(errorsEnum.getMessage());
        this.errorCode = errorsEnum.getCode();
    }

    /**
     * @param errorsEnum   错误枚举
     * @param errorMessage 错误信息
     */
    public BaseException(@NotNull ErrorsEnum errorsEnum, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorsEnum.getCode();
    }

}
