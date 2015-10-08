package com.wangyin.test.fund.tool.service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.wangyin.test.fund.tool.bean.RpcRequestBean;
import com.wangyin.test.fund.tool.dubbo.DirectRefConfig;
import com.wangyin.test.fund.tool.dubbo.DubboRpcClient;
import com.wangyin.test.fund.tool.exception.BadRequestException;
import com.wangyin.test.fund.tool.util.ParameterUtils;

@Service
public class ProxyService {

    public JSONObject call(RpcRequestBean request) throws Exception {

        DirectRefConfig config = new DirectRefConfig();
        transToConfig(request, config);

        DubboRpcClient client = new DubboRpcClient(config);

        Method method = ParameterUtils.getMethod(config.getInterfaze(), request.getAction(), null);
        Map map = new HashMap();
        if (method != null) {
            Class[] paramclass = method.getParameterTypes();
            String[] className = new String[paramclass.length];
            for (int i = 0; i < paramclass.length; i++) {
                className[i] = paramclass[i].getName();
            }

            Object[] values = transToParams(paramclass, request);

            map = client.callService(method.getName(), className, values);
        }

        return JSONObject.fromObject(map);

    }

    private Object[] transToParams(Class[] paramclass, RpcRequestBean request) {
        Object[] params = new Object[paramclass.length];
        String data = request.getData();
        if (JSONUtils.mayBeJSON(data)) {
            JSONObject mapData = JSONObject.fromObject(data);
            if (paramclass.length == 1) {
                if (mapData.size() == 1) {
                    for (Object key : mapData.keySet())
                        params[0] = ParameterUtils.getParameters(paramclass[0], mapData.opt((String) key));
                } else {
                    params[0] = ParameterUtils.getParameters(paramclass[0], mapData);
                }
            } else {
                for (int i = 0; i < paramclass.length; i++) {
                    if (mapData.containsKey(String.valueOf(i))) {
                        params[i] = ParameterUtils.getParameters(paramclass[i], mapData.get(String.valueOf(i)));
                    }
                }
            }
        } else {
            params[0] = ParameterUtils.getParameters(paramclass[0], data);
        }

        return params;
    }

    private void transToConfig(RpcRequestBean request, DirectRefConfig config) throws Exception {

        if (StringUtils.isNotBlank(request.getApp())) {
            config.setApplicationConfig(new ApplicationConfig(request.getApp()));
        } else {
            config.setApplicationConfig(new ApplicationConfig(RpcRequestBean.APP_DEFAULT));
        }

        config.setVersion(request.getVersion());
        config.setGroup(request.getGroup());
        config.setGeneric(true);
        config.setUrl(request.getReference());

        String[] token = request.getReference().split("/");
        if (token.length > 1) {
            try {
                Class clazz = Class.forName(token[token.length - 1]);
                config.setInterfaze(clazz);
            } catch (ClassNotFoundException e) {
                throw new BadRequestException("没找到接口" + token[token.length - 1] + "请检查jar包依赖", e);
            }
        }

    }

}
