package br.com.cristhian.seven.seven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootApplication
public class SevenApplication {
	private static String apiKey = System.getenv("IMDB_API_KEY");

	public static void main(String[] args) {
		SpringApplication.run(SevenApplication.class, args);
		String top250String = doGet();
		String itensString = top250String.substring(top250String.indexOf("[") + 1, top250String.lastIndexOf("]"));
		String[] items = itensString.split("},");
		List<String> titles = getTitles(items);
		List<String> fullTitles = getFullTitles(items);
		List<String> imageUrls = getUrls(items);
		List<String> imdbRatings = getRatings(items);
		List<String> ratingCounts = getRatingCounts(items);
	}

	private static List<String> getTitles(String[] items) {
		return getValues(items, "\"title\":\"(.*?)\"");
	}

	private static List<String> getFullTitles(String[] items) {
		return getValues(items, "\"fullTitle\":\"(.*?)\"");
	}

	private static List<String> getUrls(String[] items) {
		return getValues(items, "\"image\":\"(.*?)\"");
	}

	private static List<String> getRatings(String[] items) {
		return getValues(items, "\"imDbRating\":\"(.*?)\"");
	}

	private static List<String> getRatingCounts(String[] items) {
		return getValues(items, "\"imDbRatingCount\":\"(.*?)\"");
	}

	private static List<String> getValues(String[] items, String regex) {
		List<String> itemsList = new ArrayList<>();
		for (String item : items) {
			var pattern = Pattern.compile(regex);
			var matcher = pattern.matcher(item);
			if (matcher.find()) {
				itemsList.add(matcher.group(1));
			}
		}
		return itemsList;
	}

	public static String doGet() {
		HttpClient httpClient = HttpClient.newBuilder().build();
		HttpRequest request = null;
		request = HttpRequest
				.newBuilder()
				.uri(URI.create(String.format("https://imdb-api.com/en/API/Top250Movies/%s", apiKey)))
				.GET()
				.build();

		return httpClient.sendAsync(request, BodyHandlers.ofString())
				.thenApply(HttpResponse::body)
				.join();
	}

}
