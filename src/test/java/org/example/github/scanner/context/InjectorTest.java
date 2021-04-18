package org.example.github.scanner.context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class InjectorTest {

    @Test
    void get_whenObjectIsInsideContext_thenReturnFromContext() {
        BeanCreator beanCreator = Mockito.mock(BeanCreator.class);
        Date dateForTest = new Date();
        Map<String, Object> context = new HashMap<>();
        context.put(dateForTest.getClass().getCanonicalName(), dateForTest);
        Injector subj = new Injector(context, beanCreator);

        Date dateFromContext = subj.get(Date.class);

        Assertions.assertSame(dateForTest, dateFromContext);
    }

    @Test
    void get_whenObjectIsNotInsideContext_thenNewOneIsCreatedAndReturned() {
        BeanCreator beanCreator = Mockito.mock(BeanCreator.class);
        when(beanCreator.createAndGet(any(), any())).thenReturn(new Date());
        Map<String, Object> context = new HashMap<>();
        Injector subj = new Injector(context, beanCreator);

        subj.get(Date.class);
        Mockito.verify(beanCreator, times(1)).createAndGet(Date.class, context);
    }

    @Test
    void get() {
        Injector subj = new Injector();
        Date dateFirst = subj.get(Date.class);
        Date dateSecond = subj.get(Date.class);
        Assertions.assertSame(dateFirst, dateSecond);
    }
}