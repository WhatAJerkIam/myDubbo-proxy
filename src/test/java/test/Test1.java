package test;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.reflections.Reflections;

import com.wangyin.test.fund.tool.reflect.ReflectionInstance;
import com.wangyin.test.fund.tool.util.ParameterUtils;

public class Test1 {

    public static JsonConfig getJsonConfig() {
        JsonConfig conf = new JsonConfig();
        conf.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
            private final String format = "yyyy-MM-dd HH:mm:ss";

            public Object processObjectValue(String arg0, Object arg1, JsonConfig arg2) {
                if (arg1 == null)
                    return "";
                if (arg1 instanceof java.util.Date) {
                    String str = new SimpleDateFormat(format).format((java.util.Date) arg1);
                    return str;
                }
                return arg1.toString();
            }

            public Object processArrayValue(Object arg0, JsonConfig arg1) {
                return null;
            }
        });

        return conf;

    }

    public static void main(String[] args) {
        Reflections rf = new Reflections("com.wangyin.industry.fundshop.*", "com.wangyin.industry.fund.*");
        Set<Class<? extends Serializable>> classes = rf.getSubTypesOf(Serializable.class);

        for (Class c : classes) {
            ReflectionInstance reflectionInstance = new ReflectionInstance(c);
            Object instance = null;
            try {
                instance = reflectionInstance.build();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("---------------------------------------");
                continue;
            }

            System.out.println(ReflectionToStringBuilder.toString(instance));
            JSONObject jsonInstance = JSONObject.fromObject(instance, getJsonConfig());
            System.out.println(jsonInstance);

            Object rfInstance = ParameterUtils.getParameters(c, jsonInstance);
            JSONObject rfJson = JSONObject.fromObject(rfInstance, getJsonConfig());
            System.out.println(rfJson);
            System.out.println(rfJson.equals(jsonInstance));
            System.out.println("---------------------------------------");
        }
    }

}
