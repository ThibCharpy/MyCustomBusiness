package com.dev.mcb.resource;

import com.dev.mcb.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    public UserResource() {
    }

    @GET
    public User getCustomer(){
        return new User("Thibault","testtest");
    }
}
