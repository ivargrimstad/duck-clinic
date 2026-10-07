package dukes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "specialties")
@SequenceGenerator(name = "specialtySeq", sequenceName = "specialty_seq", allocationSize = 1)
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "specialtySeq")
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(name = "name", nullable = false, length = 80)
    private String name;

    public Specialty() {}

    public Specialty(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
