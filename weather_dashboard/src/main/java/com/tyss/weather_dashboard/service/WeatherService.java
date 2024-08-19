package com.tyss.weather_dashboard.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

@Service
public class WeatherService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

//    public JSONObject getWeather(String city) {
//        String url = String.format("%s?q=%s&appid=%s&units=metric", apiUrl, city, apiKey);
//
//        
//        
//        
//        try {
//            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//            return new JSONObject(response.getBody());
//        } catch (RestClientException e) {
//            // Return a JSON object with an error message
//            JSONObject errorResponse = new JSONObject();
//            errorResponse.put("error", "City not found or invalid input. Please enter a valid city name.");
//            return errorResponse;
//        }
//    }
    

    public JSONObject getWeather(String city) {
        try {
            // Encode the city name to handle spaces and special characters
            String encodedCity = URLEncoder.encode(city, "UTF-8");

            // Build the complete API URL
            String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                                             .queryParam("q", encodedCity)
                                             .queryParam("appid", apiKey)
                                             .queryParam("units", "metric")
                                             .toUriString();

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            // Handle different response statuses
            if (response.getStatusCode() == HttpStatus.OK) {
                return new JSONObject(response.getBody());
            } else {
                throw new RuntimeException("Failed to fetch weather data: " + response.getStatusCode());
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding city name: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error fetching weather data: " + e.getMessage());
        }
    }

}
