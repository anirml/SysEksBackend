package facades;

import dto.FlightDTO;
import static entities.Airport_.IATA;
import entities.Flight;
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
    
    //TODO Remove/Change this before use
    public long getFlightCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long FlightCount = (long)em.createQuery("SELECT COUNT(f) FROM Flight f").getSingleResult();
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

        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f JOIN f.origin fo JOIN f.destination fd WHERE fo.IATA = :originIATA AND fd.IATA = :destinationIATA AND f.depatureTime >= :date AND f.depatureTime < :nextDay" ,Flight.class);
    
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

    public List<FlightDTO> getFlightsByOriginAndDestinationByDate(String originIATA, String destinationIATA, SimpleDateFormat dated) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
        public List<FlightDTO> getFlightsByDate(Date date) {
        EntityManager em = getEntityManager();
        Date nextDay = new Date(date.getTime() + 86400000);
        List<FlightDTO> FlightsDTO = new ArrayList();

        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f WHERE f.depatureTime >= :date AND f.depatureTime < :nextDay " ,Flight.class);
    
        List<Flight> flights = query.setParameter("date", date)
                .setParameter("nextDay", nextDay)
                .getResultList();


        for (Flight flight : flights) {
            FlightsDTO.add(new FlightDTO(flight));
        }
        return FlightsDTO;
    }
            
            
}
