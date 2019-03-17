package com.dev.mcb.resource;

import com.dev.mcb.User;
import com.dev.mcb.core.UserConfigEntity;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.core.enums.UserConfigType;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.mapper.UserMapper;
import com.dev.mcb.util.HashedPasswordUtil;
import io.dropwizard.hibernate.UnitOfWork;
import org.hibernate.HibernateException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserMapper userMapper;

    @Inject
    private HashedPasswordUtil hasher;

    private static final String ERROR_404 = "User not found";
    private static final String ERROR_500 = "Internal error in user resource";

    @GET
    @UnitOfWork
    public Response findAll() {
        try {
            List<User> result = Optional.ofNullable(userDAO.findAll())
                    .orElseGet(Collections::emptyList)
                    .stream().map(UserMapper::from)
                    .collect(Collectors.toList());
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response findUserById(@PathParam("id") Long userId) {
        try {
            User result = Optional.ofNullable(userDAO.findById(userId))
                    .map(UserMapper::from)
                    .orElseThrow(NotFoundException::new);
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @POST
    @Path("/new")
    @UnitOfWork
    public Response createUser(@NotNull User newUser) {
        try {
            UserEntity newUserEntity = Optional.ofNullable(newUser)
                    .map(UserMapper::to)
                    .orElseThrow(() -> new BadRequestException("No user provided for create"));
            final String salt = hasher.generateSalt();
            newUserEntity.setPassword(hasher.getHashedPassword(newUserEntity.getPassword(), salt));
            newUserEntity.getConfigurations().add(new UserConfigEntity(UserConfigType.SALT.toString(),salt));
            User result = Optional.ofNullable(userDAO.create(newUserEntity))
                    .map(UserMapper::from)
                    .orElse(null);
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    public Response updateUser(@PathParam("id") Long userId, @NotNull User updatedUser) {
        try {
            UserEntity updateUserEntity = Optional.ofNullable(updatedUser)
                    .map(UserMapper::to)
                    .orElseThrow(() -> new BadRequestException("No user provided for update"));
            updateUserEntity.setId(userId);
            User result = Optional.ofNullable(this.userDAO.update(updateUserEntity))
                    .map(UserMapper::from)
                    .orElse(null);
            return Response.ok(result).build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public Response deleteUser(@PathParam("id") Long userId){
        try {
            this.userDAO.delete(userId);
            return Response.ok().build();
        } catch (HibernateException he) {
            return Response.status(500).entity(ERROR_500).build();
        }
    }
}
