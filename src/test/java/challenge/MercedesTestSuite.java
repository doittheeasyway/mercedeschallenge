package challenge;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        tags = "@CT1",
        publish = true,
        plugin = {"json:target/serenity-reports/cucumber_report.json"},
        features = "src/test/resources/features"
)
public class MercedesTestSuite {

    @BeforeClass
    public static void before() {
    }

    @AfterClass
    public static void after() throws Exception {
    }

}
