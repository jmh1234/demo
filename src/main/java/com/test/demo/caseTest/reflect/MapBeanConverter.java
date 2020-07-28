package com.test.demo.caseTest.reflect;

import org.apache.commons.beanutils.PropertyUtils;

import java.util.HashMap;
import java.util.Map;

public class MapBeanConverter {
    // 传入一个遵守Java Bean约定的对象，读取它的所有属性，存储成为一个Map
    // 例如，对于一个DemoJavaBean对象 { id = 1, name = "ABC" }
    // 应当返回一个Map { id -> 1, name -> "ABC", longName -> false }
    // 提示：
    //  1. 读取传入参数bean的Class
    //  2. 通过反射获得它包含的所有名为getXXX/isXXX，且无参数的方法（即getter方法）
    //  3. 通过反射调用这些方法并将获得的值存储到Map中返回
    private static Map<String, Object> beanToMap(Object bean) {
        Map<String, Object> map = new HashMap<>();
        try {
            map.put("id", PropertyUtils.getProperty(bean, "id"));
            map.put("name", PropertyUtils.getProperty(bean, "name"));
            map.put("longName", PropertyUtils.getProperty(bean, "longName"));
           /* Class<?> clazz = bean.getClass();
            map.put("id", clazz.getMethod("getId").invoke(bean));
            map.put("name", clazz.getMethod("getName").invoke(bean));
            map.put("longName", clazz.getMethod("isLongName").invoke(bean));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 传入一个遵守Java Bean约定的Class和一个Map，生成一个该对象的实例
    // 传入参数DemoJavaBean.class和Map { id -> 1, name -> "ABC"}
    // 应当返回一个DemoJavaBean对象 { id = 1, name = "ABC" }
    // 提示：
    //  1. 遍历map中的所有键值对，寻找klass中名为setXXX，且参数为对应值类型的方法（即setter方法）
    //  2. 使用反射创建klass对象的一个实例
    //  3. 使用反射调用setter方法对该实例的字段进行设值
    private static DemoJavaBean mapToBean(Class klass, Map<String, Object> map) {
        DemoJavaBean demoJavaBean = null;
        try {
            demoJavaBean = (DemoJavaBean) klass.newInstance();
            PropertyUtils.setSimpleProperty(demoJavaBean, "id", map.get("id"));
            PropertyUtils.setSimpleProperty(demoJavaBean, "name", map.get("name"));

//            demoJavaBean = (DemoJavaBean) klass.newInstance();
//            demoJavaBean.setId(Integer.valueOf(map.get("id").toString()));
//            demoJavaBean.setName(map.get("name").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return demoJavaBean;
    }

    public static void main(String[] args) {
        DemoJavaBean bean = new DemoJavaBean();
        bean.setId(100);
        bean.setName("AAAAAAAAAAAAAAAAAAA");
        System.out.println(beanToMap(bean));

        Map<String, Object> map = new HashMap<>();
        map.put("id", 123);
        map.put("name", "ABCDEFG");
        System.out.println(mapToBean(DemoJavaBean.class, map));
    }

    public static class DemoJavaBean {
        private Integer id;
        private String name;
        private String privateField = "privateField";

        public int isolate() {
            System.out.println(privateField);
            return 0;
        }

        public String is() {
            return "";
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getName(int i) {
            return name + i;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isLongName() {
            return name.length() > 10;
        }

        @Override
        public String toString() {
            return "DemoJavaBean{"
                    + "id="
                    + id
                    + ", name='"
                    + name
                    + '\''
                    + ", longName="
                    + isLongName()
                    + '}';
        }
    }
}
