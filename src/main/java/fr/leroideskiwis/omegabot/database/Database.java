package fr.leroideskiwis.omegabot.database;

import java.sql.*;
import java.util.Optional;

public class Database {

    private Connection connection;
    public static Database database;

    public static Database getDatabase() throws SQLException {
        return database == null ? database = new Database("data/database.sqlite") : database;
    }

    public Database(String file) throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:sqlite:"+file);
    }

    private PreparedStatement preparedStatement(String query, Object... objects) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for(int i = 0; i < objects.length; i++){
            preparedStatement.setObject(i+1, objects[i]);
        }
        return preparedStatement;
    }

    public void execute(String query, Object... objects) throws SQLException {
        try(PreparedStatement preparedStatement = preparedStatement(query, objects)){
            preparedStatement.execute();
        }
    }

    public <T> Optional<T> getFirst(String query, String columnName, Class<T> clazz, Object... objects) throws SQLException {
        PreparedStatement preparedStatement = preparedStatement(query, objects);
        ResultSet resultSet = preparedStatement.executeQuery();
        Optional<T> result = resultSet.next() ? Optional.of(resultSet.getObject(columnName, clazz)) : Optional.empty();
        resultSet.close();
        preparedStatement.close();
        return result;
    }

}
