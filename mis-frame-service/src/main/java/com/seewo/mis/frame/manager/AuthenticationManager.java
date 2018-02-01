package com.seewo.mis.frame.manager;

import com.alibaba.dubbo.config.annotation.Reference;
import com.seewo.mis.authentication.api.AuthenticationService;
import com.seewo.mis.authentication.api.ResourceRoleRelationService;
import com.seewo.mis.authentication.api.UserRoleRelationService;
import com.seewo.mis.authentication.dto.AddUserRoleRelationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * 权限操作管理
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2018-01-25
 * @since 1.0
 */
@Slf4j
@Component
public class AuthenticationManager {
    @Reference
    private UserRoleRelationService     userRoleRelationService;
    @Reference
    private ResourceRoleRelationService resourceRoleRelationService;
    @Reference
    private AuthenticationService authenticationService;

    @PostConstruct
    public void test() {
        //批量新增用户角色关联关系
        AddUserRoleRelationDto dto = new AddUserRoleRelationDto();
        dto.setTraceId(UUID.randomUUID().toString());

        userRoleRelationService.addUserRoleRelation(dto);
        //获取用户菜单列表
        /*ViewResourceRequestDto dto = new ViewResourceRequestDto();
        dto.setTraceId(UUID.randomUUID().toString());
        dto.setUserUid("e1023f4a67004d73af24b837e89b1dcc");
        dto.setSchoolId("001");
        dto.setAppKey("f7e186b56bde490b9cc02b8bb54a49f4");
        BaseResponse response = authenticationService.getViewSource(dto);
        log.info("处理结果:{}", JSON.toJSONString(response));*/
        /*demoService.say(" wang wang wang ");*/

        //新增用户角色关联关系
        /*AddUserRoleRelationDto dto = new AddUserRoleRelationDto();
        List<UserRoleRelationDto> userRoleRelationDtoList = new ArrayList<>();
        UserRoleRelationDto userRoleRelationDto = new UserRoleRelationDto();
        userRoleRelationDto.setUserUid("ce4af266cfb34b30af6e0eff92dd1112");
        userRoleRelationDto.setRoleId("177835398112739328");
        userRoleRelationDto.setSchoolUid("ff80818145de76070145dee3bbca6d53");
        userRoleRelationDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        userRoleRelationDtoList.add(userRoleRelationDto);

        dto.setUserRoleRelationDtoList(userRoleRelationDtoList);
        BaseResponse response = userRoleRelationService.addUserRoleRelation(null);*/


        //新增角色资源关联关系
        /*AddResourceRoleRelationDto dto = new AddResourceRoleRelationDto();
        List<RoleDto> roleDtoList = new ArrayList<>();
        RoleDto roleDto = new RoleDto();
        roleDto.setId("177835398112739328");
        roleDtoList.add(roleDto);
        List<ResourceDto> resourceDtoList = new ArrayList<>();
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setId("182553193211760640");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182883485743058944");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182883892217253888");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182883994776375296");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182884108446208000");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182893480647856128");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182893682280632320");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182893836337418240");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182896349279162368");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182896742415470592");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182896895717281792");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182896981943783424");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182897091125710848");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182897159723552768");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182897390783565824");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182897537772949504");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        resourceDto = new ResourceDto();
        resourceDto.setId("182897648422883328");
        resourceDto.setType(1);
        resourceDto.setAppKey("0lc2ab08ec6f4c99a40bbd6796cf9b72");
        resourceDtoList.add(resourceDto);

        dto.setRoleDtoList(roleDtoList);
        dto.setResourceDtoList(resourceDtoList);
        BaseResponse response = resourceRoleRelationService.addOrUpdateResourceRoleRelation(dto);
        log.info("处理结果:{}", response);*/
    }
}
