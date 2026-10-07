package dukes.rest;

import dukes.model.Vet;
import dukes.service.VetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/vets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VetResource {

    @Inject
    private VetService vetService;

    @GET
    public List<Vet> list() {
        return vetService.findAll();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return vetService.findById(id)
                         .map(v -> Response.ok(v).build())
                         .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid Vet vet) {
        Vet saved = vetService.save(vet);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Vet vet) {
        return vetService.findById(id).map(existing -> {
            vet.setId(id);
            return Response.ok(vetService.save(vet)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        vetService.delete(id);
        return Response.noContent().build();
    }
}
