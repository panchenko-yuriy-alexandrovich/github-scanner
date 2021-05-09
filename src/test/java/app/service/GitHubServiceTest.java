package app.service;

import static app.service.GitHubService.SEARCH_PATH;
import static java.lang.Thread.currentThread;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import app.net.GitHubApi;
import app.service.model.SearchGitHubResult;

class GitHubServiceTest {

    static final String fileName = "github-response.json";

    GitHubApi api = Mockito.mock(GitHubApi.class);
    GitHubService gitHubService = new GitHubService(api);

    @BeforeEach
    void init() throws IOException {
        String json = new String(requireNonNull(currentThread().getContextClassLoader().getResourceAsStream(fileName))
                .readAllBytes(), StandardCharsets.UTF_8);

        when(api.getRaw(any())).thenReturn(json);
    }

    @Test
    void search_whenApiReturnJson_thenParseItAndReturn() {
        String query = "selenide";
        String realProject = "selenide/selenide";

        SearchGitHubResult result = gitHubService.search(query);

        assertNotNull(result);
        assertEquals(1681, result.getTotalCount());
        assertTrue(result.getItems().stream().anyMatch(i -> realProject.equals(i.getFullName())));

        verify(api, times(1)).getRaw(String.format(SEARCH_PATH, query));
    }

    @Test
    void search_whenParseIsWrong_thenThrowParseException() {
        String msg = "wrong parse data";

        when(api.getRaw(any())).thenReturn(msg);

        ParseException parseException =
                assertThrows(ParseException.class, () -> gitHubService.search("test"));

        String expMsg = String.format(ParseException.PATTERN, msg, SearchGitHubResult.class.getCanonicalName());

        assertEquals(expMsg, parseException.getMessage());
    }

    @Test
    void search_whenSpecialSymbolsAreInQuery_thenQueryIsEscaped() {
        gitHubService.search("   привет тест % /?&");

        String encodedQuery = "+++%D0%BF%D1%80%D0%B8%D0%B2%D0%B5%D1%82+%D1%82%D0%B5%D1%81%D1%82+%25+%2F%3F%26";

        verify(api, times(1))
                .getRaw(String.format(SEARCH_PATH, encodedQuery));
    }

    @Test
    void getPath_whenPageIsNull_thenUrlWithoutPage() {
        String query = "angryip";

        String angryipPath = gitHubService.getPath(query, null);

        assertTrue(angryipPath.contains("?q=" + query));
        assertFalse(angryipPath.contains("&page"));
    }

    @Test
    void getPath_whenPageIsNotNull_thenUrlWithPage() {
        String query = "angryip";

        String angryipPath = gitHubService.getPath(query, 2);

        assertTrue(angryipPath.contains("&page=2"));
    }
}