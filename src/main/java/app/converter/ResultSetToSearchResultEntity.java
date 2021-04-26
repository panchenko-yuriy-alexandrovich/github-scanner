package app.converter;

import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.db.model.SearchResultEntity;

public class ResultSetToSearchResultEntity {

    public SearchResultEntity convert(ResultSet resultSet) throws SQLException {
        final Clob clob = resultSet.getClob(3);
        String resultQuery = clob.getSubString(1, (int) Math.min(Integer.MAX_VALUE, clob.length()));

        return new SearchResultEntity(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultQuery,
                resultSet.getTimestamp(4).toLocalDateTime()
        );
    }
}
