package guru.qa.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import guru.qa.components.RandomFaker;
import guru.qa.config.CredentialsConfig;
import guru.qa.helpers.Attach;
import guru.qa.pages.RegistrationPage;
import io.qameta.allure.selenide.AllureSelenide;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;

import static com.codeborne.selenide.Configuration.remote;

public class TestBase {

    public RegistrationPage registrationPage = new RegistrationPage();
    public RandomFaker randomFaker = new RandomFaker();

    static CredentialsConfig credentials =
            ConfigFactory.create(CredentialsConfig.class);

    @BeforeAll
    static void setup() {

        String login = credentials.login();
        String password = credentials.password();
        String selenoidUrl = System.getProperty("selenoidUrl", "selenoid.autotests.cloud/wd/hub/");

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.startMaximized = true;
        //Configuration.remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub/";

        Configuration.remote = String.format("https://%s:%s@%s/wd/hub/", login, password, selenoidUrl);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("enableVNC", true);
        capabilities.setCapability("enableVideo", true);

        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    public void tearDown() {
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
    }
}
