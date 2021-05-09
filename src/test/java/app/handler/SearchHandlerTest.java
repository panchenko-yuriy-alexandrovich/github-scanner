package app.handler;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import app.parse.SearchResultParser;
import app.service.SearchService;
import app.service.model.SearchResponse;
import io.jooby.Context;
import io.jooby.Value;

class SearchHandlerTest {

    SearchService searchService = mock(SearchService.class);
    SearchResultParser searchResultParser = mock(SearchResultParser.class);
    Context ctx = mock(Context.class);
    Value val = mock(Value.class);

    SearchHandler searchHandler = new SearchHandler(searchService, searchResultParser);

    @Test
    void apply_whenServiceReturnResult_thenReturnJsonFromResult() {
        when(val.value()).thenReturn("test");
        when(ctx.path(anyString())).thenReturn(val);

        SearchResponse serviceResponse = new SearchResponse(null, null, null, null, null);

        when(searchService.search(anyString())).thenReturn(serviceResponse);
        when(searchResultParser.write(any())).thenReturn("selenide/test");

        Object result = searchHandler.apply(ctx);

        assertTrue(result instanceof String);

        String strResult = (String) result;
        assertTrue(strResult.contains("selenide/test"));
    }
}