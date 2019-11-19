package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;
import facades.ApiScrapeFacade;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * @author lam@cphbusiness.dk
 */
@Path("info")
public class DemoResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"message\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"message\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"message\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
    
    
    
    
    
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
   // @Path("starwars/{childID}")
    @Path("starwars/person/{starwarsID}")
    public String  getSwappiDataPerson(@PathParam("starwarsID") int id) throws MalformedURLException, IOException{
    URL url = new URL("https://swapi.co/api/people/"+id);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Accept", "application/json;charset=UTF-8");
    con.setRequestProperty("User-Agent", "server"); //remember if you are using SWAPI
    con.setRequestProperty("Access-Control-Allow-Origin", "server");
    Scanner scan = new Scanner(con.getInputStream());
    String jsonStr = null;
    if (scan.hasNext()) {
      jsonStr = scan.nextLine();
    }
    scan.close(); 
    return jsonStr;
  }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
   // @Path("starwars/{childID}")
    @Path("starwars/planet/{planetsID}")
    public String  getSwappiDataPlanets(@PathParam("planetsID") int id) throws MalformedURLException, IOException{
    URL url = new URL("https://swapi.co/api/planets/"+id);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("Accept", "application/json;charset=UTF-8");
    con.setRequestProperty("User-Agent", "server"); //remember if you are using SWAPI
    con.setRequestProperty("Access-Control-Allow-Origin", "server");
    Scanner scan = new Scanner(con.getInputStream());
    String jsonStr = null;
    if (scan.hasNext()) {
      jsonStr = scan.nextLine();
    }
    scan.close(); 
    return jsonStr;
  }
   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("swapidata")
    public void getSwapiData(@Suspended final AsyncResponse ar) throws ExecutionException, InterruptedException {
        ar.setTimeoutHandler(asyncResponse -> asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Timeout response too long..").build()));
        ar.setTimeout(8, TimeUnit.SECONDS);
        new Thread(() -> {
            ApiScrapeFacade apiScrapeFacade = new ApiScrapeFacade();
            try {
                ar.resume(GSON.toJson(apiScrapeFacade.runParralel()));
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}