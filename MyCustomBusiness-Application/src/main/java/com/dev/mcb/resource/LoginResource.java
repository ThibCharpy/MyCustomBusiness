package com.dev.mcb.resource;

import com.dev.mcb.core.UserConfigEntity;
import com.dev.mcb.core.UserEntity;
import com.dev.mcb.core.enums.UserConfigType;
import com.dev.mcb.util.JJWTUtil;
import io.dropwizard.auth.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

    @POST
    @PermitAll
    @Path("/login")
    public Response login(@Auth UserEntity userEntity){
        List<UserConfigEntity> config = userEntity.getConfigurations().stream()
                .filter(userConfigEntity -> userConfigEntity.getUser().getId().equals(userEntity.getId()))
                .filter(userConfigEntity -> UserConfigType.PRIVATE_KEY.toString().equals(userConfigEntity.getKey()))
                .collect(Collectors.toList());

        if (config.size() > 1) {
            LOGGER.error("Retrieved multiple configurations values");
            throw new IllegalStateException("Multiple configuration values retrieved");
        }

        final String privateKey = config.get(0).getValue();

        final String token = JJWTUtil.issueToken(userEntity.getUsername(), privateKey);

        return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    @POST
    @Path("/logout")
    public Response logout(){
        LOGGER.warn("Logout function no developed at this time");
        return Response.ok().build();
    }
}
