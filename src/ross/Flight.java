package ross;

import ross.enums.FlightStatus;
import ross.enums.FlightType;

import java.sql.Timestamp;
import java.util.List;

public class Flight {
    private FlightType flightType;
    private FlightStatus flightStatus;
    private String flightNumber;
    private String cityOfArrivalDeparture;
    private String terminal;
    private String gate;
    private Timestamp timestamp;

    private List<Passenger> passengers;
    private List<Ticket> tickets;

    public Flight() {
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public FlightType getFlightType() {
        return flightType;
    }

    public void setFlightType(FlightType flightType) {
        this.flightType = flightType;
    }

    public FlightStatus getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(FlightStatus flightStatus) {
        this.flightStatus = flightStatus;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getCityOfArrivalDeparture() {
        return cityOfArrivalDeparture;
    }

    public void setCityOfArrivalDeparture(String cityOfArrivalDeparture) {
        this.cityOfArrivalDeparture = cityOfArrivalDeparture;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getGate() {
        return gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }

}