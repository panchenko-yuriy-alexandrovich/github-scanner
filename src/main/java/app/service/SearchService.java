package app.service;

import java.time.LocalDateTime;

import app.db.SearchRepo;
import app.db.model.SearchResultEntity;
import app.parse.SearchResultParser;
import app.service.model.SearchResponse;
import app.service.model.SearchResult;

public class SearchService {

    private final GitHubService gitHubService;
    private final SearchRepo searchRepo;
    private final SearchResultParser searchResultParser;
    private final DiffService diffService;

    public SearchService(GitHubService gitHubService, SearchRepo searchRepo, SearchResultParser searchResultParser, DiffService diffService) {
        this.gitHubService = gitHubService;
        this.searchRepo = searchRepo;
        this.searchResultParser = searchResultParser;
        this.diffService = diffService;
    }

    public SearchResponse search(String query) {
        SearchResultEntity resultEntity = searchRepo.findByQuery(query);

        SearchResult currentSearch = gitHubService.search(query);
        SearchResult previousSearch = null;
        LocalDateTime prevTime = null;
        if (resultEntity != null) {
            previousSearch = searchResultParser.read(resultEntity.getResult());
            prevTime = resultEntity.getDateTime();
            resultEntity.setDateTime(LocalDateTime.now());
            resultEntity.setResult(searchResultParser.write(currentSearch));
            searchRepo.update(resultEntity);
        } else {
            searchRepo.save(new SearchResultEntity(query, searchResultParser.write(currentSearch)));
        }

        return new SearchResponse(
                currentSearch,
                LocalDateTime.now(),
                previousSearch,
                prevTime,
                diffService.create(previousSearch, currentSearch)
        );
    }
}
