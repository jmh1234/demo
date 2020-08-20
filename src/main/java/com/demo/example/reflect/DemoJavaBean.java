package com.demo.example.reflect;

public class DemoJavaBean {
    private Integer id;
    private String name;
    private String privateField = "privateField";

    public int isolate() {
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

    private boolean isLongName() {
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
