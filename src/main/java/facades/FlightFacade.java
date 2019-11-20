package facades;

import dto.FlightDTO;
import static entities.Airport_.IATA;
import entities.Flight;
import java.util.ArrayList;
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

    
    
        public List<FlightDTO> getFlightsByAirport(int zip) {
        EntityManager em = getEntityManager();

        List<FlightDTO> FlightsDTO = new ArrayList();

        TypedQuery<Flight> query = em.createQuery("SELECT f FROM Flight f JOIN f.airport a WHERE a.IATA = :IATA", Flight.class);
        List<Flight> flights = query.setParameter("IATA", IATA).getResultList();

        for (Flight flight : flights) {
            FlightsDTO.add(new FlightDTO(flight));
        }
        return FlightsDTO;
    }
    
    
}
