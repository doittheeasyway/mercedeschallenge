# Mercedes Benz Challenge
This challenge uses Selenium, Serenity framework and Gherkin syntax for BDD.

### Getting started, requirements
- `Java JDK19`
- `IntelIj` or another Java IDE
- `Maven`
 
Webdriver binaries will be downloaded automatically by a `WebdriverManager`

## Executing the tests
To run the sample project, you can either just run the `MercedesTestSuite.java` test runner class

By default, the tests will run using Chrome. You can run them in Firefox by overriding the `driver` property in file serenity.properties

```Gherkin
webdriver.driver=chrome
or
webdriver.driver=firefox
```

By default, the tests will run scenario `@CT1`.
You can change it at the `tags` attribute in file `MercedesTestSuite.java`

```java
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        tags = "@CT1",
        publish = true
)
public class MercedesTestSuite {

    @BeforeClass
    public static void before() {
    }

    @AfterClass
    public static void after() throws Exception {
    }

}
```

## Project details
Bellow some details of the project

### Feature file
The file with the feature and Gherkin syntax and the designed scenarios is `ChallengeFeature.feature`

### Gherkin syntax was used
BDD scenarios typically follow a structured format known as the Given-When-Then (GWT) format.

Each scenario consists of:
```Gherkin
- a context (Given)
- an action or event (When)
- and the expected outcome (Then)
```
Adhering to this format provides clarity and makes the scenarios more readable.

### The scenario requested in this challenge
Both variations of the project uses Gherkin syntax
```Gherkin
Feature: Challenge for Mercedes Benz

@CT1
Scenario: Validate A Class models price are between £15,000 and £60,000
    Given Hugo is researching is researching for specific models
    When he selects model "Hatchbacks"
    And he does mouse over "A Class" model available
    And then proceeds to "Build your car"
    And filter by Fuel type "Diesel"
    Then he looks up prices are between "15.000" to "60.000"
```

### General and reusable scenario
```Gherkin
Feature: Challenge for Mercedes Benz

@CT2
Scenario Outline: Validate A Class models price are between £15,000 and £60,000
    Given "<User>" is researching for specific models
    When "<User>" selects model "<Model>"
    And "<User>" does mouse over "<CarClass>" model available
    And then proceeds to "<SiteArea>"
    And filter by Fuel type "<FuelType>"
    Then he looks up prices are between "<MinValue>" to "<MaxValue>"

Examples:
| User | Model      | CarClass | SiteArea       | FuelType | MinValue | MaxValue |
| Hugo | Hatchbacks | A Class  | Build your car | Diesel   | 15.000   | 60.000   |
| Peter| Hatchbacks | C Class  | Build your car | Gas      | 21.000   | 90.000   |
```
### Screenplay pattern adopted
The automation followed and uses the Screenplay pattern.

The Screenplay pattern describes tests in terms of actors and the tasks they perform.
Tasks are represented as objects performed by an actor whenever possible, falling back to classical methods.

This makes them more flexible and composable, at the cost of being a bit more wordy

### The Screenplay implementation
Aa we can see bellow at the `MercedesStepDefinitions.java` class
screenplay classes emphasise reusable components and a very readable declarative style.
```java
    @Given("{actor} is researching for specific models")
    public void researchingThings(Actor actor) {
            actor.wasAbleTo(NavigateToMercedesBenz.theMercedesBenzHomePage());
    }
    
    @When("{actor} selects model {string}")
    public void selectsModel(Actor actor, String term) {
            mercedesBenzHomePage.acceptCookies();
            mercedesBenzHomePage.selectModel();
    }
    
    @And("{actor} does mouse over {string} model available")
    public void mouseOverModel(Actor actor, String term) {
            mercedesBenzHomePage.mouseOverModel();
    }
    
    @And("then proceeds to {string}")
    public void proceedTo(String term) {
            mercedesBenzHomePage.buildYourCar();
    }
    
    @And("filter by Fuel type {string}")
    public void filterBy(String fuelType) {
            mercedesBenzHomePage.filterByFuel(fuelType);
    }
    
    @Then("{actor} looks up prices are between {string} to {string}")
    public void assuresMinMaxPriceInterval(Actor actor, String lowerLimit, String upperLimit) {
            mercedesBenzHomePage.assuresMinMaxPriceInterval(lowerLimit, upperLimit);
    }
```

### Navigate to
The `NavigateToMercedesBenz.java` class is responsible for opening the home page:
```java
public static Performable theMercedesBenzHomePage() {
    WebDriver driver = getDriver();
    driver.manage().window().maximize();
    return Task.where("{0} opens Mercedes Benz UK home page",
    Open.browserOn().the(MercedesBenzHomePage.class));
}
```

### Page interactions
The `MercedesBenzHomePage.java` class does the actual page interactions:
```java
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
```

### Step implementation
Whereas the actual step implementation is done at `ShadowRoot.java` class.

Mercedes home page implements ShwdowRoot elements, so this shadow cast approach was implemented using the shadow name itself just for better readability and separation of concerns:
```java
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

        /** element for click accept all cookies */
        WebElement btn = shadowRoot.findElement(By.cssSelector(BTN_ACCEPT_ALL_COOKIES));
        standBy(clockMs);

        return btn;
    }

    public static WebElement selectModel() {
        and the code goes on...
```
### The path to every element

For avoid having hardcoded paths, all elements are mapped within `MercedesBenzElements.java` class
```java
public class MercedesBenzElements {

    public static final String SHADOW_ROOT_HOME = "/html/body/div[1]/div/div/div/dh-io-vmos";
    public static final String SHADOW_ROOT_COOKIES = "/html/body/cmm-cookie-banner";
    public static final String SHADOW_ROOT_LIST = "/html/body/div[1]/div/div/div/owcc-car-configurator";
    public static final String LABEL_PRICE_TAG = "cc-motorization-header__price--with-environmental-hint";
    public static final String BTN_ACCEPT_ALL_COOKIES = "div > div > div > cmm-buttons-wrapper > div > div > button:nth-child(2)";
    public static ...
    and the code goes on...

```
### Evidences
Evidences are available at the root of the project, inside the folder `evidences` in the form of a:
- png snapshot
- text file

As required by the challenge


### Final thoughts
This project of the tryed to follow _Single Responsibility Principle_, of witch I'm a big fan

A lot could be improved of course.

The main advantage of the approach used in this example is not in the lines of code written due to the small size of the complexity, although the project was VERY HARD TO DO ;)


