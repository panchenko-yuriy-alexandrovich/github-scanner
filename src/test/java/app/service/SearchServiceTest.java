package app.service;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import app.converter.SearchGitHubResultToSearchResult;
import app.db.SearchRepo;
import app.db.model.SearchResultEntity;
import app.parse.SearchResultParser;
import app.service.model.SearchGitHubResult;
import app.service.model.SearchResponse;
import app.service.model.SearchResult;

class SearchServiceTest {

    GitHubService gitHubService = mock(GitHubService.class);
    SearchRepo searchRepo = mock(SearchRepo.class);
    SearchResultParser searchResultParser = mock(SearchResultParser.class);
    DiffService diffService = new DiffService();
    SearchGitHubResultToSearchResult searchGitHubResultToSearchResult = new SearchGitHubResultToSearchResult();

    SearchService subj = new SearchService(gitHubService, searchRepo, searchResultParser, diffService, searchGitHubResultToSearchResult);

    @Test
    void search_whenRepoReturnNull_thenSaveIsCalled() {
        when(searchRepo.findByQuery(any(String.class))).thenReturn(null);

        SearchGitHubResult result = new SearchGitHubResult();
        result.setTotalCount(42);
        result.setItems(emptyList());
        when(gitHubService.search(any(String.class))).thenReturn(result);

        String parsedResult = "test";
        when(searchResultParser.write(any())).thenReturn(parsedResult);

        SearchResponse searchResponse = subj.search("test");

        assertNotNull(searchResponse);
        assertNotNull(searchResponse.getCurrentDate());
        assertEquals(42, searchResponse.getCurrentSearch().getTotalCount());
        assertNull(searchResponse.getPreviousSearch());
        assertNull(searchResponse.getPreviousDate());
        assertEquals(42, searchResponse.getDiff().getCountDiff());

        verify(searchRepo, times(1)).save(any(SearchResultEntity.class));
    }

    @Test
    void search_whenRepoReturnNotNull_thenUpdateIsCalled() {
        when(searchRepo.findByQuery(any(String.class))).thenReturn(new SearchResultEntity());

        SearchGitHubResult result = new SearchGitHubResult();
        result.setTotalCount(42);
        result.setItems(emptyList());
        when(gitHubService.search(any())).thenReturn(result);

        SearchResult resultPrev = new SearchResult();
        resultPrev.setTotalCount(3);
        resultPrev.setNames(emptySet());
        when(searchResultParser.read(any(), any())).thenReturn(resultPrev);
        when(searchResultParser.write(any())).thenReturn("test");

        SearchResponse searchResponse = subj.search("test");

        assertNotNull(searchResponse);
        assertNotNull(searchResponse.getPreviousSearch());
        assertNotNull(searchResponse.getCurrentDate());
        assertEquals(42, searchResponse.getCurrentSearch().getTotalCount());
        assertEquals(3, searchResponse.getPreviousSearch().getTotalCount());
        assertEquals(42 - 3, searchResponse.getDiff().getCountDiff());

        verify(searchRepo, times(1)).update(any(SearchResultEntity.class));
    }
}