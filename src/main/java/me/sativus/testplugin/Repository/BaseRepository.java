package me.sativus.testplugin.Repository;

import me.sativus.testplugin.utils.HibernateUtil;

import static me.sativus.testplugin.utils.HibernateUtil.executeTransaction;

public class BaseRepository {
    public <T> T save(T model) {
        return executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            return session.merge(model);
        });
    }

    public <T> void delete(T model) {
        executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            session.remove(model);
            return null;
        });
    }
}
