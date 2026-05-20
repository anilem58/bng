package com.blissandglow.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final String PROPS_FILE = "/db.properties";
    private static final String url;
    private static final String username;
    private static final String password;

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream(PROPS_FILE)) {
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

    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
