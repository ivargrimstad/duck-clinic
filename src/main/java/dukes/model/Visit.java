package dukes.model;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "visits")
@SequenceGenerator(name = "visitSeq", sequenceName = "visit_seq", allocationSize = 1)
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visitSeq")
    private Long id;

    @NotNull
    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @NotBlank
    @Size(max = 1024)
    @Column(name = "description", nullable = false, length = 1024)
    private String description;

    @JsonbTransient
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "duck_id", nullable = false)
    private Duck duck;

    public Visit() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate visitDate) { this.visitDate = visitDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Duck getDuck() { return duck; }
    public void setDuck(Duck duck) { this.duck = duck; }
}
