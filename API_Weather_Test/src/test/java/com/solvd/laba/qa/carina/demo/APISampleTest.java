package com.solvd.laba.qa.carina.demo;

import com.solvd.laba.qa.carina.demo.api.weatherapi.DeleteWeatherMethods;
import com.solvd.laba.qa.carina.demo.api.weatherapi.GetWeatherMethods;
import com.solvd.laba.qa.carina.demo.api.weatherapi.PostWeatherMethods;
import io.restassured.response.Response;
import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.zebrunner.carina.core.IAbstractTest;
import com.solvd.laba.qa.carina.demo.api.DeleteUserMethod;
import com.solvd.laba.qa.carina.demo.api.GetUserMethods;
import com.solvd.laba.qa.carina.demo.api.PostUserMethod;
import com.zebrunner.carina.api.APIMethodPoller;
import com.zebrunner.carina.api.apitools.validation.JsonCompareKeywords;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.core.registrar.tag.Priority;
import com.zebrunner.carina.core.registrar.tag.TestPriority;

/**
 * This sample shows how create REST API tests.
 *
 * @author qpsdemo
 */
public class APISampleTest implements IAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Test
    public void testGetWeather(){
        GetWeatherMethods getWeatherMethods = new GetWeatherMethods();
        getWeatherMethods.callAPIExpectSuccess();
        getWeatherMethods.validateResponse(JSONCompareMode.LENIENT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        getWeatherMethods.validateResponseAgainstSchema("api/weather/_get/response.schema");
    }

    @Test
    public void testDeleteWeather(){
        DeleteWeatherMethods deleteWeatherMethods = new DeleteWeatherMethods();
        deleteWeatherMethods.setProperties("api/weather/weather.properties");
        deleteWeatherMethods.callAPIExpectSuccess();
        deleteWeatherMethods.validateResponse();
    }


    @Test
    public void testPostWeather() {
        PostWeatherMethods postWeatherMethods = new PostWeatherMethods();
        postWeatherMethods.setProperties("api/weather/weather.properties");
        postWeatherMethods.addProperty("sys_country", "US");

        // making the call to endpoint
        Response response = postWeatherMethods.callAPIExpectSuccess();
        String cloud = response.jsonPath().getString("0.cloud.all");
        LOGGER.info("cloud=" + cloud);

        Assert.assertEquals(cloud, 40, "cloud all is not as expected");

        postWeatherMethods.validateResponse();
    }

    @Test
    public void testPostWeatherMissingSomeFields() throws Exception {
        PostWeatherMethods postWeatherMethods = new PostWeatherMethods();
        postWeatherMethods.getProperties().remove("name");
        postWeatherMethods.getProperties().remove("weather_main");
        postWeatherMethods.callAPIExpectSuccess();
        postWeatherMethods.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "qpsdemo")
    public void testCreateUser() throws Exception {
        LOGGER.info("test");
        setCases("4555,54545");
        PostUserMethod api = new PostUserMethod();
        api.setProperties("api/users/user.properties");

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

    @Test()
    @MethodOwner(owner = "qpsdemo")
    public void testCreateUserMissingSomeFields() throws Exception {
        PostUserMethod api = new PostUserMethod();
        api.setProperties("api/users/user.properties");
        api.getProperties().remove("name");
        api.getProperties().remove("username");
        api.callAPIExpectSuccess();
        api.validateResponse();
    }

    @Test()
    @MethodOwner(owner = "qpsdemo")
    public void testGetUsers() {
        GetUserMethods getUsersMethods = new GetUserMethods();
        getUsersMethods.callAPIExpectSuccess();
        getUsersMethods.validateResponse(JSONCompareMode.STRICT, JsonCompareKeywords.ARRAY_CONTAINS.getKey());
        getUsersMethods.validateResponseAgainstSchema("api/users/_get/rs.schema");
    }

    @Test()
    @MethodOwner(owner = "qpsdemo")
    @TestPriority(Priority.P1)
    public void testDeleteUsers() {
        DeleteUserMethod deleteUserMethod = new DeleteUserMethod();
        deleteUserMethod.setProperties("api/users/user.properties");
        deleteUserMethod.callAPIExpectSuccess();
        deleteUserMethod.validateResponse();
    }

}
