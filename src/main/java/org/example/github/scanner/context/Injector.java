package org.example.github.scanner.context;

import java.util.HashMap;
import java.util.Map;

public class Injector {

    private final Map<String, Object> context;

    private final BeanCreator beanCreator;

    public Injector() {
        beanCreator = new BeanCreator();
        context = new HashMap<>();
    }

    public Injector(Map<String, Object> context, BeanCreator beanCreator) {
        this.context = context;
        this.beanCreator = beanCreator;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> clazz) {
        Object singleton = context.get(clazz.getCanonicalName());
        if (singleton == null) {
            singleton = beanCreator.createAndGet(clazz, context);
        }

        return (T) singleton;
    }

}
