package server2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.wangyin.industry.fundshop.app.model.FundShopResponse;
import com.wangyin.industry.fundshop.query.enums.FundQueryOrderBy;
import com.wangyin.industry.fundshop.query.enums.FundQueryOrderColumn;
import com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade;
import com.wangyin.industry.fundshop.query.model.FundInfoQueryNSortRequest;
import com.wangyin.industry.fundshop.query.model.FundInfoQueryNSortResult;
import com.wangyin.test.fund.tool.dubbo.DirectRefConfig;
import com.wangyin.test.fund.tool.dubbo.ReferenceManager;
import com.wangyin.test.fund.tool.dubbo.DubboRpcClient;

public class TestRefManager {

    public void test1() {

        ReferenceManager rf = ReferenceManager.getInstance();
        DirectRefConfig conf = new DirectRefConfig();
        conf.setApplicationConfig(new ApplicationConfig("dubbotest"));
        conf.setInterfaze(IFundInfoQueryNSortFacade.class);
        conf.setUrl("dubbo://172.24.4.69:20883/com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade");
        conf.setVersion("1.0.0");
        conf.setGroup("shopbetafunc");
        IFundInfoQueryNSortFacade service = rf.getService(conf);
        FundShopResponse<FundInfoQueryNSortResult> res = service.queryDicts(new FundInfoQueryNSortRequest());
        System.out.println(res);
    }

    private void test2() {
        DirectRefConfig conf = new DirectRefConfig();
        conf.setApplicationConfig(new ApplicationConfig("dubbotest"));
        conf.setInterfaze(IFundInfoQueryNSortFacade.class);
        conf.setUrl("dubbo://172.24.4.69:20883/com.wangyin.industry.fundshop.query.facade.IFundInfoQueryNSortFacade");
        conf.setVersion("1.0.0");
        conf.setGroup("shopbetafunc");
        conf.setGeneric(true);
        DubboRpcClient client = new DubboRpcClient(conf);

        Map params = new HashMap();
        List list = new ArrayList();
        Map subMap = new HashMap();
        subMap.put("column", FundQueryOrderColumn.RISKLEVEL);
        subMap.put("orderby", FundQueryOrderBy.DESC);
        list.add(subMap);
        params.put("anchorId", 26);
        // params.put("fundCode", "2");
        params.put("infoNeed", false);
        params.put("pageSize", 20);
        params.put("orders", list);
        Map res = client.callService("query",
            new String[] { "com.wangyin.industry.fundshop.query.model.FundInfoQueryNSortRequest" },
            new Object[] { params });

        System.out.println(JSONObject.fromObject(res));
        System.out.println(JSONObject.fromObject(params));

    }


    public static void main(String[] args) {
        TestRefManager t = new TestRefManager();
        // t.test1();
        t.test2();

    }
}
