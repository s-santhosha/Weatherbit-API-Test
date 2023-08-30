package test.java.WeatherBitApiTests;

import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.Base.BaseTest;
import test.java.utils.helpers.HelperMethods;

import java.util.List;

import static io.restassured.path.json.JsonPath.from;

public class MultipleCitiesTest extends BaseTest {

    HelperMethods helperMethods;

    @DataProvider(name = "cities")
    public Object[][] getLatLongData() {
        String[][] testCitiesIDRecords = getData("cities", "cities_data.xlsx");
        return testCitiesIDRecords;
    }

    @Test(dataProvider = "cities")
    public void getWeatherInfoWithCitiesIDTest(String citiesID) {

        helperMethods = new HelperMethods();

        RestAssured.baseURI = prop.getProperty("ServiceURL");

        Response response = RestAssured.given().
                param("cities", citiesID).
                param("key", prop.getProperty("API_KEY")).
                when().
                get("/current").
                then().
                assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

        String responseBody = response.getBody().asString();

        System.out.println("response is: " + responseBody);
        testReporter.log(LogStatus.INFO, "Response is : " + responseBody);
    }
}
