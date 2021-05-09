package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import app.context.BeanFactory;
import app.handler.SearchHandler;
import io.jooby.MockContext;
import io.jooby.MockRouter;

class AppTest {

    @Test
    void health() {
        MockRouter router = new MockRouter(new App());

        String expectedResult = "{\"status\":\"UP\"}";

        assertEquals(expectedResult, router.get("/api/health").value());
    }

    @Test
    void search() {
        App app = new App();
        BeanFactory context = mock(BeanFactory.class);
        App.context = context;

        MockRouter router = new MockRouter(app);

        SearchHandler handler = mock(SearchHandler.class);
        when(handler.apply(any())).thenReturn("test");
        when(context.getOrCreate(SearchHandler.class)).thenReturn(handler);

        MockContext mockContext = new MockContext();
        mockContext.setBody("{\"name\":\"test\"}");
        assertEquals("test", router.post("/api/search").value());
    }

}