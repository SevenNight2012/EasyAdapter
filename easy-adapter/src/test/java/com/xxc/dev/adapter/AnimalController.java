package com.xxc.dev.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class AnimalController<T extends Animal> implements EasyController<T> {

    @Override
    public int layoutId() {
        return 0;
    }

    @Override
    public void initView(@NonNull View itemView, @NonNull EasyHolder holder) {

    }

    @Override
    public void onBindItem(@NonNull T item, int position, @NonNull EasyHolder holder, @Nullable List<Object> payloads) {

    }
}
