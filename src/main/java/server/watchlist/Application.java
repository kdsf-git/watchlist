package server.watchlist;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import server.watchlist.data.DataHandler;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	try {
			DataHandler.startup();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        SpringApplication.run(Application.class, args);
    }

}
