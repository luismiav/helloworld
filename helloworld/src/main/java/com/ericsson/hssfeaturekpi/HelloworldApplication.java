package com.ericsson.hssfeaturekpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication
@RestController

public class HelloworldApplication {
	
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
	
	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}
	
	@HystrixCommand (fallbackMethod="GetDefaultGreeting")
	@RequestMapping(value="/greeting", method=RequestMethod.GET)
	public String sayHello(){
		
		String greeting ="Hello HSS guys from the spring app ";
		
		ResponseEntity<String> message = restTemplate.getForEntity("http://hello-message/greeting", String.class);
		
		return greeting +"\n"+ message.getBody();
	}
	
	public String GetDefaultGreeting () {
		System.out.println("Fallback to default greeting");
		
		String greeting ="Hello HSS guys from the spring app ";
		
		return greeting + "\n This is the defult message since the service offering message is failing"; 
	}
}
