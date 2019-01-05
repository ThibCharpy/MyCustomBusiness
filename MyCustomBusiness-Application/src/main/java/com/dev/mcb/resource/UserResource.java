package com.dev.mcb.resource;

import com.dev.mcb.User;
import com.dev.mcb.dao.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    public UserResource() { }

    @GET
    @UnitOfWork
    @Produces("application/json")
    public Response findAll() {
        //TODO: to be implemented
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    @Produces("application/json")
    public Response findUserById(@PathParam("id") Long userId) {
        //TODO: to be implemented
        return Response.ok().build();
    }

    @POST
    @Path("/new")
    @UnitOfWork
    @Consumes("application/json")
    @Produces("application/json")
    public Response createUser(User newUser) {
        //TODO: to be implemented
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}/update")
    @UnitOfWork
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateUser(@PathParam("id") Long userId, User updatedUser) {
        //TODO: to be implemented
        return Response.ok().build();
    }
}
