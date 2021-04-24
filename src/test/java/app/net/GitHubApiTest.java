package app.net;

import static app.net.GitHubApi.BASE_URI;
import static app.net.GitHubApi.HttpException.PATTERN;
import static app.net.GitHubApi.SLASH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GitHubApiTest {

    HttpClient mockHttpClient = mock(HttpClient.class);
    GitHubApi subj = new GitHubApi(mockHttpClient);

    @Test
    void getRaw_whenPathStartsWithSlash_thenThrowException() {
        String path = "/test";

        GitHubApi.BadPathException badPathException = Assertions.assertThrows(GitHubApi.BadPathException.class,
                () -> subj.getRaw(path));

        String expectedMsg = String.format(GitHubApi.BadPathException.PATTERN_SLASH_START, path);
        assertEquals(expectedMsg, badPathException.getMessage());
    }

    @Test
    void getRaw_whenPathIsNull_thenThrowException() {
        String path = null;

        GitHubApi.BadPathException badPathException = Assertions.assertThrows(GitHubApi.BadPathException.class,
                () -> subj.getRaw(path));

        assertEquals(GitHubApi.BadPathException.MSG_EMPTY, badPathException.getMessage());
    }

    @Test
    void getRaw_whenPathIsEmpty_thenThrowException() {
        String path = "";

        GitHubApi.BadPathException badPathException = Assertions.assertThrows(GitHubApi.BadPathException.class,
                () -> subj.getRaw(path));

        assertEquals(GitHubApi.BadPathException.MSG_EMPTY, badPathException.getMessage());
    }

    @Test
    void getRaw_whenHttpClientThrowException_thenThrowCustomException() throws IOException, InterruptedException {
        String path = "test";

        when(mockHttpClient.send(any(), any())).thenThrow(new InterruptedException("testException"));

        GitHubApi.HttpException httpException = Assertions.assertThrows(GitHubApi.HttpException.class,
                () -> subj.getRaw(path));

        String expectedMsg = String.format(PATTERN, BASE_URI + SLASH + path);
        assertEquals(expectedMsg, httpException.getMessage());
    }

    @Test
    void createRequest_whenPathPassed_thenTheSamePathInsideRequest() throws URISyntaxException {
        String path = BASE_URI + SLASH + "test";

        HttpRequest request = subj.createRequest(path);

        assertNotNull(request);
        assertEquals(path, request.uri().toString());
    }

    @Test
    void getRaw_whenHttpClientReturnResponse_thenReturnBodyFromResponse() throws IOException, InterruptedException {
        String path = BASE_URI + SLASH + "test";
        String expectedResult = "{body}";
        HttpResponse<Object> response = mock(HttpResponse.class);
        when(mockHttpClient.send(any(), any())).thenReturn(response);
        when(response.body()).thenReturn(expectedResult);

        String result = subj.getRaw(path);

        assertNotNull(result);
        assertEquals(expectedResult, result);
    }
}