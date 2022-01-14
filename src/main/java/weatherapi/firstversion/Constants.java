package weatherapi.firstversion;

public class Constants {
    public static final String OPEN_WEATHER_API = System.getenv("OPENWEATHERAPI");
    public static final String REDISPASSWORD = System.getenv("RedisPwd");
    public static final String REDIS_WEATHER_BEAN = "REDIS_WEATHER";
    public static final String OPENWEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
}
