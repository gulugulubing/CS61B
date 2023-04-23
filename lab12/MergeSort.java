import edu.princeton.cs.algs4.Queue;

import java.util.HashSet;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }


    /** Returns a queue of queues that each contain one item from items. */
    //SEEM TO USELESS IN THIS LAB
    private static <Item extends Comparable> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> singleItemQueus = new Queue<>();
        while (!items.isEmpty()) {
            Queue<Item> single = new Queue<>();
            single.enqueue(items.dequeue());
            singleItemQueus.enqueue(single);
        }
        return singleItemQueus;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> sortedQueue = new Queue<>();
        while ((!q1.isEmpty()) || (!q2.isEmpty())) {
            sortedQueue.enqueue(getMin(q1, q2));
        }
        return sortedQueue;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> mergeSort(
            Queue<Item> items) {
        int size = items.size();
        if (size <= 1) {
            return items;
        }
        Queue <Item> q1 = new Queue<>();
        while (q1.size() < size / 2) {
            q1.enqueue(items.dequeue());
        }

        if (q1.size() == 1 || items.size() == 1) {
            return mergeSortedQueues(q1, items);
        }
        return mergeSortedQueues(mergeSort(q1), mergeSort(items));
    }

    public static void main(String args[]) {
        //Queue<String> students = new Queue<String>();
        //students.enqueue("Alice");
        //students.enqueue("Vanessa");
        //students.enqueue("Ethan");

        //test makeSingleItemQueues
        //System.out.println(makeSingleItemQueues(students).dequeue().dequeue());

        //Queue<String> sortedStudents = MergeSort.mergeSort(students);
        //System.out.println("origin: " + students);
        //System.out.println("sorted: " + sortedStudents);

        /*test mergeSortedQueues
        Queue<Integer> q1 = new Queue<>();
        Queue<Integer> q2 = new Queue<>();
        q1.enqueue(2);
        q1.enqueue(15);
        q1.enqueue(17);
        q1.enqueue(32);
        q2.enqueue(17);
        q2.enqueue(17);
        q2.enqueue(19);
        q2.enqueue(26);
        q2.enqueue(41);
        System.out.println(mergeSortedQueues(q1, q2));
        */
        //test mergeSortedQueues
        Queue<Integer> q1 = new Queue<>();
        q1.enqueue(32);
        q1.enqueue(15);
        q1.enqueue(2);
        q1.enqueue(17);
        q1.enqueue(19);
        q1.enqueue(26);
        q1.enqueue(41);
        q1.enqueue(17);
        q1.enqueue(17);
        System.out.println(q1);
        System.out.println(mergeSort(q1));
    }
}
