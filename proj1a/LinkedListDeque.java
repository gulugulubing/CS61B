/**cirle structure*/
public class LinkedListDeque<T> {
    /**must be no-staitc,otherwise can not use GenreicType,
     * sentinel,this,private is better here*/
    private class StuffNode {
        private T item;
        private StuffNode prev;
        private StuffNode next;
        public StuffNode(T i, StuffNode n) {
            item = i;
            if (n == null) {
                next = this;
                prev = this;
            } else {
                prev = sentinel;
                next = n;
                n.prev = this;
            }
        }
    }

    private StuffNode sentinel; //end user need not know this
    private int size;
    public LinkedListDeque() {
        sentinel = new StuffNode(null, null); //first para should be null,because null is Genre
        size = 0;
    }

    public void addFirst(T x) {
        sentinel.next = new StuffNode(x, sentinel.next);
        size++;
    }

    public void addLast(T x) {
        StuffNode temp = sentinel.prev;
        sentinel.prev = new StuffNode(x, sentinel);
        sentinel.prev.prev = temp;
        temp.next = sentinel.prev;
        size++;
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
        StuffNode temp;
        temp = sentinel.next;
        while (temp != sentinel) {
            System.out.print(temp.item);
            System.out.print(" ");
            temp = temp.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size > 0) {
            T temp = sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size--;
            return temp;
        }
        return null;
    }

    public T removeLast() {
        if (size > 0) {
            T temp = sentinel.prev.item;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size--;
            return temp;
        }
        return null;
    }

    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int temp = -1;
        StuffNode tempNode = sentinel;
        while (temp < index) {
            temp++;
            tempNode = tempNode.next;
        }
        return tempNode.item;
    }

    public T getRecursive(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        if (index == 0) {
            return sentinel.next.item;
        }
        T temp;
        StuffNode tempNode = sentinel;
        sentinel = sentinel.next;
        temp = getRecursive(index - 1);
        sentinel = tempNode;
        return temp;
    }

    /** self test
    public static void main(String[] args){
        LinkedListDeque<String> L = new LinkedListDeque<String>("bone");
        //LinkedListDeque<String> L = new LinkedListDeque<String>();test null list
        //L.addFirst("thug");
        L.addLast("thug");
        LinkedListDeque<Integer> LL = new LinkedListDeque<Integer>(4);
        //LinkedListDeque<Integer> LL = new LinkedListDeque<Integer>();//test null list
        //LL.addFirst(1);
        LL.addLast(1);
        System.out.println(L.get(0));
        System.out.println(L.getRecursive(0));
        System.out.println(L.get(1));
        System.out.println(L.getRecursive(1));
        System.out.println(LL.get(0));
        System.out.println(LL.getRecursive(0));
        System.out.println(LL.get(1));
        System.out.println(LL.getRecursive(1));
        //System.out.println(L.size);
        //System.out.println(LL.size);
        L.printDeque();
        LL.printDeque();
        //System.out.println();
        //System.out.println("remove first");
        //String y = L.removeFirst();
       // int yy = LL.removeFirst();
       // System.out.println(y);
        //System.out.println(yy);
        //System.out.println(L.size);
        //System.out.println(LL.size);
        //L.printDeque();
        //LL.printDeque();
        System.out.println();
        System.out.println("remove last");
        String z = L.removeLast();
        int zz = LL.removeLast();
        System.out.println(z);
        System.out.println(zz);
        System.out.println(L.size);
        System.out.println(LL.size);
        L.printDeque();
        LL.printDeque();
    }
 */

}
