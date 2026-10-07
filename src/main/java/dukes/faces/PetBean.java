package dukes.faces;

import dukes.model.Owner;
import dukes.model.Pet;
import dukes.model.PetType;
import dukes.service.OwnerService;
import dukes.service.PetService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class PetBean implements Serializable {

    @Inject
    private PetService petService;

    @Inject
    private OwnerService ownerService;

    private List<Pet> pets;
    private List<PetType> petTypes;
    private Pet pet = new Pet();
    private Long selectedOwnerId;

    @PostConstruct
    public void init() {
        petTypes = petService.findAllTypes();
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String ownerIdParam = params.get("ownerId");
        if (ownerIdParam != null) {
            selectedOwnerId = Long.parseLong(ownerIdParam);
            pets = petService.findByOwner(selectedOwnerId);
            ownerService.findById(selectedOwnerId).ifPresent(o -> pet.setOwner(o));
        } else {
            pets = petService.findAll();
        }
    }

    public void save() {
        if (pet.getOwner() == null && selectedOwnerId != null) {
            ownerService.findById(selectedOwnerId).ifPresent(pet::setOwner);
        }
        petService.save(pet);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Pet saved!", null));
        pet = new Pet();
        if (selectedOwnerId != null) {
            pets = petService.findByOwner(selectedOwnerId);
        } else {
            pets = petService.findAll();
        }
    }

    public void delete(Long id) {
        petService.delete(id);
        pets = (selectedOwnerId != null) ? petService.findByOwner(selectedOwnerId) : petService.findAll();
    }

    public void prepareEdit(Long id) {
        pet = petService.findById(id).orElse(new Pet());
    }

    public void resetForm() {
        pet = new Pet();
        if (selectedOwnerId != null) {
            ownerService.findById(selectedOwnerId).ifPresent(o -> pet.setOwner(o));
        }
    }

    public List<Owner> getAllOwners() { return ownerService.findAll(); }
    public List<Pet> getPets() { return pets; }
    public List<PetType> getPetTypes() { return petTypes; }
    public Pet getPet() { return pet; }
    public void setPet(Pet pet) { this.pet = pet; }
    public Long getSelectedOwnerId() { return selectedOwnerId; }
    public void setSelectedOwnerId(Long selectedOwnerId) { this.selectedOwnerId = selectedOwnerId; }
}
