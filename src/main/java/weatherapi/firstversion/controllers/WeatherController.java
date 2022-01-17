package weatherapi.firstversion.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import weatherapi.firstversion.model.Weather;
import weatherapi.firstversion.service.WeatherServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(path="/weather")
public class WeatherController {
    private final Logger logger = Logger.getLogger(WeatherServiceImpl.class.getName());
    @Autowired
    private WeatherServiceImpl weatherService;

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String cityForm(@RequestParam(required=true) String city, Model model) {
        String jsonData = weatherService.getWeather(city);
        List<Weather> weatherList = Weather.jsonToWeatherObj(jsonData);
        for (Weather w:weatherList) {
            logger.log(Level.INFO, "City temp is " + w.getTemp());
            logger.log(Level.INFO, "City weatherDescription is " + w.getWeatherDescription());
            logger.log(Level.INFO, "City weatherMain is " + w.getWeatherMain());
        }
        model.addAttribute("city", weatherList.get(0).getCity());
        model.addAttribute("weather", weatherList.get(0));
        return "weather";
    }
    
    @GetMapping("/main")
    public String returnToIndex(Model model) {
        return "index";
    }
}
