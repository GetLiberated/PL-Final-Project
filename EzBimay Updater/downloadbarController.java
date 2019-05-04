package sample;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class downloadbarController {
    public ProgressBar bar;
    public Text text;

    public void initialize(){
        new Thread(this::run).start();

//        Task test = new Task() {
//            @Override
//            protected Object call() throws Exception {
//                URL url = new URL("https://github.com/savageRex/EzBimay/releases/latest/download/EzBimay.zip");
//                Download d = new Download(url);
//
//                while (d.getStatus() != 2) {
//                    double size = d.getSize();
//                    String downloadSize;
//                    if (size > 1024) {
//                        if (size / 1024 > 1024.0) downloadSize = String.format("%.1f MB)", ((size / 1024) / 1024));
//                        else downloadSize = String.format("%.1f KB)", (size / 1024));
//                    } else downloadSize = String.format("%.1f B)", size);
//                    double downloaded = d.getDownloaded();
//                    if (downloaded > 1024) {
//                        if (downloaded / 1024 > 1024.0)
//                            text.setText(String.format("Downloading update (%.1f MB  / " + downloadSize, ((downloaded / 1024) / 1024)));
//                        else
//                            text.setText(String.format("Downloading update (%.1f KB  / " + downloadSize, (downloaded / 1024)));
//                    } else text.setText(String.format("Downloading update (%.1f B  / " + downloadSize, downloaded));
//                    updateProgress(d.getDownloaded(), d.getSize());
//                }
//                return true;
//            }
//        };
//        bar.progressProperty().bind(test.progressProperty());
//        test.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent event) {
//                System.exit(0);
//            }
//        });
//        new Thread(test).start();
    }

    private void run() {
        try {
            URL url = new URL("https://github.com/savageRex/EzBimay/releases/latest/download/EzBimay.jar");
            Download d = new Download(url);

            while (d.getStatus() != 2) {
                double size = d.getSize();
                String downloadSize;
                if (size > 1024) {
                    if (size / 1024 > 1024.0) downloadSize = String.format("%.1f MB)", ((size / 1024) / 1024));
                    else downloadSize = String.format("%.1f KB)", (size / 1024));
                } else downloadSize = String.format("%d B)", (int) size);
                double downloaded = d.getDownloaded();
                if (downloaded > 1024) {
                    if (downloaded / 1024 > 1024.0)
                        text.setText(String.format("Downloading update (%.1f MB  / " + downloadSize, ((downloaded / 1024) / 1024)));
                    else
                        text.setText(String.format("Downloading update (%.1f KB  / " + downloadSize, (downloaded / 1024)));
                } else text.setText(String.format("Downloading update (%d B  / " + downloadSize, (int) downloaded));
                bar.setProgress(downloaded / size);
            }
            String[] args = new String[]{"java", "-jar", System.getProperty("user.dir") + "/EzBimay.jar"};
            new ProcessBuilder(args).start();
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
