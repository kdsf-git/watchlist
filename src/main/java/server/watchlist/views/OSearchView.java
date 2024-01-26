package server.watchlist.views;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import jakarta.servlet.http.Cookie;
import server.watchlist.data.AnimeEntry;
import server.watchlist.data.DataHandler;
import server.watchlist.data.ResponseObject;
import server.watchlist.data.ResponseObjectList;
import server.watchlist.layouts.NavBar;
import server.watchlist.layouts.ResponseObjectLayout;
import server.watchlist.layouts.ResponseObjectLayoutList;
import server.watchlist.security.Auth;
import server.watchlist.security.Session;

@Route("/osearch")
public class OSearchView extends VerticalLayout implements Auth, HasUrlParameter<String> {
	private ResponseObjectLayoutList responseObjectLayouts;
	private NavBar navBar;
	private String searchString;
	private Anchor anilistSearchLink;
	private Session session;
	
	public void setSession(Session s) {
		this.session = s;
	}
	
	private void configureList() {
		
		List<ResponseObject> ros;
		ros = DataHandler.requestInfoBySearch(searchString);
		
		List<AnimeEntry> aeList = new ArrayList<AnimeEntry>();
		for(ResponseObject ro : ros) {
			aeList.add(new AnimeEntry(ro.getId()));
		}
		
		DataHandler.refreshDetails();
		
		try {
			responseObjectLayouts = new ResponseObjectLayoutList(DataHandler.getUser(getUsername()), ros, true);
		} catch (Exception e) {
			Notification.show("error");
			e.printStackTrace();
		}
		
		navBar = new NavBar();
		
		anilistSearchLink = new Anchor();
		anilistSearchLink.setText("AniList search page");
		anilistSearchLink.setHref("https://anilist.co/search/anime?search=" + searchString);
		
		this.removeAll();
		this.add(navBar);
		responseObjectLayouts.addTo(this);
		this.add(anilistSearchLink);
	}
	
	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		searchString = parameter;
		if(searchString == null)
			searchString = "";
		configureList();
	}
}
