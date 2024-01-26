package server.watchlist.data;

import java.util.ArrayList;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import server.watchlist.security.Session;

public class User {
	private String name;
	private ArrayList<Session> sessions;
	private ArrayList<AnimeEntry> list;
	private UserTitleLanguagePreference lang;
	
	@JsonCreator
	public User(@JsonProperty("name") String name, @JsonProperty("sessions") ArrayList<Session> sessions, @JsonProperty("list") ArrayList<AnimeEntry> list, @JsonProperty("lang") UserTitleLanguagePreference lang) {
		this.name = name;
		this.sessions = sessions;
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
	
	@JsonProperty("sessions")
	public ArrayList<Session> getSessions() {
		return sessions;
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
	
	public void setLang(UserTitleLanguagePreference lang) {
		this.lang = lang;
	}
	
	public Session getValidSession(String sessionId) {
		for(int i = 0; i < sessions.size(); i++) {
			if(sessions.get(i).isValid(sessionId))
				return sessions.get(i);
		}
		return null;
	}
	
	public void addSession(Session s) {
		if(s == null)
			return;
		sessions.add(s);
		DataHandler.save();
	}
	
	public void removeSession(Session s) {
		if(s == null)
			return;
		sessions.remove(s);
		DataHandler.save();
	}
	
	public void removeAllSessions() {
		sessions = new ArrayList<Session>();
		DataHandler.save();
	}
}
