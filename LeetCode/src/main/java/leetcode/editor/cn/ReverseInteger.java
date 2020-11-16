//Given a 32-bit signed integer, reverse digits of an integer. 
//
// Note: 
//Assume we are dealing with an environment that could only store integers withi
//n the 32-bit signed integer range: [−231, 231 − 1]. For the purpose of this prob
//lem, assume that your function returns 0 when the reversed integer overflows. 
//
// 
// Example 1: 
// Input: x = 123
//Output: 321
// Example 2: 
// Input: x = -123
//Output: -321
// Example 3: 
// Input: x = 120
//Output: 21
// Example 4: 
// Input: x = 0
//Output: 0
// 
// 
// Constraints: 
//
// 
// -231 <= x <= 231 - 1 
// 
// Related Topics 数学 
// 👍 2346 👎 0

package leetcode.editor.cn;

public class ReverseInteger {
    public static void main(String[] args) {
        Solution solution = new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    static class Solution {
        public int reverse(int x) {
            long n = 0;
            while (x != 0) {
                n = n * 10 + x % 10; // 对10取余，第一次为个位，第二次时 因为每次乘10都会将这个数逐次变为高位
                x = x / 10; // 对原来的数 降位
            }
            return (int) n == n ? (int) n : 0;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}