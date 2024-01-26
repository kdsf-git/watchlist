package server.watchlist.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimeEntry {
	private boolean finished;
	private boolean watching;
	private int id;
	private String note;
	
	@JsonCreator
	public AnimeEntry(@JsonProperty("id") int id) {
		finished = false;
		watching = false;
		this.id = id;
		note = "";
	}
	
	@JsonProperty("finished")
	public boolean getFinished() {
		return finished;
	}
	
	@JsonProperty("finished")
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	@JsonProperty("id")
	public int getId() {
		return id;
	}
	
	@JsonProperty("note")
	public String getNote() {
		return note;
	}
	
	@JsonProperty("note")
	public void setNote(String note) {
		this.note = note;
	}
	
	@JsonProperty("watching")
	public void setWatching(Boolean watching) {
		this.watching = watching;
	}

	@JsonProperty("watching")
	public Boolean getWatching() {
		return watching;
	}
}
