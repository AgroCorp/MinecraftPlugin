package me.sativus.testplugin.Repository;

import me.sativus.testplugin.utils.HibernateUtil;

import static me.sativus.testplugin.utils.HibernateUtil.executeTransaction;

public class BaseRepository {
    public <T> void save(T model) {
        executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            session.merge(model);
            return null;
        });
    }

    public <T> void delete(T model) {
        executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            session.remove(model);
            return null;
        });
    }
}
