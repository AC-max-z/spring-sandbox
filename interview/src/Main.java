import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * I will just save some interview prep/leetcode problems/basic algorithms here,
 * because whatever
 */
public class Main {
    public static void main(String[] args) {

        // Find max and min values from string of numbers
        String str = "3245 12 345 12 222 1";
        var list = List.of(str.split(" "));
        System.out.println("Finding min and max values in list: " + list);

        Integer max = list.stream()
                .map(Integer::parseInt)
                .max(Comparator.comparingInt(a -> a))
                .get();
        Integer min = list.stream()
                .map(Integer::parseInt)
                .min(Comparator.comparingInt(a -> a))
                .get();
        System.out.println("Min:" + min + " Max:" + max);
        System.out.println("==========================\n");


        // Merge and sort two arrays of numbers
        int[] arr1 = new int[]{1, 2, 3, 0, 0, 0};
        int[] arr2 = new int[]{5, 7, 2};
        System.out.println("Merge and sort arrays: "
                + Arrays.toString(arr1) + ", " + Arrays.toString(arr2));
        System.out.println("Merge and sort:" +
                Arrays.toString(mergeAndSort(arr1, 3, arr2, 3))
        );
        System.out.println("==========================\n");


        // Replace specified number from array of integers with substitute value
        System.out.println("Remove element 2:" +
                removeElement(new int[]{0, 1, 2, 2, 3, 0, 4, 2}, 2)
        );
        System.out.println("==========================\n");


        // Replace all duplicate values from array of integers with substitute value
        // and place them at the end of the array
        System.out.println(removeDuplicates(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));
        System.out.println("==========================\n");


        // Fibonacci
        System.out.println("Fibonacci 19: " + fib(19));
        System.out.println("Using DP: " + fibonacci(19));
        System.out.println("==========================\n");


        // Calculate minimum amount of jumps needed to reach the end of array
        // (can jump forward the amount of steps that is current index value,
        // once jumped unused jumps from previous index are no longer available)
        System.out.println(jump(new int[]{2, 3, 1, 1, 4}));
        System.out.println("==========================\n");


        // Reverse string
        System.out.println(reverse("yssup"));
        System.out.println("==========================\n");


        // check if provided time string is valid
        var timeString = "23:72";
        System.out.println(validateTimeString(timeString));
        System.out.println("==========================\n");


        // Rotate integer array to the right
        System.out.println(Arrays.toString(rotateRight(new int[]{1, 2, 3, 4, 5}, 2)));
        System.out.println("==========================\n");

    }

    /**
     * @param arr
     * @param steps
     * @return
     */
    static int[] rotateRight(int[] arr, int steps) {
        // naive (rotating one by one)

        // if steps > arr.length
        steps = steps % arr.length;
//        for (int i = 0; i < steps; i++) {
//            rotateByOne(arr);
//        }

        // efficient (using reverse)
        reverse(arr, 0, arr.length - 1);
        reverse(arr, 0, steps - 1);
        reverse(arr, steps, arr.length - 1);
        return arr;
    }

    /**
     * @param arr
     */
    private static void rotateByOne(int[] arr) {
        int last = arr[arr.length - 1];
        for (int i = arr.length - 1; i > 0; i--) {
            arr[i] = arr[i - 1];
        }
        arr[0] = last;
    }

    /**
     * Reverses the order of numbers in integer array from start to end
     *
     * @param arr
     * @param start
     * @param end
     */
    private static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;
        }
    }

    /**
     * @param timeString
     * @return
     */
    static boolean validateTimeString(String timeString) {
        try {
            LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
            System.out.println("Time string " + timeString + " is valid");
            return true;
        } catch (Exception e) {
            System.out.println("Time string " + timeString + " is not valid");
            return false;
        }
    }

    /**
     * @param n
     * @return
     */
    static int fib(int n) {
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }

    /**
     * @param n
     * @return
     */
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

    /**
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     * @return
     */
    public static int[] mergeAndSort(int[] nums1, int m, int[] nums2, int n) {
        if (n >= 0) System.arraycopy(nums2, 0, nums1, m, n);
        Arrays.sort(nums1);
        return nums1;
    }

    /**
     * @param nums
     * @param val
     * @return
     */
    public static String removeElement(int[] nums, int val) {
        System.out.println("Initial array: " + Arrays.toString(nums));
        System.out.println("Finding and replacing duplicates of: " + val);
        int valCount = 0;
        int changeValue = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) {
                valCount++;
                nums[i] = changeValue;
            }
        }
//        int nonZeroIndex = 0;
//        for (int i = 0; i < nums.length; i++) {
//            if (nums[i] != changeValue) {
//                nums[nonZeroIndex++] = nums[i];
//            }
//        }
//        while (nonZeroIndex < nums.length) {
//            nums[nonZeroIndex++] = changeValue;
//        }
        System.out.println("Duplicate val count: " + valCount);
        return Arrays.toString(nums);
    }

    /**
     * @param nums
     * @return
     */
    public static String removeDuplicates(int[] nums) {
        int replacementVal = 101;
        System.out.println("Replacing duplicates from initial array: " + Arrays.toString(nums));
        System.out.println("With replacement value: " + replacementVal);
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
        System.out.println("Number of duplicates: " + finalCount);
        return Arrays.toString(nums);
    }


    /**
     * @param nums
     * @return
     */
    public static boolean canJump(int[] nums) {
        boolean[] dp = new boolean[nums.length];
        int lastIndex = nums.length - 1;
        int closestIndexWithWayOut = lastIndex;
        dp[lastIndex] = true;
        for (int i = 1; i < nums.length; i++) {
            int nextBackwardCheckIndex = lastIndex - i;
            int availableJumps = nums[nextBackwardCheckIndex];
            if (availableJumps >= closestIndexWithWayOut - nextBackwardCheckIndex) {
                dp[nextBackwardCheckIndex] = true;
                closestIndexWithWayOut = nextBackwardCheckIndex;
            }
        }
        return dp[0];
    }


    /**
     * @param nums
     * @return
     */
    public static int jump(int[] nums) {
        int maxReach = nums[0];
        int minJumps = 0;
        int indexCandidate = 0;
        // candidate algorithm
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

    /**
     * Reverse string
     *
     * @param initialString
     * @return
     */
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

    /**
     * Kadane algorithm
     *
     * @param nums
     * @return
     */
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