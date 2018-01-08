package com.seewo.mis.frame.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Dto 基础类,每一次闭环请求使用不同的traceId
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-06
 * @version 1.0
 */
@Data
public class BaseDto implements Serializable{
    /**日志唯一ID*/
    @NotNull
    private String traceId;
}
