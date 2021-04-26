package app.service;

import static java.lang.Math.min;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import app.net.GitHubApi;
import app.service.model.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class GitHubService {

    public static final String SEARCH_PATH = "search/repositories?q=%s";
    public static final String SEARCH_PATH_PAGE = SEARCH_PATH + "&page=%d";

    private final GitHubApi api;

    private final ObjectMapper objectMapper;

    public GitHubService(GitHubApi api) {
        this.api = api;
        objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public SearchResult search(String query) {

        return search(query, null);
    }

    public SearchResult search(String query, Integer page) {
        String path = getPath(query, page);
        String raw = api.getRaw(path);

        SearchResult searchResult;

        try {
            searchResult = objectMapper.readValue(raw, SearchResult.class);
        } catch (JsonProcessingException e) {
            throw new ParseException(
                    String.format(ParseException.PATTERN, raw.substring(0, min(raw.length(), 100)),
                            SearchResult.class.getCanonicalName()),
                    e
            );
        }

        return searchResult;
    }

    String getPath(String query, Integer page) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        return page == null ?
                String.format(SEARCH_PATH, encodedQuery) :
                String.format(SEARCH_PATH_PAGE, encodedQuery, page);
    }

    public static class ParseException extends RuntimeException {

        public static final String PATTERN = "Error on parsing string with first 100 symbols [%s] to class %s";

        public ParseException(String msg, Throwable clause) {
            super(msg, clause);
        }
    }
}
