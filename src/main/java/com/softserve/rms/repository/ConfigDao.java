package com.softserve.rms.repository;

import com.softserve.rms.entities.DataSourceConfig;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ConfigDao {

    public DataSourceConfig findByName(String name,Connection connection) throws SQLException {
        DataSourceConfig dataSourceConfig = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from datasourceconfig where name =?")) {
            preparedStatement.setString(1,name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    dataSourceConfig =
                            new DataSourceConfig(
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    resultSet.getString(6));
                }
            }
        }
        return dataSourceConfig;
    }
}
