package com.dev.mcb.resource;


import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    @POST
    public void login(){

    }

    @POST
    public void logout(){

    }
}
