package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main extends Application {
    static Stage primaryStage = null;
    static FXMLLoader loader;
    String fxml;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        primaryStage.getProperties().put("hostServices", this.getHostServices());

        File data, password;
        if (System.getProperty("os.name").contains("Windows 10")) {
            data = new File("kext");
            password = new File("plugin");
        }
        else {
            String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
            data = new File(dir + "kext");
            password = new File(dir + "plugin");
        }
        if (data.exists()) {
            if (password.exists()) fxml = "password.fxml";
            else fxml = "app.fxml";
        }
        else fxml = "login.fxml";
        try {
            loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
//            primaryStage.setTitle("EzBimay v" + version);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add( new Image( Main.class.getResourceAsStream( "logo.png" )));
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(event -> {
                if (fxml.equals("app.fxml")) {
                    Controller c = loader.getController();
                    c.close();
                }
                else if (fxml.equals("login.fxml")) {
                    loginController lc = loader.getController();
                    lc.close();
                }
            });
            primaryStage.show();
        }
        catch (IOException e) {
            File file = new File("error.log");
            PrintStream ps = new PrintStream(file);
            e.printStackTrace(ps);
            loader = new FXMLLoader(getClass().getResource("error.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(event -> {
                if (fxml.equals("app.fxml")) {
                    Controller c = loader.getController();
                    c.close();
                }
                else if (fxml.equals("login.fxml")) {
                    loginController lc = loader.getController();
                    lc.close();
                }
            });
            primaryStage.show();
        }
    }

    public static void main(String[] args) { launch(args); }
}

// To do:
// 1.
// 2. one webdriver wait
// 3. save on another file on windows
// 4. automatic error report