package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openqa.selenium.JavascriptExecutor;

import java.io.File;
import java.net.URLDecoder;

public class updateController {
    public Button okButton;
    public Button cancelButton;

    public void ok(){
        try {
            Controller c = Main.loader.getController();
            c.killBrowser();
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
            String dir;
            if (System.getProperty("os.name").contains("Mac")) dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "Updater.jar");
            else dir = (System.getProperty("user.dir") + "\\Updater.jar");
            String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            String[] args = new String[] {javaBin, "-jar", dir};
            new ProcessBuilder(args).start();
            System.exit(0);
//            Controller c = Main.loader.getController();
//            String url = "https://github.com/savageRex/EzBimay/releases";
//            ((JavascriptExecutor)c.driver).executeScript("window.open('"+url+"','_blank');");
//            Stage stage = (Stage) okButton.getScene().getWindow();
//            stage.close();
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    public void cancel(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
