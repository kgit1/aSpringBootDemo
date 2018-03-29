package com.kong.sboot.aSpringBootDemo;

import java.io.IOException;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;

import com.kong.sboot.aSpringBootDemo.github.GithubClient;
import com.kong.sboot.aSpringBootDemo.github.RepositoryEvent;

public class GithubPropertiesIndicator implements HealthIndicator{

	private final GithubClient githubClient;	
	
	public GithubPropertiesIndicator(GithubClient githubClient) {
		this.githubClient = githubClient;
	}



	@Override
	public Health health() {
		try {
			ResponseEntity<RepositoryEvent[]>response= this.githubClient.fetchEvents("spring-projects", "spring.name");
		if(response.getStatusCode().is2xxSuccessful()) {
			return Health.up().build();
		}else {
			return Health.down().build();
		}
		}catch (Exception e) {
			return Health.down(e).build();
		}
	}

}
