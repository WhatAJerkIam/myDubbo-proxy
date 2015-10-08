package com.wangyin.test.fund.tool.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author yangjun3
 * @date 2015年9月7日 下午2:39:18
 * @description: HTTP请求参数
 */
@ToString
public class RpcRequestBean {
    public static final String APP_DEFAULT = "dubbotest";

    @Getter
    @Setter
    private String reference; // e.g:"dubbo://172.24.4.69:20883/com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade"

    @Getter
    @Setter
    private String action;// method

    @Getter
    @Setter
    private String version;// interface version

    @Getter
    @Setter
    private String group;// service group e.g: shopbetafunc

    @Getter
    @Setter
    private String app; // application name

    @Getter
    @Setter
    private String data; // method params json or string

    public static void main(String[] args) {
        RpcRequestBean lombokbean = new RpcRequestBean();
        lombokbean.setAction("1");
        System.out.println(lombokbean);
    }
}
