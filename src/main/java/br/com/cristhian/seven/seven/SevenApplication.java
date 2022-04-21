package br.com.cristhian.seven.seven;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.util.UriEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

@SpringBootApplication
public class SevenApplication {
	private static String apiKey  = System.getenv("IMDB_API_KEY");

	public static void main(String[] args) {
		SpringApplication.run(SevenApplication.class, args);
		System.out.println(apiKey);
		makeRequest("inception 2010");
	}

	public static void makeRequest(String query) {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = null;
		request = HttpRequesst.newBuilder()
				.uri(URI.create(String.format("https://imdb-api.com/en/API/Search/%s/%s", apiKey,
						UriEncoder.encode(query))))
				.GET().build();

		httpClient.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.thenAccept(System.out::println)
				.join();
	}

}
