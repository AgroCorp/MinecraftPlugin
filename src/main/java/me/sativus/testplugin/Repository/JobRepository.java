package me.sativus.testplugin.Repository;

import static me.sativus.testplugin.utils.HibernateUtil.executeTransaction;

import java.util.List;

import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import me.sativus.testplugin.DAO.Job;
import me.sativus.testplugin.utils.HibernateUtil;

public class JobRepository extends BaseRepository<Job> {
    public JobRepository() {
        super(Job.class);
    }

    public Job findByName(String name) {
        return executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Job> cq = cb.createQuery(Job.class);
                Root<Job> root = cq.from(Job.class);
                cq.where(cb.equal(root.get("name"), name));

                return session.createQuery(cq).getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
        });
    }

    public Job getOrCreate(Job job) {
        Job existsJob = findByName(job.getName());
        if (existsJob == null) {
            return save(job);
        } else {
            return existsJob;
        }
    }

    public List<Job> getAllJob() {
        return executeTransaction(HibernateUtil.getSessionFactory(), session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Job> cq = cb.createQuery(Job.class);
            Root<Job> jobRoot = cq.from(Job.class);
            cq.select(jobRoot);

            return session.createQuery(cq).getResultList();
        });
    }
}
