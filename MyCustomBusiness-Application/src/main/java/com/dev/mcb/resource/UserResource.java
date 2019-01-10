package com.dev.mcb.resource;

import com.dev.mcb.User;
import com.dev.mcb.util.service.UserService;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserService userService;

    private static final String ERROR_404 = "User not found";
    private static final String ERROR_500 = "Internal error in user resource";

    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            List<User> result = userService.findAll();
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserById(@PathParam("id") Long userId) {
        try {
            Optional<User> result = userService.findUserById(userId);
            if (result.isPresent())
                return Response.ok(result.get()).build();
            else
                return Response.status(404).entity(ERROR_404).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @POST
    @Path("/new")
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(User newUser) {
        try {
            User result = userService.createUser(newUser);
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @DELETE
    @Path("/{id}/delete")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") Long userId){
        try {
            userService.deleteUser(userId);
            return Response.ok().build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @PUT
    @Path("/{id}/update")
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long userId, User updatedUser) {
        try {
            User result = userService.updateUser(userId,updatedUser);
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }
}
