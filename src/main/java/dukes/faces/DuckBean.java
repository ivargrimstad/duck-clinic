package dukes.faces;

import dukes.model.Duck;
import dukes.model.DuckType;
import dukes.model.Owner;
import dukes.service.DuckService;
import dukes.service.OwnerService;
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
public class DuckBean implements Serializable {

    @Inject
    private DuckService duckService;

    @Inject
    private OwnerService ownerService;

    private List<Duck> ducks;
    private List<DuckType> duckTypes;
    private Duck duck = new Duck();
    private Long selectedOwnerId;
    // ID-based bindings for the select menus (avoids needing FacesConverter)
    private Long selectedTypeId;
    private Long selectedDuckOwnerId;

    @PostConstruct
    public void init() {
        duckTypes = duckService.findAllTypes();
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String ownerIdParam = params.get("ownerId");
        if (ownerIdParam != null) {
            selectedOwnerId = Long.parseLong(ownerIdParam);
            ducks = duckService.findByOwner(selectedOwnerId);
            ownerService.findById(selectedOwnerId).ifPresent(o -> duck.setOwner(o));
        } else {
            ducks = duckService.findAll();
        }
    }

    public void save() {
        // Resolve type from selectedTypeId
        if (selectedTypeId != null) {
            duckTypes.stream().filter(t -> t.getId().equals(selectedTypeId))
                    .findFirst().ifPresent(duck::setType);
        }
        // Resolve owner: use form-bound owner ID, or the page-level selectedOwnerId
        if (selectedDuckOwnerId != null) {
            ownerService.findById(selectedDuckOwnerId).ifPresent(duck::setOwner);
        } else if (duck.getOwner() == null && selectedOwnerId != null) {
            ownerService.findById(selectedOwnerId).ifPresent(duck::setOwner);
        }
        duckService.save(duck);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Duck saved!", null));
        resetForm();
        ducks = (selectedOwnerId != null) ? duckService.findByOwner(selectedOwnerId) : duckService.findAll();
    }

    public void delete(Long id) {
        duckService.delete(id);
        ducks = (selectedOwnerId != null) ? duckService.findByOwner(selectedOwnerId) : duckService.findAll();
    }

    public void prepareEdit(Long id) {
        duck = duckService.findById(id).orElse(new Duck());
        // Sync the select menu IDs with the current duck's associations
        if (duck.getType() != null) selectedTypeId = duck.getType().getId();
        if (duck.getOwner() != null) selectedDuckOwnerId = duck.getOwner().getId();
    }

    public void resetForm() {
        duck = new Duck();
        selectedTypeId = null;
        selectedDuckOwnerId = null;
        if (selectedOwnerId != null) {
            ownerService.findById(selectedOwnerId).ifPresent(o -> duck.setOwner(o));
        }
    }

    public List<Owner> getAllOwners() { return ownerService.findAll(); }
    public List<Duck> getDucks() { return ducks; }
    public List<DuckType> getDuckTypes() { return duckTypes; }
    public Duck getDuck() { return duck; }
    public void setDuck(Duck duck) { this.duck = duck; }
    public Long getSelectedOwnerId() { return selectedOwnerId; }
    public void setSelectedOwnerId(Long selectedOwnerId) { this.selectedOwnerId = selectedOwnerId; }
    public Long getSelectedTypeId() { return selectedTypeId; }
    public void setSelectedTypeId(Long selectedTypeId) { this.selectedTypeId = selectedTypeId; }
    public Long getSelectedDuckOwnerId() { return selectedDuckOwnerId; }
    public void setSelectedDuckOwnerId(Long selectedDuckOwnerId) { this.selectedDuckOwnerId = selectedDuckOwnerId; }
}
