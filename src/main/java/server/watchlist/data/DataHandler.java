package server.watchlist.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;

public class DataHandler {
	private static User[] users;
	private static ConcurrentLinkedQueue<String> requestQ = new ConcurrentLinkedQueue<String>();
	private static ConcurrentLinkedQueue<String> responseQ = new ConcurrentLinkedQueue<String>();
	private static HashMap<Integer, ResponseObject> details = new HashMap<Integer, ResponseObject>();
	private static Thread t;
	
	public static void startup() throws StreamReadException, DatabindException, IOException {
		deserializeUsers();
		startApiThread();
		for(User u : users) {
			for(AnimeEntry ae : u.getList()) {
				requestInfoById(ae.getId());
			}
		}
	}
	
	public static void save() {
		try {
			serializeUsers();
		} catch (IOException e) {
			e.printStackTrace();
		}
		refreshDetails();
	}
	
	private static void startApiThread() {
		t = new Thread(new ApiThread(requestQ, responseQ));
		t.start();
	}
	
	public static ResponseObject getDetails(int id) {
		return details.getOrDefault(id, new ResponseObject(new ResponseObject.Data(new ResponseObject.Data.Media(id, 0, new String[] {}, new ResponseObject.Data.Media.Title("", ""), new ResponseObject.Data.Media.CoverImage("https://jatheon.com/wp-content/uploads/2018/03/404-image.png")))));
	}
	
	public static void requestInfoById(int id) {
		String requestString = buildRequestString("Int", "id", id);
		queueRequest(requestString);
	}
	
	public static ResponseObject requestInfoBySearch(String search) {
		while(!requestQ.isEmpty());
		ConcurrentLinkedQueue<String> qIn_manual = new ConcurrentLinkedQueue<String>();
		ConcurrentLinkedQueue<String> qOut_manual = new ConcurrentLinkedQueue<String>();
		Thread t_manual = new Thread(new ApiThread(qIn_manual, qOut_manual));
		String requestString = buildRequestString("String", "search", "\"" + search + "\"");
		t_manual.start();
		qIn_manual.offer(requestString);
		
		final long T_TIMEOUT = 25_000000000L;
		long start = System.nanoTime();
		
		while(qOut_manual.isEmpty() && (System.nanoTime() - start - T_TIMEOUT < 0));
		String out = qOut_manual.poll();
		if(out == null)
			return null;
		try {
			return deserializeResponse(out);
		} catch (JsonProcessingException e) {
			System.out.println("error while deserializing");
			return null;
		}
	}
	
	private static void queueRequest(String requestString) {
		requestQ.offer(requestString);
	}
	
	private static <T> String buildRequestString(String variableType, String variableName, T variableValue) {
		String requestString = "";
		requestString += "{\"query\":\"query ($";
		requestString += variableName;
		requestString += ": ";
		requestString += variableType;
		requestString += ") {";
		requestString += "Media(" + variableName + ": $" + variableName + ", type: ANIME){id episodes title{romaji english} coverImage{medium} genres}"; //internal query (from graphql query builder)
		requestString += "}\", \"variables\": { \"";
		requestString += variableName;
		requestString += "\": ";
		requestString += variableValue;
		requestString += "}}";
		return requestString;
	}
	
	public static Cookie getUsernameCookie(String username) {
		Cookie c = new Cookie("username", username);
		c.setHttpOnly(true);
		c.setMaxAge(60 * 60 * 24 * 30 * 3);
		c.setPath("/");
		return c;
	}
	
	public static Cookie getSessionIdCookie(String username) {
		Cookie c;
		try {
			c = new Cookie("sessionId", getUser(username).getSessionId());
		} catch (Exception e) {
			return new Cookie("", "");
		}
		c.setHttpOnly(true);
		c.setMaxAge(60 * 60 * 24 * 30 * 3);
		c.setPath("/");
		return c;
	}
	
	public static void refreshDetails() {
		while(responseQ.peek() != null) {
			ResponseObject r = null;
			try {
				r = deserializeResponse(responseQ.poll());
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			if(r == null)
				continue;
			details.put(r.data.media.id, r);
		}
	}
	
	private static ResponseObject deserializeResponse(String responseString) throws JsonMappingException, JsonProcessingException {
		return new ObjectMapper().readValue(responseString, ResponseObject.class);
	}
	
	public static void deserializeUsers() throws StreamReadException, DatabindException, IOException {
		byte[] data = Files.readAllBytes(Paths.get("data.json"));
		users = new ObjectMapper().readValue(data, User[].class);
	}
	
	public static void serializeUsers() throws JsonProcessingException, IOException {
		Files.writeString(Paths.get("data.json"), new ObjectMapper().writeValueAsString(users), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
	}
	
	public static User[] getUsers() {
		return users;
	}
	
	public static User getUser(String name) throws Exception {
		for(User u : users) {
			if(u.getName().equals(name))
				return u;
		}
		throw new Exception("User not found");
	}
	
	public static String getSessionId(String username) throws Exception {
		return getUser(username).getSessionId();
	}
}
