package dukes.service;

import dukes.model.Owner;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OwnerService {

    @PersistenceContext
    private EntityManager em;

    public List<Owner> findAll() {
        return em.createQuery("SELECT o FROM Owner o ORDER BY o.lastName, o.firstName", Owner.class)
                 .getResultList();
    }

    public List<Owner> findByLastName(String lastName) {
        return em.createQuery(
                "SELECT o FROM Owner o WHERE LOWER(o.lastName) LIKE LOWER(:name) ORDER BY o.lastName",
                Owner.class)
                 .setParameter("name", "%" + lastName + "%")
                 .getResultList();
    }

    public Optional<Owner> findById(Long id) {
        return Optional.ofNullable(em.find(Owner.class, id));
    }

    @Transactional
    public Owner save(Owner owner) {
        if (owner.getId() == null) {
            em.persist(owner);
            return owner;
        }
        return em.merge(owner);
    }

    @Transactional
    public void delete(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
