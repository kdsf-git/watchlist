package server.watchlist.views;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;

import jakarta.servlet.http.Cookie;
import server.watchlist.data.DataHandler;

@Route
public class MainView extends VerticalLayout implements BeforeEnterObserver {
	private Label loginLbl;
	private TextField username;
	private PasswordField password;
	private Button okBtn;
	private String key = "";
	
    public MainView() {
    	DataHandler.refreshDetails();
    	
    	loginLbl = new Label("Login");
        username = new TextField("username");
        password = new PasswordField("key");
        okBtn = new Button("OK", event -> {clickOkBtn();});
        okBtn.addClickShortcut(Key.ENTER);
        this.add(loginLbl, username, password, okBtn);
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	if(checkForExistingAuthCookie())
    		event.forwardTo(ListView.class);
    	DataHandler.refreshDetails();
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
				return true;
			}
		} catch (Exception e) {}
    	return false;
    }
    
    private String generateKey() {
    	return UUID.randomUUID().toString();
    }
    
	private void clickOkBtn() {
		try {
			DataHandler.getUser(username.getValue());
		} catch (Exception e) {
			Notification.show("wrong username");
			return;
		}
    	//empty password => key generation
    	if(password.getValue().equals("")) {
    		key = generateKey();
    		try {
				Files.write(Path.of("key_" + username.getValue() + ".txt"), key.getBytes(), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				Notification.show("server error");
				return;
			}
    		Notification.show("key generated");
    	} else {
    		if(password.getValue().equals(key)) {
    			try {
					DataHandler.getUser(username.getValue()).generateNewSessionId();
					DataHandler.save();
				} catch (Exception e) {
					Notification.show("wrong username");
					return;
				}
    			VaadinService.getCurrentResponse().addCookie(DataHandler.getUsernameCookie(username.getValue()));
    			VaadinService.getCurrentResponse().addCookie(DataHandler.getSessionIdCookie(username.getValue()));
    			
    			UI.getCurrent().navigate(ListView.class);
    		} else {
    			Notification.show("wrong key");
    		}
    	}
    }
}
