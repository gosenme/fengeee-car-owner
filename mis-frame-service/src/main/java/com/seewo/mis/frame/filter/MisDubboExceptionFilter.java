package com.seewo.mis.frame.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.seewo.mis.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

/**
 * 清理DUBBO RPC调用异常栈
 *
 * @author : wanggaoxiang@cvte.com
 * Date: 2017-12-01
 * @version 1.0
 */
@Activate(group = Constants.PROVIDER, after = {"exception", "monitor", "timeout", "trace", "context", "generic", "classloader", "echo","validation"})
@Slf4j
public class MisDubboExceptionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        Result result = invoker.invoke(invocation);
        if (result.getException() instanceof BaseException) {
            RpcException exception = new RpcException(result.getException());
            exception.setStackTrace(new StackTraceElement[]{});
            result.getException().setStackTrace(new StackTraceElement[]{});
            return new RpcResult(exception);
        }

        return result;

    }

}
