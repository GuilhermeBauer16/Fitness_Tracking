package com.github.GuilhermeBauer16.FitnessTracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class FitnessTrackingApplication {

	public static void main(String[] args) {

		SpringApplication.run(FitnessTrackingApplication.class, args);



	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		int strength = 10;
		return new BCryptPasswordEncoder(strength, new SecureRandom());
	}

}
