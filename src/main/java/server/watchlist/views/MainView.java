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
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import jakarta.servlet.http.Cookie;
import server.watchlist.data.DataHandler;
import server.watchlist.security.Auth;
import server.watchlist.security.Session;

@Route
public class MainView extends VerticalLayout implements Auth {
	private Label loginLbl;
	private TextField username;
	private PasswordField password;
	private Button okBtn;
	private String key = "";
	private Session session;
	
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
    public void setSession(Session s) {
    	this.session = s;
    }
    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
    	if(checkForExistingAuthCookie()) {
    		event.forwardTo(ListView.class);
    	}
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
    			Session s = new Session();
    			try {
					DataHandler.getUser(username.getValue()).addSession(s);
				} catch (Exception e) {
					Notification.show("wrong username");
					return;
				}
    			
    			VaadinService.getCurrentResponse().addCookie(DataHandler.makeUsernameCookie(username.getValue()));
    			VaadinService.getCurrentResponse().addCookie(DataHandler.makeNewSessionIdCookie(s));
    			
    			UI.getCurrent().navigate(ListView.class);
    			UI.getCurrent().getPage().reload();
    		} else {
    			Notification.show("wrong key");
    		}
    	}
    }
}
