package com.seewo.mis.frame.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.fastjson.JSON;
import com.seewo.mis.frame.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;

import java.lang.reflect.Field;
import java.util.UUID;

import static com.seewo.mis.frame.common.constant.BaseConstant.LOG_MAX_LENGTH;
import static com.seewo.mis.frame.common.constant.BaseConstant.TRACE_ID;

/**
 * 提供端日志拦截器
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Activate(group = Constants.PROVIDER, after = {"exception", "monitor", "timeout", "trace", "context", "generic", "classloader", "echo"})
@Slf4j
public class MisDubboProviderLogFilter implements Filter {

    private String getApplicationName(Invoker<?> invoker) {
        return invoker.getUrl().getParameter("application");
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        if(StringUtils.isEmpty(RpcContext.getContext().getAttachment(TRACE_ID))){
            RpcContext.getContext().setAttachment(TRACE_ID,StringUtils.replace(UUID.randomUUID().toString(),"-",""));
        }
        ThreadContext.put(TRACE_ID,RpcContext.getContext().getAttachment(TRACE_ID));
        log.info("{}端,接口:{} 方法:{} , 入参:{}",Constants.PROVIDER, invocation.getInvoker().getInterface(), invocation.getMethodName(),invocation.getArguments());
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        if (result.hasException()) {
            long invokeTime = System.currentTimeMillis() - start;
            if ((result.getException() instanceof BaseException) || (result.getException().getCause() instanceof BaseException)) {
                log.info("{}端,耗时:{}ms,业务异常信息:{}", Constants.PROVIDER, invokeTime, result.getException().getCause());
            } else {
                log.error("{}端,耗时:{}ms,系统异常信息:{}", Constants.PROVIDER, invokeTime, result.getException());
            }
            result.getException().setStackTrace(new StackTraceElement[]{});
            if (!(result.getException() instanceof RpcException)) {
                try {
                    Field exception = RpcResult.class.getDeclaredField("exception");
                    exception.setAccessible(true);
                    result.getException().setStackTrace(new StackTraceElement[]{});
                    exception.set(result, new RpcException("异常源头:" + invoker.getUrl().getAddress() + ",应用名:" + getApplicationName(invoker), result.getException()));
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    log.error("重置异常发生异常:{}", e);
                }
            }
        } else {
            long invokeTime = System.currentTimeMillis() - start;
            String resultJson = JSON.toJSONString(ObjectUtils.defaultIfNull(result.getValue(), ""));
            if (resultJson.length() > LOG_MAX_LENGTH) {
                log.info("{}端,接口:{},方法:{},耗时:{},请求结果:{},结果长度:{}",Constants.PROVIDER, invocation.getInvoker().getInterface(), invocation.getMethodName(),invokeTime,resultJson.substring(0, LOG_MAX_LENGTH), resultJson.length());
            } else {
                log.info("{}端,接口:{},方法:{},耗时:{},请求结果:{}",Constants.PROVIDER, invocation.getInvoker().getInterface(), invocation.getMethodName(),invokeTime, resultJson);
            }
        }
        return result;
    }
}