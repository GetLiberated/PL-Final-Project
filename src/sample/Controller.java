package sample;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.*;
import java.net.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import microsoft.exchange.webservices.data.autodiscover.IAutodiscoverRedirectionUrl;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.Appointment;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Controller implements Serializable{
    private String username, email, password, version = "1.1.4";
    private String chromeversion;
    private List<String> options;
    private LinkedHashMap<String, String> studCourse = new LinkedHashMap<>();
    private int exam;
    private boolean updateAvailable, fail = false;
    public AnchorPane body;
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public Hyperlink button5;
    public Button button6;
    public Button button7;
    public Hyperlink button8;
    public Button button9;
    public ChoiceBox<String> box1;
    public TabPane tabs;
    public CheckBox passwordCheckbox;
    public CheckBox checkUpdate;
    public Text versionText;
    public ImageView check;
    private WebDriver driver, hdriver, hdriver2;
    private double xOffSet = 0;
    private double yOffSet = 0;
    public boolean mac = false;

    public void initialize() throws Exception{
        makeWindowDragable();
        versionText.setText("v"+version);

        if (System.getProperty("os.name").equals("Windows 10")) options = Files.readAllLines(Paths.get("options"));
        else {
            String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
            options = Files.readAllLines(Paths.get(dir + "options"));
        }
        if (options.get(2).equals("checkforupdate=\"yes\"")) checkUpdate.setSelected(true);
        else checkUpdate.setSelected(false);
        if (options.get(3).equals("currentexam=\"final\"")) exam = 2;
        else if (options.get(3).equals("currentexam=\"mid\""))exam = 1;
        else exam = 0;
//        String currentversion = options.get(4);
//        String newversion = "";
//        for (int i = 0; i < currentversion.length(); i++) {
//            newversion += currentversion.charAt(i);
//
//            if (i == 15) {
//                newversion += version+"\"";
//                break;
//            }
//        }
//        options.set(4, newversion);
        if (options.get(4).contains("mac")) mac = true;
        if (!options.get(5).equals("chromeversion=\"\"")) chromeversion = options.get(5).substring(15,17);
        for (int i = 7; i < options.size(); i++) {
            String c = options.get(i);
            String[] course = c.split(",");
            studCourse.put(course[0], course[1]);
            box1.getItems().add(course[0]);
        }
        box1.setValue(box1.getItems().get(0));

        File appPassword;
        if (System.getProperty("os.name").equals("Windows 10")) appPassword = new File("plugin");
        else {
            String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
            appPassword = new File(dir + "plugin");
        }
        if (appPassword.exists()){
            passwordCheckbox.setSelected(true);
        }

        ObjectInputStream kext, lib, dat;
        if (System.getProperty("os.name").equals("Windows 10")) {
            kext = new ObjectInputStream(new FileInputStream("kext"));
            lib = new ObjectInputStream(new FileInputStream("lib"));
            dat = new ObjectInputStream(new FileInputStream("dat"));
        }
        else {
            String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
            kext = new ObjectInputStream(new FileInputStream(dir + "kext"));
            lib = new ObjectInputStream(new FileInputStream(dir + "lib"));
            dat = new ObjectInputStream(new FileInputStream(dir + "dat"));
        }

        byte [] key = (byte []) kext.readObject();
        SecretKey myDesKey = new SecretKeySpec(key, "DES");
        Cipher desCipher;
        desCipher = Cipher.getInstance("DES");

//            byte[] textEncrypted = Files.readAllBytes(Paths.get("email.txt"));
        byte [] textEncrypted = (byte []) lib.readObject();
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        byte[] textDecrypted = desCipher.doFinal(textEncrypted);
        String s = new String(textDecrypted);
        username = s;
        email = username + "@binus.ac.id";

//            textEncrypted = Files.readAllBytes(Paths.get("pass.txt"));
        textEncrypted = (byte []) dat.readObject();
        desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
        textDecrypted = desCipher.doFinal(textEncrypted);
        s = new String(textDecrypted);
        password = s;

        kext.close();
        lib.close();
        dat.close();

        checkOS();
//        if (mac) update();
//        else
            new Thread(this::dailyRoutine).start();
        openBimay();
    }

    public void timer(double second) {
        try {Thread.sleep((int) second*1000);}
        catch (Exception e){}
    }

    public void checkOS() {
        String os = System.getProperty("os.name");
        try {
            if (os.contains("Mac")) {
                File chrome = new File("/Applications/Google Chrome.app/Contents/Versions");
                if (chrome.exists()) {
                    File[] chromeVers = chrome.listFiles();
                    String currentChromeVer = chromeVers[chromeVers.length - 1].toString().substring(50, 52);
                    String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                    if (!chromeversion.equals(currentChromeVer)) {
                        URL website = new URL("https://chromedriver.storage.googleapis.com/" + getChromedriverVer(currentChromeVer) + "/chromedriver_mac64.zip");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream(dir + "chromedriver_mac64.zip");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        String[] args = new String[] {"unzip" , dir + "chromedriver_mac64.zip" , "chromedriver" , "-d" , dir};
                        Process proc = new ProcessBuilder(args).start();
                        proc.waitFor();
                        File zip = new File(dir + "chromedriver_mac64.zip");
                        zip.delete();
                        chromeversion = currentChromeVer;
                    }
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setHeadless(true);
                    chromeOptions.addArguments("window-size=1200x600");
                    System.setProperty("webdriver.chrome.driver", dir + "chromedriver");
                    hdriver = new ChromeDriver(chromeOptions);
                    hdriver2 = new ChromeDriver(chromeOptions);
                }
//                hdriver = new HtmlUnitDriver(true) {
//                    @Override
//                    protected WebClient newWebClient(BrowserVersion version) {
//                        WebClient webClient = super.newWebClient(version);
//                        webClient.getOptions().setThrowExceptionOnScriptError(false);
//                        return webClient;
//                    }
//                };
            }

            else if (os.contains("Windows 10")) {
                File chrome = new File("C:/Program Files (x86)/Google/Chrome/Application");
                if (chrome.exists()) {
                    File[] chromeVers = chrome.listFiles();
                    String currentChromeVer = chromeVers[0].toString().substring(49, 51);
                    if (!chromeversion.equals(currentChromeVer)) {
                        URL website = new URL("https://chromedriver.storage.googleapis.com/" + getChromedriverVer(currentChromeVer) + "/chromedriver_win32.zip");
                        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                        FileOutputStream fos = new FileOutputStream("chromedriver_win32.zip");
                        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                        fos.close();
                        String[] args = new String[] {"powershell.exe", "-NoP", "-NonI", "-Command", "\"Expand-Archive '" + System.getProperty("user.dir") + "\\chromedriver_win32.zip' '" + System.getProperty("user.dir") + "'\""};
                        Process proc = (new ProcessBuilder(args)).start();
                        proc.waitFor();
                        File zip = new File(System.getProperty("user.dir") + "/chromedriver_win32.zip");
                        zip.delete();
                        chromeversion = currentChromeVer;
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
                    hdriver = new ChromeDriver(chromeOptions);
                    hdriver2 = new ChromeDriver(chromeOptions);
                }
            }

//            else if (os.contains("Windows 7")) {
//                File chrome = new File("C:/Program Files (x86)/Google/Application");
//                if (chrome.exists()) {
//                    System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
//                    driver = new ChromeDriver();
//                } else driver = new InternetExplorerDriver();
//            }
        }
        catch (Exception e){}
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

    static class RedirectionUrlCallback implements IAutodiscoverRedirectionUrl {
        public boolean autodiscoverRedirectionUrlValidationCallback(
                String redirectionUrl) {
            return redirectionUrl.toLowerCase().startsWith("https://");
        }
    }

    public void update(){
        if (checkUpdate.isSelected()){
            WebDriverWait wait = new WebDriverWait(hdriver, 30);
            hdriver.navigate().to("https://github.com/savageRex/EzBimay/releases/latest");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[4]")));
            String url = hdriver.getCurrentUrl();
            String ver = url.substring(50);
            String[] newvers = ver.split("\\.");
            String[] curvers = version.split("\\.");
            if (Integer.parseInt(newvers[0]) > Integer.parseInt(curvers[0]) || Integer.parseInt(newvers[1]) > Integer.parseInt(curvers[1]) || Integer.parseInt(newvers[2]) > Integer.parseInt(curvers[2])){
                updateAvailable = true;
            }
        }
        if (updateAvailable) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("update.fxml"));
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UTILITY);
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.show();
                    }
                    catch (Exception e){}
                }
            });
        }
        if (hdriver!=null) {
            hdriver.quit();
        }
    }

    public void dailyRoutine(){
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials(email, password);
        service.setCredentials(credentials);

        new Thread(() -> {
            WebDriverWait wait = new WebDriverWait(hdriver, 60);
            hdriver.navigate().to("https://binusmaya.binus.ac.id/login/");
            WebElement emailBox = hdriver.findElement(By.xpath("//input[@type='text']"));
            emailBox.sendKeys(username);
            WebElement passwordBox = hdriver.findElement(By.xpath("//input[@type='password']"));
            passwordBox.sendKeys(password);
            WebElement button = hdriver.findElement(By.xpath("//input[@type='submit']"));
            button.click();
            hdriver.navigate().to("https://binusmaya.binus.ac.id/newStudent/index.html#/learning/lecturing");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"calendar\"]/table/tbody/tr/td[3]/span[2]")));
            WebElement sync = hdriver.findElement(By.xpath("//*[@id=\"calendar\"]/table/tbody/tr/td[3]/span[2]"));
            sync.click();
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"dialog-content-status\"]")));
            hdriver.navigate().to("https://binusmaya.binus.ac.id/newstudent/#/exam/studentexam");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody")));
            try {
                if (exam == hdriver.findElements(By.xpath("//*[@id=\"tableTemplate\"]/table")).size()) {
                    if (exam == 1) {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr")));
                        int midAmount = hdriver.findElements(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr")).size();
                        for (int i = 1; i < midAmount; i++) {
                            String course = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr[" + i + "]/td[1]")).getText();
//                String CLASS = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr[" + i + "]/td[2]")).getText();
                            String[] thedate = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr[" + i + "]/td[3]")).getText().split(", ");
//                String day = thedate[0];
                            String date = thedate[1];
                            String[] time = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr[" + i + "]/td[4]")).getText().split(" - ");
                            String startTime = time[0];
                            String endTime = time[1];
                            String location = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr[" + i + "]/td[5]")).getText();
                            String room = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table/tbody/tr[" + i + "]/td[6]")).getText();
                            try {
                                service.autodiscoverUrl(email, new RedirectionUrlCallback());
                                Appointment appointment = new Appointment(service);
                                appointment.setSubject("[MID EXAM] - " + course);
                                appointment.setBody(MessageBody.getMessageBodyFromText("EXAM"));
                                appointment.setLocation(room + " - " + location);
                                Date startDate, endDate;
                                if (date.length() == 10) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm");
                                    startDate = formatter.parse(date + " " + startTime);
                                    endDate = formatter.parse(date + " " + endTime);
                                } else {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                    startDate = formatter.parse(date + " " + startTime);
                                    endDate = formatter.parse(date + " " + endTime);
                                }
                                appointment.setStart(startDate);
                                appointment.setEnd(endDate);
                                appointment.save();
                            } catch (Exception e) {
                            }
                        }
                        exam = 2;
                    }
                    else if (exam == 2) {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr")));
                        int finalAmount = hdriver.findElements(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr")).size();
                        for (int i = 1; i < finalAmount; i++) {
                            String course = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr[" + i + "]/td[1]")).getText();
//                String CLASS = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr[" + i + "]/td[2]")).getText();
                            String[] thedate = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr[" + i + "]/td[3]")).getText().split(", ");
//                String day = thedate[0];
                            String date = thedate[1];
                            String[] time = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr[" + i + "]/td[4]")).getText().split(" - ");
                            String startTime = time[0];
                            String endTime = time[1];
                            String location = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr[" + i + "]/td[5]")).getText();
                            String room = hdriver.findElement(By.xpath("//*[@id=\"tableTemplate\"]/table[1]/tbody/tr[" + i + "]/td[6]")).getText();
                            try {
                                service.autodiscoverUrl(email, new RedirectionUrlCallback());
                                Appointment appointment = new Appointment(service);
                                appointment.setSubject("[FINAL EXAM] - " + course);
                                appointment.setBody(MessageBody.getMessageBodyFromText("EXAM"));
                                appointment.setLocation(room + " - " + location);
                                Date startDate, endDate;
                                if (date.length() == 10) {
                                    SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy HH:mm");
                                    startDate = formatter.parse(date + " " + startTime);
                                    endDate = formatter.parse(date + " " + endTime);
                                } else {
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                    startDate = formatter.parse(date + " " + startTime);
                                    endDate = formatter.parse(date + " " + endTime);
                                }
                                appointment.setStart(startDate);
                                appointment.setEnd(endDate);
                                appointment.save();
                            }
                            catch (Exception a) {}
                        }
                        exam = 1;
                    }
                }
            }
            //*[@id="tableTemplate"]/table/tbody/tr[1]
            //*[@id="tableTemplate"]/table
            catch (Exception e){}
            update();
        }).start();
        WebDriverWait wait = new WebDriverWait(hdriver2, 60);
        hdriver2.navigate().to("https://binusmaya.binus.ac.id/login/");
        WebElement emailBox = hdriver2.findElement(By.xpath("//input[@type='text']"));
        emailBox.sendKeys(username);
        WebElement passwordBox = hdriver2.findElement(By.xpath("//input[@type='password']"));
        passwordBox.sendKeys(password);
        WebElement button = hdriver2.findElement(By.xpath("//input[@type='submit']"));
        button.click();
        //*[@id="tableTemplate"]/table[2]/tbody/tr[1]
        //*[@id="tableTemplate"]/table[1]/tbody/tr[1]
        //*[@id="tableTemplate"]/table[1]
        for (String s: studCourse.values()){
            String link = s;
            String assignment = new String();
            String assignmentWord = "/assignment";
            int index = 47;
            for (int i = 0; i < link.length(); i++) {
                assignment += link.charAt(i);

                if (i == index) {
                    assignment += assignmentWord;
                }
            }
            hdriver2.navigate().to(assignment);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"iTemplatesEvaluationContent\"]/table/tbody/tr")));
//            int assignmentAmount = hdriver.findElements(By.xpath("//*[@id=\"iTemplatesEvaluationContent\"]/table/tbody/tr")).size();
            WebElement table = hdriver2.findElement(By.id("iTemplatesEvaluationContent"));
            WebElement tableBody = table.findElement(By.tagName("tbody"));
            List<WebElement> row = tableBody.findElements(By.tagName("tr"));
            for (int i = 0; i < row.size()-1; i++) {
                if (!row.get(0).getText().equals("No individual assignments have been uploaded yet")) {
                    List<WebElement> coloumn = row.get(i).findElements(By.tagName("td"));
                    if (!coloumn.get(3).getText().contains("History")) {
                        String name = hdriver2.findElement(By.xpath("*[@id=\"iTemplatesEvaluationContent\"]/table/tbody/tr[" + i+1 + "]/td[1]")).getText();
                        String date = hdriver2.findElement(By.xpath("*[@id=\"iTemplatesEvaluationContent\"]/table/tbody/tr[" + i+1 + "]/td[2]")).getText();
                        String time = hdriver2.findElement(By.xpath("*[@id=\"iTemplatesEvaluationContent\"]/table/tbody/tr[" + i+1 + "]/td[3]")).getText();
                        try {
                            service.autodiscoverUrl(email, new RedirectionUrlCallback());
                            Appointment appointment = new Appointment(service);
                            Set<String> course = studCourse.keySet();
                            for (String c : course) {
                                if (studCourse.get(c).equals(s)) {
                                    appointment.setSubject("[ASSIGNMENT] - " + c);
                                    break;
                                }
                            }
                            appointment.setBody(MessageBody.getMessageBodyFromText(name));
                            Date startDate, endDate;
                            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
                            startDate = formatter.parse(date + " " + time);
                            endDate = formatter.parse(date + " " + time);
                            appointment.setStart(startDate);
                            appointment.setEnd(endDate);
                            appointment.save();
                        }
                        catch (Exception a) {}
                    }
                }
            }
            //*[@id="iTemplatesEvaluationContent"]/table/tbody/tr[1]/td[4].size()
            //*[@id="iTemplatesEvaluationContent"]/table/tbody/tr[1]/td[1]
            //*[@id="iTemplatesEvaluationContent"]/table/tbody/tr[1]
            //*[@id="iTemplatesEvaluationContent"]/table/tbody
            hdriver2.navigate().to("https://binusmaya.binus.ac.id/newStudent/");
        }
        if (hdriver2!=null) {
            hdriver2.quit();
        }
//        System.out.println("Oskarisama desu.");
        check.setVisible(true);
    }

    public void openWebsite(String url){
        driver.switchTo().window(driver.getWindowHandle());
        driver.navigate().to(url);
    }

    public void openBimay(){
        if (chromeversion != null) {
            if (mac) {
                String dir = null;
                try {
                    dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                } catch (UnsupportedEncodingException e) {}
                System.setProperty("webdriver.chrome.driver", dir + "chromedriver");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-infobars");
                driver = new ChromeDriver(chromeOptions);
            }
            else {
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("disable-infobars");
                driver = new ChromeDriver(chromeOptions);
            }
        }
        else {
            if (System.getProperty("os.name").contains("Mac")) driver = new SafariDriver();
        }
        String url = "https://binusmaya.binus.ac.id/login/";
        openWebsite(url);
        WebElement emailBox = driver.findElement(By.xpath("//input[@type='text']"));
        emailBox.sendKeys(username);
        WebElement passwordBox = driver.findElement(By.xpath("//input[@type='password']"));
        passwordBox.sendKeys(password);
        WebElement button = driver.findElement(By.xpath("//input[@type='submit']"));
        button.click();
        if (driver.getCurrentUrl().equals("https://binusmaya.binus.ac.id/login/?error=1")) {
            fail = true;
            logout();
        }
        if (studCourse.containsValue("course")) {
            updateCourse();
        }
        driver.manage().window().maximize();
        Main.primaryStage.requestFocus();
        new Thread(() ->{
            WebDriverWait wait = new WebDriverWait(driver, 60);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"nobledream\"]")));
            timer(2);
            try {
                ((JavascriptExecutor)driver).executeScript("return document.getElementsByClassName('fancybox-overlay fancybox-overlay-fixed')[0].remove();");
            } catch (Exception e) {
            }
        }).start();
    }

    public void reOpenBimay(){
        killBrowser();
        openBimay();
    }

    public void viewScore(){
        String url = "https://binusmaya.binus.ac.id/newStudent/#/score/viewscore";
        openWebsite(url);
    }

    public void examSchedule(){
        String url = "https://binusmaya.binus.ac.id/newstudent/#/exam/studentexam";
        openWebsite(url);
    }

    public void classSchedule(){
        String url = "https://binusmaya.binus.ac.id/newStudent/index.html#/learning/lecturing";
        openWebsite(url);
    }

    public void updateCourse() {
        tabs.getSelectionModel().select(0);
        button6.setDefaultButton(false);
        studCourse.clear();
        box1.getItems().clear();
        driver.switchTo().window(driver.getWindowHandle());
        if (!driver.getCurrentUrl().equals("https://binusmaya.binus.ac.id/newStudent/#/index")) driver.navigate().to("https://binusmaya.binus.ac.id/newStudent/");
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"nobledream\"]")));

        // Click menu
        WebElement menu = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]"));
        menu.click();
        timer(1);

        new Thread(() ->{
            while (true) try {
                ((JavascriptExecutor)driver).executeScript("return document.getElementsByClassName('fancybox-overlay fancybox-overlay-fixed')[0].remove();");
            } catch (Exception e) {
            }
        }).start();

        // Click courses (tab 1)
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Courses")));
        WebElement courses = driver.findElement(By.linkText("Courses"));
        courses.click();

        menu.click();
        timer(1);
        menu.click();
        timer(1);
        courses.click();

        // Click degree/graduate (tab 2)
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[2]")));
//        WebElement tab2 = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[2]"));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(tab2.getText())));
//        WebElement degree = driver.findElement(By.linkText(tab2.getText()));
//        wait.until(ExpectedConditions.visibilityOf(degree));
//        degree.click();

        // Click semester (tab 3)
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[3]")));
        WebElement tab3 = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[3]"));
        BufferedReader semesters = new BufferedReader(new StringReader(tab3.getText()));
        String currentSemester;
        try {
            while( (currentSemester = semesters.readLine()) != null) {
                break;
            }
    //        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(currentSemester)));
            WebElement semester = driver.findElement(By.linkText(currentSemester));
    //        wait.until(ExpectedConditions.visibilityOf(semester));
            semester.click();

            // Read courses (tab 4)
    //        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[4]")));
            WebElement element = driver.findElement(By.xpath("//*[@id=\"main-nav-expand\"]/div/ul[4]"));
            BufferedReader allCourses = new BufferedReader(new StringReader(element.getText()));
            String line;
            while( (line = allCourses.readLine()) != null) {
                WebElement course = driver.findElement(By.linkText(line));
                studCourse.put(line, course.getAttribute("href"));
                box1.getItems().add(line);
            }
        }
        catch (Exception e){}
        menu.click();
//        Set<String> course = studCourse.keySet();
//        for (String s : course){
//            if (studCourse.get(s).substring(70, 73).equals("LAB")) {
//                driver.navigate().to(studCourse.get(s));
//                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]")));
//                WebElement CLASS = driver.findElement(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]"));
//                String link = studCourse.get(s).substring(0, 70) + "LEC/" + CLASS.getAttribute("value");
//                studCourse.put(s, link);
//                box1.getItems().add(s);
//                driver.navigate().to("https://binusmaya.binus.ac.id/newStudent/");
//            } else box1.getItems().add(s);
//        }
        box1.setValue(box1.getItems().get(0));
        Main.primaryStage.requestFocus();
    }

    public void openAssignment(){
        String course = box1.getValue();
        String link = studCourse.get(course);
        String assignment = new String();
        String assignmentWord = "/assignment";
        int index = 47;
        for (int i = 0; i < link.length(); i++) {
            assignment += link.charAt(i);

            if (i == index) {
                assignment += assignmentWord;
            }
        }
//        String currentlink;
//        for (String s: studCourse.values()) {
//            currentlink = "";
//            for (int i = 0; i < s.length(); i++) {
//                currentlink += s.charAt(i);
//
//                if (i == index) {
//                    currentlink += assignmentWord;
//                }
//            }
//            if (currentlink.equals(driver.getCurrentUrl())) {
//                driver.navigate().to("https://binusmaya.binus.ac.id/newStudent/");
//                break;
//            }
//        }
        if (driver.getCurrentUrl().substring(43,48).equals("class")) driver.navigate().to("https://binusmaya.binus.ac.id/newStudent/");
        openWebsite(assignment);
        if (link.substring(70, 73).equals("LAB")){
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]")));
            WebElement CLASS = driver.findElement(By.xpath("//*[@id=\"ddlclasslist\"]/option[2]"));
            String newlink = studCourse.get(course).substring(0, 70) + "LEC/" + CLASS.getAttribute("value");
            studCourse.put(course, newlink);
        }
    }

    public void officialWebsite(){
        String url = "https://savagerex.github.io/EzBimay";
        ((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
        // Source
        // https://stackoverflow.com/questions/17547473/how-to-open-a-new-tab-using-selenium-webdriver
        // by: kernowcode
    }

    public void suggestion(){
        String url = "https://savagerex.github.io/EzBimay#support";
        ((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
        // Source
        // https://stackoverflow.com/questions/17547473/how-to-open-a-new-tab-using-selenium-webdriver
        // by: kernowcode
    }

    public void whatsnew(){
        String url = "https://github.com/savageRex/EzBimay/releases/latest";
        ((JavascriptExecutor)driver).executeScript("window.open('"+url+"','_blank');");
        // Source
        // https://stackoverflow.com/questions/17547473/how-to-open-a-new-tab-using-selenium-webdriver
        // by: kernowcode
    }

    public void createPassword(){
        if (passwordCheckbox.isSelected()){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("passwordbox.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UTILITY);
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.setOnCloseRequest(event -> {
                    passwordboxController c = loader.getController();
                    c.cancel();
                });
                stage.show();
            }
            catch (Exception e){}
        }
        else {
            try {
                if (System.getProperty("os.name").equals("Windows 10")) Files.deleteIfExists(Paths.get("plugin"));
                else {
                    String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                    Files.deleteIfExists(Paths.get(dir + "plugin"));
                }
            }
            catch (Exception e){}
        }
    }

    public void help(){
        tabs.getSelectionModel().select(1);
        button6.setDefaultButton(true);
    }

    public void save(){
        List<String> savefile = new ArrayList<>();
        savefile.add(options.get(0));
        savefile.add(options.get(1));
        if (checkUpdate.isSelected()) savefile.add("checkforupdate=\"yes\"");
        else savefile.add("checkforupdate=\"no\"");
        if (exam == 2) savefile.add("currentexam=\"final\"");
        else if (exam == 1) savefile.add("currentexam=\"mid\"");
        else savefile.add("currentexam=\"\"");
        if (mac) savefile.add(options.get(4));
        else savefile.add("OS=\"windows\"");
        savefile.add("chromeversion=\""+chromeversion+"\"");
        savefile.add(options.get(6));
        Set<String> course = studCourse.keySet();
        for (String s : course){
            savefile.add(s + "," + studCourse.get(s));
        }
        try {
            if (System.getProperty("os.name").equals("Windows 10")) Files.write(Paths.get("options"), savefile);
            else {
                String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                Files.write(Paths.get(dir + "options"), savefile);
            }
        }
        catch (Exception e){}
    }

    public void logout(){
        try {
            killBrowser();
            if (System.getProperty("os.name").equals("Windows 10")) {
                Files.delete(Paths.get("dat"));
                Files.delete(Paths.get("kext"));
                Files.delete(Paths.get("lib"));
                Files.delete(Paths.get("options"));
                Files.deleteIfExists(Paths.get("plugin"));
            }
            else {
                String dir = new File(URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile(), "UTF-8")).toString().replace("EzBimay.jar", "");
                Files.delete(Paths.get(dir + "dat"));
                Files.delete(Paths.get(dir + "kext"));
                Files.delete(Paths.get(dir + "lib"));
                Files.delete(Paths.get(dir + "options"));
                Files.deleteIfExists(Paths.get(dir + "plugin"));
            }
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            if (fail) {
                loginController lc = Main.loader.getController();
                lc.waitText.setText("Password change detected.");
                lc.waitText.setVisible(true);
                lc.waitText2.setText("Please login with your new password.");
                lc.waitText2.setVisible(true);
            }
            Stage stage = (Stage) body.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
        catch (Exception e){}
    }

    public void killBrowser(){
        if (hdriver!=null) {
            hdriver.quit();
        }
        if (driver!=null) {
            driver.quit();
        }
    }

    public void makeWindowDragable() {
        body.setOnMousePressed( (event) -> {
            xOffSet = event.getSceneX();
            yOffSet = event.getSceneY();
        });
        body.setOnMouseDragged( (event) -> {
            Main.primaryStage.setX(event.getScreenX() - xOffSet);
            Main.primaryStage.setY(event.getScreenY() - yOffSet);
        });
    }

    public void minimize(){
        Main.primaryStage.setIconified(true);
    }

    public void close(){
        killBrowser();
        save();
        System.exit(0);
    }
}
