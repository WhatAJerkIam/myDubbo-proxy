package com.wangyin.test.fund.tool.dubbo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.alibaba.dubbo.config.ApplicationConfig;

/**
 * @author yangjun3
 * @date 2015年9月24日 下午2:50:52
 * @description: referenceConfig
 */
@ToString
public class RefConfig {
    @Getter
    @Setter
    protected Class interfaze; // 接口类

    @Getter
    @Setter
    protected ApplicationConfig applicationConfig; // 应用配置

    @Getter
    @Setter
    protected String group; // 组

    @Getter
    @Setter
    protected String version; // 版本

    @Getter
    @Setter
    protected boolean generic; // dubbo调用方式，是否使用genericService

}
