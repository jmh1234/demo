package com.test.demo.caseTest.streamTest;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class Test {
    private List<String> beforeJava7(List<Dish> dishList) {
        List<Dish> lowCaloricDishes = new ArrayList<>();

        //1.筛选出卡路里小于400的菜肴
        for (Dish dish : dishList) {
            if (dish.getCalories() < 400) {
                lowCaloricDishes.add(dish);
            }
        }

        //2.对筛选出的菜肴进行排序
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });

        //3.获取排序后菜肴的名字
        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish d : lowCaloricDishes) {
            lowCaloricDishesName.add(d.getName());
        }
        return lowCaloricDishesName;
    }

    private List<String> afterJava8(List<Dish> dishList) {
        return dishList.stream()
                .filter(d -> d.getCalories() < 400)  //筛选出卡路里小于400的菜肴
                .sorted(comparing(Dish::getCalories))  //根据卡路里进行排序
                .map(Dish::getName)  //提取菜肴名称
                .collect(Collectors.toList()); //转换为List
    }

    static Dish setData(String name, boolean vegetarian, int calories, String type) {
        Dish dish = new Dish();
        dish.setCalories(calories);
        dish.setName(name);
        dish.setType(type);
        dish.setVegetarian(vegetarian);
        return dish;
    }

    public static void main(String[] args) {
        Test test = new Test();
        List<Dish> dishList = new ArrayList<>();
        dishList.add(setData("冰淇淋", false, 10, "零食"));
        dishList.add(setData("烧烤", false, 50, "肉类"));
        dishList.add(setData("鸡腿", false, 30, "肉类"));
        dishList.add(setData("青菜", true, 5, "蔬菜"));
        dishList.add(setData("糖", false, 25, "糖类"));

        List<Dish> dish1List = new ArrayList<>();
        dish1List.add(setData("冰淇淋", false, 10, "零食"));
        dish1List.add(setData("烧烤", false, 50, "肉类"));
        dish1List.add(setData("鸡腿", false, 30, "肉类"));
        dish1List.add(setData("青菜", true, 5, "蔬菜"));
        dish1List.add(setData("糖", false, 25, "糖类"));
//        List<String> list = forkJoinTest.beforeJava7(dishList);
//        List<String> list1 = forkJoinTest.afterJava8(dishList);
//        System.out.println(list);
//        System.out.println(list1);


        //双重for循环
        for (int i = 0; i < dishList.size(); i++) {
            for (int j = 0; j < dish1List.size(); j++) {
                if (dishList.get(i).getType().equals(dish1List.get(j).getType())) {
                    dishList.get(i).setCalories(dish1List.get(j).getCalories());
                }
            }
        }
//        System.out.println(dishList);
        //将集合转换为map,key为sceneCode
        Map<String, Integer> collect = dish1List.stream().collect(
                Collectors.toMap(Dish::getType, Dish::getCalories, (s, a) -> s + a));

        //遍历原先集合,改变exitStatus的值
        dishList.forEach(
                fusRecomConfigDO -> {
                    if (collect.containsKey(fusRecomConfigDO.getType())) {
                        fusRecomConfigDO.setCalories(collect.get(fusRecomConfigDO.getType()));
                    }
                }
        );
        System.out.println(dishList);
    }
}
