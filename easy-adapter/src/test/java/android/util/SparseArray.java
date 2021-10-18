package android.util;

import java.util.HashMap;

public class SparseArray<E> extends HashMap<Integer, E> {

    public void put(int key, E value) {
        super.put(key, value);
    }

    public E valueAt(int index) {
        return get(index);
    }
}
