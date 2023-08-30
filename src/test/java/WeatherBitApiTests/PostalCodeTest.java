package test.java.WeatherBitApiTests;

import test.java.Base.BaseTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.path.json.JsonPath.from;

public class PostalCodeTest extends BaseTest {


    @DataProvider(name = "Postal_Code")
    public Object[][] getPostalCodeData() {
        String[][] testPostalCodeRecords = getData("postal_code", "postal_code_data.xlsx");
        return testPostalCodeRecords;
    }

    @Test(dataProvider = "Postal_Code")
    public void getWeatherInfoWithPostalCodeTest(String postalCode) {

        RestAssured.baseURI = prop.getProperty("ServiceURL");

        Response response = RestAssured.given().
                param("postal_code", postalCode).
                param("key", prop.getProperty("API_KEY")).
                when().
                get("/current").
                then().
                assertThat().spec(checkStatusCodeAndContentType).and().extract().response();

        String responseBody = response.getBody().asString();
        System.out.println("response is: " + responseBody);

        ArrayList<String> returnedTimestamp = from(responseBody).get("data.timestamp_utc");

        ArrayList<Object> returnedWeather = from(responseBody).get("data.weather");


        for (int i = 0; i < returnedTimestamp.size(); i++) {
            testReporter.log(LogStatus.INFO, "Timestamp returned : " + returnedTimestamp.get(i) + "==>"+ "Weather is: " + returnedWeather.get(i));
            System.out.println("timestamp is: " + returnedTimestamp.get(i) + " weather is: " + returnedWeather.get(i));
        }
    }
}
