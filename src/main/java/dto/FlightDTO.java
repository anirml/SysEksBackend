
package dto;

import entities.Airport;
import entities.Flight;
import java.util.Date;


public class FlightDTO {

    private final Long id;
    private final Date depatureTime;
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
        this.depatureTime = f.getDepatureTime();
        this.flightDuration = f.getDuration();
        this.departureAirportName = f.getOrigin().getPlaceName();
        this.departureAirportCode = f.getOrigin().getIATA();
        this.arrivalAirportName = f.getDestination().getPlaceName();
        this.arrivalAirportCode = f.getDestination().getIATA();
        this.airline = "JJU-Flights";
        this.price = f.getPrice();
        this.link = f.getLink();
    }

    public Date getDepatureTime() {
        return depatureTime;
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