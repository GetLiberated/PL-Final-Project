package sample;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("downloadbar.fxml"));
            Parent root = loader.load();
            primaryStage.setTitle("EzBimay Updater");
            primaryStage.setScene(new Scene(root));
            primaryStage.getIcons().add( new Image( Main.class.getResourceAsStream( "logo.png" )));
            primaryStage.setResizable(false);
            primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}