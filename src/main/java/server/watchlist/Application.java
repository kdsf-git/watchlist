package server.watchlist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import server.watchlist.data.DataHandler;

@SpringBootApplication
@Theme(variant = Lumo.DARK)
public class Application implements AppShellConfigurator {
	public static final String version = "v2.0";

    public static void main(String[] args) {
    	try {
			DataHandler.startup();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        SpringApplication.run(Application.class, args);
    }
    
    public static void log(String s) {
    	try {
			Files.write(Path.of("log.txt"), (s + "\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
