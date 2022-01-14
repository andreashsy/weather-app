package weatherapi.firstversion.model;

import jakarta.json.JsonObject;

public class Weather {
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
}
