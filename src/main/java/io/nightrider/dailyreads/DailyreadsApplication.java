package io.nightrider.dailyreads;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import io.nightrider.dailyreads.connection.DataStaxProperties;

@SpringBootApplication
public class DailyreadsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DailyreadsApplication.class, args);
	}

	@Bean
	public CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer(DataStaxProperties dataStaxProperties){
		Path bundle = dataStaxProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

}
