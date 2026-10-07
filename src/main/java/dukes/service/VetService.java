package dukes.service;

import dukes.model.Vet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VetService {

    @Inject
    private EntityManagerFactoryProducer emfp;

    public List<Vet> findAll() {
        EntityManager em = emfp.createEntityManager();
        try {
            return em.createQuery("SELECT v FROM Vet v ORDER BY v.lastName, v.firstName", Vet.class).getResultList();
        } finally { em.close(); }
    }

    public Optional<Vet> findById(Long id) {
        EntityManager em = emfp.createEntityManager();
        try {
            return Optional.ofNullable(em.find(Vet.class, id));
        } finally { em.close(); }
    }

    public Vet save(Vet vet) {
        EntityManager em = emfp.createEntityManager();
        try {
            em.getTransaction().begin();
            Vet result = (vet.getId() == null) ? persist(em, vet) : em.merge(vet);
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
            Vet v = em.find(Vet.class, id);
            if (v != null) em.remove(v);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally { em.close(); }
    }

    private Vet persist(EntityManager em, Vet vet) {
        em.persist(vet);
        return vet;
    }
}
