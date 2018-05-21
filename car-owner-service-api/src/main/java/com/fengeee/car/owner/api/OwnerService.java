package com.fengeee.car.owner.api;

import com.seewo.mis.common.response.BaseResponse;

/**
 * @author wanggaoxiang@cvte.com
 * @since 1.0
 */
public interface OwnerService {
    /**
     * 从文件中导入车主信息
     *
     * @param fileName 车主信息文件
     * @return 导入结果
     */
    BaseResponse addOwnerFromFile(String fileName);
}
