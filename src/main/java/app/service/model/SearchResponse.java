package app.service.model;

import java.time.LocalDateTime;

public class SearchResponse {

    private SearchResult currentSearch;
    private LocalDateTime currentDate;
    private SearchResult previousSearch;
    private LocalDateTime previousDate;
    private Difference diff;

    public SearchResponse(SearchResult currentSearch, LocalDateTime currentDate, SearchResult previousSearch, LocalDateTime previousDate, Difference diff) {
        this.currentSearch = currentSearch;
        this.currentDate = currentDate;
        this.previousSearch = previousSearch;
        this.previousDate = previousDate;
        this.diff = diff;
    }

    public SearchResult getCurrentSearch() {
        return currentSearch;
    }

    public LocalDateTime getCurrentDate() {
        return currentDate;
    }

    public SearchResult getPreviousSearch() {
        return previousSearch;
    }

    public LocalDateTime getPreviousDate() {
        return previousDate;
    }

    public Difference getDiff() {
        return diff;
    }
}
