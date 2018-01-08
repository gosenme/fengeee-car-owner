package com.seewo.mis.frame.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 性别枚举
 * 0: 未确定
 * 1: 男
 * 2: 女
 *
 * 枚举规范: 1.第一个下标对应的是0 ,而且是一个默认值
 *          2.枚举需要实现BaseEnum接口.才方便做转换
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SexEnum implements BaseEnum {
    /**默认*/
    DEFAULT(0),
    /**男*/
    MAN(1),
    /**女*/
    WOMAN(1);
    private int    index;

    @Override
    public Integer getIndex() {
        return index;
    }
}
