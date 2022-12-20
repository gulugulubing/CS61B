package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    //just for
    private MyHashMap(int bucketNum) {
        buckets = new ArrayMap[bucketNum];
        this.clear();
    }

    /* Removes all the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return buckets[hash(key)].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (loadFactor() > MAX_LF) {
            //actually resize() only when lf > 1, because loadFactor return int
            resize(this.buckets.length * 2);
        }
        if (!containsKey(key)) {
            size = size + 1;
        }
        ArrayMap<K, V> bk = buckets[hash(key)];
        bk.put(key, value);
    }

    private void resize(int newSize) {
        //must create newHashMap,because hash(key) will be changed,
        // old (key,value)s will in different bucket.
        //you cannot just create newBuckets and then put(key, value)
        //because old (key,value)s will in same bucket.
        MyHashMap<K, V> newHashMap = new MyHashMap<>(newSize);
        for (ArrayMap<K, V> bucket : buckets) {
            for (K key : bucket) {
                newHashMap.put(key, bucket.get(key));
            }
        }
        this.buckets = newHashMap.buckets;
        this.size = newHashMap.size;
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
        Set<K> keyset = new HashSet<>();
        for(ArrayMap<K, V> am : buckets) {
            keyset.addAll(am.keySet());
        }
        return keyset;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int bucketIndex = hash(key);
        V ret = buckets[bucketIndex].remove(key);
        size = 0;
        for(ArrayMap<K, V> am : buckets) {
            size = size + am.size;
        }
        return ret;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int bucketIndex = hash(key);
        V ret = buckets[bucketIndex].remove(key,value);
        for(ArrayMap<K, V> am : buckets) {
            size = size + am.size;
        }
        return ret;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }

    public static void main(String[] args) {
       MyHashMap<String, Integer> myhm = new MyHashMap<>();
        myhm.put("a", 1);
        myhm.put("b", 2);
        myhm.put("c", 3);
        myhm.put("d", 4);
        myhm.put("e", 5);
        myhm.put("f", 6);
        myhm.put("g", 7);
        myhm.put("h", 8);
        myhm.put("i", 9);
        myhm.put("j", 10);
        myhm.put("k", 11);
        myhm.put("l", 12);
        myhm.put("m", 13);
        myhm.put("n", 14);
        myhm.put("o", 15);
        myhm.put("p", 16);
        System.out.println(myhm.size());
        myhm.remove("p");
        myhm.remove("o");
        myhm.remove("n");
        myhm.remove("m");
        myhm.remove("l");
        myhm.remove("k");
        myhm.remove("j");
        myhm.remove("i");
        myhm.remove("h");
        myhm.remove("g");
        myhm.put("z", 17);
        myhm.put("g", 7);
        myhm.put("h", 8);
        myhm.put("i", 9);
        myhm.put("j", 10);
        myhm.put("k", 11);
        myhm.put("l", 12);
        myhm.put("m", 13);
        myhm.put("n", 14);
        myhm.put("o", 15);
        myhm.put("p", 16);
        myhm.put("aa", 1);
        myhm.put("bb", 2);
        myhm.put("cc", 3);
        myhm.put("dd", 4);
        myhm.put("ee", 5);
        myhm.put("ff", 6);
        myhm.put("gg", 7);
        myhm.put("hh", 8);
        myhm.put("ii", 9);
        myhm.put("jj", 10);
        myhm.put("kk", 11);
        myhm.put("ll", 12);
        myhm.put("mm", 13);
        myhm.put("nn", 14);
        myhm.put("oo", 15);
        Set<String>  k =  myhm.keySet();
        System.out.println(k.size());
        System.out.println(myhm.containsKey("a"));
        System.out.println(myhm.get("a"));
        System.out.println(myhm.size);
        System.out.println(myhm.size());
    }
}
