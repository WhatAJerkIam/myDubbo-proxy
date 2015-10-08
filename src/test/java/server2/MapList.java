package server2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class MapList<T> extends HashMap<String, List<T>> {

    public static void main(String[] args) {
        System.out.println(Map.class.isAssignableFrom(MapList.class));
        System.out.println(List.class.isAssignableFrom(MapList.class));
        System.out.println(MapList.class.getName());
        System.out.println(MapList.class.getSimpleName());
        // System.out.println(MapList.class.getSuperclass());
        // System.out.println(MapList.class.getGenericSuperclass());
        // System.out.println(MapList.class.getGenericSuperclass().getClass().);
        


        
        MapList ml = new MapList<Integer>();
        System.out.println(ReflectionToStringBuilder.toString(ml.getClass().getGenericSuperclass()));

        System.out.println(ArrayList.class.isArray());
    }

}
