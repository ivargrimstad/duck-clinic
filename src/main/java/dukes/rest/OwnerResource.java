package dukes.rest;

import dukes.model.Owner;
import dukes.service.OwnerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OwnerResource {

    @Inject
    private OwnerService ownerService;

    @GET
    public List<Owner> list(@QueryParam("lastName") String lastName) {
        if (lastName != null && !lastName.isBlank()) {
            return ownerService.findByLastName(lastName);
        }
        return ownerService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return ownerService.findById(id)
                           .map(o -> Response.ok(o).build())
                           .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid Owner owner) {
        Owner saved = ownerService.save(owner);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Owner owner) {
        return ownerService.findById(id).map(existing -> {
            owner.setId(id);
            return Response.ok(ownerService.save(owner)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        ownerService.delete(id);
        return Response.noContent().build();
    }
}
