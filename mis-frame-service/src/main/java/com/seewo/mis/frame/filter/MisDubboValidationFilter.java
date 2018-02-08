package com.seewo.mis.frame.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.validation.Validation;
import com.alibaba.dubbo.validation.Validator;
import com.seewo.mis.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolationException;

import static com.seewo.mis.frame.constant.ErrorsEnum.REQUEST_PARAMS_NOT_VALID;

/**
 * 参数校验拦截器
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-22
 * @version 1.0
 */
@Slf4j
@Activate(group = {Constants.CONSUMER, Constants.PROVIDER}, value = Constants.VALIDATION_KEY, order = 10000)
public class MisDubboValidationFilter implements Filter {

    private Validation validation;

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (validation != null && !invocation.getMethodName().startsWith("$")
                && ConfigUtils.isNotEmpty(invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.VALIDATION_KEY))) {
            try {
                Validator validator = validation.getValidator(invoker.getUrl());
                if (validator != null) {
                    validator.validate(invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
                }
            } catch (ConstraintViolationException ex) {
                if (log.isWarnEnabled()) {
                    log.warn("dubbo参数校验失败:{}", ex.toString());
                }
                if (ex.getConstraintViolations() != null && !ex.getConstraintViolations().isEmpty()) {
                    StringBuilder message = new StringBuilder();
                    message.append("dubbo参数校验失败-->");
                    ex.getConstraintViolations().forEach(v -> message.append(v.getPropertyPath()).append(":").append(v.getMessage()).append(","));
                    return new RpcResult(new BaseResponse(REQUEST_PARAMS_NOT_VALID, message.toString()));
                }
            } catch (Exception e) {
                if (log.isWarnEnabled()) {
                    log.warn("dubbo参数校验失异常:{}", e.getMessage());
                }
                return new RpcResult(new BaseResponse(REQUEST_PARAMS_NOT_VALID));
            }
        }
        return invoker.invoke(invocation);
    }
}
