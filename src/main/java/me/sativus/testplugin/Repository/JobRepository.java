package me.sativus.testplugin.Repository;

import static me.sativus.testplugin.utils.HibernateUtil.executeTransaction;

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
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Job> cq = cb.createQuery(Job.class);
            Root<Job> root = cq.from(Job.class);
            cq.where(cb.equal(root.get("name"), name));

            return session.createQuery(cq).getSingleResult();
        });
    }
    

    public Job getOrCreate(String name) {
        Job job = findByName(name);
        if (job == null) {
            job = new Job(name);
            return save(job);
        } else {
            return job;
        }
    }
}
