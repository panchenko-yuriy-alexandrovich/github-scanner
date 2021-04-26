package app.service.model;

import java.util.Set;

public class Difference {
    private int countDiff;
    private Set<String> deletedFromLastSearch;
    private Set<String> addedFromLastSearch;

    public Difference() {
    }

    public int getCountDiff() {
        return countDiff;
    }

    public void setCountDiff(int countDiff) {
        this.countDiff = countDiff;
    }

    public Set<String> getDeletedFromLastSearch() {
        return deletedFromLastSearch;
    }

    public void setDeletedFromLastSearch(Set<String> deletedFromLastSearch) {
        this.deletedFromLastSearch = deletedFromLastSearch;
    }

    public Set<String> getAddedFromLastSearch() {
        return addedFromLastSearch;
    }

    public void setAddedFromLastSearch(Set<String> addedFromLastSearch) {
        this.addedFromLastSearch = addedFromLastSearch;
    }
}
