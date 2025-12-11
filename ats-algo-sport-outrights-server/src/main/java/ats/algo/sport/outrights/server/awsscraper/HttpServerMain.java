package ats.algo.sport.outrights.server.awsscraper;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

/**
 * This server is ready to run on the AWS, call the following url to get scraper excuted:
 * 
 * 
 * http://localhost:5901/echoGetupdatechamp (champ refers the Championship, can change to premier to scraper
 * Premier-leagues data) This scraping only scraps the historical rating data, and save it as XX-rating-data. (object)
 * 
 * after complete the scraping call the following URL to combind the match history and the rating history
 * http://localhost:5901/echoGetbindchamp or ...bindpremier
 * 
 */

@SuppressWarnings("restriction")
public class HttpServerMain {
    static int port = 5901;



    public static void main(String[] args) throws IOException {
        System.out.println("Scraping Server Started"); // Display the string.
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        System.out.println("server started at " + port);
        server.createContext("/", new RootHandler());
        server.createContext("/echoHeader", new EchoHeaderHandler());
        EchoGetHandler getHandler = new EchoGetHandler();
        ConnectingMGDB mgdb = new ConnectingMGDB();// "test_jin", "test_collection1"
        getHandler.setMgDBConnection(mgdb);
        server.createContext("/echoGet", getHandler);
        server.createContext("/echoPost", new EchoPostHandler());
        server.setExecutor(null);
        server.start();

    }



}
