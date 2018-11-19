package com.dev.mcb.resource;

import com.codahale.metrics.annotation.Timed;
import com.dev.mcb.Customer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

    public CustomerResource() {
    }

    @GET
    public Customer getCustomer(){
        return new Customer("Thibault","testtest");
    }
}
