package org.example.github.scanner.context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BeanCreator {

    @SuppressWarnings("unchecked")
    public <T> T createAndGet(Class<T> clazz, Map<String, Object> context) {
        return (T) createAndGet(clazz, context, new HashSet<>());
    }

    private Object createAndGet(Class<?> clazz, Map<String, Object> context, Set<String> alreadyFoundBeans) {
        Optional<Constructor<?>> constructorWithoutDependencies = Arrays.stream(clazz.getDeclaredConstructors())
                                                                        .filter(c -> c.getParameterCount() == 0)
                                                                        .findFirst();

        return constructorWithoutDependencies.isPresent() ?
                createWithoutDependencies(constructorWithoutDependencies.get(), context) :
                createWithDependencies(clazz, context, alreadyFoundBeans);
    }

    Object createObjectWithoutDependencies(Constructor<?> constructor) {
        Object result;
        try {
            constructor.setAccessible(true);
            result = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new CreationBeanException(
                    String.format("Can not create an instance of class %s", constructor.getDeclaringClass().getCanonicalName()),
                    e
            );
        }

        return result;
    }

    Object createObjectWithDependencies(Constructor<?> constructor, List<?> initArgs) {
        Object result;
        try {
            constructor.setAccessible(true);
            result = constructor.newInstance(initArgs.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new CreationBeanException(
                    String.format(CreationBeanException.PATTERN, constructor.getDeclaringClass().getCanonicalName()),
                    e
            );
        }

        return result;
    }

    private Object createWithoutDependencies(Constructor<?> constructor, Map<String, Object> context) {
        Object creation = createObjectWithoutDependencies(constructor);
        context.put(constructor.getDeclaringClass().getCanonicalName(), creation);

        return creation;
    }

    private Object createWithDependencies(Class<?> clazz, Map<String, Object> context, Set<String> alreadyFoundBeans) {
        Constructor<?> constructor = Arrays.stream(clazz.getDeclaredConstructors()).findFirst().get();
        String className = constructor.getDeclaringClass().getCanonicalName();
        if (alreadyFoundBeans.contains(className)) { //TODO construct a tree of all dependencies and analyze
            throw new CreationBeanLoopException(String.format(CreationBeanLoopException.PATTERN, className));
        }
        alreadyFoundBeans.add(className);
        List<Object> filledConstructorArgs = new ArrayList<>();
        for (Class<?> dep : constructor.getParameterTypes()) {
            String depName = dep.getCanonicalName();
            Object obj = context.get(depName);
            if (obj == null) {
                obj = createAndGet(dep, context, alreadyFoundBeans);
                context.put(dep.getCanonicalName(), obj);
            }
            filledConstructorArgs.add(obj);
        }

        return createObjectWithDependencies(constructor, filledConstructorArgs);
    }

    public static class CreationBeanException extends RuntimeException {

        public final static String PATTERN = "Can not create an instance of class %s";

        public CreationBeanException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class CreationBeanLoopException extends RuntimeException {

        public final static String PATTERN = "The class %s was already found in the chain of creation.";

        public CreationBeanLoopException(String msg) {
            super(msg);
        }
    }
}
