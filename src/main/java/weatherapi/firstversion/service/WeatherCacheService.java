package weatherapi.firstversion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weatherapi.firstversion.repositories.RedisRepo;

@Service
public class WeatherCacheService {

    @Autowired
    RedisRepo redisRepo;

    public void cache(String cityName, String jsonStr) {
        redisRepo.save(cityName, jsonStr);
    }

    public String get(String cityName) {
        Optional<String> opt = redisRepo.get(cityName);        
        return opt.get();
    }

    public boolean hasKey(String key) {
        Optional<String> opt = redisRepo.get(key);
        return opt.isPresent();
    }
    
}
