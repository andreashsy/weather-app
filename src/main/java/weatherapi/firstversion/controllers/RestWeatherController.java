package weatherapi.firstversion.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import weatherapi.firstversion.service.WeatherServiceImpl;

@RestController
@RequestMapping(path="/weather", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestWeatherController {
    @Autowired
    private WeatherServiceImpl weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<String> cityFormAPI(@PathVariable String city, Model model) {
        String jsonData = weatherService.getWeather(city);
        return ResponseEntity.ok(jsonData);
    }
    
}
