package dukes.service;

import dukes.model.Visit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VisitService {

    @PersistenceContext
    private EntityManager em;

    public List<Visit> findByPet(Long petId) {
        return em.createQuery(
                "SELECT v FROM Visit v WHERE v.pet.id = :petId ORDER BY v.visitDate DESC",
                Visit.class)
                 .setParameter("petId", petId)
                 .getResultList();
    }

    public Optional<Visit> findById(Long id) {
        return Optional.ofNullable(em.find(Visit.class, id));
    }

    @Transactional
    public Visit save(Visit visit) {
        if (visit.getId() == null) {
            em.persist(visit);
            return visit;
        }
        return em.merge(visit);
    }

    @Transactional
    public void delete(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
