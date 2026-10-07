package dukes.service;

import dukes.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VisitService {

    @Inject
    private EntityManagerFactoryProducer emfp;

    public List<Visit> findByDuck(Long duckId) {
        EntityManager em = emfp.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT v FROM Visit v WHERE v.duck.id = :duckId ORDER BY v.visitDate DESC",
                    Visit.class)
                     .setParameter("duckId", duckId).getResultList();
        } finally { em.close(); }
    }

    public Optional<Visit> findById(Long id) {
        EntityManager em = emfp.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Visit.class, id));
        } finally { em.close(); }
    }

    public Visit save(Visit visit) {
        EntityManager em = emfp.createEntityManager();
        try {
            em.getTransaction().begin();
            Visit result = (visit.getId() == null) ? persist(em, visit) : em.merge(visit);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    public void delete(Long id) {
        EntityManager em = emfp.createEntityManager();
        try {
            em.getTransaction().begin();
            Visit v = em.find(Visit.class, id);
            if (v != null) em.remove(v);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    private Visit persist(EntityManager em, Visit visit) {
        em.persist(visit);
        return visit;
    }
}
