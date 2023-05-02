package server.watchlist.layouts;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

import server.watchlist.views.HasNavBar;

public class NavBar extends HorizontalLayout {
	private Button homeBtn;
	private TextField searchTxt;
	private Button searchListBtn;
	private Button searchOnlineBtn;
	private HasNavBar parent;
	
	public NavBar(HasNavBar parent) {
		homeBtn = new Button("Home");
		homeBtn.addClickListener(event -> {parent.clickHomeBtn();});
		searchTxt = new TextField();
		searchListBtn = new Button("Search list");
		searchListBtn.addClickListener(event -> {parent.clickSearchListBtn(searchTxt.getValue());});
		searchListBtn.addClickShortcut(Key.ENTER);
		searchOnlineBtn = new Button("Search online");
		searchOnlineBtn.addClickListener(event -> {parent.clickSearchOnlineBtn(searchTxt.getValue());});
		
		this.add(homeBtn, searchTxt, searchListBtn, searchOnlineBtn);
	}
}
