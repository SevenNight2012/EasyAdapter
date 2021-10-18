package com.xxc.dev.adapter;

import android.util.SparseArray;

import java.util.HashMap;
import java.util.Map;

public class SparseUtils {


    public static <E> Map<Integer, E> convertMap(SparseArray<E> sparseArray) {
        Map<Integer, E> map = new HashMap<>();
        if (null == sparseArray) {
            return map;
        }
        for (int i = 0; i < sparseArray.size(); i++) {
            map.put(i, sparseArray.valueAt(i));
        }
        return map;
    }
}
