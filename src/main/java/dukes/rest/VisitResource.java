package dukes.rest;

import dukes.model.Visit;
import dukes.service.DuckService;
import dukes.service.VisitService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/visits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VisitResource {

    @Inject
    private VisitService visitService;

    @Inject
    private DuckService duckService;

    @GET
    public List<Visit> list(@QueryParam("duckId") Long duckId) {
        if (duckId != null) {
            return visitService.findByDuck(duckId);
        }
        return List.of();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return visitService.findById(id)
                           .map(v -> Response.ok(v).build())
                           .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid Visit visit) {
        Visit saved = visitService.save(visit);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Visit visit) {
        return visitService.findById(id).map(existing -> {
            visit.setId(id);
            return Response.ok(visitService.save(visit)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        visitService.delete(id);
        return Response.noContent().build();
    }
}
