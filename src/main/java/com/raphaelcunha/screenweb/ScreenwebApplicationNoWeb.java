package com.raphaelcunha.screenweb;

import com.raphaelcunha.screenweb.principal.Principal;
import com.raphaelcunha.screenweb.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
//public class ScreenwebApplicationNoWeb implements CommandLineRunner {
//
//	@Autowired
//	private SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenwebApplicationNoWeb.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//
//		Principal principal = new Principal(repository);
//		principal.showMenu();
//
//	}
//}
