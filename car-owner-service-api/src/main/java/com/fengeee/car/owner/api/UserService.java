package com.fengeee.car.owner.api;

import com.fengeee.car.owner.dto.UserInfoDto;
import com.seewo.mis.common.response.BaseResponse;
import com.seewo.mis.frame.dto.UserInfoDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigInteger;

/**
 * app 业务服务接口
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
public interface UserService {

    /**
     * 获取所有的app列表
     *
     * @return
     */
    BaseResponse getAllUserList();


    @interface GetUserById {}
    /**
     * 获取所有的app列表
     *
     * @param id 用户ID
     * @return 用户信息
     */
    BaseResponse getUserById(@Min(value = 100000000000000000L, message = "id不能小于100000000000000000")
                             @Max(value = 999999999999999999L, message = "id不能大于999999999999999999") BigInteger id);

    @interface AddUser {}
    /**
     * 新增用户
     *
     * @param userInfoDto 用户信息
     */
    BaseResponse addUser(UserInfoDto userInfoDto);
}