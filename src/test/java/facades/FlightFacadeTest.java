
package facades;

import dto.FlightDTO;
import java.io.IOException;
import java.net.ProtocolException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
//import static oracle.jrockit.jfr.events.Bits.longValue;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FlightFacadeTest {
    
   

    private static EntityManagerFactory emf;
    private static FlightFacade facade;

    public FlightFacadeTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = FlightFacade.getFacadeExample(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,EMF_Creator.Strategy.DROP_AND_CREATE);
       facade = FlightFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() throws IOException, ProtocolException, ParseException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE from Role").executeUpdate();
            em.createQuery("DELETE from User").executeUpdate();
            FlightDTO flight1 = new FlightDTO(Long.valueOf(1),new Date(),Long.valueOf(3000),"Copenhagen","CPH","LondonHR","LHR","SAS",3000,"www.sas.com");
            FlightDTO flight2 = new FlightDTO(Long.valueOf(2),new Date(),Long.valueOf(5000),"Paris","CDG","Copenhagen","CPH","SAS",5000,"www.sas.com");
            FlightDTO flight3 = new FlightDTO(Long.valueOf(3),new Date(),Long.valueOf(4000),"Copenhagen","CPH","Tokyo","NRT","SAS",4000,"www.sas.com");
            FlightDTO flight4 = new FlightDTO(Long.valueOf(4),new Date(),Long.valueOf(2000),"LondonHR","LHR","Copenhagen","CPH","SAS",2000,"www.sas.com");
            //FlightDTO flight5 = new FlightDTO(longValue(5),new Date(),longValue(7000),"Copenhagen","CPH","LondonHR","LHR","SAS",1000,"www.sas.com");
            
            List<FlightDTO> f = new ArrayList();
            
            f.add(flight1);
            f.add(flight2);
            f.add(flight3);
            f.add(flight4);
            
            List k = facade.getValidAirportDeparture(f);           
            
           Assert.isTrue(k.contains("CPH")&&k.contains("LHR")&&k.contains("CDG"), "Not found");
          
        } finally {
            em.close();
        }
    }

  //  @AfterEach
  //  public void tearDown() {
//        Remove any data after each test was run
  //  }



}