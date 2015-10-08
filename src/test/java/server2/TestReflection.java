package server2;

import org.reflections.ReflectionUtils;

import com.wangyin.industry.fund.biz.api.entity.TransferOutOrder;

public class TestReflection {

    public int get(int i) {
        return i;
    }

    public static void main(String[] args) throws SecurityException, NoSuchMethodException {
        // Set<Method> md = ReflectionUtils.getAllMethods(List.class,
        // ReflectionUtils.withParametersAssignableTo(Collection.class),
        // ReflectionUtils.withReturnType(boolean.class));

        // System.out.println(md);

        System.out.println(ReflectionUtils.getAllFields(TransferOutOrder.class));
    }

}
