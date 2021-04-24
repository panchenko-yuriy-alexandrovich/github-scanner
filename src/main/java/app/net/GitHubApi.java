package app.net;

import static app.net.GitHubApi.BadPathException.MSG_EMPTY;
import static app.net.GitHubApi.BadPathException.PATTERN_SLASH_START;
import static java.lang.String.format;
import static java.net.http.HttpResponse.BodyHandlers.ofString;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class GitHubApi {

    public static final String BASE_URI = "https://api.github.com";

    public static final String SLASH = "/";

    private final HttpClient httpClient;

    public GitHubApi(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String getRaw(String path) {

        if (path == null || path.isEmpty()) {
            throw new BadPathException(MSG_EMPTY);
        }

        if (path.startsWith(SLASH)) {
            throw new BadPathException(format(PATTERN_SLASH_START, path));
        }

        String responseBody;
        String url = BASE_URI + SLASH + path;
        try {
            responseBody = httpClient.send(createRequest(url), ofString()).body();
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new HttpException(
                    format(HttpException.PATTERN, url),
                    e
            );
        }

        return responseBody;
    }

    HttpRequest createRequest(String path) throws URISyntaxException {

        return HttpRequest.newBuilder(new URI(path))
                          .GET()
                          .header("Accept", "application/vnd.github.v3+json").build();
    }

    public static class HttpException extends RuntimeException {

        public final static String PATTERN = "Error reading data from %s";

        public HttpException(String msg, Throwable cause) {
            super(msg, cause);
        }
    }

    public static class BadPathException extends RuntimeException {

        public final static String PATTERN_SLASH_START = "You can not start path %s with '/'";

        public final static String MSG_EMPTY = "You can not have an empty path";

        public BadPathException(String msg) {
            super(msg);
        }
    }
}
