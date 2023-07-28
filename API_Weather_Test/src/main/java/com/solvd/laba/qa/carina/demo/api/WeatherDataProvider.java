package com.solvd.laba.qa.carina.demo.api;

import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.DataProvider;

public class WeatherDataProvider {

  @DataProvider(name = "weatherlocation")
  public static Object[][] getWeatherData() {
    return new Object[][]{
        { -0.12574,51.50853}, //london
        {139.6917, 35.6895}, //tokyo
        {-71.0589, 42.3601} //boston
    };
  }

}
