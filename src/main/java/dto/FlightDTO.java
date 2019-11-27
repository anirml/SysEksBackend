
package dto;

import entities.Airport;
import entities.Flight;
import java.util.Date;


public class FlightDTO {

    private final Long id;
    private final Date departureTime;
    private final Long flightDuration;
    private final String departureAirportName;
    private final String departureAirportCode;
    private final String arrivalAirportName;
    private final String arrivalAirportCode;
    
    private final String airline;
    
    private final double price;
    private final String link;

    public FlightDTO(Flight f) {
        this.id = f.getId();
        this.departureTime = f.getDepartureTime();
        this.flightDuration = f.getDuration();
        this.departureAirportName = f.getOrigin().getPlaceName();
        this.departureAirportCode = f.getOrigin().getIATA();
        this.arrivalAirportName = f.getDestination().getPlaceName();
        this.arrivalAirportCode = f.getDestination().getIATA();
        this.airline = "JJU-Flights";
        this.price = f.getPrice();
        this.link = f.getLink();
    }

    public FlightDTO(Long id, Date departureTime, Long flightDuration, String departureAirportName, String departureAirportCode, String arrivalAirportName, String arrivalAirportCode, String airline, double price, String link) {
        this.id = id;
        this.departureTime = departureTime;
        this.flightDuration = flightDuration;
        this.departureAirportName = departureAirportName;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportName = arrivalAirportName;
        this.arrivalAirportCode = arrivalAirportCode;
        this.airline = airline;
        this.price = price;
        this.link = link;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public Long getFlightDuration() {
        return flightDuration;
    }

    public String getDepartureAirportName() {
        return departureAirportName;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public String getArrivalAirportName() {
        return arrivalAirportName;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public String getAirline() {
        return airline;
    }

    public double getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }
       
    
}