package org.example.weatherdashboard;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class WeatherDashboardApplication implements CommandLineRunner {

	private final WeatherService weatherService;

	public WeatherDashboardApplication(WeatherService weatherService) {
		this.weatherService = weatherService;
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherDashboardApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);

		boolean running = true;

		while (running) {
			displayMenu();

			System.out.print("Choose a city: ");
			String choice = scanner.nextLine();

			String city = switch (choice) {
				case "1" -> "St. Louis,MO,US";
				case "2" -> "Chicago,IL,US";
				case "3" -> "Nashville,TN,US";
				case "4" -> null;
				default -> "";
			};

			if (city == null) {
				running = false;
				System.out.println("Have a great day!");
			} else if (city.isBlank()) {
				System.out.println("Please enter a valid menu option.");
			} else {
				displayWeather(city);
			}
		}

		scanner.close();
	}

	private void displayMenu() {
		System.out.println();
		System.out.println("=== Morning Weather Dashboard ===");
		System.out.println("1. St. Louis");
		System.out.println("2. Chicago");
		System.out.println("3. Nashville");
		System.out.println("4. Exit");
		System.out.println();
	}

	private void displayWeather(String city) {
		try {
			WeatherResponse response =
					weatherService.getCurrentWeather(city);

			if (response == null) {
				System.out.println("No weather data was returned.");
				return;
			}

			String description = "Unavailable";

			if (response.getWeather() != null
					&& !response.getWeather().isEmpty()) {
				description =
						response.getWeather().get(0).getDescription();
			}

			System.out.println();
			System.out.println("--- Current Weather ---");
			System.out.println("City: " + response.getName());
			System.out.printf(
					"Temperature: %.1f°F%n",
					response.getMain().getTemp()
			);
			System.out.println(
					"Conditions: " + capitalize(description)
			);
			System.out.println(
					"Humidity: "
							+ response.getMain().getHumidity()
							+ "%"
			);

		} catch (RuntimeException exception) {
			System.out.println();
			System.out.println("Could not retrieve the weather.");
			System.out.println(
					"Check the city, internet connection, and API key."
			);
		}
	}

	private String capitalize(String text) {
		if (text == null || text.isBlank()) {
			return "Unavailable";
		}

		return Character.toUpperCase(text.charAt(0))
				+ text.substring(1);
	}
}