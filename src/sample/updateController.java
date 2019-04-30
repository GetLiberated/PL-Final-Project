package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class updateController {
    public Button okButton;
    public Button cancelButton;
    boolean update;

    public void ok(){
        try {
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            update = true;
        }
        catch (Exception e){}
    }

    public void cancel(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
