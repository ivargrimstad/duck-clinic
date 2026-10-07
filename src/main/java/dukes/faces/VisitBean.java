package dukes.faces;

import dukes.model.Pet;
import dukes.model.Visit;
import dukes.service.PetService;
import dukes.service.VisitService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class VisitBean implements Serializable {

    @Inject
    private VisitService visitService;

    @Inject
    private PetService petService;

    private List<Visit> visits;
    private Visit visit = new Visit();
    private Pet pet;

    @PostConstruct
    public void init() {
        visit.setVisitDate(LocalDate.now());
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String petIdParam = params.get("petId");
        if (petIdParam != null) {
            Long petId = Long.parseLong(petIdParam);
            pet = petService.findById(petId).orElse(null);
            visits = visitService.findByPet(petId);
            if (pet != null) visit.setPet(pet);
        }
    }

    public void save() {
        visitService.save(visit);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Visit saved!", null));
        visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        if (pet != null) {
            visit.setPet(pet);
            visits = visitService.findByPet(pet.getId());
        }
    }

    public void delete(Long id) {
        visitService.delete(id);
        if (pet != null) visits = visitService.findByPet(pet.getId());
    }

    public List<Visit> getVisits() { return visits; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Pet getPet() { return pet; }
}
