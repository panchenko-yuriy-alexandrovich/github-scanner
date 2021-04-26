package app.parse;

import static java.lang.Math.min;

import app.service.ParseException;
import app.service.model.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SearchResultParser {

    private final ObjectMapper objectMapper;

    public SearchResultParser() {
        objectMapper = new ObjectMapper();
    }

    public SearchResultParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SearchResult read(String raw) {
        SearchResult result;
        try {
            result = objectMapper.readValue(raw, SearchResult.class);
        } catch (JsonProcessingException e) {
            throw new ParseException(
                    String.format(ParseException.PATTERN,
                            raw.substring(0, min(raw.length(), 100)),
                            SearchResult.class.getCanonicalName()),
                    e
            );
        }

        return result;
    }

    public String write(SearchResult searchResult) {
        String result;
        try {
            result = objectMapper.writeValueAsString(searchResult);
        } catch (JsonProcessingException e) {
            throw new ParseException(
                    "Error from parsing SearchResult to string",
                    e
            );
        }

        return result;
    }
}
