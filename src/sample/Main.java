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
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main extends Application {
    public static Stage primaryStage = null;
    public static FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Main.primaryStage = primaryStage;
        primaryStage.getProperties().put("hostServices", this.getHostServices());
        String fxml;
        File data = new File("kext");
        File password = new File("plugin");
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
            primaryStage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            loader = new FXMLLoader(getClass().getResource("error.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    }

    public static void main(String[] args) { launch(args); }
}

// to do list:
// 1. firefox support
// 2. more secure
// 3.