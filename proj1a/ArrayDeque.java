/**Invariants:
 nextFirst:when addFirst,always minus 1,until 0,then circles. default value is arbitrary
 nextLast:when addLast,always add 1,until equals length,then circles. default value is arbitrary
 */

public class ArrayDeque<T> {
    private int nextFirst;
    private int nextLast;
    private int size;
    private T[] items;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /**copy items to the new array with differnt length*/
    private void arrayCopy(T[] newArray) {
        int temp = nextFirst + 1;
        int tempSize = size;
        int i = 0;
        while (tempSize > 0) {
            if (temp == items.length) {
                temp = 0;
            }
            newArray[i] = items[temp];
            temp++;
            tempSize--;
            i++;
        }
    }

    /**change size*/
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        arrayCopy(a);
        items = a;
        nextLast = size;
        if (nextLast == items.length) {
            nextLast = 0;
        }
        nextFirst = a.length - 1;
    }

    public void addFirst(T x) {
        int rFACTOR = 2;
        if (size == items.length) {
            resize(size * rFACTOR);
        }
        items[nextFirst] = x;
        size++;
        nextFirst--;
        if (nextFirst == -1) {
            nextFirst = items.length - 1;
        }
    }

    public void addLast(T x) {
        int rFACTOR = 2;
        if (size == items.length) {
            resize(size * rFACTOR);
        }
        items[nextLast] = x;
        size++;
        nextLast++;
        if (nextLast == items.length) {
            nextLast = 0;
        }
    }

    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int temp = nextFirst + 1;
        int tempSize = size;
        while (tempSize > 0) {
            if (temp == items.length) {
                temp = 0;
            }
            System.out.print(items[temp]);
            System.out.print(" ");
            temp++;
            tempSize--;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size > 0) {
            float useRatio = (float) size / (float) items.length;
            if (useRatio <= 0.25) {
                resize(items.length / 4);;
            }
            int tempIndex;
            if (nextFirst == items.length - 1) {
                tempIndex = 0;
                nextFirst = 0;
            } else {
                tempIndex = ++nextFirst;
            }
            size--;
            return items[tempIndex];
        } else {
            return null;
        }
    }

    public T removeLast() {
        if (size > 0) {
            float useRatio = (float) size / (float) items.length;
            if (useRatio <= 0.25) {
                resize(items.length / 4);
            }
            int tempIndex;
            if (nextLast == 0) {
                tempIndex = items.length - 1;
                nextLast = items.length - 1;
            } else {
                tempIndex = --nextLast;
            }
            size--;
            return items[tempIndex];
        } else {
            return null;
        }
    }

    public T get(int index) {
        if (index < 0 || index > (items.length - 1)) {
            return null;
        }
        int temp = nextFirst + index + 1;
        if (temp < items.length) {
            return items[temp];
        } else {
            return items[temp - items.length];
        }
    }

    //self test
    public static void main(String[] args) {
        ArrayDeque<String> L = new ArrayDeque<>();
        L.addFirst("a");
        L.addLast("b");
        L.addFirst("a");
        L.addLast("b");
        L.addFirst("a");
        L.addLast("b");
        L.addFirst("a");
        L.addLast("b");
        L.addFirst("a");
        L.addLast("b");
        L.addFirst("a");
        L.addLast("b");
        L.removeFirst();
        L.removeLast();
        L.removeFirst();
        L.removeLast();
        L.removeFirst();
        L.removeLast();
        L.removeFirst();
        L.removeLast();
        L.removeFirst();
        L.removeLast();
        L.removeFirst();
        System.out.println(L.isEmpty());
        System.out.println(L.size());
        L.size();
        L.printDeque();
    }

}
