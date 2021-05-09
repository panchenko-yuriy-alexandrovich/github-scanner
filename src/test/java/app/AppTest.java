package app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.jooby.MockRouter;

class AppTest {

    @Test
    void health() {
        MockRouter router = new MockRouter(new App());

        String expectedResult = "{\"status\":\"UP\"}";

        assertEquals(expectedResult, router.get("/api/health").value());
    }

}