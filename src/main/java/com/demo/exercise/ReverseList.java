package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 反转链表
 *
 * @author Ji MingHao
 * @since 2022-03-01 15:41
 */
public class ReverseList {
    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        listNode.next = new ListNode(2);
        listNode.next.next = new ListNode(3);
        listNode.next.next.next = new ListNode(4);
        listNode.next.next.next.next = new ListNode(5);

        ReverseList solution = new ReverseList();
        solution.reverseList(listNode);
    }

    public ListNode reverseList(ListNode head) {
        ListNode result = null;
        ListNode next;
        while (head != null) {
            next = head.next;
            head.next = result;
            result = head;
            head = next;
        }
        return result;
    }
}
