package com.xxc.dev.adapter;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class DefaultControllerInfoFactoryTest {


    @Test
    public void testParseMethod() {
        DefaultControllerInfoFactory handler = new DefaultControllerInfoFactory(SubControllerFactory.class);
        Map<Class<? extends ControllerFactory>, Map<Integer, ViewTypeMethod>> factoryMethod = DefaultControllerInfoFactory.FACTORY_METHOD;
        Map<Integer, ViewTypeMethod> methodMap = factoryMethod.get(SubControllerFactory.class);
        if (null == methodMap) {
            System.out.println("methodMap is null");
            return;
        }
        Set<Integer> keySet = methodMap.keySet();
        for (Integer key : keySet) {
            ViewTypeMethod method = methodMap.get(key);
            if (method != null) {
                Method viewTypeMethod = method.getMethod();
                String simpleName = viewTypeMethod.getDeclaringClass().getSimpleName();
                System.out.println("viewType :" + key + "  " + viewTypeMethod.getName() + "  " + simpleName);
            }
        }
    }

    @Test
    public void instanceTest() {
        Class<InstanceDemo> subControllerClass = InstanceDemo.class;
        try {
            Constructor<InstanceDemo> constructor = subControllerClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignableFromTest() {
        boolean assignableFrom = EasyController.class.isAssignableFrom(DogController.class);
        System.out.println("--- " + assignableFrom);
    }

    @Test
    public void testParameterizedType() {
        DefaultControllerInfoFactory.prepare(SubControllerFactory.class);
        Map<Class<? extends ControllerFactory>, Map<Integer, ViewTypeMethod>> factoryMethods = DefaultControllerInfoFactory.FACTORY_METHOD;
        Map<Integer, ViewTypeMethod> methodMap = factoryMethods.get(SubControllerFactory.class);
        if (methodMap == null) {
            System.out.println("methodMap is null");
            return;
        }
        ViewTypeMethod viewTypeMethod = methodMap.get(5);
        if (viewTypeMethod == null) {
            System.out.println("view type 5 is null");
            return;
        }
//        Type returnType = viewTypeMethod.getReturnType();
        long thisTime = System.currentTimeMillis();
        Class<?> returnClass = viewTypeMethod.getReturnClass();
        ParameterizedType parameterizedType = findParentParameterizedType(returnClass);
        showParameterizedType(parameterizedType);
        long during = System.currentTimeMillis() - thisTime;
        System.out.println("---> " + during);
    }

    private ParameterizedType findParentParameterizedType(Class<?> clazz) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            Type[] genericInterfaces = c.getGenericInterfaces();
            ParameterizedType type = findParameterizedInterface(genericInterfaces);
            if (null != type) {
                return type;
            }
        }
        return null;
    }

    private ParameterizedType findParameterizedInterface(Type[] interfaces) {
        if (null == interfaces) {
            return null;
        }
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();
                if (EasyController.class.getName().equals(rawType.getTypeName())) {
                    return (ParameterizedType) type;
                }
            }
        }
        return null;
    }

    private void showParameterizedType(ParameterizedType type) {
        if (null == type) {
            return;
        }
        Type rawType = type.getRawType();
        System.out.println("raw type "+rawType);
        Type[] actualTypeArguments = type.getActualTypeArguments();
        System.out.println(rawType.getTypeName());
        for (Type actualTypeArgument : actualTypeArguments) {
            System.out.println(actualTypeArgument.getTypeName());
        }
    }

    /**
     * 方法返回类型为接口带泛型时
     * interface com.xxc.dev.adapter.EasyController
     * com.xxc.dev.adapter.EasyController<com.xxc.dev.adapter.Dog>
     * com.xxc.dev.adapter.EasyController<com.xxc.dev.adapter.Dog>
     *
     * 方法返回类型为class时
     * class com.xxc.dev.adapter.DogController
     * class com.xxc.dev.adapter.DogController
     * com.xxc.dev.adapter.DogController
     */
    @Test
    public void findReturnType() {
        DefaultControllerInfoFactory.prepare(SubControllerFactory.class);
        Map<Class<? extends ControllerFactory>, Map<Integer, ViewTypeMethod>> factoryMethod = DefaultControllerInfoFactory.FACTORY_METHOD;
        Map<Integer, ViewTypeMethod> viewTypeMethodMap = factoryMethod.get(SubControllerFactory.class);
        if (null == viewTypeMethodMap) {
            return;
        }
        ViewTypeMethod viewTypeMethod = viewTypeMethodMap.get(11);
        if (viewTypeMethod == null) {
            return;
        }
        Class<?> returnClass = viewTypeMethod.getReturnClass();
        Type returnType = viewTypeMethod.getReturnType();
        System.out.println(returnClass+"\n"+returnType);
        String typeName = returnType.getTypeName();
        System.out.println(typeName);
    }

}