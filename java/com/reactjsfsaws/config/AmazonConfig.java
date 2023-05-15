package com.reactjsfsaws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonConfig<AWSCredentials> {
	
	 @Bean
	    public AmazonS3 S3() {
	        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
	                "accesskey for IAM role",
	                "secretkey for IAM role"
	        );

	        return AmazonS3ClientBuilder
	                .standard()
	                .withRegion("ap-south-1")
	                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
	                .build();
	    }

}
