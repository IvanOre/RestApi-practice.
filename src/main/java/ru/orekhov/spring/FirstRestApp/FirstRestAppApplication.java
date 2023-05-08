package ru.orekhov.spring.FirstRestApp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FirstRestAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirstRestAppApplication.class, args);
	}

	@Bean // создали бин для мапера в контексте спринга
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

}
