package com.seewo.mis.frame.web.controller;

import com.seewo.mis.common.response.BaseResponse;
import com.seewo.mis.frame.api.UserService;
import com.seewo.mis.frame.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * @author : wanggaoxiang@cvte.com
 * Date: 2018-02-24
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/{userId}")
    public BaseResponse getUserById(@PathVariable String userId) {
        return userService.getUserById(new BigInteger(userId));
    }

    @PutMapping(value = "/addUser")
    public BaseResponse addUser(@RequestBody UserInfoDto userInfoDto) {
        return userService.addUser(userInfoDto);
    }
}
