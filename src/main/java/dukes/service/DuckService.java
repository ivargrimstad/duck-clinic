package dukes.service;

import dukes.model.Duck;
import dukes.model.DuckType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DuckService {

    @Inject
    private EntityManagerFactoryProducer emfp;

    public List<Duck> findAll() {
        EntityManager em = emfp.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM Duck d JOIN FETCH d.owner JOIN FETCH d.type ORDER BY d.name",
                    Duck.class).getResultList();
        } finally { em.close(); }
    }

    public List<Duck> findByOwner(Long ownerId) {
        EntityManager em = emfp.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT d FROM Duck d JOIN FETCH d.owner JOIN FETCH d.type WHERE d.owner.id = :ownerId ORDER BY d.name",
                    Duck.class)
                     .setParameter("ownerId", ownerId).getResultList();
        } finally { em.close(); }
    }

    public Optional<Duck> findById(Long id) {
        EntityManager em = emfp.createEntityManager();
        try {
            // JOIN FETCH owner and type so they're accessible after EM close
            List<Duck> results = em.createQuery(
                    "SELECT d FROM Duck d JOIN FETCH d.owner JOIN FETCH d.type WHERE d.id = :id",
                    Duck.class).setParameter("id", id).getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } finally { em.close(); }
    }

    public List<DuckType> findAllTypes() {
        EntityManager em = emfp.createEntityManager();
        try {
            return em.createQuery("SELECT t FROM DuckType t ORDER BY t.name", DuckType.class).getResultList();
        } finally { em.close(); }
    }

    public Duck save(Duck duck) {
        EntityManager em = emfp.createEntityManager();
        try {
            em.getTransaction().begin();
            Duck result = (duck.getId() == null) ? persist(em, duck) : em.merge(duck);
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
            Duck d = em.find(Duck.class, id);
            if (d != null) em.remove(d);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    private Duck persist(EntityManager em, Duck duck) {
        em.persist(duck);
        return duck;
    }
}
