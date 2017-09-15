package com.vlkan.hrrs.example.jaxrs;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class HelloResource {

    @POST
    @Produces({MediaType.TEXT_PLAIN})
    @Consumes({MediaType.TEXT_PLAIN})
    public Response sayHello(@QueryParam("name") String name, byte[] requestBody) {
        String responseBody = String.format("Hello, %s! (%d)%n", name, requestBody.length);
        return Response.ok(responseBody).build();
    }

}
