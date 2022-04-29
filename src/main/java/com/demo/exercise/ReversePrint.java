package com.demo.exercise;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * 从尾到头打印链表
 *
 * @author Ji MingHao
 * @since 2022-03-01 15:24
 */
public class ReversePrint {

    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(3);
        listNode.next.next = new ListNode(2);

        ReversePrint solution = new ReversePrint();
        System.out.println(Arrays.toString(solution.reversePrint(listNode)));
    }

    public int[] reversePrint(ListNode head) {
        Stack<Integer> stack = new Stack<>();
        while (head != null) {
            stack.push(head.val);
            head = head.next;
        }

        int[] result = new int[stack.size()];
        int i = 0;
        while (!stack.empty()) {
            result[i] = stack.pop();
            i++;
        }
        return result;
    }
}
