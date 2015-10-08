package server2;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;


public class TestAliJson {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Class clz = com.wangyin.industry.fund.biz.api.enums.BizTradeOrderType.class;
        System.out.println(ReflectionToStringBuilder.toString(clz.getEnumConstants()));

        Class clz2 = int.class;
        System.out.println(clz2.isPrimitive());
        clz2.newInstance();
    }

}
