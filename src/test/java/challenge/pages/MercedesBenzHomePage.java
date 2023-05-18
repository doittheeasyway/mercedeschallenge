package challenge.pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

@DefaultUrl("https://www.mercedes-benz.co.uk")
public class MercedesBenzHomePage extends PageObject {

    private WebDriver driver = getDriver();

    @Step
    public void acceptCookies() {
        WebElement btn = ShadowRoot.acceptCookies();
        btn.click();
    }

    @Step
    public void selectModel() {
        WebElement btn = ShadowRoot.selectModel();
        btn.click();
    }

    @Step
    public void mouseOverModel() {
        WebElement btn = ShadowRoot.mouseOverModel();
        Actions actions = new Actions(driver);
        actions.moveToElement(btn).perform();
    }

    @Step
    public void buildYourCar() {
        WebElement btn = ShadowRoot.buildYourCar();
        btn.click();
    }

    @Step
    public void filterByFuel(String fuelType) {
        ShadowRoot.filterByFuel(fuelType);
    }

    public void assuresMinMaxPriceInterval(String lowerLimit, String upperLimit) {
        ShadowRoot.assuresMinMaxPriceInterval(lowerLimit, upperLimit);
    }
}
