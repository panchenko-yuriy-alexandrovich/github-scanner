package app.service.model;

import java.util.Set;

public class SearchResult {

    private int totalCount;

    private Set<String> names;

    public SearchResult() {
    }

    public SearchResult(int totalCount, Set<String> names) {
        this.totalCount = totalCount;
        this.names = names;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<String> getNames() {
        return names;
    }

    public void setNames(Set<String> names) {
        this.names = names;
    }
}
