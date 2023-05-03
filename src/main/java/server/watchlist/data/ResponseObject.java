package server.watchlist.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseObject {
	public Data data = new Data(new Data.Media(0, 0, new String[] {}, new Data.Media.Title("", ""), new Data.Media.CoverImage("")));
	
	@JsonCreator
	public ResponseObject(@JsonProperty("Data") Data data) {
		this.data = data;
	}
	
	public static class Data {
		public Media media;
		
		@JsonCreator
		public Data(@JsonProperty("Media") Media media2) {
			this.media = media2;
		}
		
		public static class Media {
			public int id;
			public int episodes;
			public String[] genres;
			public Title title;
			public CoverImage coverImage;
			
			@JsonCreator
			public Media(@JsonProperty("id") int id, @JsonProperty("episodes") int episodes, @JsonProperty("genres") String[] genres, @JsonProperty("title") Title title, @JsonProperty("coverImage") CoverImage coverImage) {
				this.id = id;
				this.episodes = episodes;
				this.genres = genres;
				this.title = title;
				this.coverImage = coverImage;
			}
			
			public static class Title {
				public String romaji;
				public String english;
				
				@JsonCreator
				public Title(@JsonProperty("romaji") String romaji, @JsonProperty("english") String english) {
					this.romaji = romaji;
					this.english = english;
				}
			}
			
			public static class CoverImage {
				public String medium;
				
				@JsonCreator
				public CoverImage(@JsonProperty("medium") String medium) {
					this.medium = medium;
				}
			}
		}
	}
}
