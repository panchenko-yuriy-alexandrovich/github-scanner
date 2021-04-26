package app.db.model;

import java.time.LocalDateTime;

public class SearchResultEntity {

    private Long id;
    private String query;
    private String result;
    private LocalDateTime dateTime;

    public SearchResultEntity() {
    }

    public SearchResultEntity(Long id, String query, String result, LocalDateTime dateTime) {
        this.id = id;
        this.query = query;
        this.result = result;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
