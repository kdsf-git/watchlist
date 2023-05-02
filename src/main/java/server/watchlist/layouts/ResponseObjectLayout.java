package server.watchlist.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import server.watchlist.data.AnimeEntry;
import server.watchlist.data.DataHandler;
import server.watchlist.data.ResponseObject;
import server.watchlist.data.User;

public class ResponseObjectLayout extends HorizontalLayout {
	private ResponseObject response;
	private AnimeEntry animeEntry;
	private User user;
	private boolean changeButton = false;
	
	private Checkbox finishedBox;
	private Image image;
	private Label titleLbl;
	private Label episodesLbl;
	private Label idLbl;
	private Label genresLbl;
	private TextField noteTxt;
	private VerticalLayout column1, column2, column3, column4, column5;
	private Button deleteBtn;
	
	public ResponseObjectLayout(ResponseObject response, String username) {
		this(response, username, false);
	}
	
	public ResponseObjectLayout(ResponseObject response, String username, boolean changeButton) {
		this.response = response;
		this.changeButton = changeButton;
		
		try {
			user = DataHandler.getUser(username);
		} catch (Exception e) {
			Notification.show("error");
			return;
		}
		
		animeEntry = user.findAnimeEntry(response.data.media.id);
		if(animeEntry == null)
			animeEntry = new AnimeEntry(response.data.media.id);
		
		finishedBox = new Checkbox();
		finishedBox.setValue(animeEntry.getFinished());
		finishedBox.addValueChangeListener(event -> {changeFinishedBox();});
		column1 = new VerticalLayout();
		column1.add(finishedBox);
		
		image = new Image();
		image.setSrc(response.data.media.coverImage.medium);
		column2 = new VerticalLayout();
		column2.add(image);
		try {
			titleLbl = new Label(
						switch(DataHandler.getUser(username).getLang()) {
						case english -> response.data.media.title.english;
						case romaji -> response.data.media.title.romaji;
						}
					);
		} catch (Exception e) {
			Notification.show("error");
			return;
		}
		idLbl = new Label("#" + response.data.media.id);
		episodesLbl = new Label("Episodes: " + response.data.media.episodes);
		column3 = new VerticalLayout();
		column3.add(titleLbl, idLbl, episodesLbl);
		String s = "";
		for(String genre : response.data.media.genres) {
			s += genre + "\n";
		}
		genresLbl = new Label(s);
		column4 = new VerticalLayout();
		column4.add(genresLbl);
		noteTxt = new TextField();
		noteTxt.setValue(animeEntry.getNote());
		noteTxt.addValueChangeListener(event -> {changeNoteTxt();});
		noteTxt.setWidthFull();
		column5 = new VerticalLayout();
		column5.add(noteTxt);
		
		column1.setJustifyContentMode(JustifyContentMode.CENTER);
		column2.setJustifyContentMode(JustifyContentMode.CENTER);
		column3.setJustifyContentMode(JustifyContentMode.CENTER);
		column4.setJustifyContentMode(JustifyContentMode.CENTER);
		column5.setJustifyContentMode(JustifyContentMode.CENTER);
		
		column1.setWidth("20px");
		column2.setWidth("150px");
		column3.setWidth("600px");
		column4.setWidth("200px");
		column5.setWidth("700px");
		
		this.add(column1, column2, column3, column4, column5);
		
		addDeleteBtn();
	}
	
	private void addDeleteBtn() {
		if(changeButton) {
			deleteBtn = new Button("ADD");
			deleteBtn.addClickListener(event -> {clickAddBtn();});
			deleteBtn.getStyle().set("background-color", "#99ff99");
			deleteBtn.getStyle().set("font-color", "white");
			deleteBtn.setWidth("20px");
		} else {
			deleteBtn = new Button("DEL");
			deleteBtn.addClickListener(event -> {clickDeleteBtn();});
			deleteBtn.getStyle().set("background-color", "red");
			deleteBtn.getStyle().set("font-color", "white");
			deleteBtn.setWidth("20px");
		}
		this.add(deleteBtn);
	}
	
	public ResponseObject getResponseObject() {
		return response;
	}
	
	private void changeFinishedBox() {
		animeEntry.setFinished(finishedBox.getValue());
		DataHandler.save();
		Notification.show("updated");
	}
	
	private void changeNoteTxt() {
		animeEntry.setNote(noteTxt.getValue());
		DataHandler.save();
		Notification.show("updated");
	}
	
	private void clickDeleteBtn() {
		user.getList().remove(animeEntry);
		DataHandler.save();
		Notification.show("deleted");
	}
	
	private void clickAddBtn() {
		DataHandler.requestInfoById(response.data.media.id);
		user.getList().add(animeEntry);
		DataHandler.save();
		Notification.show("added");
	}
}
