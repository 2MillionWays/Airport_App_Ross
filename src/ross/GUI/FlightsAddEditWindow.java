package ross.GUI;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ross.DB.AddDeleteUpdate;
import ross.DB.DataBaseRequests;
import ross.TextFieldsControl;
import ross.enums.FlightStatus;
import ross.enums.FlightType;

import java.util.logging.ErrorManager;
import java.util.regex.Matcher;

public class FlightsAddEditWindow extends Application {
    Stage flightsAddEdit = new Stage();
    GridPane gridPane;
    public static Button addFlight = new Button("Add New Flight");
    public static Button editFlight = new Button("Apply Edit");
    public static Button cancelFlight = new Button("Cancel");
    ComboBox<FlightType> flightTypeComboBox = new ComboBox<>();
    ComboBox<FlightStatus> flightStatusComboBox = new ComboBox<>();
    TextField textFlightNumber = new TextField();
    TextField textCity = new TextField();
    TextField textTerminal = new TextField();
    TextField textGate = new TextField();
    TextField textYear = new TextField();
    TextField textMonth = new TextField();
    TextField textDay = new TextField();
    TextField textHours = new TextField();
    TextField textMinutes = new TextField();


    @Override
    public void start(Stage primaryStage) throws Exception {
        gridPane = new GridPane();

        Label errorFlightNumber = new Label("Incorrect \nInput!");
        errorFlightNumber.setFont(Font.font("Arial", 11));
        errorFlightNumber.setTextFill(Paint.valueOf("RED"));
        errorFlightNumber.setVisible(false);

        Label errorCity = new Label("Incorrect \nInput!");
        errorCity.setFont(Font.font("Arial", 11));
        errorCity.setTextFill(Paint.valueOf("RED"));
        errorCity.setVisible(false);

        Label errorTerminal = new Label("Incorrect \nInput!");
        errorTerminal.setFont(Font.font("Arial", 11));
        errorTerminal.setTextFill(Paint.valueOf("RED"));
        errorTerminal.setVisible(false);

        Label errorGate = new Label("Incorrect \nInput!");
        errorGate.setFont(Font.font("Arial", 11));
        errorGate.setTextFill(Paint.valueOf("RED"));
        errorGate.setVisible(false);

        Label errorDateValidation = new Label("Invalid \nDate!");
        errorDateValidation.setFont(Font.font("Arial", 11));
        errorDateValidation.setTextFill(Paint.valueOf("RED"));
        errorDateValidation.setVisible(false);

        Label errorTime = new Label("Invalid \nTime!");
        errorTime.setFont(Font.font("Arial", 11));
        errorTime.setTextFill(Paint.valueOf("RED"));
        errorTime.setVisible(false);

        Label errorInvalidInput = new Label("Invalid \nInput!");
        errorInvalidInput.setFont(Font.font("Arial", 11));
        errorInvalidInput.setTextFill(Paint.valueOf("RED"));
        errorInvalidInput.setVisible(false);

        TextFieldsControl.textLimit(textFlightNumber,7);
        TextFieldsControl.textCheck(textFlightNumber,"[A-Z]{2,3}[0-9]{3,4}", errorFlightNumber);
        TextFieldsControl.textLimit(textCity,16);
        TextFieldsControl.textCheck(textCity,"[A-Z][a-z]+", errorCity);
        TextFieldsControl.textLimit(textTerminal,1);
        TextFieldsControl.textCheck(textTerminal,"[A-Z]", errorTerminal);
        TextFieldsControl.textLimit(textGate,3);
        TextFieldsControl.textCheck(textGate,"[A-Z][0-9]{1,2}", errorGate);
        TextFieldsControl.textLimit(textYear,4);
        TextFieldsControl.textCheck(textYear,"(201[6-9]|2020)", errorDateValidation);
        TextFieldsControl.textLimit(textMonth,2);
        TextFieldsControl.textCheck(textMonth,"(0[1-9]|1[012])", errorDateValidation);
        TextFieldsControl.textLimit(textDay,2);
        TextFieldsControl.textCheck(textDay,"[12][0-9]|3[01]|0?[1-9]",errorDateValidation);
        TextFieldsControl.textLimit(textHours,2);
        TextFieldsControl.textCheck(textHours,"(0[0-9]|1[0-9]|2[0-3])", errorTime);
        TextFieldsControl.textLimit(textMinutes,2);
        TextFieldsControl.textCheck(textMinutes,"(0[0-9]|[1-5][0-9])",errorTime);

        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label labelFlightType = new Label("Flight Type");
        ObservableList<FlightType> flightTypeObservableList = FXCollections.observableArrayList(FlightType.values());
        flightTypeComboBox.setItems(flightTypeObservableList);

        Label labelFlightStatus = new Label("Flight Status");
        ObservableList<FlightStatus> flightStatusObservableList = FXCollections.observableArrayList(FlightStatus.values());
        flightStatusComboBox.setItems(flightStatusObservableList);
        ObservableList<FlightStatus> ArrivalsObservableList = FXCollections.observableArrayList(FlightStatus.ARRIVED, FlightStatus.CANCELED,
                FlightStatus.DELAYED, FlightStatus.EXPECTED_AT, FlightStatus.IN_FLIGHT, FlightStatus.UNKNOWN);
        ObservableList<FlightStatus> DeparturesObservableList = FXCollections.observableArrayList(FlightStatus.CHECK_IN,
                FlightStatus.GATE_CLOSED, FlightStatus.DEPARTED_AT, FlightStatus.UNKNOWN, FlightStatus.CANCELED, FlightStatus.DELAYED);

        Label labelFlightNumber = new Label("Flight Number");
        Label labelCity = new Label("From/Destination");
        Label labelTerminal = new Label("Terminal");
        Label labelGate = new Label("Gate");
        flightTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<FlightType>() {
            @Override
            public void changed(ObservableValue<? extends FlightType> observable, FlightType oldValue, FlightType newValue) {
                if (newValue == FlightType.ARRIVAL) {
                    flightStatusComboBox.setItems(ArrivalsObservableList);
                    textGate.setDisable(true);
                    textGate.clear();
                } else if (newValue == FlightType.DEPARTURE) {
                    flightStatusComboBox.setItems(DeparturesObservableList);
                    textGate.setDisable(false);
                }
            }
        });

        Label labelFlightDate = new Label("Flight Date");
        textYear.setPrefWidth(50);
        textMonth.setPrefWidth(32);
        textDay.setPrefWidth(32);
        HBox yearMonthDay = new HBox(10, textYear, textMonth, textDay);

        Label labelFlightTime = new Label("Flight Time");
        Label time = new Label(":");
        textHours.setPrefWidth(32);
        textMinutes.setPrefWidth(32);
        HBox hoursMinutes = new HBox(5, textHours, time, textMinutes, errorTime);


        addFlight.setVisible(false);
        addFlight.disableProperty().bind(
                flightTypeComboBox.valueProperty().isNull().or(flightStatusComboBox.valueProperty().isNull())
                        .or(textFlightNumber.textProperty().isEmpty()).or(textCity.textProperty().isEmpty())
                        .or(textTerminal.textProperty().isEmpty()).or(textYear.textProperty().isEmpty())
                        .or(textMonth.textProperty().isEmpty()).or(textDay.textProperty().isEmpty())
                        .or(textHours.textProperty().isEmpty()).or(textMinutes.textProperty().isEmpty()));

        addFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ddmmyyyy = textDay.getText()+"/" + textMonth.getText()+"/" + textYear.getText();
                if (!TextFieldsControl.checkForErrorsFlights(errorFlightNumber,errorCity,errorTerminal,errorGate,errorDateValidation,errorTime)){
                    errorInvalidInput.setVisible(true);
                } else if (!TextFieldsControl.validateDate(ddmmyyyy)) {
                    errorDateValidation.setVisible(true);
                } else {
                    errorInvalidInput.setVisible(false);
                    errorDateValidation.setVisible(false);
                    String dateTime = textYear.getText() + textMonth.getText() + textDay.getText() + textHours.getText() + textMinutes.getText();
                    AddDeleteUpdate.addFlight(flightTypeComboBox, flightStatusComboBox, textFlightNumber, textCity, textTerminal, textGate, dateTime);
                    Main.flightObservableList.removeAll(Main.flightObservableList);
                    Main.flightObservableList.addAll(DataBaseRequests.getFlightByStatus("ARRIVAL"));
                    Main.flightObservableList1.removeAll(Main.flightObservableList1);
                    Main.flightObservableList1.addAll(DataBaseRequests.getFlightByStatus("DEPARTURE"));
                    primaryStage.close();
                }
            }
        });
        editFlight.setVisible(false);
        editFlight.disableProperty().bind(
                flightTypeComboBox.valueProperty().isNull().or(flightStatusComboBox.valueProperty().isNull())
                        .or(textFlightNumber.textProperty().isEmpty()).or(textCity.textProperty().isEmpty())
                        .or(textTerminal.textProperty().isEmpty()).or(textYear.textProperty().isEmpty())
                        .or(textMonth.textProperty().isEmpty()).or(textDay.textProperty().isEmpty())
                        .or(textHours.textProperty().isEmpty()).or(textMinutes.textProperty().isEmpty()));
        editFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String ddmmyyyy = textDay.getText()+"/" + textMonth.getText()+"/" + textYear.getText();
                if (!TextFieldsControl.checkForErrorsFlights(errorFlightNumber,errorCity,errorTerminal,errorGate,errorDateValidation,errorTime)) {
                    errorInvalidInput.setVisible(true);
                } else if (!TextFieldsControl.validateDate(ddmmyyyy)) {
                    errorDateValidation.setVisible(true);
                } else {
                    errorInvalidInput.setVisible(false);
                    errorDateValidation.setVisible(false);
                    String dateTime = textYear.getText() + textMonth.getText() + textDay.getText() + textHours.getText() + textMinutes.getText();
                    if (!Main.flightTableView.getSelectionModel().isEmpty()) {
                        AddDeleteUpdate.applyEditFlight(Main.flightTableView, flightTypeComboBox, flightStatusComboBox, textFlightNumber,
                                textCity, textTerminal, textGate, dateTime);
                        Main.flightObservableList.removeAll(Main.flightObservableList);
                        Main.flightObservableList.addAll(DataBaseRequests.getFlightByStatus("ARRIVAL"));
                    }
                    if (!Main.flightTableView1.getSelectionModel().isEmpty()) {
                        AddDeleteUpdate.applyEditFlight(Main.flightTableView1, flightTypeComboBox, flightStatusComboBox, textFlightNumber,
                                textCity, textTerminal, textGate, dateTime);
                        Main.flightObservableList1.removeAll(Main.flightObservableList1);
                        Main.flightObservableList1.addAll(DataBaseRequests.getFlightByStatus("DEPARTURE"));
                    }
                    primaryStage.close();
                }
            }
        });
        cancelFlight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });

        Label title = new Label("Add/Edit Flight");
        title.setFont(new Font("Arial", 20));

        gridPane.setPadding(new Insets(0, 0, 0, 15));
        gridPane.add(title, 1, 0);
        gridPane.add(labelFlightType, 0, 1);gridPane.add(flightTypeComboBox, 1, 1);
        gridPane.add(labelFlightStatus, 0, 2);gridPane.add(flightStatusComboBox, 1, 2);
        gridPane.add(labelFlightNumber, 0, 3);gridPane.add(textFlightNumber, 1, 3);gridPane.add(errorFlightNumber, 2, 3);
        gridPane.add(labelCity, 0, 4);gridPane.add(textCity, 1, 4);gridPane.add(errorCity, 2, 4);
        gridPane.add(labelTerminal, 0, 5);gridPane.add(textTerminal, 1, 5);gridPane.add(errorTerminal, 2, 5);
        gridPane.add(labelGate, 0, 6);gridPane.add(textGate, 1, 6);gridPane.add(errorGate, 2, 6);
        gridPane.add(labelFlightDate, 0, 7);gridPane.add(yearMonthDay, 1, 7);gridPane.add(errorDateValidation,2,7);
        gridPane.add(labelFlightTime, 0, 8);gridPane.add(hoursMinutes, 1, 8);gridPane.add(errorInvalidInput,2,8);
        gridPane.add(addFlight, 0, 9);gridPane.add(editFlight, 1, 9);gridPane.add(cancelFlight, 2, 9);

        Scene scene = new Scene(gridPane, 340, 365);
        primaryStage.setTitle("Add/Edit Flight");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void clearAllFields() {
        flightTypeComboBox.getSelectionModel().clearSelection();
        flightStatusComboBox.getSelectionModel().clearSelection();
        textFlightNumber.clear();textCity.clear();textTerminal.clear();textGate.clear();
        textYear.clear();textMonth.clear();textDay.clear();textHours.clear();textMinutes.clear();
    }
}
