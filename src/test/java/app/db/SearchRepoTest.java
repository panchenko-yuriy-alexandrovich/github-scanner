package app.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import app.converter.ResultSetToSearchResultEntity;
import app.db.model.SearchResultEntity;

class SearchRepoTest extends DbTest {

    @Test
    void findByQuery() throws SQLException {
        SearchRepo searchRepo = new SearchRepo(super.ds, new ResultSetToSearchResultEntity());
        ds.migrate();

        SearchResultEntity test = searchRepo.findByQuery("test");

        assertEquals("test", test.getQuery());
        assertEquals("test", test.getResult());
    }

    @Test
    void findByQuery_whenNoDataFound_thenNullReturn() throws SQLException {
        SearchRepo searchRepo = new SearchRepo(super.ds, new ResultSetToSearchResultEntity());
        ds.migrate();

        SearchResultEntity test = searchRepo.findByQuery("not existing search");

        assertNull(test);
    }

    @Test
    void insert() throws SQLException {
        SearchRepo searchRepo = new SearchRepo(super.ds, new ResultSetToSearchResultEntity());
        ds.migrate();

        SearchResultEntity searchResultEntity = new SearchResultEntity();
        searchResultEntity.setQuery("insert");
        searchResultEntity.setResult("insert,insert again");

        SearchResultEntity savedSearchResult = searchRepo.save(searchResultEntity);

        assertNotNull(savedSearchResult.getId());
        assertEquals("insert,insert again", savedSearchResult.getResult());

        SearchResultEntity searchResult = searchRepo.findByQuery(savedSearchResult.getQuery());

        assertEquals(savedSearchResult.getResult(), searchResult.getResult());
    }

    @Test
    void update() throws SQLException {
        SearchRepo searchRepo = new SearchRepo(super.ds, new ResultSetToSearchResultEntity());
        ds.migrate();

        SearchResultEntity searchResult = searchRepo.findByQuery("test");

        LocalDateTime dateTime = searchResult.getDateTime();

        String newResult = "some new test data";
        searchResult.setResult(newResult);

        searchRepo.update(searchResult);

        SearchResultEntity afterUpdateSearchResult = searchRepo.findByQuery("test");

        assertEquals(newResult, afterUpdateSearchResult.getResult());
        assertNotEquals(dateTime, afterUpdateSearchResult.getDateTime());
        assertTrue(afterUpdateSearchResult.getDateTime().isAfter(dateTime));
    }

    @Test
    void delete() throws SQLException {
        SearchRepo searchRepo = new SearchRepo(super.ds, new ResultSetToSearchResultEntity());
        ds.migrate();

        SearchResultEntity test = searchRepo.findByQuery("test");

        assertNotNull(test);
        assertNotNull(test.getId());

        searchRepo.delete(test);

        SearchResultEntity afterDeletion = searchRepo.findByQuery("test");

        assertNull(afterDeletion);
    }
}