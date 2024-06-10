package it.aulab.spec_prog_finale;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class SpecProgFinaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpecProgFinaleApplication.class, args);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ModelMapper instanceModelMapper(){
		ModelMapper mapper = new ModelMapper();
		return mapper;
	}

}
