package com.raphaelcunha.screenweb;

import com.raphaelcunha.screenweb.model.SerieData;
import com.raphaelcunha.screenweb.service.ConsumerAPI;
import com.raphaelcunha.screenweb.service.DataConversion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenwebApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenwebApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var consumerAPI = new ConsumerAPI();
		var url = "https://omdbapi.com/?t=gilmore+girls&Season=1&apikey=739d4cd";
		var json = consumerAPI.getData(url);
		System.out.println(json);

		DataConversion dataConversion = new DataConversion();
		SerieData data = dataConversion.getData(json, SerieData.class);

		System.out.println(data);

	}
}
