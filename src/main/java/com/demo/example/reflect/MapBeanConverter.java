package com.demo.example.reflect;

import com.demo.constant.ProgramConstant;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * MapBeanConverter
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class MapBeanConverter {

    /**
     * 传入一个遵守Java Bean约定的对象，读取它的所有属性，存储成为一个Map
     * 例如，对于一个DemoJavaBean对象 { id = 1, name = "ABC" }
     * 应当返回一个Map { id -> 1, name -> "ABC", longName -> false }
     * 提示：
     * 1. 读取传入参数bean的Class
     * 2. 通过反射获得它包含的所有名为getXXX/isXXX，且无参数的方法（即getter方法）
     * 3. 通过反射调用这些方法并将获得的值存储到Map中返回
     *
     * @param bean bean
     * @return Map
     */
    private static Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> map = new HashMap<>(ProgramConstant.MAP_INIT_CAPACITY);
        try {
            Class<?> clazz = bean.getClass();
            Method[] declaredMethods = clazz.getDeclaredMethods();
            Arrays.stream(declaredMethods)
                    .filter(method -> (method.getName().startsWith("get") || method.getName().startsWith("is")) && method.getParameterCount() == 0)
                    .forEach(method -> {
                        try {
                            map.put(getKeyByGetter(method), PropertyUtils.getProperty(bean, getKeyByGetter(method)));
                        } catch (Exception e) {
                            System.out.println("错误key：" + getKeyByGetter(method));
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private static String getKeyByGetter(Method method) {
        String replace = method.getName()
                .replace("get", "")
                .replace("is", "");
        if ("".equals(replace)) {
            return method.getName();
        }
        StringBuilder sb = new StringBuilder(replace);
        char c = (char) (replace.charAt(0) + 32);
        sb.deleteCharAt(0).insert(0, c);
        return sb.toString();
    }

    private static DemoJavaBean mapToBean(Map<String, Object> map) {
        DemoJavaBean demoJavaBean = null;
        try {
            demoJavaBean = DemoJavaBean.class.newInstance();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                PropertyUtils.setProperty(demoJavaBean, entry.getKey(), entry.getValue());
            }
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return demoJavaBean;
    }

    public static void main(String[] args) {
        DemoJavaBean bean = new DemoJavaBean();
        bean.setId(100);
        bean.setName("AAAAAAAAAAAAAAAAAAA");
        System.out.println(beanToMap(bean));

        Map<String, Object> map = new HashMap<>(ProgramConstant.MAP_INIT_CAPACITY);
        map.put("id", 123);
        map.put("name", "ABCDEFG");
        System.out.println(mapToBean(map));
    }
}
