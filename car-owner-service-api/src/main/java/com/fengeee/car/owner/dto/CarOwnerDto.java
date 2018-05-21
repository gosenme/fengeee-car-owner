package com.fengeee.car.owner.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author wanggaoxiang@cvte.com
 * @since 1.0
 */
@Data
@Accessors(chain = true)
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class CarOwnerDto {
    @ApiModelProperty(value = "车主文件路径")
    private String filePath;
}
