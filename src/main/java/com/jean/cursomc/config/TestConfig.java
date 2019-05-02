package com.jean.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.jean.cursomc.services.DBService;
import com.jean.cursomc.services.EmailService;
import com.jean.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
	
	@Bean //o metodo bean serve como componente para injeção, assim, devolve a instancia desse bean na injeção
	public EmailService emailService() {
		return new MockEmailService();
	}
	
}
