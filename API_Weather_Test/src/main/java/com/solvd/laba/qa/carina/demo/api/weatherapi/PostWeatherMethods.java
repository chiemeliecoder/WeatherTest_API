package com.solvd.laba.qa.carina.demo.api.weatherapi;

import com.zebrunner.carina.api.AbstractApiMethodV2;
import com.zebrunner.carina.api.annotation.Endpoint;
import com.zebrunner.carina.api.annotation.RequestTemplatePath;
import com.zebrunner.carina.api.annotation.ResponseTemplatePath;
import com.zebrunner.carina.api.annotation.SuccessfulHttpStatus;
import com.zebrunner.carina.api.http.HttpMethodType;
import com.zebrunner.carina.api.http.HttpResponseStatusType;
import com.zebrunner.carina.utils.Configuration;

@Endpoint(url = "${base_url}/data/2.5/weather?q={city_name}&appid={Api_Key}", methodType = HttpMethodType.POST)
@RequestTemplatePath(path = "api/weather/_post/request.json")
@ResponseTemplatePath(path = "api/weather/_post/response.json")
@SuccessfulHttpStatus(status = HttpResponseStatusType.CREATED_201)
public class PostWeatherMethods extends AbstractApiMethodV2 {

  public PostWeatherMethods() {
    replaceUrlPlaceholder("base_url", Configuration.getEnvArg("api_url"));
  }
}
