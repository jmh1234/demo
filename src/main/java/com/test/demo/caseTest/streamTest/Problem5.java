package com.test.demo.caseTest.streamTest;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Problem5 {
    public static class Order {
        private Integer id;
        private String name;

        public Order(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    // 例如，传入参数[{id=1,name='肥皂'},{id=2,name='牙刷'}]
    // 返回一个映射{1->Order(1,'肥皂'),2->Order(2,'牙刷')}
    public static Map<Integer, Order> toMap(List<Order> orders) {
        return orders.stream().sorted(Comparator.comparingInt(o -> o.id)).collect(Collectors.toMap(Order::getId, order -> order, (order, order2) -> order));
    }
}
