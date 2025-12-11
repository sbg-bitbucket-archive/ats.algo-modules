package ats.algo.sport.outrights.server.awsscraper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



@SuppressWarnings("restriction")
public class EchoGetHandler implements HttpHandler {

    ConnectingMGDB mgDBConnection = null;


    public ConnectingMGDB getMgDBConnection() {
        return mgDBConnection;
    }

    public void setMgDBConnection(ConnectingMGDB mgDBConnection) {
        this.mgDBConnection = mgDBConnection;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<String, Object>();
        URI requestedUri = he.getRequestURI();
        String command = requestedUri.toString();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
        /**
         * test write to db
         */
        String toScrape = "";
        if (command.contains("champ"))
            toScrape = "championship";
        else if (command.contains("premier"))
            toScrape = "premier-league";

        // send response

        String excutingOrder = "";
        if (command.contains("update")) {
            this.mgDBConnection.updateDB(toScrape);
            excutingOrder = " scraping";
        } else if (command.contains("bind")) {
            this.mgDBConnection.bindDatas(toScrape);
            excutingOrder = " binding";
        } else if (command.contains("drop"))
            this.mgDBConnection.dropDBCollection();
        else if (command.contains("shutdown"))
            this.mgDBConnection.shutdownConnection();

        String response = "EchoGetHandler Response: Processing the request " + toScrape + excutingOrder;

        for (String key : parameters.keySet())
            response += key + " = " + parameters.get(key) + "\n";
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());

        os.close();
    }

    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        @SuppressWarnings("unchecked")
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
