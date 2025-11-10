package me.sativus.testplugin.Repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import me.sativus.testplugin.DAO.Wallet;
import me.sativus.testplugin.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import me.sativus.testplugin.DAO.User;

public class UserRepository {

    public void save(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
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

    public User findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(User.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Fetch user with wallets eagerly loaded
    public User findByUUIDWithWallet(UUID uuid) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            root.fetch("wallet");

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

    public void delete(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
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