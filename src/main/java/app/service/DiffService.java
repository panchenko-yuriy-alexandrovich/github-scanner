package app.service;

import static java.util.Collections.emptySet;
import static java.util.function.Predicate.not;

import java.util.Set;
import java.util.stream.Collectors;

import app.service.model.Difference;
import app.service.model.SearchResult;

public class DiffService {

    public Difference create(SearchResult previousSearch, SearchResult currentSearch) {
        Difference difference = new Difference();
        Set<String> currentProjects = currentSearch.getNames();

        if (previousSearch == null) {
            difference.setCountDiff(currentSearch.getTotalCount());
            difference.setDeletedFromLastSearch(emptySet());
            difference.setAddedFromLastSearch(currentProjects);
        } else {
            difference.setCountDiff(Math.abs(previousSearch.getTotalCount() - currentSearch.getTotalCount()));
            Set<String> preciousProjects = previousSearch.getNames();


            difference.setDeletedFromLastSearch(
                    preciousProjects.stream().filter(not(currentProjects::contains)).collect(Collectors.toSet()));
            difference.setAddedFromLastSearch(
                    currentProjects.stream().filter(not(preciousProjects::contains)).collect(Collectors.toSet()));
        }

        return difference;
    }
}
