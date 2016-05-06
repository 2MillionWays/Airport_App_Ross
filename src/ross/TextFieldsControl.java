package ross;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import ross.GUI.FlightsAddEditWindow;

public class TextFieldsControl {


    public static void textLimit(TextField textField, int maxLength) {
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > maxLength) {
                    String replace = textField.getText().substring(0, maxLength);
                    textField.setText(replace);
                }
            }
        });
    }

    public static void textCheck(TextField textField, String regExp, Label label){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()>=1 & !textField.getText().matches(regExp)){
                    label.setVisible(true);
                }else {
                    label.setVisible(false);
                }
            }
        });
    }

    public static void textLimitAndCheck(TextField textField,int min, int max, String regExp, Label label){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > max) {
                    String replace = textField.getText().substring(0, max);
                    textField.setText(replace);
                }
                if(newValue.length()>= min & !textField.getText().matches(regExp)){
                    label.setVisible(true);
                }else {
                    label.setVisible(false);
                }
            }
        });
    }

    public static Boolean checkForErrorsFlights(Label errorNumber, Label errorCity, Label errorTerminal, Label errorGate,
                                                 Label errorYMD, Label errorHM){
        Boolean result = true;
        if (errorNumber.isVisible()||errorCity.isVisible()||errorTerminal.isVisible()||
                errorGate.isVisible()||errorYMD.isVisible()||errorHM.isVisible()){
            result = false;
        }
        return result;
    }

    public static Boolean checkForErrorsPassenger(Label errorName, Label errorLastName, Label errorNationality, Label errorPassport,
                                                Label errorDate, Label errorFlightNumber){
        Boolean result = true;
        if (errorName.isVisible()||errorLastName.isVisible()||errorNationality.isVisible()||
                errorPassport.isVisible()||errorDate.isVisible()||errorFlightNumber.isVisible()){
            result = false;
        }
        return result;
    }

    public static Boolean checkForErrorsTickets(Label errorNumber, Label errorPrice){
        Boolean result = true;
        if (errorNumber.isVisible()||errorPrice.isVisible()){
            result = false;
        }
        return result;
    }

    public static Boolean validateDate(String ddmmyyyy){
        String regExp = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec)))" +
                "\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))" +
                "\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)(?:0?2|(?:Feb))" +
                "\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))" +
                "\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
        Boolean result = ddmmyyyy.matches(regExp);
        return result;
    }

}
