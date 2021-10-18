package com.xxc.dev.adapter;

import java.lang.reflect.Constructor;

public class ControllerInfo {

    private final Class<?> mControllerBeanClazz;

    public EasyController<?> mController;

    ControllerInfo() {
        mControllerBeanClazz = null;
    }

    public ControllerInfo(Class<?> controllerClazz, Class<?> controllerBeanClazz) {
        mControllerBeanClazz = controllerBeanClazz;
        try {
            Constructor<?> constructor = controllerClazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            mController = (EasyController<?>) constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isBean(Object bean) {
        return null != mControllerBeanClazz && mControllerBeanClazz.isInstance(bean);
    }
}
