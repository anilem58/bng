package com.blissandglow.listener;

import com.blissandglow.util.DBConnection;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection c = DBConnection.getConnection()) {
            LOG.info("Bliss and Glow: database connection verified at startup.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Bliss and Glow: could not connect to database at startup.", e);
        }
    }
}
