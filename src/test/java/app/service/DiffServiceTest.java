package app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import app.service.model.Difference;
import app.service.model.SearchResult;

class DiffServiceTest {

    DiffService subj = new DiffService();

    @Test
    void create_whenPrevIsNull_thenAllDataFromCurrent() {
        SearchResult current = new SearchResult();
        current.setTotalCount(42);
        current.setNames(Collections.singleton("Galaxy"));

        Difference difference = subj.create(null, current);

        assertNotNull(difference);
        assertEquals(42, difference.getCountDiff());
        assertNotNull(difference.getDeletedFromLastSearch());
        assertEquals(0, difference.getDeletedFromLastSearch().size());
        assertEquals(1, difference.getAddedFromLastSearch().size());
        assertTrue(difference.getAddedFromLastSearch().contains("Galaxy"));
    }

    @Test
    void create() {
        SearchResult current = new SearchResult();
        SearchResult prev = new SearchResult();

        current.setTotalCount(42);
        prev.setTotalCount(3);

        current.setNames(new HashSet<>(Arrays.asList("Test", "Galaxy")));
        prev.setNames(new HashSet<>(Arrays.asList("Del", "Galaxy")));

        Difference difference = subj.create(prev, current);

        assertNotNull(difference);
        assertEquals(42 - 3, difference.getCountDiff());
        assertNotNull(difference.getDeletedFromLastSearch());
        assertEquals(1, difference.getDeletedFromLastSearch().size());
        assertEquals(1, difference.getAddedFromLastSearch().size());
        assertTrue(difference.getAddedFromLastSearch().contains("Test"));
        assertTrue(difference.getDeletedFromLastSearch().contains("Del"));
    }
}