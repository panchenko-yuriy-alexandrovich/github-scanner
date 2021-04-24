package app.context;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BeanFactoryTest {

    @Test
    void get_whenObjectIsInsideContext_thenReturnFromContext() {
        BeanCreator beanCreator = Mockito.mock(BeanCreator.class);
        Date dateForTest = new Date();
        Map<String, Object> context = new HashMap<>();
        context.put(dateForTest.getClass().getCanonicalName(), dateForTest);
        BeanFactory subj = new BeanFactory(context, beanCreator);

        Date dateFromContext = subj.getOrCreate(Date.class);

        assertSame(dateForTest, dateFromContext);
    }

    @Test
    void get_whenObjectIsNotInsideContext_thenNewOneIsCreatedAndReturned() {
        BeanCreator beanCreator = Mockito.mock(BeanCreator.class);
        when(beanCreator.createAndGet(any(), any())).thenReturn(new Date());
        Map<String, Object> context = new HashMap<>();
        BeanFactory subj = new BeanFactory(context, beanCreator);

        Date createdDate = subj.getOrCreate(Date.class);

        assertNotNull(createdDate);

        verify(beanCreator, times(1)).createAndGet(Date.class, context);
    }

    @Test
    void get() {
        BeanFactory subj = new BeanFactory();
        Date dateFirst = subj.getOrCreate(Date.class);
        Date dateSecond = subj.getOrCreate(Date.class);
        assertSame(dateFirst, dateSecond);
    }

    @Test
    void add() {
        BeanFactory subj = new BeanFactory();
        Date dateFirst = subj.add(new Date());
        Date dateSecond = subj.getOrCreate(Date.class);
        assertSame(dateFirst, dateSecond);
    }
}