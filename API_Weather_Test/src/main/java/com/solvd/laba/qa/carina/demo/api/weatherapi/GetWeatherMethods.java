package com.solvd.laba.qa.carina.demo.api.weatherapi;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.utils.Configuration;

@Endpoint(url = "${base_url}//data/2.5/weather?q={city_name}&appid={Api_Key}", methodType = HttpMethodType.GET)
@ResponseTemplatePath(path = "api/weather/_get/response.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.OK_200)
public class GetWeatherMethods extends AbstractApiMethodV2 {

  public GetWeatherMethods() {
    replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
  }
}
