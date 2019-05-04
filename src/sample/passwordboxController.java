package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class passwordboxController {
    public Button okButton;
    public Button cancelButton;
    public TextField passwordBox;
    public PasswordField passwordBox2;
    public Label wrongText;

    public void ok(){
        if (Files.exists(Paths.get("plugin"))){
            try {
                ObjectInputStream kext = new ObjectInputStream(new FileInputStream("kext"));
                ObjectInputStream plugin = new ObjectInputStream(new FileInputStream("plugin"));
                byte [] key = (byte []) kext.readObject();
                SecretKey myDesKey = new SecretKeySpec(key, "DES");
                Cipher desCipher;
                desCipher = Cipher.getInstance("DES");
                byte [] textEncrypted = (byte []) plugin.readObject();
                desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
                byte[] textDecrypted = desCipher.doFinal(textEncrypted);
                String s = new String(textDecrypted);
                if (s.equals(passwordBox2.getText())){
                    wrongText.setVisible(false);
                    kext.close();
                    plugin.close();
                    Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
                    Stage stage = (Stage) okButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                }
                else wrongText.setVisible(true);
            }
            catch (Exception e){}
        }
        else {
            try {
                ObjectInputStream kext = new ObjectInputStream(new FileInputStream("kext"));
                ObjectOutputStream plugin = new ObjectOutputStream(new FileOutputStream("plugin"));
                byte [] key = (byte []) kext.readObject();
                SecretKey myDesKey = new SecretKeySpec(key, "DES");
                Cipher desCipher;
                desCipher = Cipher.getInstance("DES");
                byte[] text = passwordBox.getText().getBytes();
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                byte[] textEncrypted = desCipher.doFinal(text);
                plugin.writeObject(textEncrypted);
                kext.close();
                plugin.close();
                Stage stage = (Stage) okButton.getScene().getWindow();
                stage.close();
            }
            catch (Exception e) {}
        }
    }

    public void cancel(){
        if (Files.exists(Paths.get("plugin"))){
            System.exit(0);
        }
        else {
            Controller c = Main.loader.getController();
            c.passwordCheckbox.setSelected(false);
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }
    }
}
