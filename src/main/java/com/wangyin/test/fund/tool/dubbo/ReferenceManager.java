package com.wangyin.test.fund.tool.dubbo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;

/**
 * @author yangjun3
 * @date 2015年9月6日 下午5:24:05
 * @description: 直连引用管理
 */
public class ReferenceManager {
    public static ReferenceManager getInstance() {
        return singleton;
    }

    private static ReferenceManager singleton = new ReferenceManager();

    /**
     * 引用缓存
     */
    private static Map<Class, ReferenceConfig> serviceMap = new ConcurrentHashMap<Class, ReferenceConfig>();
    private ReferenceConfigCache cache = ReferenceConfigCache.getCache();

    private ReferenceManager() {
    }

    public <T> T getService(DirectRefConfig config) {
        ReferenceConfig<T> reference = getReference(config);
        T service = cache.get(reference);
        if (service == null) {
            service = reference.get();
        }
        return service;
    }

    private <T> ReferenceConfig<T> getReference(DirectRefConfig config) {
        if (serviceMap.containsKey(config.getInterfaze())) {
            return serviceMap.get(config.getInterfaze());
        }

        ReferenceConfig<T> reference = new ReferenceConfig<T>();
        reference.setInterface(config.getInterfaze());
        reference.setApplication(config.getApplicationConfig());
        reference.setGroup(config.getGroup());
        reference.setVersion(config.getVersion());
        reference.setUrl(config.getUrl());
        // reference.setRegistry(registry);


        reference.setGeneric(config.isGeneric());

        serviceMap.put(config.getInterfaze(), reference);
        return reference;
    }

    public void delete(DirectRefConfig config) {
        ReferenceConfig reference = getReference(config);
        serviceMap.remove(config.getInterfaze());
        cache.destroy(reference);
    }

    public void clear(DirectRefConfig config) {
        serviceMap.clear();
        cache.destroyAll();
    }


}
