package challenge.navigation;

import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import org.openqa.selenium.WebDriver;
import challenge.pages.MercedesBenzHomePage;

import static net.serenitybdd.core.Serenity.getDriver;

public class NavigateToMercedesBenz {
    public static Performable theMercedesBenzHomePage() {
        WebDriver driver = getDriver();
        driver.manage().window().maximize();
        return Task.where("{0} opens Mercedes Benz UK home page",
                Open.browserOn().the(MercedesBenzHomePage.class));
    }
}
