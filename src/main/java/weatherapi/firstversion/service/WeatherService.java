package weatherapi.firstversion.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static weatherapi.firstversion.Constants.*;

import weatherapi.firstversion.model.Weather;
import weatherapi.firstversion.repositories.RedisRepo;

@Service
public class WeatherService {
    private final Logger logger = Logger.getLogger(WeatherService.class.getName());
    private String appid;
    @Autowired
    RedisRepo redisRepo;

    public WeatherService() {
        if ((OPEN_WEATHER_API != null) && (OPEN_WEATHER_API.trim().length() > 0)) {
            this.appid = OPEN_WEATHER_API;
        } else {
            this.appid = "failed";
        }
    }

    public List<Weather> getWeather(String city) {
        String jsonDataString;
        if (this.hasKey(city)) {
            //get from cache (redis)
            logger.log(Level.INFO, "City exists in cache, loading from Redis...");
            Optional<String> opt = redisRepo.get(city);
            jsonDataString = opt.get();
        } else {
            // get from Openweather API
            logger.log(Level.INFO, "City does not exist in cache, loading from Openweather API...");
            String encodedCity = city.replace(" ","+");
            
            String url = UriComponentsBuilder
                .fromUriString(OPENWEATHER_BASE_URL)
                .queryParam("q", encodedCity)
                .queryParam("appid", appid)
                .queryParam("units", "metric")
                .queryParam("lang", "en")
                .toUriString();
            logger.log(Level.INFO, "url: " + url);
            RestTemplate template = new RestTemplate();
            ResponseEntity<String> resp = template.getForEntity(url, String.class);
            if (resp.getStatusCode() != HttpStatus.OK) {
                throw new IllegalArgumentException("Error: status code %s".formatted(resp.getStatusCode().toString()));
            }
            jsonDataString = resp.getBody();
            //save to Redis cache
            redisRepo.save(city, jsonDataString);
            }
            // convert json string to weather object
            try (InputStream is = new ByteArrayInputStream(jsonDataString.getBytes())) {
                final JsonReader reader = Json.createReader(is);
                final JsonObject result = reader.readObject();
                final String temp = result.getJsonObject("main").getJsonNumber("temp").toString();
                final JsonArray readings = result.getJsonArray("weather");
                return readings.stream()
                    .map(v -> (JsonObject)v)
                    .map(Weather::create)
                    .map(w -> {
                        w.setCity(city);
                        w.setTemp(temp);
                        return w;
                    })                    
                    .collect(Collectors.toList());
            } catch (Exception e) {
                logger.log(Level.INFO, e.toString());
                return new LinkedList<Weather>();
            }    
    }

    public boolean hasKey(String key) {
        Optional<String> opt = redisRepo.get(key);
        return opt.isPresent();
    }
    
}
