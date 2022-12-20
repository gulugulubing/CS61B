package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.equals(p.key)) {
            return p.value;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size++;
            return new Node(key, value);
        }
        if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> ks = new HashSet<>();
        return keySetHelp(root, ks);
    }

    private Set<K> keySetHelp(Node p, Set<K> ks) {
        if (p == null) {
            return ks;
        }
        ks.add(p.key);
        ks = keySetHelp(p.left, ks);
        ks = keySetHelp(p.right, ks);
        return ks;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    /* my way is too complex
    @Override
    public V remove(K key) {
        V value = removeHelp(key, root, null);
        if (value == null) {
            return null;
        }
        size--;
        return value;
    }

    private V removeHelp(K key, Node p, Node pFather) {
        if (p == null) {
            return null;
        }
        if (key.equals(p.key)) {
            return hibbardDeletion(key, p, pFather);
        } else if (key.compareTo(p.key) < 0) {
            return removeHelp(key, p.left, p);
        } else {
            return removeHelp(key, p.right, p);
        }
    }

    //my Hibbard deletion
    private V hibbardDeletion(K key, Node p, Node pFather) {
        V val = p.value;
        Node predecessorFather = p;
        Node successorFather = p;
        Node successor = p.right;
        Node predecessor = p.left;
        if (predecessor != null) {
            //have left children
            while (predecessor.right != null) {
                predecessorFather = predecessor;
                predecessor = predecessor.right;
            }
            p.key = predecessor.key;
            p.value = predecessor.value;
            if (predecessorFather == p) {
                //one line corner case
                p.left = predecessor.left;
            } else {
                predecessorFather.right = predecessor.left;
            }
            return val;
        } else if (successor != null) {
            //not have left but have right children
            while (successor.left != null) {
                successorFather = successor;
                successor = successor.left;
            }
            p.key = successor.key;
            p.value = successor.value;
            if (successorFather == p) {
                //one line corner case
                p.right = successor.right;
            } else {
                successorFather.left = successor.right;
            }
            return val;
        } else {
            //have no children
            if (pFather == null) {
                root = null;
                return val;
            }
            if (pFather.left != null && pFather.left.key == key) {
                pFather.left = null;
                return val;
            }
            pFather.right = null;
            return val;
        }
    }
    */

    @Override
    public V remove(K key) {
        V val = get(key);
        if (val == null) {
            return null;
        }
        size--;
        root = remove(key, root);
        return val;
    }

    private Node remove(K key,Node x) {
        if (x == null) return null;
        int cmp = x.key.compareTo(key);
        if      (cmp < 0) x.right = remove(key, x.right);
        else if (cmp > 0) x.left = remove(key, x.left);
        else {
            if (x.left == null) return x.right;
            if (x.right == null) return x.left;
            Node t = x;
            x = min(t.right);
            x.right = removeMin(t.right);
            x.left = t.left;
        }
        return x;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else                return min(x.left);
    }

    private Node removeMin(Node x) {
        if (x.left == null) return x.right;
        x.left = removeMin(x.left);
        return x;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    /* my way is too complex
    @Override
    public V remove(K key, V value) {
        value = removeHelp(key, value, root, null);
        if (value == null) {
            return null;
        }
        size--;
        return value;
    }

    private V removeHelp(K key, V value, Node p, Node pFather) {
        if (p == null) {
            return null;
        }
        if (key.equals(p.key)) {
            if (!value.equals(p.value)) {
                return null;
            }
            return hibbardDeletion(key, p, pFather);
        } else if (key.compareTo(p.key) < 0) {
            return removeHelp(key, value, p.left, p);
        } else {
            return removeHelp(key, value, p.right, p);
        }
    }
    */

    @Override
    public V remove(K key, V value) {
        V val = get(key);
        if (val != value) {
            return null;
        }
        size--;
        root = remove(key,root);
        return val;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public static void main(String[] args) {
        BSTMap<String, Integer> bstmap = new BSTMap<>();
        bstmap.put("dog", 4);
        bstmap.put("bag", 2);
        bstmap.put("flat", 7);
        bstmap.put("alf", 1);
        bstmap.put("cat", 3);
        //bstmap.put("elf", 5);
        //bstmap.put("glut", 8);
        //bstmap.put("eys", 6);
        System.out.println(bstmap.remove("dog"));
        Set<String>  k =  bstmap.keySet();
        System.out.println(k);
        System.out.println(bstmap.get("bag"));
    }
}
