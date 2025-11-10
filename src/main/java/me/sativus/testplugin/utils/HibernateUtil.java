package me.sativus.testplugin.utils;

import me.sativus.testplugin.DAO.User;
import me.sativus.testplugin.DAO.Wallet;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static void initialize(String host, int port, String database,
                                  String username, String password) {
        try {
            Configuration configuration = new Configuration();

            // MariaDB connection settings
            configuration.setProperty("hibernate.connection.driver_class",
                    "org.mariadb.jdbc.Driver");
            configuration.setProperty("hibernate.connection.url",
                    "jdbc:mariadb://" + host + ":" + port + "/" + database);
            configuration.setProperty("hibernate.connection.username", username);
            configuration.setProperty("hibernate.connection.password", password);

            // Hibernate settings - MariaDB dialect
            configuration.setProperty("hibernate.dialect",
                    "org.hibernate.dialect.MariaDBDialect");
            configuration.setProperty("hibernate.hbm2ddl.auto", "update");
            configuration.setProperty("hibernate.show_sql", "false");

            // Connection pool settings
            configuration.setProperty("hibernate.connection.pool_size", "10");
            configuration.setProperty("hibernate.current_session_context_class",
                    "thread");

            // Optional: Additional MariaDB-specific settings
            configuration.setProperty("hibernate.connection.characterEncoding",
                    "utf8mb4");
            configuration.setProperty("hibernate.connection.useUnicode", "true");

            // Register your entity classes here
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Wallet.class);

            sessionFactory = configuration.buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}