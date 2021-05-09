package app.parse;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import app.service.ParseException;
import app.service.model.SearchResponse;
import app.service.model.SearchGitHubResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class SearchGitHubResultParserTest {

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    SearchResultParser subj = new SearchResultParser(objectMapper);

    @Test
    void read() {
        SearchResultParser subj = new SearchResultParser();
        SearchGitHubResult searchResult = subj.read("{\"totalCount\":0,\"items\":[]}", SearchGitHubResult.class);

        assertEquals(0, searchResult.getTotalCount());
        assertNotNull(searchResult.getItems());
        assertEquals(0, searchResult.getItems().size());
    }

    @Test
    void read_whenParseIsWrong_thenExceptionThrow() throws JsonProcessingException {
        when(objectMapper.readValue(any(String.class), any(Class.class))).thenThrow(JsonProcessingException.class);

        Throwable exception = assertThrows(ParseException.class,
                () -> subj.read("test", SearchGitHubResult.class));

        assertTrue(exception.getMessage().contains("test"));
        verify(objectMapper, times(1)).readValue("test", SearchGitHubResult.class);
    }

    @Test
    void write() {
        SearchGitHubResult objectToWrite = new SearchGitHubResult();
        objectToWrite.setTotalCount(0);
        objectToWrite.setItems(emptyList());

        SearchResultParser subj = new SearchResultParser();

        String resultString = subj.write(objectToWrite);

        assertEquals("{\"totalCount\":0,\"items\":[]}", resultString);
    }

    @Test
    void write_whenParseIsWrong_thenExceptionThrow() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);


        SearchGitHubResult objectToWrite = new SearchGitHubResult();
        Throwable exception = assertThrows(ParseException.class,
                () -> subj.write(objectToWrite));

        assertTrue(exception.getMessage().contains("parsing SearchResult to string"));
        verify(objectMapper, times(1)).writeValueAsString(objectToWrite);
    }

    @Test
    void write_withJava8Dates() {
        SearchResultParser subj = new SearchResultParser();

        LocalDateTime now = now();
        SearchResponse resp = new SearchResponse(null, now, null, null, null);

        String str = subj.write(resp);
        assertTrue(str.contains(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))));
    }
}