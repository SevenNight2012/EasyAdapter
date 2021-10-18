package com.xxc.dev.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Easy adapter core class
 *
 * @param <Bean> data bean
 */
public class EasyAdapter<Bean> extends RecyclerView.Adapter<EasyHolder> {

    protected List<Bean> mDataList = new ArrayList<>();
    protected LayoutInflater mInflater;

    private final ViewTypeFinder mViewTypeFinder;
    private ControllerInfoFactory mControllerInfoFactory;

    public EasyAdapter(int viewType) {
        this(new DefaultTypeFinder(viewType));
    }

    public EasyAdapter(ViewTypeFinder viewTypeFinder) {
        mViewTypeFinder = viewTypeFinder;
    }

    public void setControllerFactory(Class<? extends ControllerFactory> factoryClazz) {
        mControllerInfoFactory = new DefaultControllerInfoFactory(factoryClazz);
    }

    @NonNull
    @Override
    public EasyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (null == mInflater) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        ControllerInfo controllerInfo = mControllerInfoFactory.createControllerInfo(viewType);
        EasyController<?> controller = controllerInfo.mController;
        int layoutId = null == controller ? R.layout.null_controller_layout : controller.layoutId();
        View itemView = mInflater.inflate(layoutId, parent, false);
        EasyHolder holder = new EasyHolder(EasyAdapter.this, itemView) {
        };
        holder.setControllerInfo(controllerInfo);
        if (null != controller) {
            controller.initView(holder.itemView, holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EasyHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull EasyHolder holder, int position, @NonNull List<Object> payloads) {
        holder.bindViewHolder(mDataList.get(position), position, payloads);
    }

    @Override
    public int getItemViewType(int position) {
        return mViewTypeFinder.findViewType(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


}
