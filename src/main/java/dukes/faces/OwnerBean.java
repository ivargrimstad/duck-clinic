package dukes.faces;

import dukes.model.Owner;
import dukes.service.OwnerService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class OwnerBean implements Serializable {

    @Inject
    private OwnerService ownerService;

    private List<Owner> owners;
    private Owner owner = new Owner();
    private String searchLastName = "";

    @PostConstruct
    public void init() {
        owners = ownerService.findAll();
    }

    public void search() {
        owners = ownerService.findByLastName(searchLastName);
    }

    public void save() {
        ownerService.save(owner);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Owner saved!", null));
        owner = new Owner();
        owners = ownerService.findAll();
    }

    public void delete(Long id) {
        ownerService.delete(id);
        owners = ownerService.findAll();
    }

    public void prepareEdit(Long id) {
        owner = ownerService.findById(id).orElse(new Owner());
    }

    public void resetForm() {
        owner = new Owner();
    }

    public List<Owner> getOwners() { return owners; }
    public Owner getOwner() { return owner; }
    public void setOwner(Owner owner) { this.owner = owner; }
    public String getSearchLastName() { return searchLastName; }
    public void setSearchLastName(String searchLastName) { this.searchLastName = searchLastName; }
}
