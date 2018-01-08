package com.seewo.mis.frame;

import com.seewo.mis.frame.constant.ErrorsEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * 基础response
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-22
 * @version 1.0
 */
@Getter
@Setter
public class BaseResponse implements Serializable {
    private String code;
    private String message;
    private Object data;

    /**
     * 成功
     */
    public BaseResponse() {
        this.code = ErrorsEnum.SUCCESS.getCode();
        this.message = ErrorsEnum.SUCCESS.getMessage();
    }

    /**
     * 成功且有业务结果
     *
     * @param data 业务结果
     */
    public BaseResponse(Object data) {
        this.code = ErrorsEnum.SUCCESS.getCode();
        this.message = ErrorsEnum.SUCCESS.getMessage();
        this.data = data;
    }

    /**
     * @param error 提示信息枚举
     */
    public BaseResponse(ErrorsEnum error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    /**
     * 自定义提示信息
     *
     * @param error 提示信息枚举
     */
    public BaseResponse(ErrorsEnum error,String message) {
        this.code = error.getCode();
        this.message = message;
    }

    /**
     * @param error 提示信息枚举
     * @param data  业务结果
     */
    public BaseResponse(ErrorsEnum error, Object data) {
        this.code = error.getCode();
        this.message = error.getMessage();
        this.data = data;
    }

}
