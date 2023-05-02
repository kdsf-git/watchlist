package server.watchlist.views;

import java.util.ArrayList;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;

import jakarta.servlet.http.Cookie;
import server.watchlist.data.AnimeEntry;
import server.watchlist.data.DataHandler;
import server.watchlist.layouts.NavBar;
import server.watchlist.layouts.ResponseObjectLayout;

@Route("/list")
public class ListView extends VerticalLayout implements BeforeEnterObserver, HasNavBar {
	private String username = "";
	
	protected ArrayList<ResponseObjectLayout> responseObjectLayouts;
	private NavBar navBar;
	
	public ListView() {
		checkForExistingAuthCookie();
		DataHandler.refreshDetails();
		
		responseObjectLayouts = new ArrayList<ResponseObjectLayout>();
		try {
			for(AnimeEntry r : DataHandler.getUser(username).getList()) {
				responseObjectLayouts.add(new ResponseObjectLayout(DataHandler.getDetails(r.getId()), username));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		navBar = new NavBar(this);
		
		this.add(navBar);
		addResponseObjectLayouts();
	}
	
	protected void addResponseObjectLayouts() {
		for(int i = 0; i < responseObjectLayouts.size(); i++) {
			if(i % 2 == 0)
				responseObjectLayouts.get(i).getStyle().set("background-color", "#f0f0f0");
			responseObjectLayouts.get(i).getStyle().set("border-radius", "1em");
			this.add(responseObjectLayouts.get(i));
		}
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if(!checkForExistingAuthCookie())
    		event.forwardTo(MainView.class);
	}
	
	private boolean checkForExistingAuthCookie() {
    	Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
    	String username = null;
    	String sessionId = null;
    	for(Cookie cookie : cookies) {
    		if(cookie.getName().equals("username")) {
    			username = cookie.getValue();
    		} else if(cookie.getName().equals("sessionId")) {
    			sessionId = cookie.getValue();
    		}
    	}
    	try {
			if(DataHandler.getSessionId(username).equals(sessionId)) {
				this.username = username;
				return true;
			}
		} catch (Exception e) {}
    	return false;
    }

	@Override
	public void clickSearchListBtn(String query) {
		UI.getCurrent().navigate(SearchListView.class, query);
	}

	@Override
	public void clickSearchOnlineBtn(String query) {
		UI.getCurrent().navigate(OSearchView.class, query);
	}
	
	@Override
	public void clickHomeBtn() {
		UI.getCurrent().navigate(ListView.class);
	}
}
