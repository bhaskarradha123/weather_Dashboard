package com.tyss.weather_dashboard.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tyss.weather_dashboard.service.WeatherService;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String getWeather(@RequestParam(value = "city", required = false) String city, Model model) {
//    	 Show the form again with the error message
    	if (city == null || city.isEmpty()) {
            model.addAttribute("error", "City name is required to get weather information.");
            return "weather";  
        }
    	try {
            JSONObject weatherData = weatherService.getWeather(city);

            // Extract data from the JSON object and add to the model
            model.addAttribute("location", weatherData.getString("name") + ", " + weatherData.getJSONObject("sys").getString("country"));
            model.addAttribute("temperature", weatherData.getJSONObject("main").getDouble("temp"));
            model.addAttribute("description", weatherData.getJSONArray("weather").getJSONObject(0).getString("description"));
            model.addAttribute("humidity", weatherData.getJSONObject("main").getInt("humidity"));
            model.addAttribute("windSpeed", weatherData.getJSONObject("wind").getDouble("speed"));
            model.addAttribute("visibility", weatherData.optInt("visibility", 0) / 1000.0);  // visibility in kilometers

            return "weather";
        } catch (Exception e) {
            model.addAttribute("error", "City not found or error fetching data. Please try again.");
            return "weather";
        }
    }
    
    
    
    
//    @GetMapping("/weather")
//    public String getWeather(@RequestParam(value = "city", required = false) String city, Model model) {
//        
//    	// Show the form again with the error message
//    	if (city == null || city.isEmpty()) {
//            model.addAttribute("error", "City name is required to get weather information.");
//            return "weather";  
//        }
//        
//
//        JSONObject weatherData = weatherService.getWeather(city);
//
//        if (weatherData.has("error")) {
//            model.addAttribute("error", weatherData.getString("error"));
//            return "weather";
//        }
//
//
//
//        model.addAttribute("location", weatherData.getString("name"));
//        model.addAttribute("temperature", weatherData.getJSONObject("main").getDouble("temp"));
//        model.addAttribute("description", weatherData.getJSONArray("weather").getJSONObject(0).getString("description"));
//        model.addAttribute("humidity", weatherData.getJSONObject("main").getInt("humidity"));
//        model.addAttribute("windSpeed", weatherData.getJSONObject("wind").getDouble("speed"));
//        model.addAttribute("visibility", weatherData.getInt("visibility") / 1000.0);
//
//        return "weather";
//    }

}
