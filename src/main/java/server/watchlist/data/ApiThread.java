package server.watchlist.data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ApiThread implements Runnable {
	private final long DELTA_T = 1500000000L;

	private ConcurrentLinkedQueue<String> requestQ;
	private ConcurrentLinkedQueue<String> responseQ;
	private long lastT;

	public ApiThread(ConcurrentLinkedQueue<String> requestQ, ConcurrentLinkedQueue<String> responseQ) {
		this.requestQ = requestQ;
		this.responseQ = responseQ;
		lastT = System.nanoTime();
	}

	@Override
	public void run() {
		while (true) {
			while ((System.nanoTime() - lastT) - DELTA_T < 0);
			String requestString = null;
			while (requestString == null) {
				requestString = requestQ.poll();
			}

			// send request
			HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
			HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://graphql.anilist.co")).timeout(Duration.ofSeconds(20)).header("Content-Type", "application/json").POST(BodyPublishers.ofString(requestString)).build();
			String responseString;
			try {
				HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
				if(response.statusCode() == 200) {
					responseString = response.body();
				} else {
					responseString = null;
				}
			} catch (IOException | InterruptedException e) {
				responseString = null;
			}
			if(responseString == null)
				continue;
			//return responseString to other thread
			responseQ.offer(responseString);
		}
	}

}
