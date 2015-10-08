package com.wangyin.test.fund.tool.dubbo;

import com.alibaba.dubbo.rpc.service.GenericService;
import com.thoughtworks.xstream.InitializationException;

/**
 * @author yangjun3
 * @date 2015年9月24日 下午2:52:25
 * @description: dubboRpc调用客户端
 */
public class DubboRpcClient {
    private DirectRefConfig refConfig;

    public DubboRpcClient(DirectRefConfig config) {
        if (config == null)
            throw new InitializationException("refConfig is null");
        this.refConfig = config;
    }

    public <T> T callService(String methodName, String[] parameterTypes, Object[] params) {
        ReferenceManager manager = ReferenceManager.getInstance();
        GenericService service = manager.getService(this.refConfig);
        return (T) service.$invoke(methodName, parameterTypes, params);
    }

}
