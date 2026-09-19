package com.journal.Journal.service;

import com.journal.Journal.api.response.WeatherApiResponse;
import com.journal.Journal.cache.AppCache;
import com.journal.Journal.constants.Placeholders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
//    @Value("${weather.api.key}")
//    private String weatherApiKey;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AppCache appCache;
    @Autowired
    private RedisService redisService;
    public WeatherApiResponse getWeatherInfo(String city) {
        WeatherApiResponse weatherApiResponse = redisService.get("weather_of_"+city,WeatherApiResponse.class);
        if(weatherApiResponse!=null) {
            return weatherApiResponse;
        }
        else {
            String finalApi = appCache.APP_CACHE.get(AppCache.keys.WEATHER_URI.toString()).replace(Placeholders.CITY,city).replace(Placeholders.API_KEY,appCache.APP_CACHE.get(AppCache.keys.WEATHER_API_KEY.toString()));
            ResponseEntity<WeatherApiResponse> response = restTemplate.exchange(finalApi, HttpMethod.GET,null, WeatherApiResponse.class);
            WeatherApiResponse body = response.getBody();
            if(body!=null) {
                redisService.set("weather_of_"+city,body,300L);
            }
            return body;
        }
    }
}
