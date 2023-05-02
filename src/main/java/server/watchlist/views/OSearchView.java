package server.watchlist.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;

import jakarta.servlet.http.Cookie;
import server.watchlist.data.DataHandler;
import server.watchlist.data.ResponseObject;
import server.watchlist.layouts.NavBar;
import server.watchlist.layouts.ResponseObjectLayout;

@Route("/osearch")
public class OSearchView extends VerticalLayout implements BeforeEnterObserver, HasNavBar, HasUrlParameter<String> {
	private String username;
	private ResponseObject responseObject;
	
	private NavBar navBar;
	private ResponseObjectLayout responseObjectLayout;
	
	public OSearchView() {
		DataHandler.refreshDetails();
		navBar = new NavBar(this);
		
		this.add(navBar);
		if(responseObjectLayout == null)
			return;
	}
	
	@Override
	public void setParameter(BeforeEvent event, String parameter) {
		checkForExistingAuthCookie();
		responseObject = DataHandler.requestInfoBySearch(parameter);
		if(responseObject == null)
			return;
		responseObjectLayout = new ResponseObjectLayout(responseObject, username, true);
		this.add(responseObjectLayout);
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
	public void clickHomeBtn() {
		UI.getCurrent().navigate(ListView.class);
	}

	@Override
	public void clickSearchListBtn(String query) {
		UI.getCurrent().navigate(SearchListView.class, query);
	}

	@Override
	public void clickSearchOnlineBtn(String query) {
		UI.getCurrent().navigate(OSearchView.class, query);
	}
}
