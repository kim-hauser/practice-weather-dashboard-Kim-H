package org.example.weatherdashboard;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private static final String WEATHER_URL =
            "https://api.openweathermap.org/data/2.5/weather";

    private final RestTemplate restTemplate;
    private final String apiKey;

    public WeatherService() {
        this.restTemplate = new RestTemplate();
        this.apiKey = System.getenv("OPENWEATHER_API_KEY");
    }

    public WeatherResponse getCurrentWeather(String city) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "OPENWEATHER_API_KEY environment variable is not set."
            );
        }

        String url = UriComponentsBuilder
                .fromUriString(WEATHER_URL)
                .queryParam("q", city)
                .queryParam("appid", apiKey)
                .queryParam("units", "imperial")
                .toUriString();

        try {
            return restTemplate.getForObject(url, WeatherResponse.class);
        } catch (RestClientException exception) {
            throw new RuntimeException(
                    "Unable to retrieve weather for " + city + ".",
                    exception
            );
        }
    }
}