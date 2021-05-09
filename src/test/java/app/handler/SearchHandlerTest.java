package app.handler;


import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import app.parse.SearchResultParser;
import app.service.SearchService;
import app.service.model.SearchResponse;
import app.service.model.SearchResult;
import io.jooby.Body;
import io.jooby.Context;

class SearchHandlerTest {

    SearchService searchService = mock(SearchService.class);
    SearchResultParser searchResultParser = new SearchResultParser();
    Context ctx = mock(Context.class);
    Body val = mock(Body.class);

    SearchHandler searchHandler = new SearchHandler(searchService, searchResultParser);

    @Test
    void apply_whenServiceReturnResult_thenReturnJsonFromResult() {
        when(val.value()).thenReturn("{\"name\":\"test\"}");
        when(ctx.body()).thenReturn(val);

        SearchResponse serviceResponse = new SearchResponse(new SearchResult(1, singleton("selenide/test")), null, null, null, null);

        when(searchService.search(anyString())).thenReturn(serviceResponse);

        Object result = searchHandler.apply(ctx);

        assertTrue(result instanceof String);

        String strResult = (String) result;
        assertTrue(strResult.contains("selenide/test"));

        verify(searchService).search("test");
    }
}