package server.watchlist.data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseObjectList {
	private Data data;
	
	@JsonCreator
	public ResponseObjectList(@JsonProperty("data") Data data) {
		this.data = data;
	}
	
	public List<ResponseObject> toList() {
		List<ResponseObject> list = new ArrayList<ResponseObject>();
		for(ResponseObject.Data.Media m : data.page.media) {
			list.add(new ResponseObject(new ResponseObject.Data(m)));
		}
		return list;
	}
	
	public static class Data {
		private Page page;
		
		@JsonCreator
		public Data(@JsonProperty("Page") Page page) {
			this.page = page;
		}
		
		public static class Page {
			private ResponseObject.Data.Media[] media;
			
			@JsonCreator
			public Page(@JsonProperty("media") ResponseObject.Data.Media[] media) {
				this.media = media;
			}
		}
	}
}
