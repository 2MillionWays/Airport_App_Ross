package ross.DB;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import ross.Flight;
import ross.Passenger;
import ross.Ticket;
import ross.enums.FlightStatus;
import ross.enums.FlightType;
import ross.enums.Sex;
import ross.enums.TicketType;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class DataBaseRequests {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection("jdbc:h2:~/Airport_App_Ross", "Ross", "nukanaka");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection){
        try {
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Flight> getFlightByStatus(String typeOfFlight) {
        List<Flight> arrivals = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from flight where" +
                    " flight_type = ?");
            preparedStatement.setString(1, typeOfFlight);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String flightType = resultSet.getString("flight_type");
                String flightStatus = resultSet.getString("flight_status");
                String flightNumber = resultSet.getString("flight_number");
                String city = resultSet.getString("city");
                String terminal = resultSet.getString("terminal");
                String gate = resultSet.getString("gate");
                Timestamp date1 = resultSet.getTimestamp("Date_time");
                Flight flight = new Flight();
                flight.setFlightType(FlightType.valueOf(flightType));
                flight.setFlightStatus(FlightStatus.valueOf(flightStatus));
                flight.setFlightNumber(flightNumber);
                flight.setCityOfArrivalDeparture(city);
                flight.setTerminal(terminal);
                flight.setGate(gate);
                flight.setTimestamp(date1);
                flight.setPassengers(DataBaseRequests.getPassengerList(flightNumber));
                flight.setTickets(DataBaseRequests.getTicketsList(flightNumber));
                arrivals.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataBaseRequests.closeConnection(connection);
        }
        return arrivals;
    }

    public static List<Ticket> getTickets(){
        List<Ticket> ticketList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from ticket");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String ticketType = resultSet.getString("ticket_type");
                Double price = resultSet.getDouble("price");
                String flightNumber = resultSet.getString("flight_number");
                Ticket ticket = new Ticket();
                ticket.setType(TicketType.valueOf(ticketType));
                ticket.setPrice(price);
                ticket.setFlightNumber(flightNumber);
                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return ticketList;
    }

    public static List<Passenger> getPassengerList(String flightNumber){
        List<Passenger> passengerList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from passenger " +
                    "where flight_number = ?");
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String nationality = resultSet.getString("nationality");
                String passport = resultSet.getString("passport");
                Date dob = resultSet.getDate("date_of_birth");
                Calendar dateOfBirth = Calendar.getInstance();
                dateOfBirth.setTimeInMillis(dob.getTime());
                String ticketType = resultSet.getString("ticket_type");
                String flightNum = resultSet.getString("flight_number");
                String sex = resultSet.getString("sex");
                Passenger passenger = new Passenger();
                passenger.setFirstName(first_name);
                passenger.setSecondName(last_name);
                passenger.setSex(Sex.valueOf(sex));
                passenger.setNationality(nationality);
                passenger.setPassport(passport);
                passenger.setTicketType(TicketType.valueOf(ticketType));
                passenger.setFlightNumber(flightNum);
                passengerList.add(passenger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return passengerList;
    }

    public static List<Passenger> getPassengers(){
        List<Passenger> passengerList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from passenger");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String nationality = resultSet.getString("nationality");
                String passport = resultSet.getString("passport");
                Date dob = resultSet.getDate("date_of_birth");
                String ticketType = resultSet.getString("ticket_type");
                String flightNum = resultSet.getString("flight_number");
                String sex = resultSet.getString("sex");
                Passenger passenger = new Passenger();
                passenger.setFirstName(first_name);
                passenger.setSecondName(last_name);
                passenger.setSex(Sex.valueOf(sex));
                passenger.setNationality(nationality);
                passenger.setPassport(passport);
                passenger.setDateOfBirth(dob);
                passenger.setTicketType(TicketType.valueOf(ticketType));
                passenger.setFlightNumber(flightNum);
                passengerList.add(passenger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return passengerList;
    }

    public static List<Ticket> getTicketsList(String flightNumber){
        List<Ticket> ticketList = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from ticket " +
                    "where flight_number = ?");
            preparedStatement.setString(1, flightNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String ticketType = resultSet.getString("ticket_type");
                Double price = resultSet.getDouble("price");
                String flightNum = resultSet.getString("flight_number");
                Ticket ticket = new Ticket();
                ticket.setType(TicketType.valueOf(ticketType));
                ticket.setPrice(price);
                ticket.setFlightNumber(flightNum);
                ticketList.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return ticketList;
    }

    public static List<Passenger> searchPassenger (String lastName, String firstName){
        List<Passenger> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("select * from passenger " +
                    "where first_name = ? and last_name = ?");
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String nationality = resultSet.getString("nationality");
                String passport = resultSet.getString("passport");
                Date dob = resultSet.getDate("date_of_birth");
                Calendar dateOfBirth = Calendar.getInstance();
                dateOfBirth.setTimeInMillis(dob.getTime());
                String ticketType = resultSet.getString("ticket_type");
                String flightNumber = resultSet.getString("flight_number");
                String sex = resultSet.getString("sex");
                Passenger passenger = new Passenger();
                passenger.setFirstName(first_name);
                passenger.setSecondName(last_name);
                passenger.setSex(Sex.valueOf(sex));
                passenger.setNationality(nationality);
                passenger.setPassport(passport);
                passenger.setTicketType(TicketType.valueOf(ticketType));
                passenger.setFlightNumber(flightNumber);
                result.add(passenger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return result;
    }

    public static String choiceBoxTickets(int newValue){
        String searchRequest = null;
        switch (newValue){
            case 0: searchRequest = "select * from ticket where flight_number = ?";
                break;
            case 1: searchRequest = "select * from ticket where price = ?";
                break;
        }
        return searchRequest;
    }

    public static String choiceBoxFlights(int newValue){
        String searchRequest = null;
        switch (newValue){
            case 0: searchRequest = "select * from flight where flight_number = ? and flight_type = ?";
                break;
            case 1: searchRequest = "select * from flight where gate = ? and flight_type = ?";
                break;
            case 2: searchRequest = "select * from flight where city = ? and flight_type = ?";
        }
        return searchRequest;
    }

    public static String choiceBoxPassengers(int newValue){
        String searchRequest = null;
        switch (newValue){
            case 0: searchRequest = "select * from passenger where first_name = ? and last_name = ?";
                break;
            case 1: searchRequest = "select * from passenger where flight_number = ?";
                break;
            case 2: searchRequest = "select * from passenger where passport = ?";
        }
        return searchRequest;
    }

    public static List<Flight> getSearchFlight(String typeOfFlight, TextField searchFlightField, ChoiceBox searchFlightBox){
        List<Flight> searchResult = new ArrayList<>();
        String searchValue = searchFlightField.getText();
        String searchType = DataBaseRequests.choiceBoxFlights(searchFlightBox.getSelectionModel().getSelectedIndex());
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(searchType);
            preparedStatement.setString(1, searchValue);
            preparedStatement.setString(2, typeOfFlight);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String flightType = resultSet.getString("flight_type");
                String flightStatus = resultSet.getString("flight_status");
                String flightNum = resultSet.getString("flight_number");
                String city = resultSet.getString("city");
                String terminal = resultSet.getString("terminal");
                String gate = resultSet.getString("gate");
                Timestamp date1 = resultSet.getTimestamp("Date_time");
                Flight flight = new Flight();
                flight.setFlightType(FlightType.valueOf(flightType));
                flight.setFlightStatus(FlightStatus.valueOf(flightStatus));
                flight.setFlightNumber(flightNum);
                flight.setCityOfArrivalDeparture(city);
                flight.setTerminal(terminal);
                flight.setGate(gate);
                flight.setPassengers(DataBaseRequests.getPassengerList(searchValue));
                flight.setTickets(DataBaseRequests.getTicketsList(searchValue));
                flight.setTimestamp(date1);
                searchResult.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return searchResult;
    }

    public static List<Ticket> getSearchTicket(TextField searchTicketField, ChoiceBox searchTicketBox){
        List<Ticket> searchResult = new ArrayList<>();
        String searchValue = searchTicketField.getText();
        String searchType = DataBaseRequests.choiceBoxTickets(searchTicketBox.getSelectionModel().getSelectedIndex());
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(searchType);
            preparedStatement.setString(1, searchValue);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String ticketType = resultSet.getString("ticket_type");
                Double price = resultSet.getDouble("price");
                String flightNumber = resultSet.getString("flight_number");
                Ticket ticket = new Ticket();
                ticket.setType(TicketType.valueOf(ticketType));
                ticket.setPrice(price);
                ticket.setFlightNumber(flightNumber);
                searchResult.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return searchResult;
    }

    public static List<Passenger> getSearchPassenger(TextField searchTicketField, ChoiceBox searchTicketBox){
        List<Passenger> searchResult = new ArrayList<>();
        String searchValue = searchTicketField.getText();
        StringTokenizer st = new StringTokenizer(searchValue);
        String st1 = st.nextToken();
        String st2 = null;
        if(st.hasMoreTokens()) {
            st2 = st.nextToken();
        }
        String searchType = DataBaseRequests.choiceBoxPassengers(searchTicketBox.getSelectionModel().getSelectedIndex());
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(searchType);
            preparedStatement.setString(1, st1);
            if (st2 != null) {
                preparedStatement.setString(2, st2);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");
                String nationality = resultSet.getString("nationality");
                String passport = resultSet.getString("passport");
                Date dob = resultSet.getDate("date_of_birth");
                String ticketType = resultSet.getString("ticket_type");
                String flightNum = resultSet.getString("flight_number");
                String sex = resultSet.getString("sex");
                Passenger passenger = new Passenger();
                passenger.setFirstName(first_name);
                passenger.setSecondName(last_name);
                passenger.setSex(Sex.valueOf(sex));
                passenger.setNationality(nationality);
                passenger.setPassport(passport);
                passenger.setDateOfBirth(dob);
                passenger.setTicketType(TicketType.valueOf(ticketType));
                passenger.setFlightNumber(flightNum);
                searchResult.add(passenger);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
        return searchResult;
    }
}
