package com.seewo.mis.frame.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * userInfo dto
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Data
public class UserInfoDto extends BaseDto {

    /** 逻辑ID */
    private String     uid;
    /** 用户名称 */
    @NotNull
    private String     name;
    /** 用户性别 0: 未知 1:男 2: 女 */
    @NotNull
    @Min(-1)
    @Max(1)
    private int    sex;
    /** 身份证号码 */
    @NotNull
    private String    idCard;
    /** 邮箱地址 */
    @NotNull
    private String     email;
}
