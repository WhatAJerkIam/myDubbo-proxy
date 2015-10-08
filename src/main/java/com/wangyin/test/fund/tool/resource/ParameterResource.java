package com.wangyin.test.fund.tool.resource;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/template")
@Controller
public class ParameterResource {

    @ResponseBody
    @RequestMapping
    public String getParameter(String clazz, String method) {
        System.out.println(clazz);
        System.out.println(method);
        return "ping successed." + new Date();
    }

}
