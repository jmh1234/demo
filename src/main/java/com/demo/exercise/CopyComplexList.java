package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 复制复杂链表
 *
 * @author Ji MingHao
 * @since 2022-03-03 21:32
 */
public class CopyComplexList {
    public static void main(String[] args) {
        Node node = new Node(7);
        CopyComplexList copyComplexList = new CopyComplexList();
        copyComplexList.copyRandomList(node);
    }

    public Node copyRandomList(Node head) {



        return null;
    }
}

class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}
