package com.xxc.dev.adapter;

/**
 * default viewType finder
 */
public class DefaultTypeFinder implements ViewTypeFinder {

    private final int mViewType;

    public DefaultTypeFinder() {
        this(0);
    }

    public DefaultTypeFinder(int viewType) {
        mViewType = viewType;
    }

    @Override
    public int findViewType(int position) {
        return mViewType;
    }
}
