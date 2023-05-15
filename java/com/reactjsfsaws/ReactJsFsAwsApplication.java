package com.reactjsfsaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ReactJsFsAwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactJsFsAwsApplication.class, args);
	}
	
	 @Bean

	  public ObjectMapper getObjectMapper() {

	    return new ObjectMapper();

	  }
	 
	 @Bean
		public CacheManager cacheManager(){
			return new ConcurrentMapCacheManager("ticketsCache");
		}

}
