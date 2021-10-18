package com.xxc.dev.adapter;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * default controller info factory
 */
class DefaultControllerInfoFactory implements ControllerInfoFactory {

    volatile static Map<Class<? extends ControllerFactory>, Map<Integer, ViewTypeMethod>> FACTORY_METHOD = new ConcurrentHashMap<>();
    private final Map<Integer, ViewTypeMethod> mViewTypeMethodMap = new HashMap<>();

    public static void prepare(Class<? extends ControllerFactory> clazz) {
        Map<Integer, ViewTypeMethod> viewTypeMethodMap = parseFactoryMethod(clazz);
        FACTORY_METHOD.put(clazz, viewTypeMethodMap);
    }

    private static Map<Integer, ViewTypeMethod> parseFactoryMethod(Class<? extends ControllerFactory> controllerFactoryClazz) {
        if (!controllerFactoryClazz.isInterface()) {
            throw new IllegalArgumentException("Controller factory class must be interface");
        }
        Method[] methods = controllerFactoryClazz.getMethods();
        Map<Integer, ViewTypeMethod> viewTypeMethodMap = new ConcurrentHashMap<>();
        for (Method method : methods) {
            ViewType viewType = method.getAnnotation(ViewType.class);
            if (null == viewType) {
                continue;
            }
            int typeValue = viewType.value();
            addMethod(typeValue, method, viewTypeMethodMap);
        }
        return viewTypeMethodMap;
    }

    private static void addMethod(int typeValue, Method method, Map<Integer, ViewTypeMethod> methodMap) {
        ViewTypeMethod viewTypeMethod = methodMap.get(typeValue);
        if (null == viewTypeMethod) {
            synchronized (DefaultControllerInfoFactory.class) {
                viewTypeMethod = new ViewTypeMethod(typeValue, method);
                methodMap.put(typeValue, viewTypeMethod);
            }
        } else {
            viewTypeMethod.addMethod(method);
        }
    }

    public DefaultControllerInfoFactory(Class<? extends ControllerFactory> controllerFactoryClazz) {
        Map<Integer, ViewTypeMethod> methodMap = FACTORY_METHOD.get(controllerFactoryClazz);
        if (methodMap == null) {
            methodMap = parseFactoryMethod(controllerFactoryClazz);
        }
        mViewTypeMethodMap.putAll(methodMap);
    }

    @NonNull
    @Override
    public ControllerInfo createControllerInfo(int viewType) {
        ViewTypeMethod viewTypeMethod = mViewTypeMethodMap.get(viewType);
        return null == viewTypeMethod ? new ControllerInfo() : viewTypeMethod.createInfo();
    }
}
