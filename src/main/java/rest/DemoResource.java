package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.FlightDTO;
import entities.Flight;
import entities.User;
import errorhandling.FlightException;
import facades.ApiGrabFacade;
import facades.FlightFacade;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

@Path("flight")
public class DemoResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final FlightFacade FACADE = FlightFacade.getFacadeExample(EMF);
    private static final ApiGrabFacade APIGRABFACADE = ApiGrabFacade.getApiScrapeFacade(EMF);
    
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
        
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    } 
     
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("fromto/{originIATA}-{destinationIATA}")
    public String SortOriginToDestination(Flight entity, @PathParam("originIATA") String originIATA, @PathParam("destinationIATA") String destinationIATA) throws FlightException, IOException, ProtocolException, ParseException {
        List<FlightDTO> flight = FACADE.getFlightsByOriginAndDestination(originIATA, destinationIATA);
      //  List<FlightDTO> flightInfoList = FACADE.getFlightsByAirport(IATA);
       List<FlightDTO> flights = APIGRABFACADE.getAllApiDataSequentially();
       List<FlightDTO> sortedFlights = new ArrayList<>();
       for(FlightDTO f : flights ){
           if (f.getDepartureAirportCode().equalsIgnoreCase(originIATA) && f.getArrivalAirportCode().equalsIgnoreCase(destinationIATA)){
               sortedFlights.add(f);
           }
       } 
       if(sortedFlights.isEmpty()){throw new FlightException("Der blev ikke fundet nogen fly ud fra din søgning");}
        return GSON.toJson(sortedFlights);
    }   
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("date/{date}")
    public String getFlightsByDate(Flight entity, @PathParam("date") String dateStr) throws ParseException {
        
        DateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
	String dateInString = dateStr + " 00:00:00";
        Date date = formatter.parse(dateInString);
               
        List<FlightDTO> flight = FACADE.getFlightsByDate(date);
        return GSON.toJson(flight);
    }  
    
    @Path("all")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllFlights() {
        List<FlightDTO> flights = FACADE.getAllFlights();
        return GSON.toJson(flights);
    }
    
    @Path("combined")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllApiFlights() throws IOException, ProtocolException, ParseException {
        List<FlightDTO> flights = APIGRABFACADE.getAllApiDataSequentially();
        return GSON.toJson(flights);
    }
    
        
}