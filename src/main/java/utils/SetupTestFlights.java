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
    Airport airport2 = new Airport("RYANAIR","RYANAIR");
 
    
    Date date = new Date();
    
    
    em.getTransaction().begin();
    
    Flight flight = new Flight(airport,airport2,date,1500,"sas.com",Long.valueOf(5000));
    Flight flight2 = new Flight(airport,airport2,date,1500,"sas.com",Long.valueOf(5000));
    Flight flight3 = new Flight(airport,airport2,date,1500,"sas.com",Long.valueOf(5000));

    em.persist(airport);
    em.persist(airport2);
    em.persist(flight);
    em.persist(flight2);
    em.persist(flight3);

    em.getTransaction().commit();

  }

}
