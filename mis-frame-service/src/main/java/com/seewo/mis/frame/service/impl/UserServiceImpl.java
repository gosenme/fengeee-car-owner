package com.seewo.mis.frame.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.seewo.mis.common.response.BaseResponse;
import com.seewo.mis.common.util.ModelMapperUtils;
import com.seewo.mis.frame.api.UserService;
import com.seewo.mis.frame.dal.entity.UserInfoEntity;
import com.seewo.mis.frame.dal.mapper.UserInfoMapper;
import com.seewo.mis.frame.dto.UserInfoDto;
import com.seewo.mis.redis.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.List;

import static com.seewo.mis.frame.constant.ErrorsEnum.EXCEPTION;
import static com.seewo.mis.frame.constant.ErrorsEnum.FAILED_TO_GET_LOCK;
import static com.seewo.mis.frame.constant.ErrorsEnum.SUCCESS;

/**
 * app业务实现类
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Slf4j
@Service(validation = "true")
public class UserServiceImpl implements UserService {

    private static final Long LOCK_EXPIRE_TIME = 5000L;

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private RedisService   redisService;

    /**
     * 获取所有用户信息
     * 1.从缓存获取用户列表
     * 2.判断列表是否为空,如果不为空则直接返回
     * 3.如果为空则从数据库里面获取用户列表,且将用户列表缓存
     *
     * @return 用户列表
     */
    @Override
    public BaseResponse getAllUserList() {
        try {
            List<UserInfoDto> allUsers = redisService.getObjectCache("allUsers", List.class);
            if (allUsers != null) {
                return new BaseResponse(SUCCESS, allUsers);
            } else {
                List<UserInfoEntity> list = userInfoMapper.selectAll();
                allUsers = ModelMapperUtils.mapList(list, UserInfoDto.class);
                redisService.cacheObject("allUsers", allUsers);
                return new BaseResponse(SUCCESS, allUsers);
            }
        } catch (Exception e) {
            log.error("获取所有用户信息异常:{}", e);
            return new BaseResponse(EXCEPTION, "获取所有用户信息异常");
        }
    }

    /**
     * 通过主键获取用户信息
     *
     * @param id user主键
     * @return 用户信息
     */
    @Override
    public BaseResponse getUserById(@Validated BigInteger id) {
        try {
            UserInfoEntity userInfo = userInfoMapper.selectByPrimaryKey(id);
            return new BaseResponse(SUCCESS,userInfo);
        } catch (Exception e) {
            log.error("通过主键获取用户信息:{}", e);
            return new BaseResponse(EXCEPTION, "通过主键获取用户信息");
        }
    }

    /**
     * 新增一个用户
     *
     * @param userInfoDto 用户信息
     */
    @Override
    public BaseResponse addUser(@Validated UserInfoDto userInfoDto) {
        String requestId = userInfoDto.getTraceId();
        boolean haveLock = redisService.lock("addUser", requestId, LOCK_EXPIRE_TIME);
        try {
            if (!haveLock) {
                return new BaseResponse(FAILED_TO_GET_LOCK, "新增用户获取分布式锁失败");
            }
            UserInfoEntity entity = ModelMapperUtils.map(userInfoDto, UserInfoEntity.class, true);
            userInfoMapper.insert(entity);
            return new BaseResponse(SUCCESS);
        } catch (Exception e) {
            log.error("新增一个用户异常:{}", e);
            return new BaseResponse(EXCEPTION, "新增一个用户异常");
        } finally {
            if (haveLock) {
                redisService.unLock("addUser", requestId);
            }
        }
    }

}
