package com.demo.exercise;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 包含min函数的栈
 *
 * @author Ji MingHao
 * @since 2022-02-28 17:10
 */
public class MinStack {
    List<Integer> stack = new ArrayList<>();

    public static void main(String[] args) {
        MinStack minStack = new MinStack();
        minStack.push(-2);
        minStack.push(0);
        minStack.push(-3);
        System.out.println(minStack.min());
        minStack.pop();
        System.out.println(minStack.top());
        System.out.println(minStack.min());
    }

    public MinStack() {

    }

    public void push(int x) {
        stack.add(x);
    }

    public void pop() {
        if (stack.size() > 0) {
            stack.remove(stack.size() - 1);
        }
    }

    public int top() {
        if (stack.size() == 0) {
            return Integer.MAX_VALUE;
        }
        return stack.get(stack.size() - 1);
    }

    public int min() {
        return stack.stream().min(Comparator.comparingInt(o -> o)).get();
    }
}
