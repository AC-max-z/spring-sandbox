package org.springsandbox;


import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String str = "3245 12 345 12 222 1";
        var list = List.of(str.split(" "));
        Integer max = list.stream().map(Integer::parseInt).max(Comparator.comparingInt(a -> a)).get();
        Integer min = list.stream().map(Integer::parseInt).min(Comparator.comparingInt(a -> a)).get();
//        System.out.println(min + " " + max);
//        System.out.println(Arrays.toString(mergeAndSort(new int[]{1, 2, 3, 0, 0, 0}, 3, new int[]{5, 7, 2}, 3)));
//        System.out.println(removeElement(new int[]{0, 1, 2, 2, 3, 0, 4, 2}, 2));
//        System.out.println(removeDuplicates(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));
//        System.out.println(fib(19));
//        System.out.println("DP: " + fibonacci(7));
        System.out.println(jump(new int[]{2, 3, 1, 1, 4}));
        System.out.println(reverse("sup"));
        var timeString = "23:72";
        try {
            LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            System.out.println("Time string is valid");
        } catch (Exception e) {
            System.out.println("Time string is not valid");
        }
    }

    static int fib(int n) {
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }

    public static int fibonacci(int n) {
        if (n <= 0) {
            return -1;
        } else if (n == 1 || n == 2) {
            return n - 1;
        } else {
            int[] dp = new int[n + 1];
            dp[0] = 0;
            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }

    public static int[] mergeAndSort(int[] nums1, int m, int[] nums2, int n) {
        if (n >= 0) System.arraycopy(nums2, 0, nums1, m, n);
        return nums1;
    }

    public static int removeElement(int[] nums, int val) {
        int valCount = 0;
        int changeValue = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != val) {
                valCount++;
                nums[i] = changeValue;
            }
        }
        int nonZeroIndex = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != changeValue) {
                nums[nonZeroIndex++] = nums[i];
            }
        }
        while (nonZeroIndex < nums.length) {
            nums[nonZeroIndex++] = changeValue;
        }
        System.out.println(Arrays.toString(nums));
        return valCount;
    }

    public static int removeDuplicates(int[] nums) {
        int replacementVal = 101;
        int index = 1;
        int currentVal = nums[0];
        while (index < nums.length) {
            if (nums[index] == currentVal) {
                nums[index] = replacementVal;
            } else {
                currentVal = nums[index];
            }
            index++;
        }
        int nonReplacedCount = 0;
        int finalCount = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != replacementVal) {
                nums[nonReplacedCount++] = nums[i];
            }
        }
        finalCount = nonReplacedCount;
        while (nonReplacedCount < nums.length) {
            nums[nonReplacedCount++] = replacementVal;
        }
        System.out.println(Arrays.toString(nums));
        return finalCount;
    }


    public static boolean canJump(int[] nums) {
        boolean[] dp = new boolean[nums.length];
        int lastIndex = nums.length - 1;
        int closestIndexWithWayOut = lastIndex;
        dp[lastIndex] = true;
        for (int i = 1; i < nums.length; i++) {
            int nextBackwardCheckIndex = lastIndex - i;
            int availableJumps = nums[nextBackwardCheckIndex];
            // Looks like this one is the solution for another, more interesting problem, but not what is needed:P
            // ah..the story of my life
//            if(nextBackwardCheckIndex + nums[nextBackwardCheckIndex] >= closestIndexWithWayOut) {
//                for (int j = nextBackwardCheckIndex; j < closestIndexWithWayOut; j++) {
//                    availableJumps--;
//                    availableJumps += nums[j];
//                    if (availableJumps <= 0) {
//                        dp[i] = false;
//                        break;
//                    }
//                }
//            }
//            dp[nextBackwardCheckIndex] = availableJumps >= 0;
            if (availableJumps >= closestIndexWithWayOut - nextBackwardCheckIndex) {
                dp[nextBackwardCheckIndex] = true;
                closestIndexWithWayOut = nextBackwardCheckIndex;
            }
        }
        return dp[0];
    }


    public static int jump(int[] nums) {
        int maxReach = nums[0];
        int minJumps = 0;
        int indexCandidate = 0;
        for (int i = 1; i < nums.length; i++) {
            maxReach--;
            minJumps++;
            if (maxReach + nums[i] >= nums.length - 1) {
                return ++minJumps;
            }
            for (int j = i; j <= i + maxReach; j++) {
                if (nums[j] >= nums[j - 1] + 1) {
                    indexCandidate = j;
                }
            }
            maxReach += nums[indexCandidate] - (indexCandidate - i);
            i = indexCandidate - 1;
        }
        return minJumps;
    }

    public static String reverse(String initialString) {
        char[] chars = initialString.toCharArray();
        char[] reversed = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            reversed[i] = chars[chars.length - 1 - i];
        }
        StringBuilder sb = new StringBuilder();
        for (char c : reversed) {
            sb.append(c);
        }
        return sb.toString();
    }

    public static int kadane(int[] nums) {
        int maxSoFar = 0;
        int maxEndingHere = 0;
        for (int num : nums) {
            maxEndingHere = maxEndingHere + num;
            if (maxEndingHere < 0) {
                maxEndingHere = 0;
            }
            if (maxSoFar < maxEndingHere) {
                maxSoFar = maxEndingHere;
            }
        }
        return maxSoFar;
    }
}