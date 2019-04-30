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
    File data = new File("kext");
    File password = new File("plugin");
    FXMLLoader loader;
    static Controller c;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
//        Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
        String fxml;
        if (data.exists()) {
            if (password.exists()) fxml = "password.fxml";
            else fxml = "app.fxml";
        }
        else fxml = "login.fxml";
        try {
            loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            if (!fxml.equals("login.fxml") && !fxml.equals("password.fxml")) c = loader.getController();
//            primaryStage.setTitle("EzBimay v" + c.version);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add( new Image( Main.class.getResourceAsStream( "logo.png" )));
            primaryStage.setResizable(false);
            primaryStage.show();
            if (!fxml.equals("login.fxml") && !fxml.equals("password.fxml")) if (c.updateAvailable){
                root = FXMLLoader.load(getClass().getResource("update.fxml"));
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();
//                updateController uc = loader.getController();
//                if (uc.update){
//                    root = FXMLLoader.load(getClass().getResource("downloadbar.fxml"));
//                    primaryStage.setScene(new Scene(root));
//                    URL url = new URL("https://github.com/odoo/odoo/archive/7.0-web.zip");
//                    Download d = new Download(url);
//                    float downloaded = d.getProgress(), size = d.getSize();
//                    downloadbarController dc = loader.getController();
//                    String downloadSize;
//                    if (size > 1024) {
//                        if (size/1024 > 1024.0) downloadSize = String.format("%.2f MB)", (size/1024)/1024);
//                        else downloadSize = String.format("%.2f KB)", size/1024);
//                    }
//                    else downloadSize = String.format("%f B)", size);
//                    while (d.getStatus() != 2) {
//                        if (downloaded > 1024) {
//                            if (downloaded/1024 > 1024.0) dc.text.setText(String.format("Downloading update (%.2f MB  / " + downloadSize, (downloaded/1024)/1024));
//                            else dc.text.setText(String.format("Downloading update (%.2f KB  / " + downloadSize, downloaded/1024));
//                        }
//                        else dc.text.setText(String.format("Downloading update (%f B  / " + downloadSize, downloaded));
//                        dc.bar.setProgress(downloaded/size);
//                    }
//                    if (downloaded == size) dc.install();
//                }
            }
            new Thread(() -> {
                if (c.chromeversion != null) c.dailyRoutine();
            }).start();
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