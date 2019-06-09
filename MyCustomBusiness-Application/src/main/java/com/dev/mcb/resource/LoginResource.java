package com.dev.mcb.resource;

import com.dev.mcb.UserCredentials;
import com.dev.mcb.core.UserConfigEntity;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.core.enums.UserConfigType;
import com.dev.mcb.dao.UserDAO;
import com.dev.mcb.util.HashedPasswordUtil;
import com.dev.mcb.util.auth.JJWTUtil;
import com.dev.mcb.util.UserConfigUtil;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

    private static final String GENERATION_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;

    private static final String COOKIE_COMMENT = "";
    private static final Integer COOKIE_MAX_AGE = 3600;
    private static final boolean COOKIE_HTTPONLY = true;
    private static final boolean COOKIE_SECURE = true;

    @Inject
    private UserDAO userDAO;

    @Inject
    private UserConfigUtil userConfigUtil;

    @Inject
    private HashedPasswordUtil hashedPasswordUtil;

    @Context
    UriInfo uriInfo;


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

        NewCookie cookie = generateNewCookie(userEntity.getId(), token);

        return Response.ok().cookie(cookie).build();
    }

    @GET
    @Path("/logout")
    public Response logout(@CookieParam("") Cookie cookie){
        LOGGER.warn("Logout function no developed at this time");
        return Response.ok().build();
    }

    /**
     * Check the user password
     * @param userEntity a user
     * @param password the password to check
     * @return true if equals else false
     */
    private boolean checkUserPassword(UserEntity userEntity, String password) {
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
    private String generateToken(UserEntity userEntity) {
        KeyPair kp;
        try {
            kp = generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Generation algorithm used do not exist");
            throw new IllegalStateException();
        }

        saveKeyForUser(userEntity, kp.getPublic().toString());

        return JJWTUtil.issueToken(userEntity.getUsername(), kp.getPrivate().toString());
    }

    /**
     * Generate an Key Pair using RSA algorithm
     * @return the RSA {@link KeyPair}
     * @throws NoSuchAlgorithmException Algorithm not found
     */
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(GENERATION_ALGORITHM);
        kpg.initialize(KEY_SIZE);

        return kpg.generateKeyPair();
    }

    /**
     * Save public part of the key for a user
     * @param userEntity the user
     * @param key the public part of the key
     */
    private void saveKeyForUser(UserEntity userEntity, String key) {
        userEntity.getConfigurations().add(new UserConfigEntity(userEntity, UserConfigType.KEY.toString(), key));
    }

    private NewCookie generateNewCookie(Long userID, String token) {
        return new NewCookie(userID.toString(),
                token,
                uriInfo.getPath(),
                uriInfo.getBaseUri().toString(),
                COOKIE_COMMENT,
                COOKIE_MAX_AGE,
                COOKIE_SECURE,
                COOKIE_HTTPONLY);
    }
}
