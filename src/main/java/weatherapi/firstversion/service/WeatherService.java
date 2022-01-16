package weatherapi.firstversion.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static weatherapi.firstversion.Constants.*;

import weatherapi.firstversion.repositories.RedisRepo;

@Service
public class WeatherService {
    private final Logger logger = Logger.getLogger(WeatherService.class.getName());
    private String appid;
    @Autowired
    RedisRepo redisRepo;
    @Autowired
    WeatherCacheService weatherCacheService;

    public WeatherService() {
        if ((OPEN_WEATHER_API != null) && (OPEN_WEATHER_API.trim().length() > 0)) {
            this.appid = OPEN_WEATHER_API;
        } else {
            this.appid = "failed";
        }
    }

    public String getWeather(String city) {
        String jsonDataString;
        if (weatherCacheService.hasKey(city)) {
            //get from cache (redis)
            logger.log(Level.INFO, "City exists in cache, loading from Redis...");
            jsonDataString = weatherCacheService.get(city);
        } else {
            // get from Openweather API
            logger.log(Level.INFO, "City does not exist in cache, loading from Openweather API...");
            String encodedCity = city.replace(" ","+");  // api uses '+' instead of whitespace
            
            String url = UriComponentsBuilder
                .fromUriString(OPENWEATHER_BASE_URL)
                .queryParam("q", encodedCity)
                .queryParam("appid", appid)
                .queryParam("units", "metric")
                .queryParam("lang", "en")
                .toUriString();
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.getForEntity(url, String.class);
            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("Error: status code %s".formatted(resp.getStatusCode().toString()));
            }
            jsonDataString = resp.getBody();
            //save to Redis cache
            weatherCacheService.cache(city, jsonDataString);
            }
            return jsonDataString;   
    }
    
}
