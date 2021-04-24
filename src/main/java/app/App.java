package app;

import java.net.http.HttpClient;

import app.context.BeanFactory;
import app.model.SearchResult;
import app.net.GitHubService;

public class App {

    public static void main(String[] args) {
        BeanFactory context = new BeanFactory();
        context.add(HttpClient.class.getCanonicalName(), HttpClient.newHttpClient());

        GitHubService gitHubService = context.getOrCreate(GitHubService.class);

        SearchResult selenide = gitHubService.search("selenide");
        System.out.println(selenide.getTotalCount());
        selenide.getItems().stream().limit(10).forEach(i -> System.out.println(i.getFullName()));
    }
}
