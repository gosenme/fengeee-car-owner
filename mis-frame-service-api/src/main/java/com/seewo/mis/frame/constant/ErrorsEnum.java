package com.seewo.mis.frame.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码规范
 * 1.成功返回码 000000
 * 2.未知异常返回码 999999
 * 3.错误码的前两位标识业务系统,目前暂定如下:
 *   01:mis后台[脚手架项目使用此编码做demo]
 *   02:集控
 *   03:录播
 *   04:环境监测
 *   05:mis数据中心
 *   ...
 * 4.错误码中间两位
 *   00:业务校验不通过
 *   01:数据库操作失败
 *   03:调用第三方返回失败
 *   ...
 *   99:其他错误
 *   ...
 * 5.错误码最后两位自增
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@AllArgsConstructor
@Getter
public enum ErrorsEnum {
    /**请求成功*/
    SUCCESS("000000", "success"),
    /**入参校验失败*/
    REQUEST_PARAMS_NOT_VALID("010000", "Request Params Illegal"),
    /**获取分布式锁失败*/
    FAILED_TO_GET_LOCK("010001", "Failed to get the lock"),
    /**新增用户信息异常*/
    INSERT_USER_INFO_FAIL("010100", "add user info fail"),
    /**获取用户信息失败*/
    GET_USER_LIST_FAIL("010101", "get user list fail"),
    /**获取雪花ID失败*/
    GET_SNOWFLAKE_FAIL("990000", "get user list fail"),
    /**找不到匹配的枚举下标*/
    STRING_CONVERSION_TO_ENUM_FAIL("990001", "找不到匹配的枚举下标"),
    /**请求异常,一些未知的错误*/
    EXCEPTION("999999", "exception");

    private String    code;
    private String message;
}
