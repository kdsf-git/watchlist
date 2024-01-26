package server.watchlist.security;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Session {
	private String id;
	
	//for deserialization only
	@JsonCreator
	public Session(@JsonProperty("id") String id) {
		this();
		this.id = id;
	}
	
	public Session() {
		generateNewId();
	}
	
	public String generateNewId() {
		id = UUID.randomUUID().toString();
		return id;
	}
	
	@JsonProperty("id")
	public String getId() {
		return id;
	}
	
	public boolean isValid(String id) {
		if(id.equals(this.id))
			return true;
		return false;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null)
			return false;
		if(!(other instanceof Session))
			return false;
		if(!(this.id.equals(((Session) other).id)))
			return false;
		return true;
	}
}
