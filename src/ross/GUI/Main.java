package ross.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ross.DB.*;
import ross.*;
import ross.enums.*;
import java.sql.*;
import java.sql.Date;


public class Main extends Application {
    static TableView<Flight> flightTableView;
    static ObservableList<Flight> flightObservableList;
    static TableView<Flight> flightTableView1;
    static ObservableList<Flight> flightObservableList1;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //tickets table
        TableView<Ticket> ticketTableView = new TableView<>();
        ObservableList<Ticket> ticketObservableList = FXCollections.observableArrayList(DataBaseRequests.getTickets());
        ticketTableView.setPrefSize(259,337);


        TableColumn columnNumber = new TableColumn("Flight Number");
        columnNumber.setCellValueFactory(new PropertyValueFactory<Ticket, String>("flightNumber"));
        TableColumn columnType = new TableColumn("Ticket Type");
        columnType.setCellValueFactory(new PropertyValueFactory<Ticket, TicketType>("type"));
        columnType.setResizable(false);
        TableColumn columnPrice = new TableColumn("Price");
        columnPrice.setCellValueFactory(new PropertyValueFactory<Ticket, Double>("price"));
        columnPrice.setResizable(false);

        ticketTableView.setItems(ticketObservableList);
        ticketTableView.getColumns().addAll(columnNumber,columnType,columnPrice);

        //add/edit ticket window

        Label errorTicketFlightNum = new Label("Incorrect \nInput!");
        errorTicketFlightNum.setFont(Font.font("Arial", 11));
        errorTicketFlightNum.setTextFill(Paint.valueOf("RED"));
        errorTicketFlightNum.setVisible(false);

        Label errorTicketPrice = new Label("Incorrect \nInput!");
        errorTicketPrice.setFont(Font.font("Arial", 11));
        errorTicketPrice.setTextFill(Paint.valueOf("RED"));
        errorTicketPrice.setVisible(false);

        Label errorTicket = new Label("Invalid \nInput!");
        errorTicket.setTooltip(new Tooltip("Correct all fields!"));
        errorTicket.setFont(Font.font("Arial", 11));
        errorTicket.setTextFill(Paint.valueOf("RED"));
        errorTicket.setVisible(false);

        VBox vBoxTicketAddUpd = new VBox(20);
        vBoxTicketAddUpd.setVisible(false);

        Label labelFlight = new Label("Flight Number");
        TextField textFlight = new TextField();
        HBox flightNumber = new HBox(10, labelFlight, textFlight,errorTicketFlightNum);

        Label labelTicketClass = new Label("Ticket Type");
        ObservableList<TicketType> ticketClassObservableList = FXCollections.observableArrayList(TicketType.values());
        ComboBox<TicketType> ticketClassComboBox = new ComboBox<>(ticketClassObservableList);
        HBox ticketType = new HBox(10, labelTicketClass, ticketClassComboBox);

        Label labelPrice = new Label("Price (USD)");
        TextField textPrice = new TextField();
        HBox price = new HBox(10, labelPrice, textPrice,errorTicketPrice);

        TextFieldsControl.textLimit(textFlight,7);
        TextFieldsControl.textCheck(textFlight,"[A-Z]{2,3}[0-9]{3,4}", errorTicketFlightNum);
        TextFieldsControl.textLimit(textPrice,7);
        TextFieldsControl.textCheck(textPrice,"((1[0-9]{3}.\\d{1,2}|\\d{3}.\\d{1,2})|(1[0-9]{3}|\\d{3}))", errorTicketPrice);

        Button addTicket = new Button("Add New Ticket");
        addTicket.setVisible(false);
        addTicket.disableProperty().bind(textFlight.textProperty().isEmpty().or(ticketClassComboBox.valueProperty().isNull()
                .or(textPrice.textProperty().isEmpty())));
        Button editTicket = new Button("Apply Edit");
        editTicket.setVisible(false);
        editTicket.disableProperty().bind(textFlight.textProperty().isEmpty().or(ticketClassComboBox.valueProperty().isNull()
                .or(textPrice.textProperty().isEmpty())));
        Button cancelTicket = new Button("Cancel");
        HBox addEditTicket = new HBox(20,addTicket,editTicket,cancelTicket, errorTicket);

        addTicket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!TextFieldsControl.checkForErrorsTickets(errorTicketFlightNum,errorTicketPrice)){
                    errorTicket.setVisible(true);
                } else {
                    errorTicket.setVisible(false);
                    String flightNumber = textFlight.getText();
                    TicketType ticketType = ticketClassComboBox.getSelectionModel().getSelectedItem();
                    Double price = Double.parseDouble(textPrice.getText());
                    AddDeleteUpdate.addTicket(flightNumber, ticketType, price);
                    textFlight.clear();
                    ticketClassComboBox.getSelectionModel().clearSelection();
                    textPrice.clear();
                    vBoxTicketAddUpd.setVisible(false);
                    ticketObservableList.removeAll(ticketObservableList);
                    ticketObservableList.addAll(DataBaseRequests.getTickets());
                }
            }
        });
        editTicket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!TextFieldsControl.checkForErrorsTickets(errorTicketFlightNum,errorTicketPrice)){
                    errorTicket.setVisible(true);
                } else {
                    errorTicket.setVisible(false);
                    AddDeleteUpdate.applyEditTicket(ticketTableView, textFlight, ticketClassComboBox, textPrice);
                    textFlight.clear();
                    ticketClassComboBox.getSelectionModel().clearSelection();
                    textPrice.clear();
                    vBoxTicketAddUpd.setVisible(false);
                    ticketObservableList.removeAll(ticketObservableList);
                    ticketObservableList.addAll(DataBaseRequests.getTickets());
                }
            }
        });
        cancelTicket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textFlight.clear();ticketClassComboBox.getSelectionModel().clearSelection();textPrice.clear();
                vBoxTicketAddUpd.setVisible(false);
            }
        });

        vBoxTicketAddUpd.getChildren().addAll(flightNumber,ticketType, price, addEditTicket);


        //Ticket search

        TextField searchTicketField = new TextField();

        Button cancelTicketSearch = new Button("Cancel Search");
        cancelTicketSearch.setVisible(false);

        ObservableList observableListTickets = FXCollections.observableArrayList("by Flight Number","by Price");
        ChoiceBox searchTicketBox = new ChoiceBox(observableListTickets);
        searchTicketBox.getSelectionModel().selectFirst();

        searchTicketField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (searchTicketBox.getSelectionModel().getSelectedIndex() == 0){
                    if (newValue.length()>7) {
                        String replace = searchTicketField.getText().substring(0, 7);
                        searchTicketField.setText(replace);
                    }
                }else if (searchTicketBox.getSelectionModel().getSelectedIndex() == 1){
                    if (newValue.length()>7) {
                        String replace = searchTicketField.getText().substring(0, 7);
                        searchTicketField.setText(replace);
                    }
                }
            }
        });

        ObservableList<Ticket> resultObsTicketList = FXCollections.observableArrayList();

        Button searchTicket = new Button("Search");
        searchTicket.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resultObsTicketList.addAll(DataBaseRequests.getSearchTicket(searchTicketField,searchTicketBox));
                ticketTableView.setItems(resultObsTicketList);
                cancelTicketSearch.setVisible(true);
            }
        });
        cancelTicketSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ticketTableView.setItems(ticketObservableList);
                cancelTicketSearch.setVisible(false);
                searchTicketField.clear();
            }
        });

        //add/delete/edit ticket button
        Button deleteTicketButton = new Button("Delete Selected");
        Button addTicketButton = new Button("Add New Ticket");
        Button editTicketButton = new Button("Edit Selected");
        addTicketButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vBoxTicketAddUpd.setVisible(true);
                addTicket.setVisible(true);
                editTicket.setVisible(false);
            }
        });
        editTicketButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vBoxTicketAddUpd.setVisible(true);
                editTicket.setVisible(true);
                addTicket.setVisible(false);
                AddDeleteUpdate.editSelectedTicket(ticketTableView,textFlight,ticketClassComboBox,textPrice);
            }
        });
        deleteTicketButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddDeleteUpdate.deleteTicket(ticketTableView);
                ticketTableView.getSelectionModel().clearSelection();
                if (ticketTableView.getItems() == resultObsTicketList) {
                    resultObsTicketList.removeAll(resultObsTicketList);
                    resultObsTicketList.addAll(DataBaseRequests.getSearchTicket(searchTicketField, searchTicketBox));
                }
                ticketObservableList.removeAll(ticketObservableList);
                ticketObservableList.addAll(DataBaseRequests.getTickets());
            }
        });


        //Ticket Tab compilation

        HBox hBoxTicketTables = new HBox(10, ticketTableView, vBoxTicketAddUpd);
        hBoxTicketTables.setPadding(new Insets(5,5,0,5));
        HBox hBoxTicketBar = new HBox(5, searchTicket, searchTicketBox, searchTicketField, cancelTicketSearch);
        hBoxTicketBar.setPadding(new Insets(5,5,0,5));
        HBox hBoxTicketBottom = new HBox(10, addTicketButton, editTicketButton,deleteTicketButton);
        hBoxTicketBottom.setPadding(new Insets(5,5,0,5));
        VBox vBoxTickets = new VBox(10, hBoxTicketBar, hBoxTicketTables, hBoxTicketBottom);

        //-----------------------------------------------------------------------------------------------------------------------
        //Arrival Flights Table
        flightTableView = new TableView<>();
        flightObservableList = FXCollections.observableArrayList(DataBaseRequests.getFlightByStatus("ARRIVAL"));
        flightTableView.setPrefSize(468,337);

        TableColumn columnArrivalDateTime = new TableColumn("Date/Time");
        columnArrivalDateTime.setCellValueFactory(new PropertyValueFactory<Flight, Timestamp>("timestamp"));
        TableColumn columnArrivalFlightNumber = new TableColumn("Flight Number");
        columnArrivalFlightNumber.setCellValueFactory(new PropertyValueFactory<Flight, String>("flightNumber"));
        TableColumn columnArrivalCity = new TableColumn("From");
        columnArrivalCity.setCellValueFactory(new PropertyValueFactory<Flight, String>("cityOfArrivalDeparture"));
        TableColumn columnArrivalTerminal = new TableColumn("Terminal");
        columnArrivalTerminal.setCellValueFactory(new PropertyValueFactory<Flight, String>("terminal"));
        TableColumn columnArrivalStatus = new TableColumn("Status");
        columnArrivalStatus.setCellValueFactory(new PropertyValueFactory<Flight, FlightStatus>("flightStatus"));

        flightTableView.setItems(flightObservableList);
        flightTableView.getColumns().addAll(columnArrivalDateTime,columnArrivalFlightNumber,
                columnArrivalCity,columnArrivalTerminal,columnArrivalStatus);

        //Departure Flights Table
        flightTableView1 = new TableView<>();
        flightObservableList1 = FXCollections.observableArrayList(DataBaseRequests.getFlightByStatus("DEPARTURE"));
        flightTableView1.setPrefSize(516,337);

        TableColumn columnDepartureDateTime = new TableColumn("Date/Time");
        columnDepartureDateTime.setCellValueFactory(new PropertyValueFactory<Flight, Timestamp>("timestamp"));
        TableColumn columnDepartureFlightNumber = new TableColumn("Flight Number");
        columnDepartureFlightNumber.setCellValueFactory(new PropertyValueFactory<Flight, String>("flightNumber"));
        TableColumn columnDepartureCity = new TableColumn("Destination");
        columnDepartureCity.setCellValueFactory(new PropertyValueFactory<Flight, String>("cityOfArrivalDeparture"));
        TableColumn columnDepartureTerminal = new TableColumn("Terminal");
        columnDepartureTerminal.setCellValueFactory(new PropertyValueFactory<Flight, String>("terminal"));
        TableColumn columnDepartureGate = new TableColumn("Gate");
        columnDepartureGate.setCellValueFactory(new PropertyValueFactory<Flight, String>("gate"));
        TableColumn columnDepartureStatus = new TableColumn("Status");
        columnDepartureStatus.setCellValueFactory(new PropertyValueFactory<Flight, FlightStatus>("flightStatus"));

        flightTableView1.setItems(flightObservableList1);
        flightTableView1.getColumns().addAll(columnDepartureDateTime,columnDepartureFlightNumber,
                columnDepartureCity,columnDepartureTerminal,columnDepartureGate,columnDepartureStatus);

        flightTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Flight>() {
            @Override
            public void changed(ObservableValue<? extends Flight> observable, Flight oldValue, Flight newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        flightTableView1.getSelectionModel().clearSelection();
                    }
                });
            }
        });
        flightTableView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Flight>() {
            @Override
            public void changed(ObservableValue<? extends Flight> observable, Flight oldValue, Flight newValue) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        flightTableView.getSelectionModel().clearSelection();
                    }
                });
            }
        });

        //Flight search

        TextField searchFlightField = new TextField();

        Button cancelFlightSearch = new Button("Cancel Search");
        cancelFlightSearch.setVisible(false);

        ObservableList observableListFlights = FXCollections.observableArrayList("by Flight Number", "by Gate", "by City");
        ChoiceBox searchFlightBox = new ChoiceBox(observableListFlights);
        searchFlightBox.getSelectionModel().selectFirst();

        searchFlightField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (searchFlightBox.getSelectionModel().getSelectedIndex() == 0){
                    if (newValue.length()>7) {
                        String replace = searchFlightField.getText().substring(0, 7);
                        searchFlightField.setText(replace);
                    }
                }else if (searchFlightBox.getSelectionModel().getSelectedIndex() == 1){
                    if (newValue.length()>3) {
                        String replace = searchFlightField.getText().substring(0, 3);
                        searchFlightField.setText(replace);
                    }
                } else if (searchFlightBox.getSelectionModel().getSelectedIndex() == 2){
                    if (newValue.length()>16) {
                        String replace = searchFlightField.getText().substring(0, 16);
                        searchFlightField.setText(replace);
                    }
                }
            }
        });

        ObservableList<Flight> resultObsFlightList = FXCollections.observableArrayList();
        ObservableList<Flight> resultObsFlightList1 = FXCollections.observableArrayList();

        Button searchFlight = new Button("Search");
        searchFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resultObsFlightList.addAll(DataBaseRequests.getSearchFlight("ARRIVAL",searchFlightField,searchFlightBox));
                resultObsFlightList1.addAll(DataBaseRequests.getSearchFlight("DEPARTURE",searchFlightField,searchFlightBox));
                flightTableView.setItems(resultObsFlightList);
                flightTableView1.setItems(resultObsFlightList1);
                cancelFlightSearch.setVisible(true);
            }
        });
        cancelFlightSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                flightTableView.setItems(flightObservableList);
                flightTableView1.setItems(flightObservableList1);
                cancelFlightSearch.setVisible(false);
                searchFlightField.clear();
            }
        });

        //add/delete/edit flight button
        FlightsAddEditWindow window = new FlightsAddEditWindow();

        Button deleteFlightButton = new Button("Delete Selected");
        Button editFlightButton = new Button("Edit Selected");
        Button addFlightButton = new Button("Add New");

        editFlightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    window.start(window.flightsAddEdit);
                    window.clearAllFields();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(!flightTableView.getSelectionModel().isEmpty()) {
                    AddDeleteUpdate.editSelectedFlight(flightTableView, window.flightTypeComboBox, window.flightStatusComboBox,
                            window.textFlightNumber, window.textCity, window.textTerminal, window.textGate, window.textYear, window.textMonth,
                            window.textDay, window.textHours, window.textMinutes);
                } else if(!flightTableView1.getSelectionModel().isEmpty()) {
                    AddDeleteUpdate.editSelectedFlight(flightTableView1, window.flightTypeComboBox, window.flightStatusComboBox,
                            window.textFlightNumber, window.textCity, window.textTerminal, window.textGate, window.textYear, window.textMonth,
                            window.textDay, window.textHours, window.textMinutes);
                }
                window.editFlight.setVisible(true);
            }
        });
        addFlightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    window.start(window.flightsAddEdit);
                    window.clearAllFields();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                window.addFlight.setVisible(true);
            }
        });
        /*AddDeleteUpdate.deleteTicket(ticketTableView);
                ticketTableView.getSelectionModel().clearSelection();
                if (ticketTableView.getItems() == resultObsTicketList) {
                    resultObsTicketList.removeAll(resultObsTicketList);
                    resultObsTicketList.addAll(DataBaseRequests.getSearchTicket(searchTicketField, searchTicketBox));
                }
                ticketObservableList.removeAll(ticketObservableList);
                ticketObservableList.addAll(DataBaseRequests.getTickets());
                */

        deleteFlightButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!flightTableView.getSelectionModel().isEmpty()) {
                    AddDeleteUpdate.deleteFlight(flightTableView);
                    if (flightTableView.getItems() == resultObsFlightList){
                        resultObsFlightList.removeAll(resultObsFlightList);
                        resultObsFlightList.addAll(DataBaseRequests.getSearchFlight("ARRIVAL",searchFlightField,searchFlightBox));
                    }
                    flightObservableList.removeAll(flightObservableList);
                    flightObservableList.addAll(DataBaseRequests.getFlightByStatus("ARRIVAL"));
                } else if(!flightTableView1.getSelectionModel().isEmpty()) {
                    AddDeleteUpdate.deleteFlight(flightTableView1);
                    if (flightTableView1.getItems() == resultObsFlightList1){
                        resultObsFlightList1.removeAll(resultObsFlightList1);
                        resultObsFlightList1.addAll(DataBaseRequests.getSearchFlight("DEPARTURE",searchFlightField,searchFlightBox));
                    }
                    flightObservableList1.removeAll(flightObservableList1);
                    flightObservableList1.addAll(DataBaseRequests.getFlightByStatus("DEPARTURE"));
                }
            }
        });


        //Flights Tab compilation
        Label labelArrivals = new Label("Arrivals");
        labelArrivals.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label labelDepartures = new Label("Departures");
        labelDepartures.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        VBox vBoxArrivals = new VBox(10,labelArrivals, flightTableView);
        VBox vBoxDepartures = new VBox(10,labelDepartures, flightTableView1);
        vBoxArrivals.setAlignment(Pos.CENTER);vBoxDepartures.setAlignment(Pos.CENTER);
        HBox hBoxFlightTables = new HBox(10, vBoxDepartures, vBoxArrivals);
        hBoxFlightTables.setPadding(new Insets(5,5,0,5));
        HBox hBoxFlightBar = new HBox(5, searchFlight, searchFlightBox, searchFlightField, cancelFlightSearch);
        hBoxFlightBar.setPadding(new Insets(5,5,0,5));
        HBox hBoxFlightBottom = new HBox(10, addFlightButton, editFlightButton, deleteFlightButton);
        hBoxFlightBottom.setPadding(new Insets(0,5,0,5));
        VBox vBoxFlights = new VBox(10, hBoxFlightBar, hBoxFlightTables, hBoxFlightBottom);

        //-----------------------------------------------------------------------------------------------------------------------
        //Passengers table
        TableView<Passenger> passengerTableView = new TableView<>();
        ObservableList<Passenger> passengerObservableList = FXCollections.observableArrayList(DataBaseRequests.getPassengers());
        passengerTableView.setPrefSize(672,360);

        TableColumn columnPassengerName = new TableColumn("First Name");
        columnPassengerName.setCellValueFactory(new PropertyValueFactory<Passenger, String>("firstName"));
        columnPassengerName.setResizable(false);
        TableColumn columnPassengerLastName = new TableColumn("Last Name");
        columnPassengerLastName.setCellValueFactory(new PropertyValueFactory<Passenger, String>("secondName"));
        columnPassengerLastName.setResizable(false);
        TableColumn columnPassengerSex = new TableColumn("Sex");
        columnPassengerSex.setCellValueFactory(new PropertyValueFactory<Passenger, Sex>("sex"));
        columnPassengerSex.setResizable(false);
        TableColumn columnPassengerNationality = new TableColumn("Nationality");
        columnPassengerNationality.setCellValueFactory(new PropertyValueFactory<Passenger, String>("nationality"));
        columnPassengerNationality.setResizable(false);
        TableColumn columnPassengerPassport = new TableColumn("Passport");
        columnPassengerPassport.setCellValueFactory(new PropertyValueFactory<Passenger, String>("passport"));
        columnPassengerPassport.setResizable(false);
        TableColumn columnPassengerDob = new TableColumn("Date of Birth");
        columnPassengerDob.setCellValueFactory(new PropertyValueFactory<Passenger, Date>("dateOfBirth"));
        columnPassengerDob.setResizable(false);
        TableColumn columnPassengerTicketType = new TableColumn("Ticket Type");
        columnPassengerTicketType.setCellValueFactory(new PropertyValueFactory<Passenger, String>("ticketType"));
        columnPassengerTicketType.setResizable(false);
        TableColumn columnPassengerFlightNum = new TableColumn("Flight Number");
        columnPassengerFlightNum.setCellValueFactory(new PropertyValueFactory<Passenger, String>("flightNumber"));

        passengerTableView.setItems(passengerObservableList);
        passengerTableView.getColumns().addAll(columnPassengerName,columnPassengerLastName,columnPassengerSex,
                columnPassengerNationality,columnPassengerPassport,columnPassengerDob,columnPassengerTicketType,columnPassengerFlightNum);

        //add/edit passenger window
        Label errorFirstName = new Label("Incorrect \nInput!");
        errorFirstName.setFont(Font.font("Arial", 11));
        errorFirstName.setTextFill(Paint.valueOf("RED"));
        errorFirstName.setVisible(false);

        Label errorLastName = new Label("Incorrect \nInput!");
        errorLastName.setFont(Font.font("Arial", 11));
        errorLastName.setTextFill(Paint.valueOf("RED"));
        errorLastName.setVisible(false);

        Label errorNationality = new Label("Incorrect \nInput!");
        errorNationality.setFont(Font.font("Arial", 11));
        errorNationality.setTextFill(Paint.valueOf("RED"));
        errorNationality.setVisible(false);

        Label errorPassport = new Label("Incorrect \nInput!");
        errorPassport.setFont(Font.font("Arial", 11));
        errorPassport.setTextFill(Paint.valueOf("RED"));
        errorPassport.setVisible(false);

        Label errorDateValidation = new Label("Invalid \nDate!");
        errorDateValidation.setFont(Font.font("Arial", 11));
        errorDateValidation.setTextFill(Paint.valueOf("RED"));
        errorDateValidation.setVisible(false);

        Label errorPassengerFlightNum = new Label("Incorrect \nInput!");
        errorPassengerFlightNum.setFont(Font.font("Arial", 11));
        errorPassengerFlightNum.setTextFill(Paint.valueOf("RED"));
        errorPassengerFlightNum.setVisible(false);

        Label errorPassenger = new Label("Invalid \nInput!");
        errorPassenger.setTooltip(new Tooltip("Correct all fields!"));
        errorPassenger.setFont(Font.font("Arial", 11));
        errorPassenger.setTextFill(Paint.valueOf("RED"));
        errorPassenger.setVisible(false);

        VBox vBoxPassengerAddUpd = new VBox(10);
        vBoxPassengerAddUpd.setVisible(false);

        Label labelFirstName = new Label("First Name");
        TextField textFirstName = new TextField();
        HBox firstName = new HBox(10, labelFirstName, textFirstName,errorFirstName);

        Label labelLastName = new Label("Last Name");
        TextField textLastName = new TextField();
        HBox lastName = new HBox(10, labelLastName, textLastName,errorLastName);

        Label labelSex = new Label("Sex");
        ObservableList<Sex> sexObservableList = FXCollections.observableArrayList(Sex.values());
        ComboBox<Sex> sexComboBox = new ComboBox<>(sexObservableList);
        HBox sex = new HBox(10, labelSex,sexComboBox);

        Label labelNationality = new Label("Nationality");
        TextField textNationality = new TextField();
        HBox nationality = new HBox(10, labelNationality,textNationality,errorNationality);

        Label labelPassport = new Label("Passport");
        TextField textPassport = new TextField();
        HBox passport = new HBox(10, labelPassport,textPassport,errorPassport);

        Label labelDob = new Label("Date Of Birth");
        TextField textYear = new TextField();
        textYear.setPrefWidth(50);
        TextField textMonth = new TextField();
        textMonth.setPrefWidth(30);
        TextField textDay = new TextField();
        textDay.setPrefWidth(30);
        HBox dateOfBirth = new HBox(10, labelDob,textYear,textMonth,textDay,errorDateValidation);

        Label labelTicketType = new Label("Ticket Type");
        ObservableList<TicketType> ticketTypeObservableList = FXCollections.observableArrayList(TicketType.values());
        ComboBox<TicketType> ticketTypeComboBox = new ComboBox<>(ticketTypeObservableList);
        HBox ticketTypePassenger = new HBox(10, labelTicketType, ticketTypeComboBox);

        Label labelFlightPassenger = new Label("Flight Number");
        TextField textFlightPassenger = new TextField();
        HBox flightNumberPassenger = new HBox(10, labelFlightPassenger, textFlightPassenger,errorPassengerFlightNum);

        TextFieldsControl.textLimit(textFirstName,16);
        TextFieldsControl.textCheck(textFirstName,"[A-Z][a-z]+",errorFirstName);
        TextFieldsControl.textLimit(textLastName,16);
        TextFieldsControl.textCheck(textLastName,"[A-Z][a-z]+",errorLastName);
        TextFieldsControl.textLimit(textNationality,16);
        TextFieldsControl.textCheck(textNationality,"([A-Z][a-z]+)|([A-Z]+)",errorNationality);
        TextFieldsControl.textLimit(textPassport,8);
        TextFieldsControl.textCheck(textPassport,"[A-Z]{2}[0-9]{5,6}",errorPassport);
        TextFieldsControl.textLimit(textYear,4);
        TextFieldsControl.textCheck(textYear,"((19[2-9][0-9])|(201[0-5]|200[0-9]))",errorDateValidation);
        TextFieldsControl.textLimit(textMonth,2);
        TextFieldsControl.textCheck(textMonth,"(0[1-9]|1[012])",errorDateValidation);
        TextFieldsControl.textLimit(textDay,2);
        TextFieldsControl.textCheck(textDay,"[12][0-9]|3[01]|0?[1-9]",errorDateValidation);
        TextFieldsControl.textLimit(textFlightPassenger,7);
        TextFieldsControl.textCheck(textFlightPassenger,"[A-Z]{2,3}[0-9]{3,4}", errorPassengerFlightNum);

        Button addPassenger = new Button("Add Passenger");
        addPassenger.setVisible(false);
        addPassenger.disableProperty().bind(textFirstName.textProperty().isEmpty().or(textLastName.textProperty().isEmpty())
                .or(sexComboBox.valueProperty().isNull()).or(textNationality.textProperty().isEmpty())
                .or(textPassport.textProperty().isEmpty()).or(textYear.textProperty().isEmpty()).or(textMonth.textProperty().isEmpty())
                .or(textDay.textProperty().isEmpty()).or(ticketTypeComboBox.valueProperty().isNull())
                .or(textFlightPassenger.textProperty().isEmpty()));
        Button editPassenger = new Button("Apply Edit");
        editPassenger.setVisible(false);
        editPassenger.disableProperty().bind(textFirstName.textProperty().isEmpty().or(textLastName.textProperty().isEmpty())
                .or(sexComboBox.valueProperty().isNull()).or(textNationality.textProperty().isEmpty())
                .or(textPassport.textProperty().isEmpty()).or(textYear.textProperty().isEmpty()).or(textMonth.textProperty().isEmpty())
                .or(textDay.textProperty().isEmpty()).or(ticketTypeComboBox.valueProperty().isNull())
                .or(textFlightPassenger.textProperty().isEmpty()));
        Button cancelPassenger = new Button("Cancel");
        HBox addEditPassenger = new HBox(15,addPassenger,editPassenger,cancelPassenger,errorPassenger);

        editPassenger.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ddmmyyyy = textDay.getText()+"/" + textMonth.getText()+"/" + textYear.getText();
                if(!TextFieldsControl.checkForErrorsPassenger(errorFirstName,errorLastName,errorNationality,errorPassport,
                        errorDateValidation, errorPassengerFlightNum)){
                    errorPassenger.setVisible(true);
                } else if (!TextFieldsControl.validateDate(ddmmyyyy)) {
                    errorDateValidation.setVisible(true);
                } else {
                    errorPassenger.setVisible(false);
                    errorDateValidation.setVisible(false);
                    AddDeleteUpdate.applyEditPassenger(passengerTableView, textFirstName, textLastName, sexComboBox, textNationality, textPassport,
                            textYear, textMonth, textDay, ticketTypeComboBox, textFlightPassenger);
                    textFirstName.clear();
                    textLastName.clear();
                    sexComboBox.getSelectionModel().clearSelection();
                    textNationality.clear();
                    textPassport.clear();
                    textYear.clear();
                    textMonth.clear();
                    textDay.clear();
                    ticketTypeComboBox.getSelectionModel().clearSelection();
                    textFlightPassenger.clear();
                    vBoxPassengerAddUpd.setVisible(false);
                    passengerObservableList.removeAll(passengerObservableList);
                    passengerObservableList.addAll(DataBaseRequests.getPassengers());
                }
            }
        });

        addPassenger.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ddmmyyyy = textDay.getText()+"/" + textMonth.getText()+"/" + textYear.getText();
                if(!TextFieldsControl.checkForErrorsPassenger(errorFirstName,errorLastName,errorNationality,errorPassport,
                        errorDateValidation, errorPassengerFlightNum)){
                    errorPassenger.setVisible(true);
                } else if (!TextFieldsControl.validateDate(ddmmyyyy)) {
                    errorDateValidation.setVisible(true);
                } else {
                    errorPassenger.setVisible(false);
                    String dateOfBirth = textYear.getText() + textMonth.getText() + textDay.getText();
                    AddDeleteUpdate.addPassenger(textFirstName, textLastName, sexComboBox, textNationality, textPassport, dateOfBirth,
                            ticketTypeComboBox, textFlightPassenger);
                    textFirstName.clear();
                    textLastName.clear();
                    sexComboBox.getSelectionModel().clearSelection();
                    textNationality.clear();
                    textPassport.clear();
                    textYear.clear();
                    textMonth.clear();
                    textDay.clear();
                    ticketTypeComboBox.getSelectionModel().clearSelection();
                    textFlightPassenger.clear();
                    vBoxPassengerAddUpd.setVisible(false);
                    passengerObservableList.removeAll(passengerObservableList);
                    passengerObservableList.addAll(DataBaseRequests.getPassengers());
                }
            }
        });
        cancelPassenger.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textFirstName.clear();textLastName.clear();sexComboBox.getSelectionModel().clearSelection();textNationality.clear();
                textPassport.clear();textYear.clear();textMonth.clear();textDay.clear();
                ticketTypeComboBox.getSelectionModel().clearSelection();textFlightPassenger.clear();
                vBoxPassengerAddUpd.setVisible(false);
            }
        });

        vBoxPassengerAddUpd.getChildren().addAll(firstName,lastName,sex,nationality,passport,dateOfBirth,ticketTypePassenger,
                flightNumberPassenger, addEditPassenger);


        //Passenger search

        TextField searchPassengerField = new TextField();

        Button cancelPassengerSearch = new Button("Cancel Search");
        cancelPassengerSearch.setVisible(false);

        ObservableList observableListPassengers = FXCollections.observableArrayList("by First & Last Name", "by Flight Number",
                "by Passport");
        ChoiceBox searchPassengerBox = new ChoiceBox(observableListPassengers);
        searchPassengerBox.getSelectionModel().selectFirst();

        searchPassengerField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (searchPassengerBox.getSelectionModel().getSelectedIndex() == 0){
                    if (newValue.length()>33) {
                        String replace = searchPassengerField.getText().substring(0, 33);
                        searchPassengerField.setText(replace);
                    }
                }else if (searchPassengerBox.getSelectionModel().getSelectedIndex() == 1){
                    if (newValue.length()>7) {
                        String replace = searchPassengerField.getText().substring(0, 7);
                        searchPassengerField.setText(replace);
                    }
                } else if (searchPassengerBox.getSelectionModel().getSelectedIndex() == 2){
                    if (newValue.length()>8) {
                        String replace = searchPassengerField.getText().substring(0, 8);
                        searchPassengerField.setText(replace);
                    }
                }
            }
        });

        searchPassengerBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                DataBaseRequests.choiceBoxPassengers(newValue.intValue());
            }
        });

        ObservableList<Passenger> resultObsPassengerList = FXCollections.observableArrayList();

        Button searchPassenger = new Button("Search");
        searchPassenger.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resultObsPassengerList.addAll(DataBaseRequests.getSearchPassenger(searchPassengerField,searchPassengerBox));
                passengerTableView.setItems(resultObsPassengerList);
                cancelPassengerSearch.setVisible(true);
            }
        });
        cancelPassengerSearch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                passengerTableView.setItems(passengerObservableList);
                cancelPassengerSearch.setVisible(false);
                searchPassengerField.clear();
            }
        });

        //add/delete/edit ticket button
        Button deletePassengerButton = new Button("Delete Selected");
        Button addPassengerButton = new Button("Add New");
        Button editPassengerButton = new Button("Edit Selected");

        addPassengerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vBoxPassengerAddUpd.setVisible(true);
                addPassenger.setVisible(true);
                editPassenger.setVisible(false);
            }
        });
        editPassengerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vBoxPassengerAddUpd.setVisible(true);
                editPassenger.setVisible(true);
                addPassenger.setVisible(false);
                AddDeleteUpdate.editSelectedPassenger(passengerTableView,textFirstName,textLastName,sexComboBox,textNationality,textPassport,
                        textYear,textMonth,textDay,ticketTypeComboBox,textFlightPassenger);
            }
        });
        deletePassengerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AddDeleteUpdate.deletePassenger(passengerTableView);
                passengerTableView.getSelectionModel().clearSelection();
                if (passengerTableView.getItems() == resultObsPassengerList){
                    resultObsPassengerList.removeAll(resultObsPassengerList);
                    resultObsPassengerList.addAll(DataBaseRequests.getSearchPassenger(searchPassengerField,searchPassengerBox));
                }
                passengerObservableList.removeAll(passengerObservableList);
                passengerObservableList.addAll(DataBaseRequests.getPassengers());
            }
        });

        //Passenger tab compilation
        HBox hBoxPassengerTables = new HBox(10, passengerTableView, vBoxPassengerAddUpd);
        hBoxPassengerTables.setPadding(new Insets(5,5,0,5));
        HBox hBoxPassengerBar = new HBox(5, searchPassenger, searchPassengerBox, searchPassengerField, cancelPassengerSearch);
        hBoxPassengerBar.setPadding(new Insets(5,5,0,5));
        HBox hBoxPassengerBottom = new HBox(10, addPassengerButton, editPassengerButton, deletePassengerButton);
        hBoxPassengerBottom.setPadding(new Insets(5,5,0,5));
        VBox vBoxPassengers = new VBox(10, hBoxPassengerBar, hBoxPassengerTables, hBoxPassengerBottom);

        //Tabs compilation
        TabPane tabPane = new TabPane();
        Tab flights = new Tab();
        flights.setText("Flights");
        flights.setContent(vBoxFlights);
        flights.setClosable(false);

        Tab passengers = new Tab();
        passengers.setText("Passengers");
        passengers.setContent(vBoxPassengers);
        passengers.setClosable(false);

        Tab tickets = new Tab();
        tickets.setText("Tickets");
        tickets.setContent(vBoxTickets);
        tickets.setClosable(false);

        tabPane.getTabs().addAll(flights,tickets,passengers);

        Scene scene = new Scene(tabPane, 994, 480);
        primaryStage.setTitle("Airport Application by Ross");
        primaryStage.getIcons().addAll(new Image("file:plane.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
