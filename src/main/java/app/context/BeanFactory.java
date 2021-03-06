package app.context;

import java.util.HashMap;
import java.util.Map;

public class BeanFactory {

    private final Map<String, Object> context;

    private final BeanCreator beanCreator;

    public BeanFactory() {
        beanCreator = new BeanCreator();
        context = new HashMap<>();
    }

    public BeanFactory(Map<String, Object> context, BeanCreator beanCreator) {
        this.context = context;
        this.beanCreator = beanCreator;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOrCreate(Class<T> clazz) {
        Object singleton = get(clazz.getCanonicalName());
        if (singleton == null) {
            singleton = beanCreator.createAndGet(clazz, context);
        }

        return (T) singleton;
    }

    public <T> T add(T obj) {
        context.put(obj.getClass().getCanonicalName(), obj);

        return obj;
    }

    public <T> T add(String key, T obj) {
        context.put(key, obj);

        return obj;
    }

    Object get(String name) {

        return context.get(name);
    }

}
