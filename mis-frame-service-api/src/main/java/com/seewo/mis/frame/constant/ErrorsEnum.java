package com.seewo.mis.frame.constant;

import com.seewo.mis.common.exception.BaseErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误码规范
 * 1.成功返回码 000000
 * 2.未知异常返回码 999999
 * 3.错误码的前两位标识业务系统,目前暂定如下:
 *   01:mis
 *   02:集控
 *   03:录播
 *   04:环境监测
 *   05:mis数据中心
 *   06:权限服务
 *   07:中控
 *   08:设备锁
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
public enum ErrorsEnum implements BaseErrorEnum{
    /**入参校验失败*/
    REQUEST_PARAMS_NOT_VALID("0100", "Request Params Illegal"),
    /**获取分布式锁失败*/
    FAILED_TO_GET_LOCK("0101", "Failed to get the lock"),
    /**新增用户信息异常*/
    INSERT_USER_INFO_FAIL("0102", "add user info fail"),
    /**获取用户信息失败*/
    GET_USER_LIST_FAIL("0103", "get user list fail"),
    /**获取雪花ID失败*/
    GET_SNOWFLAKE_FAIL("9901", "get user list fail"),
    /**找不到匹配的枚举下标*/
    STRING_CONVERSION_TO_ENUM_FAIL("9902", "找不到匹配的枚举下标");

    private String    code;
    private String message;
}
