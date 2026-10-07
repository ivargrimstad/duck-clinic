package dukes.faces;

import dukes.model.Vet;
import dukes.service.VetService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class VetBean implements Serializable {

    @Inject
    private VetService vetService;

    private List<Vet> vets;

    @PostConstruct
    public void init() {
        vets = vetService.findAll();
    }

    public List<Vet> getVets() { return vets; }
}
