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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class downloadbarController {
    public ProgressBar bar;
    public Text text;

    public void initialize(){
        new Thread(this::run).start();
    }

    private void run() {
        try {
            URL url;
            if (System.getProperty("os.name").contains("Mac")) url = new URL("https://github.com/savageRex/EzBimay/releases/latest/download/EzBimay.jar");
            else url = new URL("https://github.com/savageRex/EzBimay/releases/latest/download/EzBimay.exe");

//            new Thread(() -> {
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
//            }).start();

            if (System.getProperty("os.name").contains("Mac")) {
                String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("Updater.jar", "EzBimay.jar");
                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                FileOutputStream fos = new FileOutputStream(dir);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
//                String appdir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("/Contents/Java/Updater.jar", "").replaceAll("\\s", "\\\\ ");
                String[] args = new String[] {"open", "/Applications/EzBimay.app"};
                new ProcessBuilder(args).start();
            }
            else {
//                new File(System.getProperty("user.dir") + "\\EzBimay.exe").delete();
                String dir = System.getProperty("user.dir") + "\\EzBimay.exe";
                ReadableByteChannel rbc = Channels.newChannel(url.openStream());
                FileOutputStream fos = new FileOutputStream(dir);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                fos.close();
                Runtime.getRuntime().exec(dir);
            }
            System.exit(0);
        } catch (Exception e) {
            File file = new File("error.log");
            PrintStream ps = null;
            try {
                ps = new PrintStream(file);
            } catch (FileNotFoundException e1) {
            }
            e.printStackTrace(ps);
        }
    }
}
