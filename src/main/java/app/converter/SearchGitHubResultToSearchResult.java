package app.converter;

import java.util.stream.Collectors;

import app.service.model.SearchGitHubResult;
import app.service.model.SearchItem;
import app.service.model.SearchResult;

public class SearchGitHubResultToSearchResult implements Converter<SearchGitHubResult, SearchResult> {

    @Override
    public SearchResult convert(SearchGitHubResult from) {

        return new SearchResult(from.getTotalCount(),
                from.getItems().stream().map(SearchItem::getFullName).collect(Collectors.toSet()));
    }
}
