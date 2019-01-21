package com.mathi.region.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author zMathi
 */
public class Database {
    
    private Connection connection = null;
    private String user = null;
    private String password = null;
    private String host = null;
    private Integer port = null;
    private String database = null;
    
    public Database(String host, Integer port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.connection = getConnection();
    }
    
    private Connection createConnection() {
        try {
            String connectionString = String.format("jdbc:mysql://%s:%s/%s?autoReconnect=true", host, port, database);
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (InstantiationException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
            connection = DriverManager.getConnection(connectionString, user, password);
            return connection;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public synchronized Connection getConnection() {
        if (connection == null) {
            connection = createConnection();
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
    
    public void query(String query) {
        query(query, null);
    }
    
    public void query(String query, Object... parameters) {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (parameters != null) {
                int parametersExpected = statement.getParameterMetaData().getParameterCount();
                if (parameters.length != parametersExpected) {
                    throw new SQLException("Numeros de parametros invalidos. Esperado " + parametersExpected + " mas recebido apenas " + parameters.length + ".");
                }
                
                for (int i = 0; i < parameters.length; i++) {
                    statement.setObject(i + 1, parameters[i]);
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
