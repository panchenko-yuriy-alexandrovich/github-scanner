package app.handler;

import javax.annotation.Nonnull;

import app.parse.SearchResultParser;
import app.service.SearchService;
import app.service.model.SearchResponse;
import io.jooby.Context;
import io.jooby.Route;

public class SearchHandler implements Route.Handler {

    private final SearchService searchService;
    private final SearchResultParser searchResultParser;

    public SearchHandler(SearchService searchService, SearchResultParser searchResultParser) {
        this.searchService = searchService;
        this.searchResultParser = searchResultParser;
    }

    @Nonnull
    @Override
    public Object apply(@Nonnull Context ctx) {

        SearchResponse searchResponse = searchService.search(ctx.path("name").value());

        return searchResultParser.write(searchResponse);
    }
}
