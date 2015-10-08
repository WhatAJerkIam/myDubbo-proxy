package com.wangyin.test.fund.tool.resource;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wangyin.test.fund.tool.bean.RpcRequestBean;
import com.wangyin.test.fund.tool.exception.BadRequestException;
import com.wangyin.test.fund.tool.exception.InternalErrorException;
import com.wangyin.test.fund.tool.service.ProxyService;

@RequestMapping("/directproxy")
@Controller
public class ProxyResource {

    @Resource
    private ProxyService proxyService;

    private static final Log log = LogFactory.getLog(ProxyResource.class);

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=utf-8")
    public String proxy(@RequestBody String json) throws Exception {
        // e.g:
        // {"reference":"dubbo://172.24.4.69:20883/com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade","group":"shopbetafunc","version":"1.0.0","data":"yyyyyyyyyyyyyyyyyyy"}
        log.info("接受请求：" + json);

        try {

            JSONObject p = JSONObject.fromObject(json);
            RpcRequestBean bean = new RpcRequestBean();
            bean.setAction(p.optString("action"));
            bean.setData(p.optString("data"));
            bean.setApp(p.optString("app"));
            bean.setGroup(p.optString("group"));
            bean.setReference(p.optString("reference"));
            bean.setVersion(p.optString("version"));

            JSONObject responce = proxyService.call(bean);

            log.info("返回数据：" + responce);
            return responce.toString();
        } catch (BadRequestException e) {
            throw e;
        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException("未知异常", e);
        }

    }
}
