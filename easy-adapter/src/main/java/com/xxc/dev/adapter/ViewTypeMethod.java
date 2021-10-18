package com.xxc.dev.adapter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Save the mapping of method and viewType
 */
class ViewTypeMethod {

    /**
     * 存储创建controller的方法
     * 子类的方法会在集合头部，父类的方法会在集合尾部
     */
    final List<Method> mInstanceMethods = new ArrayList<>();

    public final int viewType;
    public final MethodInfo mInfo;

    public ViewTypeMethod(int viewType, Method method) {
        this.viewType = viewType;
        mInstanceMethods.add(method);
        mInfo = new MethodInfo(method);
    }

    public ControllerInfo createInfo() {
        return new ControllerInfo(mInfo.mControllerClazz, mInfo.mControllerBeanClazz);
    }

    public void addMethod(Method method) {
        mInstanceMethods.add(method);
    }

    public Method getMethod() {
        return mInstanceMethods.get(0);
    }

    public Class<?> getReturnClass() {
        Method method = getMethod();
        return method.getReturnType();
    }

    public Type getReturnType() {
        return getMethod().getGenericReturnType();
    }

    public boolean isMethodAbstract() {
        Method method = getMethod();
        return Modifier.isAbstract(method.getModifiers());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewTypeMethod that = (ViewTypeMethod) o;
        return viewType == that.viewType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewType);
    }

    /**
     * 解析controller泛型实体
     */
    private static class MethodInfo {
        private Class<?> mControllerClazz;
        private Class<?> mControllerBeanClazz;

        public MethodInfo(Method method) {
            parseController(method);
        }

        private void parseController(Method method) {
            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType instanceof Class) {
                findActualBeanType((Class<?>) genericReturnType);
            } else if (genericReturnType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
                Type rawType = parameterizedType.getRawType();
                if (rawType instanceof Class) {
                    Class<?> clazz = (Class<?>) rawType;
                    if (Modifier.isAbstract(clazz.getModifiers())) {
                        throw new IllegalArgumentException("The controller can not be abstract");
                    }
                    mControllerClazz = clazz;
                }
                findActualBeanType(parameterizedType);
            } else {
                throw new IllegalArgumentException("Return type must implements EasyController");
            }
        }

        /**
         * 从class中找到model bean
         *
         * @param clazz class object
         * @return model bean
         */
        private void findActualBeanType(Class<?> clazz) {
            if (clazz == EasyController.class) {
                throw new IllegalArgumentException("Return type must implements EasyController");
            }
            for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
                Type[] genericInterfaces = c.getGenericInterfaces();
                for (Type item : genericInterfaces) {
                    if (item instanceof ParameterizedType && isEasyController((ParameterizedType) item)) {
                        ParameterizedType pType = ((ParameterizedType) item);
                        mControllerClazz = clazz;
                        findActualBeanType(pType);
                    }
                }
            }
        }

        /**
         * 从泛型类型中找到model bean
         *
         * @param type ParameterizedType
         */
        private void findActualBeanType(ParameterizedType type) {
            Type actualType = type.getActualTypeArguments()[0];
            if (actualType instanceof Class) {
                mControllerBeanClazz = (Class<?>) actualType;
            }
        }

        private boolean isEasyController(ParameterizedType type) {
            String rawName = type.getRawType().toString();
            return rawName.endsWith(EasyController.class.getName());
        }
    }
}
