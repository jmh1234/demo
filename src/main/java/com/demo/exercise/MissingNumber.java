package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 找字符串中缺少的数字
 *
 * @author Ji MingHao
 * @since 2022-03-03 21:41
 */
public class MissingNumber {

    public static void main(String[] args) {
        MissingNumber missingNumber = new MissingNumber();
        int[] a = {0};
        System.out.println(missingNumber.missingNumber(a));
    }

    public int missingNumber(int[] nums) {
        if (nums.length == 0) {
            return -1;
        }
        if (nums[0] != 0) {
            return 0;
        }
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i + 1] - nums[i] != 1) {
                return nums[i + 1] - 1;
            }
        }
        return nums[nums.length - 1] + 1;
    }
}
