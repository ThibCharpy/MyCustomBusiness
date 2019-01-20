package com.dev.mcb.resource;

import com.dev.mcb.User;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.mapper.UserMapper;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private Jdbi database;

    @Inject
    private UserMapper userMapper;

    private static final String ERROR_404 = "User not found";
    private static final String ERROR_500 = "Internal error in user resource";

    public UserResource(Jdbi jdbi) {
        this.database = jdbi;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        try {
            List<User> result = getUserDao().findAll().stream().map(userMapper::from).collect(Collectors.toList());
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
            Optional<UserEntity> userEntity = getUserDao().findById(userId);
            Optional<User> result = userEntity.map(entity -> userMapper.from(entity));
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
            UserEntity newUserEntity = userMapper.map(newUser);
            User result = userMapper.from(getUserDao().create(newUserEntity));
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
            getUserDao().delete(userId);
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
            UserEntity updateUserEntity = userMapper.map(updatedUser);
            updateUserEntity.setId(userId);
            User result = userMapper.from(getUserDao().update(updateUserEntity));
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    private UserDAO getUserDao(){
        return database.onDemand(UserDAO.class);
    }
}
