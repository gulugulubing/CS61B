// Make sure to make this class a part of the synthesizer package
package synthesizer;
import java.util.Iterator;

//Make sure to make this class and all of its methods public
//Make sure to make this class extend AbstractBoundedQueue<t>

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    public Iterator<T> iterator() {
        return new ItemIterator();
    }

    private class ItemIterator implements Iterator<T> {
        private  int position;
        public ItemIterator() {
            position = 0;
        }

        public boolean hasNext() {
            return position < fillCount;
        }

        public T next() {
            T returnVal = rb[position];
            position++;
            return returnVal;
        }
    }

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        this.capacity = capacity;
        first = 0;
        last = 0;
        fillCount = 0;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        // Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("attempts to enqueue into a full ArrayRingBuffer");
        } else {
            rb[last] = x;
            last++;
            fillCount++;
            if (last == capacity) {
                last = 0;
            }
        }
    }

    /**
     * Dequeue the oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        //  Dequeue the first item. Don't forget to decrease fillCount and update
        if (!isEmpty()) {
            T temp = rb[first];
            fillCount--;
            first++;
            if (first == capacity) {
                first = 0;
            }
            return temp;
        }
        throw new RuntimeException(" attempts to deque an empty ArrayRingBuffer");
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        //  Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            //if no one catch this throw,running will stop with only hint"attepts...."
            throw new RuntimeException("attempts to peek into an empty ArrayRingBuffer");
        }
        return rb[first];
    }
}
