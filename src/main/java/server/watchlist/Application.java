package server.watchlist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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
    
    public static void log(String s) {
    	try {
			Files.write(Path.of("log.txt"), s.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
