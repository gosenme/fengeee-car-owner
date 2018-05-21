package com.fengeee.car.owner.web.controller;

import com.fengeee.car.owner.api.OwnerService;
import com.fengeee.car.owner.dto.CarOwnerDto;
import com.seewo.mis.common.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wanggaoxiang@cvte.com
 * @since 1.0
 */
@Api(description = "车主信息管理", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Slf4j
@RestController
@RequestMapping("/owner/v1")
public class OwnerServiceController {

    @Autowired
    private OwnerService ownerService;


    @ApiOperation(value = "新增车主信息")
    @ApiImplicitParam(name = "dto", value = "车主信息内容", required = true, paramType = "body", dataType = "CarOwnerDto")
    @PostMapping(value = "/")
    public BaseResponse addOwner(@RequestBody CarOwnerDto dto) {
        log.info("dto:{}",dto);
        return ownerService.addOwnerFromFile(dto.getFilePath());
    }
}
