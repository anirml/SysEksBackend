package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.FlightDTO;
import static entities.Airport_.IATA;
import entities.Flight;
import entities.User;
import facades.ApiScrapeFacade;
import facades.FlightFacade;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
@Path("flight")
public class DemoResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final FlightFacade FACADE = FlightFacade.getFacadeExample(EMF);
    
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"message\":\"Hello anonymous\"}";
    }

    /*
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
 */

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
    
    
    
    @Path("count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getRenameMeCount() {
        long count = FACADE.getFlightCount();
        
        //System.out.println("--------------->"+count);
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
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
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allf/{originIATA}-{destinationIATA}")
    public String SortOriginToDestination(Flight entity, @PathParam("originIATA") String originIATA, @PathParam("destinationIATA") String destinationIATA) {
        List<FlightDTO> flight = FACADE.getFlightsByOriginAndDestination(originIATA, destinationIATA);
      //  List<FlightDTO> flightInfoList = FACADE.getFlightsByAirport(IATA);
        return GSON.toJson(flight);
    }  
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("allff/{originIATA}-{destinationIATA}/{date}")
    public String SortOriginToDestinationByDate(Flight entity, @PathParam("originIATA") String originIATA, 
            @PathParam("destinationIATA") String destinationIATA, @PathParam("date") String dateStr) throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(dateStr);
        
        
       // LocalDateTime localDateTime = LocalDateTime.parse(dateStr + "T00:00:00");
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
       // String formatDateTime = localDateTime.format(formatter);
        
       // DateFormat format = new SimpleDateFormat("yyyy-dd-MM",Locale.ENGLISH);   MMM d, yyyy HH:mm:ss a
       //Date date = format.parse(dateStr);
        
        List<FlightDTO> flight = FACADE.getFlightsByDate(date);
      //  List<FlightDTO> flightInfoList = FACADE.getFlightsByAirport(IATA);
        return GSON.toJson(flight);
    }  
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllFlights() {
        List<FlightDTO> flights = FACADE.getAllFlights();
        return GSON.toJson(flights);
    }
    
    
    
}