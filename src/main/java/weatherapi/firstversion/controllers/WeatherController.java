package weatherapi.firstversion.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import weatherapi.firstversion.model.Weather;
import weatherapi.firstversion.service.WeatherService;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(path="/", produces = MediaType.TEXT_HTML_VALUE)
public class WeatherController {
    private final Logger logger = Logger.getLogger(WeatherService.class.getName());
    private String city;
    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public String getCityForm(Model model) {
        return "index";
    }

    @PostMapping
    public String cityForm(@RequestBody MultiValueMap<String, String> form, Model model) {
        city = form.getFirst("cityname");
        String capitalisedCityName = city.substring(0, 1).toUpperCase() + city.substring(1);
        List<Weather> weatherList = weatherService.getWeather(city);
        for (Weather w:weatherList) {
            logger.log(Level.INFO, "City temp is " + w.getTemp());
            logger.log(Level.INFO, "City weatherDescription is " + w.getWeatherDescription());
            logger.log(Level.INFO, "City weatherMain is " + w.getWeatherMain());
        }
        model.addAttribute("cityname", capitalisedCityName);
        model.addAttribute("weather", weatherList.get(0));
        return "weather";
    }
    
    @GetMapping("/main")
    public String returnToIndex(Model model) {
        return "index";
    }
}
