package dukes.rest;

import dukes.model.Duck;
import dukes.model.DuckType;
import dukes.service.OwnerService;
import dukes.service.DuckService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/ducks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DuckResource {

    @Inject
    private DuckService duckService;

    @Inject
    private OwnerService ownerService;

    @GET
    public List<Duck> list(@QueryParam("ownerId") Long ownerId) {
        if (ownerId != null) {
            return duckService.findByOwner(ownerId);
        }
        return duckService.findAll();
    }

    @GET
    @Path("/types")
    public List<DuckType> types() {
        return duckService.findAllTypes();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return duckService.findById(id)
                          .map(d -> Response.ok(d).build())
                          .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid Duck duck) {
        Duck saved = duckService.save(duck);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Duck duck) {
        return duckService.findById(id).map(existing -> {
            duck.setId(id);
            return Response.ok(duckService.save(duck)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        duckService.delete(id);
        return Response.noContent().build();
    }
}
