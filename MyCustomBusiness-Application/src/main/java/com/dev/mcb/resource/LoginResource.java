package com.dev.mcb.resource;

import com.dev.mcb.UserCredentials;
import com.dev.mcb.core.UserConfigEntity;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.util.HashedPasswordUtil;
import com.dev.mcb.util.JJWTUtil;
import com.dev.mcb.util.UserConfigUtil;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserConfigUtil userConfigUtil;

    @Inject
    private HashedPasswordUtil hashedPasswordUtil;


    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public Response login(UserCredentials credentials) {
        UserEntity userEntity = Optional.ofNullable(userDAO.findByEmail(credentials.getUsername()))
                .orElseThrow(NotFoundException::new);

        String token;
        try {
            if (!checkUserPassword(userEntity, credentials.getPassword())) {
                LOGGER.warn("Wrong password for user with id={}", userEntity.getId());
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }

            token = generateToken(userEntity);
        } catch (IllegalStateException e){
            throw new InternalServerErrorException(e);
        }

        return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    @POST
    @Path("/logout")
    public Response logout(){
        LOGGER.warn("Logout function no developed at this time");
        return Response.ok().build();
    }

    /**
     * Check the user password
     * @param userEntity a user
     * @param password the password to check
     * @return true if equals else false
     */
    private boolean checkUserPassword(UserEntity userEntity, String password) throws IllegalStateException {
        return userConfigUtil.getSalt(userEntity)
                .map(UserConfigEntity::getValue)
                .map(salt -> hashedPasswordUtil.passwordEqualToHash(password, userEntity.getPassword(), salt))
                .orElseThrow(() -> {
                    LOGGER.error("Salt do not exist for user with id={}", userEntity.getId());
                    return new IllegalStateException();
                });
    }

    /**
     * Generate a jwt token for a user
     * @param userEntity a user
     * @return the token
     */
    private String generateToken(UserEntity userEntity) throws IllegalStateException {
        return userConfigUtil.getPrivateKey(userEntity)
                .map(UserConfigEntity::getValue)
                .map(privateKey -> JJWTUtil.issueToken(userEntity.getUsername(), privateKey))
                .orElseThrow(() -> {
                    LOGGER.error("Private key do not exist for user with id={}", userEntity.getId());
                    return new IllegalStateException();
                });
    }
}
