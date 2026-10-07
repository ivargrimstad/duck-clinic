package dukes.service;

import dukes.model.Vet;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VetService {

    @PersistenceContext
    private EntityManager em;

    public List<Vet> findAll() {
        return em.createQuery("SELECT v FROM Vet v ORDER BY v.lastName, v.firstName", Vet.class)
                 .getResultList();
    }

    public Optional<Vet> findById(Long id) {
        return Optional.ofNullable(em.find(Vet.class, id));
    }

    @Transactional
    public Vet save(Vet vet) {
        if (vet.getId() == null) {
            em.persist(vet);
            return vet;
        }
        return em.merge(vet);
    }

    @Transactional
    public void delete(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
