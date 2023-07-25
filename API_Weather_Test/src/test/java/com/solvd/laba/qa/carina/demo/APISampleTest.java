package com.solvd.laba.qa.carina.demo;

import com.solvd.laba.qa.carina.demo.api.weatherapi.GetWeatherMethods;
import com.zebrunner.carina.dataprovider.IAbstractDataProvider;
import com.zebrunner.carina.dataprovider.annotations.XlsDataSourceParameters;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.api.APIMethodPoller;
import com.zebrunner.carina.api.apitools.validation.JsonCompareKeywords;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import org.testng.asserts.SoftAssert;
import static org.hamcrest.Matchers.lessThan;
import static io.restassured.RestAssured.given;




/**
 * This sample shows how create REST API tests.
 *
 * @author cezeokeke
 */
public class APISampleTest implements IAbstractTest, IAbstractDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testGetWeather(){
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPIExpectSuccess();
        getWeatherMethods.validateResponse(JSONCompareMode.LENIENT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        getWeatherMethods.validateResponseAgainstSchema("api/weather/_get/response.schema");
    }



    // Create a new data provider method to read the weather data from the CSV file
    @DataProvider(name = "weatherData")
    public Object[][] getWeatherData() {
        return new Object[][]{
            // No need to provide any data here as @CsvDataSourceParameters will handle it
//
//            {
//                createWeatherDataMap(51.50853, -0.12574, 1546300800L, "2019-01-01 00:00:00 +0000 UTC",
//                    1546300800L, "2019-01-01 00:00:00 +0000 UTC", 8.08, 4.63,
//                    1034.848, 1029.908, 78.61, null, 2.959, 292.075, null, null,
//                    null, 0, null, null, null, null, null)
//            },
//            {
//                createWeatherDataMap(51.50853, -0.12574, 1546300800L, "2019-01-01 00:00:00 +0000 UTC",
//                    1546311600L, "2019-01-01 03:00:00 +0000 UTC", 6.34, 4.6,
//                    1034.323, 1029.479, 88.804, 66, 3.277, 280.693, 0, 0,
//                    0, null, null, null, null, null, null)
//            }
        };
    }


    // Helper method to create the weather data map
//    private Map<String, Object> createWeatherDataMap(double lat, double lon, long forecastDtUnixTime, String forecastDtIso,
//        long sliceDtUnixTime, String sliceDtIso, Double temperature, Double dewPoint, double pressure,
//        double groundPressure, double humidity, Integer clouds, Double windSpeed, double windDeg,
//        Integer rain, Integer snow, Integer ice, Integer frRain, Double convective, String snowDepth,
//        String accumulated, String hours, String rate)  {
//        Map<String, Object> weatherData = new HashMap<>();
//        weatherData.put("lat", lat);
//        weatherData.put("lon", lon);
//        weatherData.put("forecast dt unixtime", forecastDtUnixTime);
//        weatherData.put("forecast dt iso", forecastDtIso);
//        weatherData.put("slice dt unixtime", sliceDtUnixTime);
//        weatherData.put("slice dt iso", sliceDtIso);
//        weatherData.put("temperature", temperature);
//        weatherData.put("dew_point", dewPoint);
//        weatherData.put("pressure", pressure);
//        weatherData.put("ground_pressure", groundPressure);
//        weatherData.put("humidity", humidity);
//        weatherData.put("clouds", clouds);
//        weatherData.put("wind_speed", windSpeed);
//        weatherData.put("wind_deg", windDeg);
//        weatherData.put("rain", rain != null ? rain : 0); // Store as Integer, not String
//        weatherData.put("snow", snow != null ? snow : 0); // Store as Integer, not String
//        weatherData.put("ice", ice != null ? ice : 0); // Store as Integer, not String
//        weatherData.put("fr_rain", frRain);
//        weatherData.put("convective", convective);
//        weatherData.put("snow_depth", snowDepth);
//        weatherData.put("accumulated", accumulated);
//        weatherData.put("hours", hours);
//        weatherData.put("rate", rate);
//        return weatherData;
//    }






    @Test(dataProvider = "DataProvider")
    @MethodOwner(owner = "cezeokeke")
    @XlsDataSourceParameters(path = "data_source/weatherDa.xlsx",  sheet = "weatherdata", dsUid = "forecast dt unixtime")
    public void testWeatherForecast(Map<String, String> weatherData){
        // Extract the values and handle null cases
        String temperatureStr = weatherData.get("temperature");
        String dewPointStr = weatherData.get("dew_point");
        String windSpeedStr = weatherData.get("wind_speed");

        SoftAssert softAssert = new SoftAssert();

        // Assertion 1: Check if temperature and dew point are not null
        softAssert.assertNotNull(temperatureStr, "Temperature should not be null.");
        softAssert.assertNotNull(dewPointStr, "Dew point should not be null.");

        // Convert values to double only if they are not null
        if (temperatureStr != null && dewPointStr != null) {
            double temperature = Double.parseDouble(temperatureStr);
            double dewPoint = Double.parseDouble(dewPointStr);

            // Assertion 2: Check if temperature is greater than dew point
            softAssert.assertTrue(temperature > dewPoint, "Temperature should be greater than dew point.");
        }

        // Assertion 3: Check if wind speed is not null
        softAssert.assertNotNull(windSpeedStr, "Wind speed should not be null.");

        // Convert wind speed value to double only if it's not null
        if (windSpeedStr != null) {
            double windSpeed = Double.parseDouble(windSpeedStr);

            // Assertion 4: Check if wind speed is not negative
            softAssert.assertTrue(windSpeed >= 0, "Wind speed should not be negative.");
        }
        

        softAssert.assertAll();

    }

    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testPositiveGetWeather() {
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        Response response = getWeatherMethods.callAPIExpectSuccess();

        // Validate the response status code
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);

        // Validate the response data (e.g., check if certain fields are present)
        String cityName = response.jsonPath().getString("name");
        Assert.assertEquals(cityName, "Boston");

        // You can add more validation checks here as per your API response format
    }


    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testGetWeatherWithValidParameters() {
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPIExpectSuccess();
        given()
            .baseUri("https://api.openweathermap.org")
            .queryParam("q", "Boston") // Replace "Boston" with the location for which you want to get weather data
            .queryParam("appid", "4aed356c1b4c1a75dac444d72901d405") // Replace "your_api_key_here" with your actual API key
            .when()
            .get("/data/2.5/weather")
            .then()
            .statusCode(200); // Assuming 200 OK for a successful request
    }


    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testNegativeInvalidQueryParam() {
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPI();
        Response response = given()
            .baseUri("https://api.openweathermap.org")
            .queryParam("invalid_param", "value")
            .when()
            .get("/data/2.5/weather") // Assuming the weather endpoint is '/weather' for invalid query parameter test
            .then()
            .statusCode(401) // Assuming 404 Bad Request for invalid query parameter
            .extract().response();

        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }

    // Boundary Value Test Case - Verify the API's behavior with boundary values
    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testBoundaryValuePagination() {
        // Assuming the API supports pagination with a maximum limit of 100 records per page
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPI();
        Response response =given()
            .baseUri("https://api.openweathermap.org")
            .queryParam("page", "1")
            .queryParam("limit", "100")
            .when()
            .get("/data/2.5/weather") // Assuming the weather endpoint is '/weather' for boundary value test
            .then()
            .statusCode(401) //Unauthorized
            .extract().response();

        System.out.println("Actual Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }




    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testErrorHandling() {
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        given()
            .baseUri("https://api.openweathermap.org")
            .when()
            .get("/nonexistent")
            .then()
            .statusCode(404); // Assuming 404 Not Found for a non-existent endpoint
    }


    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testMissingAPIKey() {
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPI();
        given()
            .baseUri("https://api.openweathermap.org")
            .queryParam("q", "Boston") // Replace "Boston" with the location for which you want to get weather data
            .when()
            .get("/data/2.5/weather")
            .then()
            .statusCode(401); // Assuming 401 Unauthorized for a missing API key
    }


    // Response Time Testing - Verify the API's response time meets requirements
    @Test
    @MethodOwner(owner = "cezeokeke")
    public void testResponseTime() {
        // Ignore SSL certificate validation (for testing only)
        RestAssured.useRelaxedHTTPSValidation();
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPI();
        Response response =given()
            .baseUri("https://api.openweathermap.org")
            .queryParam("q", "Boston") // Replace "Boston" with the location for which you want to get weather data
            .queryParam("appid", "4aed356c1b4c1a75dac444d72901d405") // Replace "4aed356c1b4c1a75dac444d72901d405" with your actual API key
            .when()
            .get("/data/2.5/weather")
            .then()
            .time(lessThan(5000L)) // Assuming the response time should be less than 5 seconds
            .extract().response();

        // Print out the response time
        long responseTime = response.timeIn(TimeUnit.MILLISECONDS);
        System.out.println("Response Time: " + responseTime + " milliseconds");


        System.out.println("Response Body: " + response.getBody().asString());
    }


    @Test()
    @MethodOwner(owner = "cezeokeke")
    public void testCreateWeather(){
        LOGGER.info("test");
        GetWeatherMethods api = new GetWeatherMethods();
        api.setProperties("api/weather/weather.properties");

        AtomicInteger counter = new AtomicInteger(0);

        api.callAPIWithRetry()
            .withLogStrategy(APIMethodPoller.LogStrategy.ALL)
            .peek(rs -> counter.getAndIncrement())
            .until(rs -> counter.get() == 4)
            .pollEvery(1, ChronoUnit.SECONDS)
            .stopAfter(10, ChronoUnit.SECONDS)
            .execute();

        api.validateResponse();
    }

}
