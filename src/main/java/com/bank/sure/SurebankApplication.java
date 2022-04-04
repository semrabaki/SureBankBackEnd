package com.bank.sure;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.NamingConventions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SurebankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SurebankApplication.class, args);
	}
	
	//model mapper is easy way to convert one object to an other object
	@Bean
	public ModelMapper modelMapper() {
		
		ModelMapper modelMapper= new ModelMapper();
		modelMapper.getConfiguration()
		.setFieldMatchingEnabled(true)
		.setFieldAccessLevel(AccessLevel.PRIVATE)
		.setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
		
		return modelMapper;
		
		
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			
			public void addCorsMappings(CorsRegistry registry)
			{
				registry.addMapping("/**").allowedOrigins("*").allowedHeaders("*").allowedMethods("*");
			}
		};
	}

}
