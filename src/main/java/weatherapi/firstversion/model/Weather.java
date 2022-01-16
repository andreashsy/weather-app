package weatherapi.firstversion.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Weather {
    private static final Logger logger = Logger.getLogger(Weather.class.getName());
    private String temp;
    private String iconUrl; 
    private String city;
    private String weatherMain;
    private String weatherDescription;

    public Weather() {
    }

    public Weather(String temp) {
        this.temp = temp;
    }
    
    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTemp() {
        return this.temp;
    }

    public void setIconUrl(String icon) {
        this.iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public String getWeatherMain() {
        return this.weatherMain;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public String getWeatherDescription() {
        return this.weatherDescription;
    }

    public static Weather create(JsonObject o) {
        Weather w = new Weather();
        // w.setTemp(o.getJsonObject("main").getString("temp"));
        w.setIconUrl(o.getString("icon"));
        w.setWeatherMain(o.getString("main"));
        w.setWeatherDescription(o.getString("description"));
        return w;
    }

    public static List<Weather> jsonToWeatherObj(String jsonDataString) {
        try (InputStream is = new ByteArrayInputStream(jsonDataString.getBytes())) {
            final JsonReader reader = Json.createReader(is);
            final JsonObject result = reader.readObject();
            final String temp = result.getJsonObject("main").getJsonNumber("temp").toString();
            final String cityName = result.getString("name");
            final JsonArray readings = result.getJsonArray("weather");
            return readings.stream()
                .map(v -> (JsonObject)v)
                .map(Weather::create)
                .map(w -> {
                    w.setCity(cityName);
                    w.setTemp(temp);
                    return w;
                })                    
                .collect(Collectors.toList());
        } catch (Exception e) {
            logger.log(Level.INFO, e.toString());
            return new LinkedList<Weather>();
        }
    }
}
