package fr.spacefox.verrucktnietzsche.resources;

import fr.spacefox.verrucktnietzsche.io.GoodEvilResponse;
import fr.spacefox.verrucktnietzsche.io.Opinion;
import fr.spacefox.verrucktnietzsche.service.GoodEvilService;
import fr.spacefox.verrucktnietzsche.service.RequestsCheckService;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/verruckt-nietzsche")
public class GoodEvilResource {

    private static final Logger LOGGER = Logger.getLogger(GoodEvilResource.class.getSimpleName());

    @Inject
    GoodEvilService goodEvilService;

    @Inject
    RequestsCheckService requestsCheckService;

    @GET
    @Path("/morale")
    @Produces(MediaType.APPLICATION_JSON)
    public GoodEvilResponse getGoodEvil(@QueryParam("s") String sentence) {
        return goodEvilService.goodOrEvil(sentence);
    }

    @POST
    @Path("/opinion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postOpinion(Opinion opinion) {
        if (opinion != null && requestsCheckService.consumes(opinion.getRequestId())) {
            return Response.ok().build();
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
