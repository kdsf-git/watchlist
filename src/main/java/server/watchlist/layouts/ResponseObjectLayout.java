package server.watchlist.layouts;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
	private Checkbox watchingBox;
	private Image image;
	private Anchor titleAnchor;
	private H3 titleLbl;
	private Paragraph episodesLbl;
	private Paragraph idLbl;
	private Paragraph genresLbl;
	private TextField noteTxt;
	private VerticalLayout column1, column2, column3, column4, column5;
	private Button deleteBtn;
	
	public ResponseObjectLayout(ResponseObject response, User user) {
		this(response, user, false);
	}
	
	public ResponseObjectLayout(ResponseObject response, User user, boolean changeButton) {
		this.response = response;
		this.changeButton = changeButton;
		this.user = user;
		
		animeEntry = user.findAnimeEntry(response.getId());
		if(animeEntry == null)
			animeEntry = new AnimeEntry(response.getId());
		
		finishedBox = new Checkbox();
		finishedBox.setValue(animeEntry.getFinished());
		finishedBox.addValueChangeListener(event -> {changeFinishedBox();});
		watchingBox = new Checkbox();
		watchingBox.setValue(animeEntry.getWatching());
		watchingBox.getStyle().set("--lumo-primary-color", "#00E676");
		watchingBox.addValueChangeListener(event -> {changeWatchingBox();});
		column1 = new VerticalLayout();
		column1.add(finishedBox);
		column1.add(watchingBox);
		
		image = new Image();
		image.setSrc(response.getMedium());
		column2 = new VerticalLayout();
		column2.add(image);
		titleAnchor = new Anchor();
		try {
			titleLbl = new H3(
						switch(user.getLang()) {
						case english -> response.getEnglish();
						case romaji -> response.getRomaji();
						}
					);
		} catch (Exception e) {
			Notification.show("error");
			return;
		}
		titleAnchor.add(titleLbl);
		titleAnchor.setHref(response.getSiteUrl());
		
		idLbl = new Paragraph("#" + response.getId());
		episodesLbl = new Paragraph("Episodes: " + response.getEpisodes());
		column3 = new VerticalLayout();
		column3.add(titleAnchor, idLbl, episodesLbl);
		String s = "";
		for(String genre : response.getGenres()) {
			s += genre + "\n";
		}
		genresLbl = new Paragraph(s);
		genresLbl.getStyle().set("white-space", "pre-line");
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
		
		this.setWidth("100%");
		column1.setWidth("1%");
		column2.setWidth("9%");
		column3.setWidth("36%");
		column4.setWidth("12%");
		column5.setWidth("42%");
		
		this.add(column1, column2, column3, column4, column5);
		
		addDeleteBtn();
	}
	
	private void addDeleteBtn() {
		if(changeButton) {
			deleteBtn = new Button(new Icon(VaadinIcon.PLUS));
			deleteBtn.addClickListener(event -> {clickAddBtn();});
			deleteBtn.getStyle().set("background-color", "#99ff99");
			deleteBtn.getStyle().set("color", "black");
		} else {
			deleteBtn = new Button(new Icon(VaadinIcon.TRASH));
			deleteBtn.addClickListener(event -> {clickDeleteBtn();});
			deleteBtn.getStyle().set("background-color", "red");
			deleteBtn.getStyle().set("color", "white");
		}
		this.add(deleteBtn);
	}
	
	public ResponseObject getResponseObject() {
		return response;
	}
	
	public AnimeEntry getAnimeEntry() {
		return animeEntry;
	}
	
	private void changeFinishedBox() {
		animeEntry.setFinished(finishedBox.getValue());
		DataHandler.save();
		Notification.show("updated");
	}
	
	private void changeWatchingBox() {
		animeEntry.setWatching(watchingBox.getValue());
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
		DataHandler.requestInfoById(response.getId());
		user.getList().add(animeEntry);
		DataHandler.save();
		Notification.show("added");
	}
}
