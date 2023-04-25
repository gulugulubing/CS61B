/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int len = asciis.length;
        String[] sorted = new String[len];
        for (int i = 0; i < len ; i++) {
            sorted[i] = asciis[i];
        }

        for (int i = len - 1; i >= 0; i--) {
            sortHelperLSD(sorted, i);
        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] count = new int[256];
        for (String s : asciis) {
            if (index > s.length() - 1) {
                count[0] += 1;
            }
            else {
                count[(int) s.charAt(index)] += 1;
            }
        }

        //for (int i : count) {
        //    System.out.println("count :" + i);
        //}

        int[] start = new int[256];
        for (int i = 0; i < 256; i++) {
            if (i == 0) {
                start[i] = count[i];
            } else {
                start[i] = start[i-1] + count[i];
            }
        }

        //for (int i : count) {
       //     System.out.println("start :" + i);
       // }

        String[] sortedOnIndex = new String[asciis.length];
        //must from last to keep stable(relative original location)
        for (int i = asciis.length - 1; i >=0 ;i--) {
            if (index > asciis[i].length() - 1) {
                sortedOnIndex[--start[0]] = asciis[i];
            } else {
                sortedOnIndex[--start[(int)asciis[i].charAt(index)]] = asciis[i];
            }
        }

        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = sortedOnIndex[i];
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String args[]) {

        String[] testStringArray = new String[5];
        testStringArray[0] = "ezb";
        testStringArray[1] = "d";
        testStringArray[2] = "bed";
        testStringArray[3] = "bbc";
        testStringArray[4] = "a";
        for (String s : testStringArray) {
            System.out.println(s);
        }
        System.out.println("sorted:");
        /*test sortHelperLSD
        sortHelperLSD(testStringArray, 2);
        for (String s : testStringArray) {
           System.out.println(s);
        }
        sortHelperLSD(testStringArray, 1);
        for (String s : testStringArray) {
            System.out.println(s);
        }
        sortHelperLSD(testStringArray, 0);
        for (String s : testStringArray) {
            System.out.println(s);
       }
         */
        for (String s : sort(testStringArray)) {
            System.out.println(s);
        }
    }
}
