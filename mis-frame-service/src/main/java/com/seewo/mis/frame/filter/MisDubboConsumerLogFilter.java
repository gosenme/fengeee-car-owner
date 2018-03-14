package com.seewo.mis.frame.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Field;

import static com.seewo.mis.frame.common.constant.BaseConstant.LOG_MAX_LENGTH;

/**
 * 消费端日志拦截器
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Activate(group = Constants.CONSUMER, after = {"exception", "monitor", "timeout", "trace", "context", "generic", "classloader", "echo"})
@Slf4j
public class MisDubboConsumerLogFilter implements Filter {

    private String getApplicationName(Invoker<?> invoker) {
        return invoker.getUrl().getParameter("application");
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);

        if (result.hasException()) {
            try {
                Field exception = RpcResult.class.getDeclaredField("exception");
                exception.setAccessible(true);
                result.getException().setStackTrace(new StackTraceElement[]{});
                exception.set(result, new RpcException(invoker.getUrl().getAddress() + "  " + getApplicationName(invoker), result.getException()));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                log.error("重置异常发生异常", e);
            }
        }
        log(start, result, invocation);
        return result;
    }

    private void log(long start, Result result, Invocation invocation) {
        long invokeTime = System.currentTimeMillis() - start;
        if (result.hasException()) {
            String resultJson = result.getException().getMessage();
            if (resultJson != null && resultJson.length() > LOG_MAX_LENGTH) {
                log.info("s_dubbo:{}ms {}.{}: argLen:{} error:{}", invokeTime, invocation.getInvoker().getInterface().getSimpleName(), invocation.getMethodName(), resultJson.length(), resultJson.substring(0, LOG_MAX_LENGTH));
            } else {
                log.info("s_dubbo:{}ms {}.{}: error:{}", invokeTime, invocation.getInvoker().getInterface().getSimpleName(), invocation.getMethodName(), resultJson);
            }
        } else {
            String resultJson = JSON.toJSONString(ObjectUtils.defaultIfNull(result.getValue(), ""));
            if (resultJson.length() > LOG_MAX_LENGTH) {
                log.info("s_dubbo:{}ms {}.{}: argLen:{} ret:{}", invokeTime, invocation.getInvoker().getInterface().getSimpleName(), invocation.getMethodName(), resultJson.length(), resultJson.substring(0, LOG_MAX_LENGTH));
            } else {
                log.info("s_dubbo:{}ms {}.{}: ret:{}", invokeTime, invocation.getInvoker().getInterface().getSimpleName(), invocation.getMethodName(), resultJson);
            }
        }


    }

}