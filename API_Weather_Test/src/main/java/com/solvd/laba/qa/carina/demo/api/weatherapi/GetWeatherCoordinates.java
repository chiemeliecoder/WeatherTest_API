package com.solvd.laba.qa.carina.demo.api.weatherapi;


import com.solvd.laba.qa.carina.demo.api.configuration.WeatherProperties;
import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.utils.config.Configuration;
import io.restassured.response.Response;
import java.lang.invoke.MethodHandles;
import java.util.Properties;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Endpoint(url = "${base_url}/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${api_key}", methodType = HttpMethodType.GET)
@ResponseTemplatePath(path = "api/weather/_get/response.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class GetWeatherCoordinates extends AbstractApiMethodV2 {
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private Response response;


  public GetWeatherCoordinates(double lon, double lat) {
    Properties properties = WeatherProperties.getProperties();
    replaceUrlPlaceholder("base_url", Configuration.getRequired("api_url"));
    replaceUrlPlaceholder("name", properties.getProperty("name"));
    replaceUrlPlaceholder("api_key", properties.getProperty("api_key"));
    replaceUrlPlaceholder("lat", String.valueOf(lat));
    replaceUrlPlaceholder("lon", String.valueOf(lon));
  }




}
