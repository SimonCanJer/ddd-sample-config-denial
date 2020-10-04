package com.denial.back.jersey;
import com.back.api.IDomain;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Class is a Jersey controller. It exposes the
 * @see #handle(int) method which just delegates
 * Attention!
 *
 */
@Path("denial")
public class JerseyController {



    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("process")
    public Response handle(@FormParam("clientId") int id) {
        try {
            int status = IDomain.LAZY.get().process(id) ? 200 : 503;
            return Response.status(status).build();
        }
        catch(Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal exception").build();
        }
    }
}
