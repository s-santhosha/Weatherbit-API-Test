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

public class LatLongTest extends BaseTest {

    HelperMethods helperMethods;

    @DataProvider(name = "Lat_Long")
    public Object[][] getLatLongData() {
        String[][] testLatLongRecords = getData("Lat_Long", "lat_long_data.xlsx");
        return testLatLongRecords;
    }

    // AC2. “As a frequent flyer, I want to get current weather data for the city at Latitude: -33.865143, Longitude: 151.209900”.

    @Test(dataProvider = "Lat_Long")
    public void getWeatherInfoWithLatLongTest(String lattitude, String longitude) {

        helperMethods = new HelperMethods();

        RestAssured.baseURI = prop.getProperty("ServiceURL");

        Response response = RestAssured.given().
                param("lat", lattitude).
                param("lon", longitude).
                param("key", prop.getProperty("API_KEY")).
                when().
                get("/current").
                then().
                assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

        String responseBody = response.getBody().asString();

        List<String> StateCodeReturned = from(responseBody).get("data.state_code");
        List<String> weatherReturned = from(responseBody).get("data.weather");

        System.out.println("response is: " + responseBody);
        testReporter.log(LogStatus.INFO, "Response is : " + responseBody);
        testReporter.log(LogStatus.INFO, "Weather is : " + weatherReturned);

    }
}
