package dukes.startup;

import dukes.model.*;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;

/**
 * Loads sample data on first startup (mirrors the Spring Pet Clinic sample dataset).
 * Runs only when the database is empty.
 */
@Singleton
@Startup
public class DataInitializer {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {
        // Guard – skip if data already exists
        Long count = em.createQuery("SELECT COUNT(o) FROM Owner o", Long.class).getSingleResult();
        if (count > 0) return;

        // --- Pet Types ---
        PetType cat    = persist(new PetType("cat"));
        PetType dog    = persist(new PetType("dog"));
        PetType lizard = persist(new PetType("lizard"));
        PetType snake  = persist(new PetType("snake"));
        PetType bird   = persist(new PetType("bird"));
        PetType hamster = persist(new PetType("hamster"));

        // --- Specialties ---
        Specialty radiology = persist(new Specialty("radiology"));
        Specialty surgery   = persist(new Specialty("surgery"));
        Specialty dentistry = persist(new Specialty("dentistry"));

        // --- Vets ---
        Vet james = new Vet(); james.setFirstName("James"); james.setLastName("Carter");
        em.persist(james);

        Vet helen = new Vet(); helen.setFirstName("Helen"); helen.setLastName("Leary");
        helen.getSpecialties().add(radiology);
        em.persist(helen);

        Vet linda = new Vet(); linda.setFirstName("Linda"); linda.setLastName("Douglas");
        linda.getSpecialties().add(surgery); linda.getSpecialties().add(dentistry);
        em.persist(linda);

        Vet rafael = new Vet(); rafael.setFirstName("Rafael"); rafael.setLastName("Ortega");
        rafael.getSpecialties().add(surgery);
        em.persist(rafael);

        Vet henry = new Vet(); henry.setFirstName("Henry"); henry.setLastName("Stevens");
        henry.getSpecialties().add(radiology);
        em.persist(henry);

        Vet sharon = new Vet(); sharon.setFirstName("Sharon"); sharon.setLastName("Jenkins");
        em.persist(sharon);

        // --- Owners & Pets ---
        Owner george = owner("George", "Franklin", "110 W. Liberty St.", "Madison", "6085551023");
        Pet leo = pet("Leo", LocalDate.of(2010, 9, 7), cat, george);
        em.persist(george);

        Owner betty = owner("Betty", "Davis", "638 Cardinal Ave.", "Sun Prairie", "6085551749");
        Pet basil = pet("Basil", LocalDate.of(2012, 8, 6), hamster, betty);
        em.persist(betty);

        Owner eduardo = owner("Eduardo", "Rodriquez", "2693 Commerce St.", "McFarland", "6085558763");
        Pet rosy  = pet("Rosy",  LocalDate.of(2011, 4, 17), dog, eduardo);
        Pet jewel = pet("Jewel", LocalDate.of(2010, 3, 7),  dog, eduardo);
        em.persist(eduardo);

        Owner harold = owner("Harold", "Davis", "563 Friendly St.", "Windsor", "6085553198");
        Pet iggy = pet("Iggy", LocalDate.of(2010, 11, 30), lizard, harold);
        em.persist(harold);

        Owner peter = owner("Peter", "McTavish", "2387 S. Fair Way", "Madison", "6085552765");
        Pet george2 = pet("George", LocalDate.of(2010, 1, 20), snake, peter);
        em.persist(peter);

        Owner jean = owner("Jean", "Coleman", "105 N. Lake St.", "Monona", "6085552654");
        Pet samantha = pet("Samantha", LocalDate.of(2012, 9, 4), cat, jean);
        Pet max      = pet("Max",      LocalDate.of(2012, 9, 4), cat, jean);
        em.persist(jean);

        Owner jeff = owner("Jeff", "Black", "1450 Oak Blvd.", "Monona", "6085555387");
        Pet lucky = pet("Lucky", LocalDate.of(2011, 8, 6), bird, jeff);
        em.persist(jeff);

        Owner maria = owner("Maria", "Escobito", "345 Maple St.", "Madison", "6085557683");
        Pet mulligan = pet("Mulligan", LocalDate.of(2007, 2, 24), dog, maria);
        em.persist(maria);

        Owner david = owner("David", "Schroeder", "2749 Blackhawk Trail", "Madison", "6085559435");
        Pet freddy = pet("Freddy", LocalDate.of(2010, 3, 9), bird, david);
        em.persist(david);

        Owner carlos = owner("Carlos", "Estaban", "2335 Independence La.", "Waunakee", "6085555487");
        Pet sly = pet("Sly", LocalDate.of(2012, 6, 8), cat, carlos);
        em.persist(carlos);

        // --- Sample Visits ---
        em.flush(); // ensure IDs are assigned

        addVisit(leo,      LocalDate.of(2023, 1, 1), "Rabies vaccination");
        addVisit(leo,      LocalDate.of(2023, 3, 4), "Annual check-up");
        addVisit(basil,    LocalDate.of(2023, 1, 8), "Annual check-up");
        addVisit(rosy,     LocalDate.of(2023, 2, 15), "Spayed");
        addVisit(jewel,    LocalDate.of(2023, 3, 4), "Dental cleaning");
        addVisit(iggy,     LocalDate.of(2023, 4, 12), "Tail fracture");
        addVisit(samantha, LocalDate.of(2023, 5, 1), "Rabies vaccination");
        addVisit(max,      LocalDate.of(2023, 5, 1), "Neutered");
    }

    private <T> T persist(T entity) {
        em.persist(entity);
        return entity;
    }

    private Owner owner(String first, String last, String address, String city, String tel) {
        Owner o = new Owner();
        o.setFirstName(first); o.setLastName(last);
        o.setAddress(address); o.setCity(city); o.setTelephone(tel);
        return o;
    }

    private Pet pet(String name, LocalDate birthDate, PetType type, Owner owner) {
        Pet p = new Pet();
        p.setName(name); p.setBirthDate(birthDate);
        p.setType(type); p.setOwner(owner);
        owner.getPets().add(p);
        return p;
    }

    private void addVisit(Pet pet, LocalDate date, String description) {
        Visit v = new Visit();
        v.setPet(pet); v.setVisitDate(date); v.setDescription(description);
        em.persist(v);
        pet.getVisits().add(v);
    }
}
