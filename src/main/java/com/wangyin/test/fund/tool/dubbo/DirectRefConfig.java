package com.wangyin.test.fund.tool.dubbo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yangjun3
 * @date 2015年9月24日 下午2:51:52
 * @description: 直连referenceConfig
 */
@ToString
public class DirectRefConfig extends RefConfig {
    @Getter
    @Setter
    private String url;

}
