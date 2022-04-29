package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 排序
 *
 * @author Ji MingHao
 * @since 2022-04-08 11:35
 */
public class Sort {

    /**
     * 冒泡排序
     */
    public int[] sortArray1(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = 0; j < i + 1; j++) {
                int temp;
                if (nums[i] < nums[j]) {
                    temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                }
            }
        }
        return nums;
    }

    /**
     * 归并排序
     */
    public int[] sortArray(int[] nums) {
        divide(nums, 0, nums.length - 1);
        return nums;
    }

    public void divide(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int mid = start + ((end - start) >> 1);
        divide(arr, start, mid);
        divide(arr, mid + 1, end);
        merge(arr, start, mid, end);
    }

    public void merge(int[] arr, int start, int mid, int end) {
        System.out.println("=========start=========：" + start);
        System.out.println("==========mid========：" + mid);
        System.out.println("==========end========：" + end);
        int l = start;
        int m = mid + 1;
        int r = end;
        int[] temp = new int[end - start + 1];
        int k = 0;
        while (l <= mid && m <= r) {
            if (arr[l] < arr[m]) {
                temp[k++] = arr[l++];
            } else {
                temp[k++] = arr[m++];
            }
        }
        while (l <= mid) {
            temp[k++] = arr[l++];
        }
        while (m <= r) {
            temp[k++] = arr[m++];
        }
        for (int i = 0; i < temp.length; i++) {
            arr[i + start] = temp[i];
        }
    }
}
