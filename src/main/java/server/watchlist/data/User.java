package server.watchlist.data;

import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
	private String name;
	private String sessionId;
	private ArrayList<AnimeEntry> list;
	private UserTitleLanguagePreference lang;
	
	@JsonCreator
	public User(@JsonProperty("name") String name, @JsonProperty("sessionId") String sessionId, @JsonProperty("list") ArrayList<AnimeEntry> list, @JsonProperty("lang") UserTitleLanguagePreference lang) {
		this.name = name;
		this.sessionId = sessionId;
		this.list = list;
		this.lang = lang;
	}
	
	public AnimeEntry findAnimeEntry(int id) {
		for(AnimeEntry ae : list) {
			if(ae.getId() == id)
				return ae;
		}
		return null;
	}
	
	@JsonProperty("sessionId")
	public String getSessionId() {
		return sessionId;
	}
	
	public String generateNewSessionId() {
		sessionId = UUID.randomUUID().toString();
		return sessionId;
	}
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	
	@JsonProperty("list")
	public ArrayList<AnimeEntry> getList() {
		return list;
	}
	
	@JsonProperty("lang")
	public UserTitleLanguagePreference getLang() {
		return lang;
	}
	
	@Override
	public String toString() {
		return "{name: " + name + ", sessionId: " + sessionId + ", list: " + list + "}";
	}
}
