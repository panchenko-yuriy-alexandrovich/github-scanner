package app.converter;

import java.sql.ResultSet;
import java.sql.SQLException;

import app.db.model.SearchResultEntity;

public class ResultSetToSearchResultEntity implements Converter<ResultSet, SearchResultEntity> {

    @Override
    public SearchResultEntity convert(ResultSet resultSet) throws SQLException {

        return new SearchResultEntity(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getTimestamp(4).toLocalDateTime()
        );
    }
}
