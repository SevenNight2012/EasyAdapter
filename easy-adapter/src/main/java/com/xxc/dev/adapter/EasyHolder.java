package com.xxc.dev.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * default ViewHolder
 */
public abstract class EasyHolder extends RecyclerView.ViewHolder {

    protected final EasyAdapter<?> mAdapter;

    private ControllerInfo mControllerInfo;

    public EasyHolder(EasyAdapter<?> adapter, View itemView) {
        super(itemView);
        mAdapter = adapter;
    }

    final void setControllerInfo(ControllerInfo controllerInfo) {
        mControllerInfo = controllerInfo;
    }

    public void bindViewHolder(Object bean, int position, List<Object> payloads) {
        if (null == mControllerInfo) {
            return;
        }
        EasyController controller = mControllerInfo.mController;
        if (controller != null && mControllerInfo.isBean(bean)) {
            controller.onBindItem(bean, position, this, payloads);
        }
    }
}
