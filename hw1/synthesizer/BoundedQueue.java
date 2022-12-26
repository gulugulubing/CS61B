package synthesizer;
import java.util.Iterator;
public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();
    int fillCount();
    void enqueue(T x);
    T dequeue();
    T peek();
    Iterator<T> iterator();
    default boolean isEmpty() {
        if (fillCount() == 0) {
            return true;
        }
        return false;
    }
    default boolean isFull() {
        if (fillCount() == capacity()) {
            return true;
        }
        return false;
    }
}
