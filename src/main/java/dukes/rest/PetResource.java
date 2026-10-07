package dukes.rest;

import dukes.model.Pet;
import dukes.model.PetType;
import dukes.service.OwnerService;
import dukes.service.PetService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PetResource {

    @Inject
    private PetService petService;

    @Inject
    private OwnerService ownerService;

    @GET
    public List<Pet> list(@QueryParam("ownerId") Long ownerId) {
        if (ownerId != null) {
            return petService.findByOwner(ownerId);
        }
        return petService.findAll();
    }

    @GET
    @Path("/types")
    public List<PetType> types() {
        return petService.findAllTypes();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") Long id) {
        return petService.findById(id)
                         .map(p -> Response.ok(p).build())
                         .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response create(@Valid Pet pet) {
        Pet saved = petService.save(pet);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, @Valid Pet pet) {
        return petService.findById(id).map(existing -> {
            pet.setId(id);
            return Response.ok(petService.save(pet)).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        petService.delete(id);
        return Response.noContent().build();
    }
}
