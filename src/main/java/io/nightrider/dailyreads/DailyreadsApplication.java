package io.nightrider.dailyreads;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;


import io.nightrider.dailyreads.author.Author;
import io.nightrider.dailyreads.author.AuthorRepository;
import io.nightrider.dailyreads.connection.DataStaxProperties;

@SpringBootApplication
public class DailyreadsApplication {

	@Autowired AuthorRepository authorRepository;

	@Value("${data.dump.location.authors}")
	private String authorsDumpLocation;

	@Value("${data.dump.location.books}")
	private String booksDumpLocation;

	public static void main(String[] args) {
		SpringApplication.run(DailyreadsApplication.class, args);
	}
	
	public void initAuthors(){

		// Converting the location string to a Path
		Path path = Paths.get(authorsDumpLocation);

		// Extracting the file lines 
		try(Stream<String> lines =  Files.lines(path)){
			lines.forEach(line -> {
				String jsonString = line.substring(line.indexOf("{"));
				try {
					JSONObject jsonObject = new JSONObject(jsonString);

		// Creating author using line data
					Author author = new Author();
					author.setName(jsonObject.getString("name"));
					author.setId(jsonObject.getString("key").replace("/authors/", ""));

		// Persisting the author
					authorRepository.save(author);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}});
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void initBooks(){

	}

	@PostConstruct
	public void start(){
		initAuthors();
		initBooks();
	}

	@Bean
	public CqlSessionBuilderCustomizer cqlSessionBuilderCustomizer(DataStaxProperties dataStaxProperties){
		Path bundle = dataStaxProperties.getSecureConnectBundle().toPath();
		return builder -> builder.withCloudSecureConnectBundle(bundle);
	}

}
