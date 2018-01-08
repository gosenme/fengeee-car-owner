package com.seewo.mis.frame.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

/**
 * 用户信息实体类
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_user_info")
public class UserInfoEntity {
    /** 唯一ID */
    @Id
    private BigInteger id;
    /** 用户名称 */
    private String     name;
    /**年龄*/
    private int        age;
    /** 用户性别 0: 未知 1:男 2: 女 */
    private String     sex;
    /** 身份证号码 */
    private String     idCard;
    /** 邮箱地址 */
    private String     email;
    /** 是否已删除 */
    @Column(name = "is_deleted")
    private Boolean    deleted;
}
