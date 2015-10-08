package server2;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.wangyin.industry.fund.biz.api.XjkBizTransferOutFacade;
import com.wangyin.industry.fund.biz.api.entity.TransferOutOrder;
import com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        ApplicationConfig application = new ApplicationConfig();
        application.setName("fund-app-shoptrade");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();

        registry.setAddress("172.24.2.35:2181");
        // registry.setGroup("dubbo");
        registry.setTimeout(10000);
        // registry.setAddress(address);
        // registry.setUsername("aaa");
        // registry.setPassword("bbb");


        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

        // 引用远程服务
        ReferenceConfig<com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade> reference = new ReferenceConfig<com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
        reference.setApplication(application);
        reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
        reference.setInterface(IFundInfoQueryNSortFacade.class);
        reference.setVersion("1.0.0");
        reference.setGroup("shopbetafunc");
        reference.setGeneric(true);


        // 和本地bean一样使用xxxService
        IFundInfoQueryNSortFacade iFundInfoQueryNSortFacade = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
        System.out.println(iFundInfoQueryNSortFacade);

        XjkBizTransferOutFacade facade;
        // facade.serviceForDeposit(new TransferOutOrder());
        TransferOutOrder order = new TransferOutOrder();

        JSON json;
    }
}
