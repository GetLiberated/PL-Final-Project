package sample;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class loginController implements Serializable{
    public TextField emailText;
    public PasswordField passwordText;
    public Button loginButton;
    public Text wrongText;
    public Text waitText;
    public Text waitText2;
    public Text failText;
    public Text failText2;
    public Text failText3;
    public Hyperlink failText4;
    private List<String> options = new ArrayList<>();
    private LinkedHashMap<String, String> studCourse = new LinkedHashMap<>();
    private WebDriver driver = null;

    public void run(){
        new Thread(this::login).start();
    }

    public void login(){
        emailText.setDisable(true);
        passwordText.setDisable(true);
        loginButton.setDisable(true);
        waitText.setVisible(false);
        waitText2.setVisible(false);
        wrongText.setVisible(false);
        failText.setVisible(false);
        failText2.setVisible(false);
        failText3.setVisible(false);
        failText4.setVisible(false);

        String os = System.getProperty("os.name");
        String currentChromeVer = "";
        try {
            if (os.contains("Mac")) {
                File chrome = new File("/Applications/Google Chrome.app/Contents/Versions");
                if (chrome.exists()) {
                    File[] chromeVers = chrome.listFiles();
                    currentChromeVer = chromeVers[chromeVers.length - 1].toString().substring(50, 52);
                    String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                    File chromedriver = new File(dir + "chromedriver");
                    if (!chromedriver.exists()) {
                        URL website = new URL("https://chromedriver.storage.googleapis.com/" + getChromedriverVer(currentChromeVer) + "/chromedriver_mac64.zip");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream(dir + "chromedriver_mac64.zip");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        String[] args = new String[]{"unzip", dir + "chromedriver_mac64.zip", "chromedriver", "-d", dir};
                        Process proc = new ProcessBuilder(args).start();
                        proc.waitFor();
                        File zip = new File(dir + "chromedriver_mac64.zip");
                        zip.delete();
                    }
//                    System.setProperty("webdriver.chrome.driver", dir + "chromedriver");
//                    driver = new ChromeDriver(chromeOptions);
                }
                driver = new HtmlUnitDriver(true) {
                    @Override
                    protected WebClient newWebClient(BrowserVersion version) {
                        WebClient webClient = super.newWebClient(version);
                        webClient.getOptions().setThrowExceptionOnScriptError(false);
                        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
                        return webClient;
                    }
                };
            } else if (os.contains("Windows 10")) {
                File chrome = new File("C:/Program Files (x86)/Google/Chrome/Application");
                if (chrome.exists()) {
                    File[] chromeVers = chrome.listFiles();
                    currentChromeVer = chromeVers[0].toString().substring(49, 51);
                    File chromedriver = new File(System.getProperty("user.dir") + "\\chromedriver.exe");
                    if (!chromedriver.exists()) {
                        URL website = new URL("https://chromedriver.storage.googleapis.com/" + getChromedriverVer(currentChromeVer) + "/chromedriver_win32.zip");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("chromedriver_win32.zip");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        String[] args = new String[]{"powershell.exe", "-NoP", "-NonI", "-Command", "\"Expand-Archive '" + System.getProperty("user.dir") + "\\chromedriver_win32.zip' '" + System.getProperty("user.dir") + "'\""};
                        Process proc = (new ProcessBuilder(args)).start();
                        proc.waitFor();
                        File zip = new File(System.getProperty("user.dir") + "\\chromedriver_win32.zip");
                        zip.delete();
                    }
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setHeadless(true);
                    chromeOptions.addArguments("window-size=1200x600");
                    chromeOptions.addArguments("disable-gpu");
                    chromeOptions.addArguments("disable-infobars");
                    chromeOptions.addArguments("no-sandbox");
                    chromeOptions.addArguments("bwsi");
                    chromeOptions.addArguments("incognito");
                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
                    driver = new ChromeDriver(chromeOptions);
                }
            }
//        else if (os.contains("Windows 7")) {
//            File chrome = new File("C:/Program Files (x86)/Google/Application");
//            if(chrome.exists()) {
//                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
//                driver = new ChromeDriver(chromeOptions);
//            }
//            else driver = new InternetExplorerDriver();
//        }
            driver.navigate().to("https://binusmaya.binus.ac.id/login/");
            WebElement emailBox = driver.findElement(By.xpath("//input[@type='text']"));
            emailBox.sendKeys(emailText.getText());
            WebElement passwordBox = driver.findElement(By.xpath("//input[@type='password']"));
            passwordBox.sendKeys(passwordText.getText());
            WebElement button = driver.findElement(By.xpath("//input[@type='submit']"));
            button.click();
            waitText.setText("Verifying your account...");
            waitText.setVisible(true);
            if (driver.getCurrentUrl().equals("https://binusmaya.binus.ac.id/newStudent/") || driver.getCurrentUrl().equals("https://binusmaya.binus.ac.id/newStudent/#/index")) {
                if (!os.contains("Mac")) {
                    waitText.setText("Please wait... Initializing. 0%");
                    WebDriverWait wait = new WebDriverWait(driver, 30);
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"nobledream\"]")));
                    WebElement menu = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]"));
                    menu.click();
                    waitText.setText("Please wait... Initializing. 20%");
                    Thread.sleep(1500);
                    WebElement courses = driver.findElement(By.linkText("Courses"));
                    courses.click();
                    WebElement tab3 = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[3]"));
                    BufferedReader semesters = new BufferedReader(new StringReader(tab3.getText()));
                    String currentSemester;
                    while ((currentSemester = semesters.readLine()) != null) {
                        break;
                    }
                    WebElement semester = driver.findElement(By.linkText(currentSemester));
                    semester.click();
                    WebElement element = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[4]"));
                    BufferedReader allCourses = new BufferedReader(new StringReader(element.getText()));
                    String line;
                    while ((line = allCourses.readLine()) != null) {
                        WebElement course = driver.findElement(By.linkText(line));
                        studCourse.put(line, course.getAttribute("href"));
                    }
                    waitText.setText("Please wait... Initializing. 50%");
                    driver.navigate().to("https://binusmaya.binus.ac.id/newstudent/#/exam/studentexam");
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody")));
                    String exam;
                    if (driver.findElements(By.xpath("//*[@id=\"tableTemplate\"]/table")).size() == 1) exam = "mid";
                    else exam = "final";
                    waitText.setText("Please wait... Initializing. 80%");
                    options.add("//I know you can read this file unlike the others, hackerman. But please don't edit anything or you're gonna have a bad time (no, seriously tho.)");
                    options.add("[General]");
                    options.add("checkforupdate=\"yes\"");
                    options.add("currentexam=\"" + exam + "\"");
                    options.add("OS=\"windows\"");
                    options.add("chromeversion=\"" + currentChromeVer + "\"");
                    options.add("[Course]");
                    Set<String> course = studCourse.keySet();
                    for (String s : course) {
                        if (studCourse.get(s).substring(70, 73).equals("LAB")) {
                            ((JavascriptExecutor) driver).executeScript("window.open('" + studCourse.get(s) + "','_blank');");
                            ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
                            driver.switchTo().window(tabs.get(tabs.size() - 1));
                            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]")));
                            WebElement CLASS = driver.findElement(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]"));
                            String link = studCourse.get(s).substring(0, 70) + "LEC/" + CLASS.getAttribute("value");
                            options.add(s + "," + link);
                        } else options.add(s + "," + studCourse.get(s));
                    }
                    waitText.setText("Please wait... Initializing. 100%");
                }
                else {
                    options.add("//I know you can read this file unlike the others, hackerman. But please don't edit anything or you're gonna have a bad time (no, seriously tho.)");
                    options.add("[General]");
                    options.add("checkforupdate=\"yes\"");
                    options.add("currentexam=\"\"");
                    options.add("OS=\"mac\"");
                    options.add("chromeversion=\"" + currentChromeVer + "\"");
                    options.add("[Course]");
                    options.add("course,course");
                }
                driver.close();
                driver.quit();
                ObjectOutputStream kext, lib, dat;
                if (System.getProperty("os.name").contains("Windows 10")) {
                    Files.write(Paths.get("options"), options);
                    kext = new ObjectOutputStream(new FileOutputStream("kext"));
                    lib = new ObjectOutputStream(new FileOutputStream("lib"));
                    dat = new ObjectOutputStream(new FileOutputStream("dat"));
                }
                else {
                    String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                    Files.write(Paths.get(dir + "options"), options);
                    kext = new ObjectOutputStream(new FileOutputStream(dir + "kext"));
                    lib = new ObjectOutputStream(new FileOutputStream(dir + "lib"));
                    dat = new ObjectOutputStream(new FileOutputStream(dir + "dat"));
                }
                KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
                SecretKey myDesKey = keygenerator.generateKey();
                Cipher desCipher;
                byte[] key = myDesKey.getEncoded();
                kext.writeObject(key);
                kext.close();

                desCipher = Cipher.getInstance("DES");
                byte[] text = emailText.getText().getBytes();
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                byte[] textEncrypted = desCipher.doFinal(text);
                lib.writeObject(textEncrypted);
                lib.close();

                desCipher = Cipher.getInstance("DES");
                text = passwordText.getText().getBytes();
                desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
                textEncrypted = desCipher.doFinal(text);
                dat.writeObject(textEncrypted);
                dat.close();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
                            Stage stage = (Stage) loginButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                        }
                        catch (Exception e){}
                    }
                });
            } else {
                driver.close();
                driver.quit();
                emailText.setDisable(false);
                passwordText.setDisable(false);
                loginButton.setDisable(false);
                wrongText.setVisible(true);
                failText.setVisible(false);
                failText2.setVisible(false);
                failText3.setVisible(false);
                failText4.setVisible(false);
            }
        }
        catch (Exception e){
//            WebElement body = driver.findElement(By.xpath("/html/body"));
//            System.out.println("getText: " + body.getText()); // return semester
//            System.out.println("innerText: " + body.getAttribute("innerText")); // return semester
//            System.out.println("value: " + body.getAttribute("value")); // null
//            System.out.println("textContent: " + body.getAttribute("textContent")); // return semester + course
//            System.out.println("innerHTML: " + body.getAttribute("innerHTML")); // return html code
            emailText.setDisable(false);
            passwordText.setDisable(false);
            loginButton.setDisable(false);
            waitText.setVisible(false);
            wrongText.setVisible(false);
            failText.setVisible(true);
            failText2.setVisible(true);
            failText3.setVisible(true);
            failText4.setVisible(true);
        }
    }

    public String getChromedriverVer(String ver) {
        String versionNumber = "";
        try {
            URL url = new URL("https://chromedriver.storage.googleapis.com/LATEST_RELEASE_" + ver);
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine.trim());
            in.close();
            String htmlPage = builder.toString();
            versionNumber = htmlPage.replaceAll("\\<.*?>", "");
        }
        catch (Exception e){}
        return versionNumber;
        // Source
        // https://stackoverflow.com/a/8278156/11106220chro
    }

    public void FAQ() {
        HostServices hostServices = (HostServices)failText4.getScene().getWindow().getProperties().get("hostServices");
        hostServices.showDocument("https://github.com/savageRex/EzBimay#faq");
    }

    public void minimize(){
        Main.primaryStage.setIconified(true);
    }

    public void close() {
        if (driver != null) driver.quit();
        System.exit(0);
    }
}
