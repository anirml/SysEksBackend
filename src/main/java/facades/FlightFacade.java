package facades;

import dto.FlightDTO;
import entities.Flight;
import java.io.IOException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public class FlightFacade {

    private static FlightFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private FlightFacade(){}
     
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static FlightFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FlightFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public long getFlightCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long FlightCount = (long)em.createQuery("SELECT COUNT(f) FROM Flight f").getSingleResult();
            //TODO maybe make if statement if flightcount = 0 with err message;
            return FlightCount;
        }finally{  
            em.close();
        }
        
    }  
    
    public List<FlightDTO> getAllFlights(){
        EntityManager em = getEntityManager();
        TypedQuery<Flight> tq = em.createQuery("SELECT f FROM Flight f", Flight.class);
        List<Flight> flights = tq.getResultList();
        List<FlightDTO> FlightsDTO = new ArrayList<>();
        em.close();
        for (Flight flight : flights) {
            FlightsDTO.add(new FlightDTO(flight));
        }
        //TODO maybe make if statement if FlightsDTO = 0 with err message;
        return FlightsDTO;
    }

    
        public List<FlightDTO> getFlightsByOriginAndDestination(String originIATA, String destinationIATA) {
        EntityManager em = getEntityManager();

        List<FlightDTO> FlightsDTO = new ArrayList();

        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f JOIN f.origin fo JOIN f.destination fd WHERE fo.IATA = :originIATA AND fd.IATA = :destinationIATA" ,Flight.class);
    
        List<Flight> flights = query.setParameter("originIATA", originIATA)
                .setParameter("destinationIATA", destinationIATA)
                .getResultList();


        for (Flight flight : flights) {
            FlightsDTO.add(new FlightDTO(flight));
        }
        return FlightsDTO;
    }
    
        public List<FlightDTO> getFlightsByOriginAndDestinationByDate(String originIATA, String destinationIATA, Date date) {
        EntityManager em = getEntityManager();
        Date nextDay = new Date(date.getTime() + 86400000);
        List<FlightDTO> FlightsDTO = new ArrayList();

        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f JOIN f.origin fo JOIN f.destination fd WHERE fo.IATA = :originIATA AND fd.IATA = :destinationIATA AND f.departureTime >= :date AND f.departureTime < :nextDay" ,Flight.class);
    
        List<Flight> flights = query.setParameter("originIATA", originIATA)
                .setParameter("destinationIATA", destinationIATA)
                .setParameter("date", date)
                .setParameter("nextDay", nextDay)
                .getResultList();


        for (Flight flight : flights) {
            FlightsDTO.add(new FlightDTO(flight));
        }
        return FlightsDTO;
    }
    
    
        public List<FlightDTO> getFlightsByDate(Date date) {
        EntityManager em = getEntityManager();
        Date nextDay = new Date(date.getTime() + 86400000);
        List<FlightDTO> FlightsDTO = new ArrayList();

        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f WHERE f.departureTime >= :date AND f.departureTime < :nextDay " ,Flight.class);
    
        List<Flight> flights = query.setParameter("date", date)
                .setParameter("nextDay", nextDay)
                .getResultList();


        for (Flight flight : flights) {
            FlightsDTO.add(new FlightDTO(flight));
        }
        return FlightsDTO;
    }
       
        
        public List<String> getValidAirportDeparture(List<FlightDTO> f) throws IOException, ProtocolException, ParseException{
            EntityManager em = getEntityManager();
            
        List<String> s = new ArrayList();
        
            for (int i = 0; i < f.size(); i++) {                
                if(!s.contains(f.get(i).getDepartureAirportCode())){
                    s.add(f.get(i).getDepartureAirportCode());                           
                }                                            
        }
        return s;            
}

        public List<String> getValidAirportArrival(List<FlightDTO> f) throws IOException, ProtocolException, ParseException{
            EntityManager em = getEntityManager();
            
        List<String> s = new ArrayList();
        
            for (int i = 0; i < f.size(); i++) {                
                if(!s.contains(f.get(i).getArrivalAirportCode())){
                    s.add(f.get(i).getArrivalAirportCode());                           
                }                                            
        }
        return s;            
}        
        
        
        
        
        
        
        
        
        
        
  
}
