
package dto;

import entities.Airport;
import entities.Flight;
import java.util.Date;


public class FlightDTO {

    private final String origin;
    private final String destination;
    private final Date depatureTime;
    private final double price;
    private final String link;
    private final Long duration;
    
        public FlightDTO(Flight f) {
        this.origin = f.getOrigin().getIATA();
        this.destination = f.getDestination().getIATA();
        this.depatureTime = f.getDepatureTime();
        this.price = f.getPrice();
        this.link = f.getLink();
        this.duration = f.getDuration();
        }


    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Date getDepatureTime() {
        return depatureTime;
    }

    public double getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    public Long getDuration() {
        return duration;
    }
   
    
}