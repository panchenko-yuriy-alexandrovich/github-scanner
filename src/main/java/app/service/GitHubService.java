package app.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import app.net.GitHubApi;
import app.parse.SearchResultParser;
import app.service.model.SearchGitHubResult;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class GitHubService {

    public static final String SEARCH_PATH = "search/repositories?q=%s";
    public static final String SEARCH_PATH_PAGE = SEARCH_PATH + "&page=%d";

    private final GitHubApi api;
    private final SearchResultParser searchResultParser;

    public GitHubService(GitHubApi api) {
        this.api = api;
        searchResultParser = new SearchResultParser(new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    public SearchGitHubResult search(String query) {

        return search(query, null);
    }

    public SearchGitHubResult search(String query, Integer page) {
        String path = getPath(query, page);
        String raw = api.getRaw(path);

        return searchResultParser.read(raw, SearchGitHubResult.class);
    }

    String getPath(String query, Integer page) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        return page == null ?
                String.format(SEARCH_PATH, encodedQuery) :
                String.format(SEARCH_PATH_PAGE, encodedQuery, page);
    }
}
