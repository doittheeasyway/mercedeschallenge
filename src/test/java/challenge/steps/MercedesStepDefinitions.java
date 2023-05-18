package challenge.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import challenge.navigation.NavigateToMercedesBenz;
import challenge.pages.MercedesBenzHomePage;

public class MercedesStepDefinitions {

    MercedesBenzHomePage mercedesBenzHomePage = new MercedesBenzHomePage();

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

}
