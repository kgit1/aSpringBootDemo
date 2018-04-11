package com.kong.sboot.aSpringBootDemo;

import javax.validation.constraints.Pattern;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties("github")//means this pojo related to github namespace in application.properties
@Validated //tells spring you want to validate this bean, in our case github.token=test which 
//means token field in this bean equals "test", and app throws error that token field not matches pattern "\\w+:\\w+" 
public class GithubProperties {	
	
	/*
	 * Github API token("user:sampletoken")
	 */
	@Pattern(regexp="\\w+:\\w+")
	private String token;	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
