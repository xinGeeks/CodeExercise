//Given two sorted integer arrays nums1 and nums2, merge nums2 into nums1 as one
// sorted array. 
//
// Note: 
//
// 
// The number of elements initialized in nums1 and nums2 are m and n respectivel
//y. 
// You may assume that nums1 has enough space (size that is equal to m + n) to h
//old additional elements from nums2. 
// 
//
// Example: 
//
// 
//Input:
//nums1 = [1,2,3,0,0,0], m = 3
//nums2 = [2,5,6],       n = 3
//
//Output: [1,2,2,3,5,6]
// 
//
// 
// Constraints: 
//
// 
// -10^9 <= nums1[i], nums2[i] <= 10^9 
// nums1.length == m + n 
// nums2.length == n 
// 
// Related Topics 数组 双指针 
// 👍 693 👎 0

package leetcode.editor.cn;

import java.util.Arrays;

public class MergeSortedArray {
    public static void main(String[] args) {
        Solution solution = new MergeSortedArray().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void merge(int[] nums1, int m, int[] nums2, int n) {
            for (int i = m, j = 0; i < m + n; i++, j++) {
                nums1[i] = nums2[j];
            }
            Arrays.sort(nums1);
        }

        public void merge1(int[] nums1, int m, int[] nums2, int n) {
            // 三指针 指针一p1、nums1有效元素尾部；指针二p2、nums2尾部；指针三p3、最终数组尾部
            int p1 = m - 1, p2 = n - 1, p = m + n - 1;
            while ((p1 >= 0) && (p2 >= 0)) {
                // 谁大谁放后面，并下标减1，后继续和下标没减1的比较依次类推
                nums1[p--] = nums1[p1] < nums2[p2] ? nums2[p2--] : nums1[p1--];
            }
            // 将nums2的0索引位置开始到长度为p2+1的部分拷贝到nums1的0索引位置依次往后p2+1的长度
            System.arraycopy(nums2, 0, nums1, 0, p2 + 1);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}