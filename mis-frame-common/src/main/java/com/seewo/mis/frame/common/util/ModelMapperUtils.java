package com.seewo.mis.frame.common.util;

import com.seewo.mis.frame.common.constant.BaseEnum;
import com.seewo.mis.frame.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.List;
import java.util.stream.Collectors;

import static com.seewo.mis.frame.common.constant.BaseEnum.getEnumOfBaseEnum;
import static com.seewo.mis.frame.constant.ErrorsEnum.STRING_CONVERSION_TO_ENUM_FAIL;

/**
 * 领域模型转换工具类
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Slf4j
public class ModelMapperUtils {
    private ModelMapperUtils() {
        throw new InstantiationError("can not instantiate utils class");
    }

    private static final ModelMapper MIS_MODEL_MAPPER = new ModelMapper();


    /**
     * 简单类型转换,属性类型暂时不支持枚举
     *
     * @param source 源
     * @param target 目标
     * @param <S>    S
     * @param <T>    T
     * @return <S, T>
     */
    public static <S, T> T map(S source, Class<T> target,boolean ambiguityIgnored) {
        MIS_MODEL_MAPPER.getConfiguration().setAmbiguityIgnored(ambiguityIgnored);
        return MIS_MODEL_MAPPER.map(source, target);
    }

    /**
     * 将list中的每一个对象转换
     *
     * @param source 源
     * @param target 目标
     * @param <S>    S
     * @param <T>    T
     * @return
     */
    public static <S, T> List<T> mapList(List<S> source, Class<T> target) {
        return source.
                parallelStream().
                map(s -> MIS_MODEL_MAPPER.map(s, target)).
                collect(Collectors.toList());
    }

    /**
     * 简单类型转换,属性类型暂时不支持枚举
     *
     * @param source 源
     * @param target 目标
     * @param <S>    S
     * @param <T>    T
     * @return <S, T>
     */
    public static <S, T> T mapSupportEum(S source, Class<T> target) {
        Converter<String, Enum> enumConverter = new Converter<String, Enum>() {
            @Override
            public Enum convert(MappingContext<String, Enum> mappingContext) {
                return (Enum) getEnumByIndex(mappingContext.getSource(), mappingContext.getDestinationType().getComponentType());
            }

            private Object getEnumByIndex(String source, Class destinationType) {
                Object[] enumConstants = destinationType.getEnumConstants();
                Integer index = Integer.valueOf(source);
                if (BaseEnum.class.isAssignableFrom(destinationType)) {
                    return getEnumOfBaseEnum(index, destinationType);
                } else {
                    for (int i = 0; i < enumConstants.length; i++) {
                        Enum enumConstant = (Enum) enumConstants[i];
                        if (enumConstant.ordinal() == index) {
                            return enumConstant;
                        }
                    }
                    throw new BaseException(STRING_CONVERSION_TO_ENUM_FAIL);
                }
            }
        };
        MIS_MODEL_MAPPER.addConverter(enumConverter);
        return MIS_MODEL_MAPPER.map(source, target);
    }

}
