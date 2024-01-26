package server.watchlist.views;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import jakarta.servlet.http.Cookie;
import server.watchlist.Application;
import server.watchlist.data.AnimeEntry;
import server.watchlist.data.DataHandler;
import server.watchlist.data.UserTitleLanguagePreference;
import server.watchlist.layouts.FiSoBar;
import server.watchlist.layouts.NavBar;
import server.watchlist.layouts.ResponseObjectLayout;
import server.watchlist.layouts.ResponseObjectLayoutList;
import server.watchlist.layouts.FilterModes;
import server.watchlist.layouts.SortingModes;
import server.watchlist.security.Auth;
import server.watchlist.security.Session;

@Route("/settings")
public class SettingsView extends VerticalLayout implements Auth {
	private NavBar navBar;
	private Paragraph langPrefLabel;
	private RadioButtonGroup<String> langPrefOptions;
	private Button logoutButton;
	private Button logoutAllButton;
	private Paragraph sessionCounter;
	private Paragraph versionIndicator;
	private Button downloadButton;
	private Session session;
	
	public SettingsView() {
		DataHandler.refreshDetails();
		
		navBar = new NavBar();
		
		langPrefLabel = new Paragraph("Language Preference");
		
		langPrefOptions = new RadioButtonGroup<String>();
		String[] langPrefOptionStrings = new String[UserTitleLanguagePreference.values().length];
		for(int i = 0; i < langPrefOptionStrings.length; i++) {
			langPrefOptionStrings[i] = UserTitleLanguagePreference.values()[i].toString();
		}
		langPrefOptions.setItems(langPrefOptionStrings);
		try {
			langPrefOptions.setValue(DataHandler.getUser(getUsername()).getLang().toString());
		} catch (Exception e) {
			Notification.show("error");
		}
		langPrefOptions.addValueChangeListener(event -> {changeLangPref();});
		
		downloadButton = new Button("Download list");

		StreamResource streamResource = new StreamResource("watchlist.txt", () -> {
			try {
				return new BufferedInputStream(new ByteArrayInputStream(DataHandler.exportList(DataHandler.getUser(getUsername())).getBytes()));
			} catch(Exception e) {
				Notification.show("error");
				return null;
			}
		});
		Anchor downloadAnchor = new Anchor(streamResource, "");
		downloadAnchor.getElement().setAttribute("download", true);
		downloadAnchor.setHref(streamResource);
		downloadAnchor.add(downloadButton);
		
		versionIndicator = new Paragraph("Version: " + Application.version);
		
		sessionCounter = new Paragraph();
		try {
			sessionCounter.setText("Active sessions: " + DataHandler.getUser(getUsername()).getSessions().size());
		} catch (Exception e) {
			Notification.show("error");
		}
		
		logoutButton = new Button("Logout");
		logoutButton.addClickListener(event -> {clickLogout();});
		
		logoutAllButton = new Button("Logout all");
		logoutAllButton.addClickListener(event -> {clickLogoutAll();});
		
		this.add(navBar);
		this.add(langPrefLabel);
		this.add(langPrefOptions);
		this.add(downloadAnchor);
		this.add(versionIndicator);
		this.add(sessionCounter);
		this.add(logoutButton);
		this.add(logoutAllButton);
	}
	
	@Override
	public void setSession(Session s) {
		this.session = s;
	}
	
	private void changeLangPref() {
		try {
			DataHandler.getUser(getUsername()).setLang(UserTitleLanguagePreference.valueOf(langPrefOptions.getValue()));
			DataHandler.save();
			Notification.show("saved");
		} catch (Exception e) {
			Notification.show("error");
		}
	}
	
	private void clickLogout() {
		try {
			DataHandler.getUser(getUsername()).removeSession(session);
			DataHandler.save();
		} catch (Exception e) {
			Notification.show("error");
		}
		UI.getCurrent().getPage().reload();
	}
	
	private void clickLogoutAll() {
		try {
			DataHandler.getUser(getUsername()).removeAllSessions();
			DataHandler.save();
		} catch (Exception e) {
			Notification.show("error");
		}
		UI.getCurrent().getPage().reload();
	}
}
