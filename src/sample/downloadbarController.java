package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class downloadbarController {
    public ProgressBar bar;
    public Text text;

    public void install(){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("updatebar.fxml"));
            Stage stage = (Stage) bar.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch (Exception e){}
    }
}
