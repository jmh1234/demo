package com.demo.exercise;

/**
 * Created with IntelliJ IDEA.
 * 二维数组中的查找目标值
 *
 * @author Ji MingHao
 * @since 2022-03-04 15:33
 */
public class FindNumberIn2DArray {
    public static void main(String[] args) {
        FindNumberIn2DArray findNumberIn2DArray = new FindNumberIn2DArray();
        int[][] a = {
                {1, 4, 7, 11, 15},
                {2, 5, 8, 12, 19},
                {3, 6, 9, 16, 22},
                {10, 13, 14, 17, 24},
                {18, 21, 23, 26, 30}
        };
        System.out.println(findNumberIn2DArray.findNumberIn2DArray(a, 20));
    }

    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        for (int[] element : matrix) {
            if (element.length == 0) {
                continue;
            }
            if (element[0] <= target && element[element.length - 1] >= target) {
                int low = 0;
                int high = element.length - 1;
                int middle;
                while (low <= high) {
                    middle = (low + high) / 2;
                    if (element[middle] > target) {
                        high = middle - 1;
                    } else if (element[middle] < target) {
                        low = middle + 1;
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
