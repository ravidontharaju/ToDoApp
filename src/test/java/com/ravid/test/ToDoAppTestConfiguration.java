package com.ravid.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.ravid.main.repository.ToDoRepository;
import com.ravid.main.service.ToDoService;
import com.ravid.main.service.TwilioService;

@Configuration
@ComponentScan(basePackages = { "com.ravid.main" })
@PropertySource("classpath:todoapplication.properties")
public class ToDoAppTestConfiguration {

	@Bean
	@Primary
	public ToDoRepository getToDoRepository() {
		return Mockito.mock(ToDoRepository.class);
	}

	@Bean
	@Primary
	public ToDoService getToDoService() {
		return Mockito.mock(ToDoService.class);
	}

	@Bean
	@Primary
	TwilioService getTwilioService() {
		return Mockito.mock(TwilioService.class);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
