public class ArrayDeque<GenericType> {
    private int nextFirst;
    private int nextLast;
    private int size;
    private GenericType[] items;

    public ArrayDeque() {
        items = (GenericType[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    private void resize(int capacity) {
        GenericType[] a = (GenericType[]) new Object[capacity];
        int temp = nextFirst + 1;
        int tempSize = size;
        int i = 0;
        while (tempSize > 0) {
            if (temp == items.length) {
                temp = 0;
            }
            a[i] = items[temp];
            temp++;
            tempSize--;
            i++;
        }
        items = a;
        nextLast = size;
        nextFirst = a.length-1;
    }

    public void addFirst(GenericType x) {
        int rFACTOR = 2;
        if (size == items.length) {
            resize(size*rFACTOR);
        }
        items[nextFirst] = x;
        size++;
        nextFirst--;
        if (nextFirst == -1) {
            nextFirst = items.length-1;
        }
    }

    public void addLast(GenericType x) {
        int RFACTOR = 2;
        if (size == items.length) {
            resize(size*RFACTOR);
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

    private void downSize() {
        GenericType[] a = (GenericType[]) new Object[items.length/4];
        int temp = nextFirst + 1;
        int tempSize = size;
        int i = 0;
        while (tempSize > 0) {
            if (temp == items.length) {
                temp = 0;
            }
            a[i] = items[temp];
            temp++;
            tempSize--;
            i++;
        }
        items = a;
        nextLast = size;
        nextFirst = a.length-1;
    }

    public GenericType removeFirst() {
        if (size > 0) {
            float useRatio = (float) size / (float) items.length;
            if (useRatio <= 0.25) {
                downSize();
            }
            int tempIndex;
            if (nextFirst == items.length-1) {
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

    public GenericType removeLast() {
        if (size > 0) {
            float useRatio = (float) size / (float) items.length;
            if (useRatio <= 0.25) {
                downSize();
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

    public GenericType get(int index) {
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

    /**self test*/
    public static void main(String[] args) {
        ArrayDeque<String> L = new ArrayDeque<>();
        L.addLast("a");
        L.addLast("b");
        L.addFirst("c");
        L.addLast("d");
        L.addLast("e");
        L.addFirst("f");
        L.addLast("g");
        L.addLast("h");
        L.addLast("a");
        L.addLast("b");
        L.addFirst("c");
        L.addLast("d");
        L.addLast("e");
        L.addFirst("f");
        L.addLast("g");
        L.addLast("h");
        L.addLast("a");
        L.addLast("b");
        L.addFirst("c");
        L.addLast("d");
        L.addLast("e");
        L.addFirst("f");
        L.addLast("g");
        L.addLast("h");
        L.addLast("a");
        L.addLast("b");
        L.addFirst("c");
        L.addLast("d");
        L.addLast("e");
        L.addFirst("f");
        L.addLast("g");
        L.addLast("h");
        //L.addLast("Z");
        //L.addFirst("Y");
        //L.printDeque();
        //System.out.println(L.get(6));
        L.removeFirst();
        L.removeFirst();
        L.removeFirst();
        L.removeFirst();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.removeFirst();
        L.removeFirst();
        L.removeFirst();
        L.removeFirst();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.removeFirst();
        L.removeFirst();
        L.removeFirst();
        L.removeFirst();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.removeLast();
        L.printDeque();
    }
}
