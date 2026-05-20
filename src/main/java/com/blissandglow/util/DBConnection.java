package com.blissandglow.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String url;
    private static final String username;
    private static final String password;

    // Load database settings once from db.properties when the class is first used
    static {
        try (InputStream in = DBConnection.class.getResourceAsStream("/db.properties")) {
            if (in == null) {
                throw new ExceptionInInitializerError("db.properties not found on classpath");
            }
            Properties props = new Properties();
            props.load(in);
            Class.forName(props.getProperty("db.driver"));
            url      = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    // Private constructor — no one should create an instance of this class
    private DBConnection() {}

    // Call this anywhere you need a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}