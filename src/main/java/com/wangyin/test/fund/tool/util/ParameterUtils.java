package com.wangyin.test.fund.tool.util;

import static org.reflections.ReflectionUtils.getConstructors;
import static org.reflections.ReflectionUtils.withParameters;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.reflections.ReflectionUtils;

import com.google.common.primitives.Primitives;
import com.wangyin.test.fund.tool.exception.BadRequestException;
import com.wangyin.test.fund.tool.reflect.NoStaticFieldPredicate;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParameterUtils {
    private final static String[] dateFormat = new String[] { "yyyy-MM-dd", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss",
            "yyyyMMddHHmmss" };

    /**
     * @description: TODO
     * @param clazz
     *            类名
     * @param name
     *            方法名
     * @param argcount
     *            方法可能含有的参数最小值
     * @return
     */
    public static Method getMethod(Class clazz, String name, Integer argcount) {
        if (clazz == null)
            return null;

        Method[] ms = clazz.getMethods();
        for (Method m : ms) {
            if (m.getName().equals(name)) {
                if (argcount == null || argcount <= m.getParameterTypes().length) {
                    return m;
                }
            }
        }
        return null;
    }

    public static Object getParameters(Class clazz, Object source) {
        Object instance = null;
        if (isNull(source))
            return null;
        if (clazz == null) {
            instance = getParameters(source);
        } else if (String.class.equals(clazz)) {
            instance = String.valueOf(source);
        } else if (TypeUtils.isEnum(clazz)) {
            instance = getEnum(clazz, source);
        } else if (clazz.isPrimitive() || TypeUtils.isWrapClass(clazz)) {
            instance = getPrimitive(clazz, source);
        } else if (TypeUtils.isMap(clazz)) {
            instance = parseMap(source);
        } else if (TypeUtils.isList(clazz)) {
            instance = parseList(source);
        } else if (TypeUtils.isArray(clazz)) {
            instance = parseArray(clazz, source);
        } else if (clazz.isInterface() || Class.class.equals(clazz)) {
            // do nothing
        } else if (TypeUtils.isNumber(clazz)) {
            instance = parseNumber(clazz, source);
        } else if (TypeUtils.isDate(clazz)) {
            instance = parseDate(clazz, source);
        } else {
            instance = parseBean(clazz, source);
        }

        return instance;
    }

    private static boolean isNull(Object source) {
        return source == null || "null".equalsIgnoreCase(String.valueOf(source));
    }

    private static Object parseDate(Class clz, Object values2) {
        if (isNull(values2))
            return null;
        if (clz.isAssignableFrom(values2.getClass())) {
            return values2;
        }
        try {
            return DateUtils.parseDateStrictly(String.valueOf(values2), dateFormat);
        } catch (ParseException e) {
            throw new IllegalArgumentException(values2 + " can not be cast to " + clz.getName());
        }
    }

    private static Object getPrimitive(Class clz, Object values2) {
        if (isNull(values2))
            return null;
        clz = Primitives.wrap(clz);
        if (clz.equals(Primitives.wrap(values2.getClass()))) {
            return values2;
        } else if (TypeUtils.isBoolean(clz)) {
            Boolean bool = BooleanUtils.toBooleanObject(values2.toString());
            if (bool == null)
                throw new IllegalArgumentException(values2 + " can not be cast to " + clz.getName());
            return bool;

        } else if (clz.equals(Character.class)) {
            if (values2 instanceof String && ((String) values2).length() == 1) {
                return ((String) values2).charAt(0);
            }
            throw new IllegalArgumentException(values2 + " can not be cast to " + clz.getName());
        } else {
            Set<Constructor> constructors = getConstructors(clz, withParameters(String.class));
            if (!constructors.isEmpty()) {
                Constructor constructor = (Constructor) constructors.toArray()[0];
                try {
                    Object prim = constructor.newInstance(String.valueOf(values2));
                    return prim;
                } catch (Exception e) {
                    throw new IllegalArgumentException("primitive type cannot be created.");
                }
            } else {

                return null;
            }
        }

    }

    private static Object parseNumber(Class clz, Object values2) {
        if (isNull(values2)) {
            return null;
        }

        if (clz.isAssignableFrom(values2.getClass())) {
            return values2;
        }

        Object parameter = values2;
        Constructor constructor = null;
        Set<Constructor> constructors = getConstructors(clz, withParameters(parameter.getClass()));
        if (!constructors.isEmpty()) {
            constructor = (Constructor) constructors.toArray()[0];
            try {
                return constructor.newInstance(parameter);
            } catch (Exception e) {
                throw new IllegalArgumentException(String.format("number type [%s] cannot be created , because [%s]",
                    clz.getName(), e));
            }
        } else {
            constructors = getConstructors(clz, withParameters(String.class));
            if (!constructors.isEmpty()) {
                constructor = (Constructor) constructors.toArray()[0];
                try {
                    return constructor.newInstance(String.valueOf(parameter));
                } catch (Exception e) {
                    throw new IllegalArgumentException(String.format(
                        "number type [%s] cannot be created , because [%s]", clz.getName(), e));
                }
            }
        }
        return null;
    }

    private static Object parseBean(Class clazz, Object source) {
        Set<Field> fields = ReflectionUtils.getAllFields(clazz, new NoStaticFieldPredicate());
        Map paramMap = toMap(source);
        Map map = new HashMap();
        for (Field f : fields) {
            if (paramMap.containsKey(f.getName())) {
                Object param = getParameters(f.getType(), paramMap.get(f.getName()));
                if (param != null) {
                    map.put(f.getName(), param);
                }
            }
        }
        map.put("class", clazz.getName());
        return map;
    }

    private static Map<String, Object> toMap(Object source) {
        if (TypeUtils.isMap(source))
            return (Map) source;

        if (source instanceof String && JSONUtils.mayBeJSON(String.valueOf(source))) {
            return JSONObject.fromObject(source);
        }

        Map map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                // if (!key.equals("class")) {
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(source);
                    map.put(key, value);
                // }

            }

        } catch (Exception e) {
            System.out.println("Bean2Map Error " + e);
        }
        return map;
    }

    private static Object parseList(Object source) {
        if (TypeUtils.isList(source))
            return (List) source;
        throw new BadRequestException("参数格式异常：" + ReflectionToStringBuilder.toString(source)
                + "can not be cast to a list.");
    }

    private static Object parseMap(Object source) {
        if (TypeUtils.isMap(source))
            return (Map) source;
        throw new BadRequestException("参数格式异常：" + ReflectionToStringBuilder.toString(source)
                + "can not be cast to a map.");
    }

    private static Object getParameters(Object source) {
        return JSONObject.fromObject(source);
    }

    private static Object parseArray(Class clz, Object source) {
        if (!TypeUtils.isArray(source.getClass())) {
            throw new IllegalArgumentException(source + " is not an array of " + clz.getName());
        }
        Object[] objs = (Object[]) source;
        Class elementClz = clz.getComponentType();
        Object[] ret = new Object[objs.length];
        for (int i = 0; i < objs.length; i++) {
            ret[i] = getParameters(elementClz, objs[i]);
        }
        return ret;
    }

    private static Object getEnum(Class clz, Object values2) {
        if (isNull(values2))
            return null;
        Object[] enumms = clz.getEnumConstants();
        for (Object enumm : enumms) {
            if (enumm.equals(values2) || enumm.toString().equals(values2)) {
                return enumm;
            }
        }
        throw new ClassCastException(values2 + " can not be cast to " + clz.getName());
    }

    public static void main(String[] args) {
        System.out.println(Map.class.isAssignableFrom(HashMap.class));
        System.out.println(Map.class.isAssignableFrom(HashSet.class));
    }
}
