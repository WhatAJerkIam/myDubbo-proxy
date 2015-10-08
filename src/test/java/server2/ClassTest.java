package server2;

import java.io.Serializable;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.reflections.Reflections;

import com.wangyin.test.fund.tool.reflect.ReflectionInstance;

public class ClassTest {

    public static void main(String[] args) {
        Reflections rf = new Reflections("com.wangyin.industry.fundshop.*", "com.wangyin.industry.fund.*");
        Set<Class<? extends Serializable>> set = rf.getSubTypesOf(Serializable.class);
        for (Class clz : set) {
            if (!clz.isInterface() && !com.wangyin.test.fund.tool.util.TypeUtils.isEnum(clz)) {

                try {
                    ReflectionInstance obj = new ReflectionInstance(clz);
                    // ClassTest.print(obj.build());
                    System.out.println(JSONObject.fromObject(obj.build()));
                } catch (Exception e) {
                    System.out.println(clz.getName());
                    e.printStackTrace();
                }

            }
        }

    }

    public static void print(Object obj) {
        System.out.println(ReflectionToStringBuilder.toString(obj));
    }

}
