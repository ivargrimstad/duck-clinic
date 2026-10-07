package dukes.faces;

import dukes.model.Duck;
import dukes.model.Visit;
import dukes.service.DuckService;
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
    private DuckService duckService;

    private List<Visit> visits;
    private Visit visit = new Visit();
    private Duck duck;

    @PostConstruct
    public void init() {
        visit.setVisitDate(LocalDate.now());
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestParameterMap();
        String duckIdParam = params.get("duckId");
        if (duckIdParam != null) {
            Long duckId = Long.parseLong(duckIdParam);
            duck = duckService.findById(duckId).orElse(null);
            visits = visitService.findByDuck(duckId);
            if (duck != null) visit.setDuck(duck);
        }
    }

    public void save() {
        visitService.save(visit);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Visit saved!", null));
        visit = new Visit();
        visit.setVisitDate(LocalDate.now());
        if (duck != null) {
            visit.setDuck(duck);
            visits = visitService.findByDuck(duck.getId());
        }
    }

    public void delete(Long id) {
        visitService.delete(id);
        if (duck != null) visits = visitService.findByDuck(duck.getId());
    }

    public List<Visit> getVisits() { return visits; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Duck getDuck() { return duck; }
}
