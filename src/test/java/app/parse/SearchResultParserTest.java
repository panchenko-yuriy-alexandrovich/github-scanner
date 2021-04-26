package app.parse;

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

import org.junit.jupiter.api.Test;

import app.service.ParseException;
import app.service.model.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class SearchResultParserTest {

    ObjectMapper objectMapper = mock(ObjectMapper.class);
    SearchResultParser subj = new SearchResultParser(objectMapper);

    @Test
    void read() {
        SearchResultParser subj = new SearchResultParser();
        SearchResult searchResult = subj.read("{\"totalCount\":0,\"items\":[]}");

        assertEquals(0, searchResult.getTotalCount());
        assertNotNull(searchResult.getItems());
        assertEquals(0, searchResult.getItems().size());
    }

    @Test
    void read_whenParseIsWrong_thenExceptionThrow() throws JsonProcessingException {
        when(objectMapper.readValue(any(String.class), any(Class.class))).thenThrow(JsonProcessingException.class);

        Throwable exception = assertThrows(ParseException.class,
                () -> subj.read("test"));

        assertTrue(exception.getMessage().contains("test"));
        verify(objectMapper, times(1)).readValue("test", SearchResult.class);
    }

    @Test
    void write() {
        SearchResult objectToWrite = new SearchResult();
        objectToWrite.setTotalCount(0);
        objectToWrite.setItems(emptyList());

        SearchResultParser subj = new SearchResultParser();

        String resultString = subj.write(objectToWrite);

        assertEquals("{\"totalCount\":0,\"items\":[]}", resultString);
    }

    @Test
    void write_whenParseIsWrong_thenExceptionThrow() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);


        SearchResult objectToWrite = new SearchResult();
        Throwable exception = assertThrows(ParseException.class,
                () -> subj.write(objectToWrite));

        assertTrue(exception.getMessage().contains("parsing SearchResult to string"));
        verify(objectMapper, times(1)).writeValueAsString(objectToWrite);
    }
}