package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;

public class updateController {
    public Button okButton;
    public Button cancelButton;

    public void ok(){
        try {
            File f = new File(System.getProperty("java.class.path"));
            File dir = f.getAbsoluteFile().getParentFile();
            String[] args = new String[] {"java", "-jar", dir.toString() + "/EzBimay_Updater.jar"};
            new ProcessBuilder(args).start();
            System.exit(0);

//            Controller c = Main.loader.getController();
//            String url = "https://github.com/savageRex/EzBimay/releases";
//            ((JavascriptExecutor)c.driver).executeScript("window.open('"+url+"','_blank');");
//            Stage stage = (Stage) okButton.getScene().getWindow();
//            stage.close();
        }
        catch (Exception e){}
    }

    public void cancel(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
