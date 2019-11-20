package utils;


import entities.Airport;
import entities.Role;
import entities.User;
import entities.Flight;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class SetupTestFlights {

  public static void main(String[] args) {

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords
    Airport airport = new Airport("SAS","SAS");
    //Airport airport2 = new Airport("RYANAIR","RYANAIR");
 
    
    Date date = new Date();
    /*
    
    Flight flight = new Flight(airport.setIATA(),date,1500.0,"www.sas.com",new Long(2));
    
    //User user = new User("user", "test");
    //User admin = new User("admin", "test");
    //User both = new User("user_admin", "test");

    em.getTransaction().begin();
    flight.setOrigin(airport);
    flight.setDestination(airport);
    flight.setDepatureTime(date);
    flight.setPrice(1500.0);
    flight.setLink("www.sas.com");
    flight.setDuration(new Long(2));
    
    em.persist(airport);
    em.persist(date);
    em.persist(1500.0);
    em.persist("www.sas.com");
    em.persist(new Long(2));

    em.getTransaction().commit();

   */
  }

}
