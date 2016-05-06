package ross.DB;

import javafx.scene.control.*;
import ross.*;
import ross.enums.*;

import java.sql.*;
import java.sql.Date;
import java.text.*;

public class AddDeleteUpdate {

    public static void addTicket(String flightNumber, TicketType ticketType, double price){
        String sqlRequest = "insert into ticket (flight_number, ticket_type, price) values (?,?,?)";
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, flightNumber);
            preparedStatement.setString(2, ticketType.toString());
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    public static void deleteTicket(TableView<Ticket> ticketTableView){
        Ticket selectedItem = ticketTableView.getSelectionModel().getSelectedItem();
        String sqlRequest = "delete from ticket where flight_number = ? and ticket_type = ? and price = ?";
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, selectedItem.getFlightNumber());
            preparedStatement.setString(2, selectedItem.getType().toString());
            preparedStatement.setDouble(3, selectedItem.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    public static void editSelectedTicket(TableView<Ticket> ticketTableView, TextField textFlight, ComboBox<TicketType> ticketTypeComboBox,
                                          TextField textPrice){
        Ticket selectedItem = ticketTableView.getSelectionModel().getSelectedItem();
        textFlight.setText(selectedItem.getFlightNumber());
        ticketTypeComboBox.getSelectionModel().select(selectedItem.getType());
        textPrice.setText(Double.toString(selectedItem.getPrice()));
    }

    public static void applyEditTicket(TableView<Ticket> ticketTableView, TextField textFlight, ComboBox<TicketType> ticketTypeComboBox,
                                       TextField textPrice){
        Ticket selectedItem = ticketTableView.getSelectionModel().getSelectedItem();
        String flightNumber = selectedItem.getFlightNumber();
        String ticketType = selectedItem.getType().toString();
        String sqlRequest = "update ticket set flight_number = ? , ticket_type = ? , price = ?  where flight_number = '"
                +flightNumber+"' and ticket_type = '"+ticketType+"'";
        String flightNumberChange = textFlight.getText();
        String flightTypeChange = ticketTypeComboBox.getSelectionModel().getSelectedItem().toString();
        Double price = Double.parseDouble(textPrice.getText());
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, flightNumberChange);
            preparedStatement.setString(2, flightTypeChange);
            preparedStatement.setDouble(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    //Passenger -------------------------

    public static Date convertTextToDateSQL(String string){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            java.util.Date dateOfBirth = dateFormat.parse(string);
            date = new Date(dateOfBirth.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void convertDateToText (Date date, TextField textYear, TextField textMonth, TextField textDay){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String result = dateFormat.format(date);
        textYear.setText(result.substring(0,4));
        textMonth.setText(result.substring(4,6));
        textDay.setText(result.substring(6,8));
    }


    public static Passenger getPassenger(TextField firstName, TextField lastName, ComboBox<Sex> sexComboBox, TextField nationality,
                                         TextField passport, String dateOfBirth, ComboBox<TicketType> ticketTypeComboBox,
                                         TextField flightNumber){
        Passenger result = new Passenger();
        result.setFirstName(firstName.getText());
        result.setSecondName(lastName.getText());
        result.setSex(sexComboBox.getSelectionModel().getSelectedItem());
        result.setNationality(nationality.getText());
        result.setPassport(passport.getText());
        result.setDateOfBirth(AddDeleteUpdate.convertTextToDateSQL(dateOfBirth));
        result.setTicketType(ticketTypeComboBox.getSelectionModel().getSelectedItem());
        result.setFlightNumber(flightNumber.getText());
        return result;
    }

    public static void addPassenger(TextField firstName, TextField lastName, ComboBox<Sex> sexComboBox, TextField nationality,
                                    TextField passport, String dateOfBirth, ComboBox<TicketType> ticketTypeComboBox, TextField flightNumber){
        Passenger addPassenger = AddDeleteUpdate.getPassenger(firstName,lastName,sexComboBox,nationality,passport,dateOfBirth,
                ticketTypeComboBox, flightNumber);
        String sqlRequest = "insert into passenger (first_name, last_name, sex, nationality, passport, " +
                "date_of_birth, ticket_type, flight_number) values (?,?,?,?,?,?,?,?)";
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, addPassenger.getFirstName());
            preparedStatement.setString(2, addPassenger.getSecondName());
            preparedStatement.setString(3, addPassenger.getSex().toString());
            preparedStatement.setString(4, addPassenger.getNationality());
            preparedStatement.setString(5, addPassenger.getPassport());
            preparedStatement.setDate(6, addPassenger.getDateOfBirth());
            preparedStatement.setString(7, addPassenger.getTicketType().toString());
            preparedStatement.setString(8, addPassenger.getFlightNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    public static void deletePassenger(TableView<Passenger> passengerTableView){
        Passenger selectedItem = passengerTableView.getSelectionModel().getSelectedItem();
        String sqlRequest = "delete from passenger where first_name = ? and last_name = ? and passport = ? and flight_number = ?";
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, selectedItem.getFirstName());
            preparedStatement.setString(2, selectedItem.getSecondName());
            preparedStatement.setString(3, selectedItem.getPassport());
            preparedStatement.setString(4, selectedItem.getFlightNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    public static void editSelectedPassenger(TableView<Passenger> passengerTableView, TextField firstName, TextField lastName,
                                             ComboBox<Sex> sexComboBox, TextField nationality, TextField passport, TextField textYear,
                                             TextField textMonth, TextField textDay, ComboBox<TicketType> ticketTypeComboBox,
                                             TextField flightNumber){
        Passenger selectedItem = passengerTableView.getSelectionModel().getSelectedItem();
        firstName.setText(selectedItem.getFirstName());
        lastName.setText(selectedItem.getSecondName());
        sexComboBox.getSelectionModel().select(selectedItem.getSex());
        nationality.setText(selectedItem.getNationality());
        passport.setText(selectedItem.getPassport());
        AddDeleteUpdate.convertDateToText(selectedItem.getDateOfBirth(),textYear,textMonth,textDay);
        ticketTypeComboBox.getSelectionModel().select(selectedItem.getTicketType());
        flightNumber.setText(selectedItem.getFlightNumber());
    }

    public static void applyEditPassenger(TableView<Passenger> passengerTableView, TextField firstName, TextField lastName,
                                          ComboBox<Sex> sexComboBox, TextField nationality, TextField passport, TextField textYear,
                                          TextField textMonth, TextField textDay, ComboBox<TicketType> ticketTypeComboBox,
                                          TextField flightNumber){
        Passenger selectedItem = passengerTableView.getSelectionModel().getSelectedItem();
        String selectedItemPassport = selectedItem.getPassport();
        String dateOfBirth = selectedItem.getDateOfBirth().toString();
        String sqlRequest = "update passenger set first_name = ?, last_name = ?, sex = ?, nationality = ?, passport = ?, date_of_birth = ?" +
                ", ticket_type = ?, flight_number = ? where passport = '"+selectedItemPassport+"' and date_of_birth = '"+dateOfBirth+"'";
        String firstNameChange = firstName.getText();
        String lastNameChange = lastName.getText();
        String sexChange = sexComboBox.getSelectionModel().getSelectedItem().toString();
        String nationalityChange = nationality.getText();
        String passportChange = passport.getText();
        Date date = AddDeleteUpdate.convertTextToDateSQL(textYear.getText()+textMonth.getText()+textDay.getText());
        String ticketTypeChange = ticketTypeComboBox.getSelectionModel().getSelectedItem().toString();
        String flightNumberChange = flightNumber.getText();
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, firstNameChange);
            preparedStatement.setString(2, lastNameChange);
            preparedStatement.setString(3, sexChange);
            preparedStatement.setString(4, nationalityChange);
            preparedStatement.setString(5, passportChange);
            preparedStatement.setDate(6, date);
            preparedStatement.setString(7, ticketTypeChange);
            preparedStatement.setString(8, flightNumberChange);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    //Flights -------------------------

    public static void deleteFlight(TableView<Flight> flightTableView){
        Flight selectedItem = flightTableView.getSelectionModel().getSelectedItem();
        String sqlRequest = "delete from flight where flight_type = ? and flight_number = ? and city = ?";
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1, selectedItem.getFlightType().toString());
            preparedStatement.setString(2, selectedItem.getFlightNumber());
            preparedStatement.setString(3, selectedItem.getCityOfArrivalDeparture());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }

    }

    public static Timestamp convertTextToTimestamp(String string){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Timestamp timestamp = null;
        try {
            java.util.Date flightDateTime = dateFormat.parse(string);
            timestamp = new Timestamp(flightDateTime.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static void convertTimestampToText(Timestamp timestamp, TextField textYear, TextField textMonth, TextField textDay,
                                              TextField textHours, TextField textMinutes){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String result = dateFormat.format(timestamp);
        textYear.setText(result.substring(0,4));
        textMonth.setText(result.substring(4,6));
        textDay.setText(result.substring(6,8));
        textHours.setText(result.substring(8,10));
        textMinutes.setText(result.substring(10,12));
    }

    public static Flight getFlight(ComboBox<FlightType> flightTypeComboBox, ComboBox<FlightStatus> flightStatusComboBox,
                                   TextField textFlightNumber, TextField textCity, TextField textTerminal, TextField textGate,
                                   String dateTime){
        Flight result = new Flight();
        result.setFlightType(flightTypeComboBox.getSelectionModel().getSelectedItem());
        result.setFlightStatus(flightStatusComboBox.getSelectionModel().getSelectedItem());
        result.setFlightNumber(textFlightNumber.getText());
        result.setCityOfArrivalDeparture(textCity.getText());
        result.setTerminal(textTerminal.getText());
        result.setGate(textGate.getText());
        result.setTimestamp(convertTextToTimestamp(dateTime));
        return result;

    }

    public static void addFlight(ComboBox<FlightType> flightTypeComboBox, ComboBox<FlightStatus> flightStatusComboBox,
                                 TextField textFlightNumber, TextField textCity, TextField textTerminal, TextField textGate,
                                 String dateTime){
        Flight addFlight = AddDeleteUpdate.getFlight(flightTypeComboBox,flightStatusComboBox,textFlightNumber,textCity,textTerminal,
                textGate,dateTime);
        String sqlRequest = "insert into flight (flight_type,flight_status,flight_number,city,terminal,gate,date_time)" +
                " values (?,?,?,?,?,?,?)";
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1,addFlight.getFlightType().toString());
            preparedStatement.setString(2,addFlight.getFlightStatus().toString());
            preparedStatement.setString(3,addFlight.getFlightNumber());
            preparedStatement.setString(4,addFlight.getCityOfArrivalDeparture());
            preparedStatement.setString(5,addFlight.getTerminal());
            preparedStatement.setString(6,addFlight.getGate());
            preparedStatement.setTimestamp(7,addFlight.getTimestamp());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }

    public static void editSelectedFlight(TableView<Flight> flightTableView, ComboBox<FlightType> flightTypeComboBox,
                                          ComboBox<FlightStatus> flightStatusComboBox, TextField textFlightNumber, TextField textCity,
                                          TextField textTerminal, TextField textGate, TextField textYear, TextField textMonth,
                                          TextField textDay, TextField textHours, TextField textMinutes){
        Flight selectedItem = flightTableView.getSelectionModel().getSelectedItem();
        flightTypeComboBox.getSelectionModel().select(selectedItem.getFlightType());
        flightStatusComboBox.getSelectionModel().select(selectedItem.getFlightStatus());
        textFlightNumber.setText(selectedItem.getFlightNumber());
        textCity.setText(selectedItem.getCityOfArrivalDeparture());
        textTerminal.setText(selectedItem.getTerminal());
        textGate.setText(selectedItem.getGate());
        AddDeleteUpdate.convertTimestampToText(selectedItem.getTimestamp(),textYear,textMonth,textDay,textHours,textMinutes);
    }

    public static void applyEditFlight(TableView<Flight> flightTableView, ComboBox<FlightType> flightTypeComboBox,
                                       ComboBox<FlightStatus> flightStatusComboBox, TextField textFlightNumber, TextField textCity,
                                       TextField textTerminal, TextField textGate, String dateTimeChange){
        Flight selectedItem = flightTableView.getSelectionModel().getSelectedItem();
        String flightType = selectedItem.getFlightType().toString();
        String flightNumber = selectedItem.getFlightNumber();
        String city = selectedItem.getCityOfArrivalDeparture();
        Timestamp timestamp = selectedItem.getTimestamp();
        String sqlRequest = "update flight set flight_type = ?,flight_status = ?,flight_number = ?,city = ?,terminal = ?, Gate = ?," +
                "date_time = ? where flight_type = '"+flightType+"' and flight_number = '"+flightNumber+"' and city = '"+
                city+"' and date_time = '"+timestamp+"'";
        String flightTypeChange = flightTypeComboBox.getSelectionModel().getSelectedItem().toString();
        String flightStatus = flightStatusComboBox.getSelectionModel().getSelectedItem().toString();
        String flightNumberChange = textFlightNumber.getText();
        String cityChange = textCity.getText();
        String terminalChange = textTerminal.getText();
        String gateChange = textGate.getText();
        Timestamp timestampChange = AddDeleteUpdate.convertTextToTimestamp(dateTimeChange);
        Connection connection = null;
        try {
            connection = DataBaseRequests.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequest);
            preparedStatement.setString(1,flightTypeChange);
            preparedStatement.setString(2,flightStatus);
            preparedStatement.setString(3,flightNumberChange);
            preparedStatement.setString(4,cityChange);
            preparedStatement.setString(5,terminalChange);
            preparedStatement.setString(6,gateChange);
            preparedStatement.setTimestamp(7,timestampChange);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DataBaseRequests.closeConnection(connection);
        }
    }
}
