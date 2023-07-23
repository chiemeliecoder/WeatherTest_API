package com.solvd.laba.qa.carina.demo.api.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherProperties {


  private static final String PROPERTIES_FILE = "weather.properties";
  private static Properties properties;



//  private static Properties properties;
  private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  static {
    initialize();
  }

  public static void initialize() {
    properties = new Properties();
    String wFile = "src/test/resources/api/weather/weather.properties";

    try(FileInputStream fileInputStream = new FileInputStream(wFile)) {
      properties.load(fileInputStream);
    } catch (IOException e) {
      throw new RuntimeException("Unable to configure settings", e);
    }
  }

  public static Properties getProperties() {
    if (properties == null) {
      try (InputStream input = WeatherProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
        properties = new Properties();
        properties.load(input);
      } catch (IOException ex) {
        ex.printStackTrace();
        // Handle the exception, e.g., log an error or throw a custom exception.
      }
    }
    return properties;
  }
}
