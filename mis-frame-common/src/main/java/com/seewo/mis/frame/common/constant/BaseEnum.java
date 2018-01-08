package com.seewo.mis.frame.common.constant;


import com.seewo.mis.frame.common.exception.BaseException;

import static com.seewo.mis.frame.constant.ErrorsEnum.STRING_CONVERSION_TO_ENUM_FAIL;


/**
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-12
 * @version 1.0
 */
public interface BaseEnum {
    Integer getIndex();

    static <T extends BaseEnum> T getEnumOfBaseEnum(Integer index, Class<T> className) {
        Object[] enumConstants = className.getEnumConstants();
        for (int i = 0; i < enumConstants.length; i++) {
            BaseEnum enumConstant = (BaseEnum) enumConstants[i];
            if (enumConstant.getIndex().equals(index)) {
                return (T) enumConstant;
            }
        }
        throw new BaseException(STRING_CONVERSION_TO_ENUM_FAIL);
    }

}
