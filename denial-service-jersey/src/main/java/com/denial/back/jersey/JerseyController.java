package com.denial.back.jersey;
import com.back.api.IDataHolder;
import com.back.api.IDomain;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The Class is a Jersey controller. It exposes the
 * @see #handle(String, String)  method which just delegates
 * See usage of the
 * @see IDomain interface over build in
 * @see IDomain#LAZY
 * I could also inject domain here.
 *
 */
@Path("denial")
public class JerseyController {



    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Path("process")
    public Response handle(@FormParam("clientId") String id,@FormParam("command") String s) {
        try {
            IDataHolder.VarResult res=IDomain.LAZY.get() .process(id,s);
            int status = res.isResult() ? 200 : 503;
            Gson gson=new Gson();
            String jsonRes=gson.toJson(res);
            return Response.status(status).entity(jsonRes).build();
        }
        catch(Exception e)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("internal exception").build();
        }
    }
}
