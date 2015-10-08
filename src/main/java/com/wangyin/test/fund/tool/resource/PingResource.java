package com.wangyin.test.fund.tool.resource;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yangjun3
 * @date 2015年9月6日 上午10:55:40
 * @description: 验证服务是否启动
 */
@RequestMapping("/ping")
@Controller
public class PingResource {

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public String ping() {
        return "ping successed." + new Date();
    }
}
