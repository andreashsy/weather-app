package weatherapi.firstversion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weatherapi.firstversion.model.Weather;
import weatherapi.firstversion.repositories.RedisRepo;

@Service
public class WeatherCacheService {

    @Autowired
    RedisRepo redisRepo;

    public void cache(String cityName, Weather weather) {
        
    }

    public Optional<Weather> get(String cityName) {
        return null;
    }
    
}
