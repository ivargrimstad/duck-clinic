package dukes.service;

import dukes.model.Pet;
import dukes.model.PetType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PetService {

    @PersistenceContext
    private EntityManager em;

    public List<Pet> findAll() {
        return em.createQuery("SELECT p FROM Pet p ORDER BY p.name", Pet.class)
                 .getResultList();
    }

    public List<Pet> findByOwner(Long ownerId) {
        return em.createQuery("SELECT p FROM Pet p WHERE p.owner.id = :ownerId ORDER BY p.name", Pet.class)
                 .setParameter("ownerId", ownerId)
                 .getResultList();
    }

    public Optional<Pet> findById(Long id) {
        return Optional.ofNullable(em.find(Pet.class, id));
    }

    public List<PetType> findAllTypes() {
        return em.createQuery("SELECT t FROM PetType t ORDER BY t.name", PetType.class)
                 .getResultList();
    }

    @Transactional
    public Pet save(Pet pet) {
        if (pet.getId() == null) {
            em.persist(pet);
            return pet;
        }
        return em.merge(pet);
    }

    @Transactional
    public void delete(Long id) {
        findById(id).ifPresent(em::remove);
    }
}
