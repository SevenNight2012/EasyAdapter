package com.xxc.dev.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * controller interface
 *
 * @param <T> data bean class type
 */
public interface EasyController<T> {

    int layoutId();

    void initView(@NonNull View itemView, @NonNull EasyHolder holder);

    void onBindItem(@NonNull T item, int position, @NonNull EasyHolder holder, @Nullable List<Object> payloads);
}
