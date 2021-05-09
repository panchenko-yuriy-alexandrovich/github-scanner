package app.parse;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.lang.Math.min;

import app.service.ParseException;
import app.service.model.SearchGitHubResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class SearchResultParser {

    private final ObjectMapper objectMapper;

    public SearchResultParser() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule()).disable(WRITE_DATES_AS_TIMESTAMPS);
    }

    public SearchResultParser(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T read(String raw, Class<T> clazz) {
        T result;
        try {
            result = objectMapper.readValue(raw, clazz);
        } catch (JsonProcessingException e) {
            throw new ParseException(
                    String.format(ParseException.PATTERN,
                            raw.substring(0, min(raw.length(), 100)),
                            SearchGitHubResult.class.getCanonicalName()),
                    e
            );
        }

        return result;
    }

    public String write(Object searchResult) {
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
