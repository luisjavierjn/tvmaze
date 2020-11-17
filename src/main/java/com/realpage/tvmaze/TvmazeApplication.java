package com.realpage.tvmaze;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.realpage.tvmaze.repositories.memrepo.ShowRepositoryInMemory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@SpringBootApplication
public class TvmazeApplication {

	@Value("${tvmaze.url.all.shows}")
	private String urlAllShows;

	public static void main(String[] args) {
		SpringApplication.run(TvmazeApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public ShowRepositoryInMemory getAllShowsFromTVMaze() throws JsonProcessingException {
		ShowRepositoryInMemory showRepositoryInMemory = new ShowRepositoryInMemory();
		ObjectMapper mapper = new ObjectMapper();
		RestTemplate restTemplate = new RestTemplate();

		int count = 1;
		ResponseEntity<String> response;
		do {
			response = restTemplate.getForEntity(urlAllShows.concat(Integer.toString(count++)), String.class);
			JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
			showRepositoryInMemory.save(root);
		} while(response.getStatusCode() == HttpStatus.OK && count < 5);

		return showRepositoryInMemory;
	}
}
