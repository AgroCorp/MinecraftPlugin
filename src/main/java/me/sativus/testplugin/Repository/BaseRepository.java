package me.sativus.testplugin.Repository;

import me.sativus.testplugin.utils.HibernateUtil;

import static me.sativus.testplugin.utils.HibernateUtil.executeTransaction;

public abstract class BaseRepository<T> {
    private final Class<T> clazz;

    public BaseRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T save(T model) {
        return executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            return session.merge(model);
        });
    }

    public void delete(T model) {
        executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            session.remove(model);
            return null;
        });
    }

    public T findById(Long id) {
        return executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            return session.find(this.clazz, id);
        });
    }
}
