package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 旋转数组的最小数字
 *
 * @author Ji MingHao
 * @since 2022-03-04 16:04
 */
public class MinArray {

    public static void main(String[] args) {
        MinArray minArray = new MinArray();
        ListNode node1 = new ListNode(1);
        node1.next = new ListNode(3);
        node1.next.next = new ListNode(5);
//        node1.next.next.next = new ListNode(2);
//        node1.next.next.next.next = new ListNode(2);

        ListNode node2 = new ListNode(2);
        node2.next = new ListNode(4);

        System.out.println(minArray.Merge(node1, node2));
    }

    public ListNode Merge(ListNode list1, ListNode list2) {
        if (list1 == null || list2 == null) {
            return list1 == null ? list2 : list1;
        }

        // 寻找主节点
        ListNode head = list1.val < list2.val ? list1 : list2;

        ListNode operateNode = head;

        ListNode cur1 = head.next;
        ListNode cur2 = list1.val < list2.val ? list2 : list1;
        while (cur1 != null && cur2 != null) {
            if (cur1.val < cur2.val) {
                operateNode.next = cur1;
                cur1 = cur1.next;
            } else {
                operateNode.next = cur2;
                cur2 = cur2.next;
            }
            operateNode = operateNode.next;
        }
        operateNode.next = cur1 == null ? cur2 : cur1;
        return head;
    }
}
