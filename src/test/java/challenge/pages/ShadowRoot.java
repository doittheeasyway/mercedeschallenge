package challenge.pages;

import net.serenitybdd.core.Serenity;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.serenitybdd.core.Serenity.getDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static challenge.elements.MercedesBenzElements.*;

public class ShadowRoot {

    private static SearchContext mShadowRootCookies;
    private static SearchContext mShadowRootHome;
    private static SearchContext mShadowRootList;
    private static SearchContext mShadowRootCheckBox;
    private static final int clockMs = 500;

    public static WebElement acceptCookies() {
        WebElement shadowHost = getDriver()
                .findElement(By.xpath(SHADOW_ROOT_COOKIES));
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        mShadowRootCookies = shadowRoot;

        /** element for click accept all */
        WebElement btn = shadowRoot.findElement(By.cssSelector(BTN_ACCEPT_ALL_COOKIES));
        standBy(clockMs);

        return btn;
    }

    public static WebElement selectModel() {
        WebElement shadowHost = getDriver()
                .findElement(By.xpath(SHADOW_ROOT_HOME));
        SearchContext shadowRoot = shadowHost.getShadowRoot();
        mShadowRootHome = shadowRoot;
        standBy(clockMs);

        /** scrool, not needed, just for watching it smoothly */
        ShadowRoot.scrollBy(0, 1000);

        /** element for click Hatchbacks */
        WebElement btn = mShadowRootHome.findElement(
                By.cssSelector(BTN_HATCHBACK)
        );
        standBy(clockMs);

        return btn;
    }

    public static WebElement mouseOverModel() {
        standBy(clockMs);

        /** element for mouse over A Class */
        WebElement btn = mShadowRootHome.findElement(
                By.cssSelector(CONTAINER_FOR_MOUSE_OVER)
        );
        standBy(clockMs);

        return btn;
    }

    public static WebElement buildYourCar() {
        standBy(clockMs);

        /** element for proceed to "Build your car" */
        WebElement btn = mShadowRootHome.findElement(
                By.cssSelector(MENU_BUILD_YOUR_CAR)
        );
        standBy(clockMs);

        return btn;
    }

    public static void filterByFuel(String fuelType) {

        /** parent shadow host */
        WebElement SHOST = getDriver().findElement(By.xpath(SHADOW_ROOT_LIST));
        SearchContext SROOT = SHOST.getShadowRoot();
        standBy(clockMs);
        mShadowRootList = SROOT;

        /** child shadow host - btn open select and checkboxes:: ccwb-multi-select */
        WebElement SHOST_CHILD = mShadowRootList.findElement(By.cssSelector(MULTISELECT_FUEL_TYPE));
        standBy(clockMs);

        /** child shadow host - checkbox for fuel type */
        List<WebElement> checkboxes = SHOST_CHILD
                .findElements(By.xpath(CHECKBOX_FUEL_TYPE));
        standBy(clockMs);

        WebElement chkDiesel = checkboxes.get(0);
        SearchContext SROOT_CHECKBOX = chkDiesel.getShadowRoot();
        mShadowRootCheckBox = SROOT_CHECKBOX;
        standBy(clockMs);

        /** scrool, not needed, just for watching it smoothly */
        ShadowRoot.scrollBy(0, 500);

        /** select fuel type */
        mShadowRootCheckBox
                .findElement(By.name(fuelType))
                .sendKeys(Keys.SPACE);
        standBy(clockMs);
    }

    public static void assuresMinMaxPriceInterval(String lowerLimit, String upperLimit) {
        /** fetch prices */
        List<WebElement> prices = mShadowRootList.findElements(By.className(LABEL_PRICE_TAG));

        Integer min = Integer.parseInt(lowerLimit.replace(".", ""));
        Integer max = Integer.parseInt(upperLimit.replace(".", ""));

        /** aggregate evidences, validate price interval and finish this test */
        saveSnapshot();
        List<Integer> cleanedPrices = saveTextFile(prices);

        /** assert price interval */
        assertTrue(cleanedPrices.get(0) >= min && cleanedPrices.get(cleanedPrices.size() - 1) <= max,
                "The value is not within the expected interval.");
    }

    /**
     * save snapshot
     */
    private static void saveSnapshot() {
        Serenity.takeScreenshot();
        File screenshotFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        String destinationFolder = System.getProperty("user.dir") + "\\evidences\\";
        String fileName = "screenshot.png";
        File destinationFile = new File(destinationFolder + fileName);
        try {
            FileUtils.copyFile(screenshotFile, destinationFile);
            System.out.println("Screenshot saved successfully!");
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    /**
     * Saves results to file, it:
     * - removes currency char and makes currency to int
     * - adds to final price list
     * - calculate min and max
     */
    private static List<Integer> saveTextFile(List<WebElement> prices) {
        List<Integer> pricesNumeric = new ArrayList<>();
        for (WebElement price : prices) {
            String currencyValue = price.getText().substring(1);
            int convertedValue = Integer.parseInt(currencyValue.replace(",", ""));
            pricesNumeric.add(convertedValue);
            System.out.println(price.getText());
        }
        Collections.sort(pricesNumeric);
        Integer min = pricesNumeric.get(0);
        Integer max = pricesNumeric.get(pricesNumeric.size() - 1);
        String filePath = System.getProperty("user.dir") + "\\evidences\\prices.txt";
        try {
            String content = "Minimum value: " + min + " & Maximum value: " + max;
            System.out.println(content);

            Files.write(Path.of(filePath), content.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pricesNumeric;
    }

    private static void scrollBy(int x, int y) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(" + x + "," + y + ")");
    }

    private static void standBy(int milisecs) {
        try {
            Thread.sleep(milisecs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
