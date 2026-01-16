package me.sativus.testplugin.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import me.sativus.testplugin.DAO.Wallet;
import me.sativus.testplugin.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import me.sativus.testplugin.DAO.User;

public class UserRepository extends BaseRepository<User> {
    public UserRepository() {
        super(User.class);
    }

    public User findByUUID(UUID uuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);

            cr.select(root).where(cb.equal(root.get("uuid"), uuid.toString()));

            List<User> results = session.createQuery(cr).getResultList();
            return results.isEmpty() ? null : results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<User> findByUUIDOptional(UUID uuid) {
        return Optional.ofNullable(findByUUID(uuid));
    }

    // Fetch user with comma separated entity list
    public User findByUUIDWithFetch(UUID uuid, String commaseparatedFetch) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);

            for (String fetch : commaseparatedFetch.split(",")) {
                root.fetch(fetch);
            }

            cr.select(root).where(cb.equal(root.get("uuid"), uuid.toString()));
            List<User> results = session.createQuery(cr).getResultList();
            return results.isEmpty() ? null : results.getFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public boolean existsByUUID(UUID uuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.uuid = :uuid", Long.class);
            query.setParameter("uuid", uuid.toString());
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getOrCreate(UUID uuid, String playerName) {
        User user = findByUUID(uuid);
        if (user == null) {
            Wallet wallet = new Wallet();

            user = new User(uuid, playerName, wallet);
            wallet.setUser(user);
            save(user);
        }
        return user;
    }
}