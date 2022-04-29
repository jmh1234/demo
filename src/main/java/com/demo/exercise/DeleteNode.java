package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 删除指定节点
 *
 * @author Ji MingHao
 * @since 2022-04-06 16:34
 */
public class DeleteNode {

    public static void main(String[] args) {
        DeleteNode minArray = new DeleteNode();
        ListNode node1 = new ListNode(1);
        node1.next = new ListNode(2);
        node1.next.next = new ListNode(3);
        node1.next.next.next = new ListNode(4);
        node1.next.next.next.next = new ListNode(5);
        ListNode node = minArray.delete(node1, 2);
        for (int i = 0; i < 10; i++) {
            System.out.println(node.val);
            node = node.next;
        }
    }

    public ListNode delete(ListNode head, int n) {
        int step = 1;
        ListNode newHead = new ListNode(0);
        newHead.next = head;
        ListNode current = newHead;
        while (step < n) {
            current = current.next;
            step++;
        }
        current.next = current.next.next;
        return newHead.next;
    }
}
