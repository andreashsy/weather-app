package weatherapi.firstversion.repositories;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import weatherapi.firstversion.Constants;

@Repository
public class RedisRepo {
    @Autowired
    @Qualifier(Constants.REDIS_WEATHER_BEAN)
    RedisTemplate<String, String> template;

    public void save(String key, String jsonString) {
        template.opsForValue().set(key, jsonString, 15, TimeUnit.MINUTES);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(template.opsForValue().get(key));
    }

}
