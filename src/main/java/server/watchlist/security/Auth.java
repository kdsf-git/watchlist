package server.watchlist.security;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.NavigationState;
import com.vaadin.flow.server.VaadinService;

import jakarta.servlet.http.Cookie;
import server.watchlist.Application;
import server.watchlist.data.DataHandler;
import server.watchlist.views.ListView;
import server.watchlist.views.MainView;

public interface Auth extends BeforeEnterObserver {
	public void setSession(Session s);
	
	public default void beforeEnter(BeforeEnterEvent event) {
		if(!checkForExistingAuthCookie()) {
			event.forwardTo(MainView.class);
		}
	}
	
	default String getUsername() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		for(Cookie c : cookies) {
			if(c.getName().equals("username"))
				return c.getValue();
		}
		return "";
	}
	
	default boolean checkForExistingAuthCookie() {
    	Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
    	String username = null;
    	String sessionId = null;
    	for(Cookie c : cookies) {
    		if(c.getName().equals("username"))
    			username = c.getValue();
    		if(c.getName().equals("sessionId"))
    			sessionId = c.getValue();
    	}
    	if(username == null || sessionId == null)
    		return false;
    	Session session = null;
		try {
			session = DataHandler.getUser(username).getValidSession(sessionId);
		} catch (Exception e) {
			Application.log("wrong username: " + username);
			return false;
		}
    	if(session == null)
    		return false;
    	VaadinService.getCurrentResponse().addCookie(DataHandler.makeUsernameCookie(username));
    	Cookie sessionCookie = DataHandler.makeNewSessionIdCookie(session);
    	this.setSession(session);
    	VaadinService.getCurrentResponse().addCookie(sessionCookie);
    	for(Cookie c : cookies) {
    		if(c.getName().equals("sessionId"))
    			c.setValue(sessionCookie.getValue());
    	}
    	return true;
	}
}
