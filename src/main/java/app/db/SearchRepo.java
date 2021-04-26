package app.db;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import app.converter.ResultSetToSearchResultEntity;
import app.db.model.SearchResultEntity;

public class SearchRepo {

    private final DataSource ds;
    private final ResultSetToSearchResultEntity resultSetToSearchResultEntity;

    public SearchRepo(DataSource ds, ResultSetToSearchResultEntity resultSetToSearchResultEntity) {
        this.ds = ds;
        this.resultSetToSearchResultEntity = resultSetToSearchResultEntity;
    }

    public SearchResultEntity findByQuery(String query) {
        final String sql = "SELECT id, query, result, date_created FROM requests WHERE query = ?";
        SearchResultEntity searchResultEntity;
        try {
            Connection connection = ds.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, query);
                try (ResultSet resultSet = statement.executeQuery()) {
                    searchResultEntity = resultSet.next() ? resultSetToSearchResultEntity.convert(resultSet) : null;
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error of retrieving data from database with query = " + query, e);
        }

        return searchResultEntity;
    }

    public SearchResultEntity save(SearchResultEntity searchResultEntity) {
        final String sql = "INSERT INTO requests(query, result) values (?, ?)";
        try {
            Connection connection = ds.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql, RETURN_GENERATED_KEYS)) {
                statement.setString(1, searchResultEntity.getQuery());
                statement.setString(2, searchResultEntity.getResult());
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    keys.next();
                    searchResultEntity.setId(keys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DbException("Error of inserting data to database with name = " + searchResultEntity.getQuery(), e);
        }

        return searchResultEntity;
    }

    public SearchResultEntity update(SearchResultEntity searchResultEntity) {
        final String sql = "UPDATE requests SET result = ?, date_created = ? WHERE id = ?";
        try {
            Connection connection = ds.getConnection();
            searchResultEntity.setDateTime(LocalDateTime.now());
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, searchResultEntity.getResult());
                statement.setTimestamp(2, Timestamp.valueOf(searchResultEntity.getDateTime()));
                statement.setLong(3, searchResultEntity.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException("Error of updating data to database with name = " + searchResultEntity.getQuery(), e);
        }

        return searchResultEntity;
    }

    public void delete(SearchResultEntity searchResultEntity) {
        final String sql = "DELETE FROM requests WHERE id = ?";
        try {
            Connection connection = ds.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, searchResultEntity.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DbException("Error of deleting data from database with name = " + searchResultEntity.getQuery(), e);
        }
    }
}
