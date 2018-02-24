package com.seewo.mis.frame.dal.entity;

import com.seewo.ColumnPrex;
import com.seewo.base.LogicDelete;
import com.seewo.keygenerator.KeyGenerator;
import com.seewo.mis.frame.dal.base.SnowKeyGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.time.LocalDateTime;

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
@ColumnPrex("c_")
public class UserInfoEntity {
    /** 唯一ID */
    @Id
    @KeyGenerator(strategy = SnowKeyGenerator.class)
    private BigInteger    id;
    /** 用户名称 */
    private String        name;
    /**年龄*/
    private int           age;
    /** 用户性别 0: 未知 1:男 2: 女 */
    private String        sex;
    /** 身份证号码 */
    private String        idCard;
    /** 邮箱地址 */
    private String        email;
    /** 是否已删除 */
    @LogicDelete
    private Boolean       isDeleted;
    /**创建时间*/
    private LocalDateTime createTime;
    /**更新时间*/
    private LocalDateTime updateTime;
}
