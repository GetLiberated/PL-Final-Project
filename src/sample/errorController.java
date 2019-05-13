package sample;


import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.openqa.selenium.WebDriver;

public class errorController {
    public Button okButton;
    public WebDriver driver;

    public void close(){
        if (driver != null) driver.quit();
        System.exit(0);
    }
}
