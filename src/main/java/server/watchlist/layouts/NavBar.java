package server.watchlist.layouts;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import server.watchlist.views.ListView;
import server.watchlist.views.OSearchView;
import server.watchlist.views.SettingsView;

public class NavBar extends HorizontalLayout {
	private Button homeBtn;
	private TextField searchTxt;
	private Button searchOnlineBtn;
	private Button settingsBtn;
	
	public NavBar() {
		homeBtn = new Button("Home");
		homeBtn.addClickListener(event -> {clickHomeBtn();});
		searchTxt = new TextField();
		searchOnlineBtn = new Button("Search online");
		searchOnlineBtn.addClickListener(event -> {clickSearchOnlineBtn(searchTxt.getValue());});
		searchOnlineBtn.addClickShortcut(Key.ENTER);
		
		settingsBtn = new Button();
		settingsBtn.setIcon(new Icon(VaadinIcon.COG));
		settingsBtn.addClickListener(event -> {clickSettingsBtn();});
		
		this.add(homeBtn, searchTxt, searchOnlineBtn, settingsBtn);
	}
	
	public void clickSettingsBtn() {
		UI.getCurrent().navigate(SettingsView.class);
	}

	public void clickSearchOnlineBtn(String query) {
		UI.getCurrent().navigate(OSearchView.class, query);
	}
	
	public void clickHomeBtn() {
		UI.getCurrent().navigate(ListView.class);
	}
}
