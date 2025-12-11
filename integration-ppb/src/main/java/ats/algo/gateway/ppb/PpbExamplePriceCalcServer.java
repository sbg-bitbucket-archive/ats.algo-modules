package ats.algo.gateway.ppb;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.requestresponse.ppb.PpbMarkets;
import ats.algo.requestresponse.ppb.PpbTennisPriceCalcRequest;
import ats.algo.requestresponse.ppb.PpbTennisPriceCalcResponse;
import ats.core.AtsBean;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;
import okhttp3.MediaType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class PpbExamplePriceCalcServer extends AtsBean {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static int newFixedThreadPool = 100;

    private static final String PORT_PROPERTY = "algo.tennis.PpbPriceCalcPort";
    private static final String DEFAULT_PORT = "8080";

    private static final Logger log = LoggerFactory.getLogger(PpbExamplePriceCalcServer.class);

    private static String response = "This is an example server to handle price calculation requests\n"
                    + "POST to PPB/PriceCalc/ to do the price calc \n";

    static class PostPriceCalcHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            long startTime = System.currentTimeMillis();
            String requestStr = read(t.getRequestBody());
            long threadId = Thread.currentThread().getId();
            log.info("Thread " + threadId + " handle Price Calc request:");

            try {
                PpbTennisPriceCalcRequest pcRequest =
                                JsonSerializer.deserialize(requestStr, PpbTennisPriceCalcRequest.class);
                String formattedJsonRequest = JsonSerializer.serialize(pcRequest, true);
                log.info(formattedJsonRequest);

                // PPB Model to handle pcRequest.
                if (t.getRequestMethod().contains("POST")) {
                    String serverId = pcRequest.getPpbMatchState().getStartOfMatchServer();
                    boolean preMatch = serverId.equals("not_set");
                    Markets markets;
                    if (preMatch)
                        markets = generatePrematchMarkets(pcRequest.getPpbMatchParams().getTeamAPreMatchLine());
                    else
                        markets = generateInplayMarkets(pcRequest.getPpbMatchParams().getTeamAPreMatchLine());
                    long endTime = System.currentTimeMillis();
                    PpbTennisPriceCalcResponse pcrResponse =
                                    new PpbTennisPriceCalcResponse(new PpbMarkets(markets.getMarkets()),
                                                    "ppb Saved state goes here", startTime, endTime);
                    String formattedJsonResponse = JsonSerializer.serialize(pcrResponse, true);
                    log.info(formattedJsonResponse);
                    byte[] bs = formattedJsonResponse.getBytes("UTF-8");

                    t.getResponseHeaders().putAll(t.getRequestHeaders());
                    t.sendResponseHeaders(200, bs.length);
                    OutputStream os = t.getResponseBody();
                    os.write(bs);
                    os.flush();
                    os.close();
                    t.close();

                } else {
                    OutputStream os = t.getResponseBody();
                    t.sendResponseHeaders(200, response.length());
                    os.write(response.toString().getBytes());
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    static class GetHandler implements HttpHandler {
        public void handle(HttpExchange t) throws IOException {
            try {
                if (t.getRequestMethod().contains("GET")) {
                    OutputStream os = t.getResponseBody();
                    t.sendResponseHeaders(200, response.length());
                    os.write(response.toString().getBytes());
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws Exception {
        String port = System.getProperty(PORT_PROPERTY);
        if (port == null)
            port = DEFAULT_PORT;
        // LogUtil.initConsoleLogging(Level.TRACE);
        // log.info("Logging level set to TRACE");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(Integer.parseInt(port));
        HttpServer server = HttpServer.create(inetSocketAddress, 0);
        server.createContext("/", new GetHandler());
        server.createContext("/PPB/PriceCalc", new PostPriceCalcHandler());
        ExecutorService executor = Executors.newFixedThreadPool(newFixedThreadPool);
        server.setExecutor(executor);
        server.start();
        String head = "This is an example server to handle price calculation requests";
        log.info(head);
        head = "POST to PPB/PriceCalc/ to do the price calc";
        log.info(head);
        log.info("Server is running now");

    }

    public static String read(InputStream input) throws IOException {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    private static Market generateMatchBettingMarket(double probAWins) {
        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match betting");
        market.setIsValid(true);
        market.put("A", probAWins);
        market.put("B", 1 - probAWins);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        return market;
    }

    // private static Market generateSetBettingMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "P:ML", "S1", "Set 1 betting");
    // market.setIsValid(true);
    // double probB = .480;
    // market.put("A", 1 - probB);
    // market.put("B", probB);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateToWinMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "FT:TW", "M", "To Win X Set");
    // market.setIsValid(true);
    // double probB = .441;
    // market.put("A", 1 - probB);
    // market.put("B", probB);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateGameWinnerMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "G:ML", "P1.1", "Set 1 Game 1 Winner");
    // market.setIsValid(true);
    // double probB = .608;
    // market.put("A", 1 - probB);
    // market.put("B", probB);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateSetCorrectScoreMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "P:CS", "P1", "Set 1 Correct Score");
    // market.setIsValid(true);
    // market.put("6-0", 0.01);
    // market.put("0-6", 0.01);
    // market.put("6-1", 0.02);
    // market.put("1-6", 0.02);
    // market.put("6-3", 0.02);
    // market.put("3-6", 0.02);
    // market.put("6-4", 0.2);
    // market.put("4-6", 0.2);
    // market.put("5-7", 0.1);
    // market.put("7-5", 0.1);
    // market.put("6-7", 0.15);
    // market.put("7-6", 0.15);
    // return market;
    // }
    //
    // private static Market generateGameCorrectScoreMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "G:CS", "P1.1", "Set 1 Game 1 Correct Score");
    // market.setIsValid(true);
    // market.put("to love A", 0.2);
    // market.put("to 15 A", 0.16);
    // market.put("to 30 A", 0.16);
    // market.put("to 40 A", 0.3);
    // market.put("to love B", 0.05);
    // market.put("to 15 B", 0.05);
    // market.put("to 30 B", 0.04);
    // market.put("to 40 B", 0.04);
    // return market;
    // }
    //
    // private static Market generatePointWinnerMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "G:PW", "P1.1.1", "Set 1 Game 1 Point 1 Winner");
    // market.setIsValid(true);
    // double probB = 0.777;
    // market.put("A", 1 - probB);
    // market.put("B", probB);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateAToWinOneSetMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "FT:W1S:A", "M", "A to win at least one set");
    // market.setIsValid(true);
    // double probB = 0.777;
    // market.put("Yes", 1 - probB);
    // market.put("No", probB);
    // return market;
    // }
    //
    // private static Market generateBToWinOneSetMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "FT:W1S:B", "M", "B to win at least one set");
    // market.setIsValid(true);
    // double probB = 0.7;
    // market.put("Yes", probB);
    // market.put("No", 1 - probB);
    // return market;
    // }

    private static Markets generatePrematchMarkets(double probAWins) {
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket(probAWins));
        // Market setBettingMarket = generateSetBettingMarket();
        // Market toWinMarket = generateToWinMarket();
        // Market gameCorrectScoreMarket = generateGameCorrectScoreMarket();
        // Market toWinA = generateAToWinOneSetMarket();
        // Market toWinB = generateBToWinOneSetMarket();
        // markets.addMarketWithShortKey(handicapMarket);
        // markets.addMarketWithShortKey(setBettingMarket);
        // markets.addMarketWithShortKey(toWinMarket);
        // markets.addMarketWithShortKey(gameCorrectScoreMarket);
        // markets.addMarketWithShortKey(toWinA);
        // markets.addMarketWithShortKey(toWinB);
        return markets;
    }

    private static Markets generateInplayMarkets(double probAWins) {
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket(probAWins));
        // Market setBettingMarket = generateSetBettingMarket();
        // Market toWinMarket = generateToWinMarket();
        // Market gameWinnerMarket = generateGameWinnerMarket();
        // Market setCorrectScoreMarket = generateSetCorrectScoreMarket();
        // Market pointWinMarket = generatePointWinnerMarket();
        // Market gameCorrectScoreMarket = generateGameCorrectScoreMarket();
        // Market toWinA = generateAToWinOneSetMarket();
        // Market toWinB = generateBToWinOneSetMarket();
        // markets.addMarketWithShortKey(handicapMarket);
        // markets.addMarketWithShortKey(setBettingMarket);
        // markets.addMarketWithShortKey(toWinMarket);
        // markets.addMarketWithShortKey(gameWinnerMarket);
        // markets.addMarketWithShortKey(setCorrectScoreMarket);
        // markets.addMarketWithShortKey(pointWinMarket);
        // markets.addMarketWithShortKey(gameCorrectScoreMarket);
        // markets.addMarketWithShortKey(toWinA);
        // markets.addMarketWithShortKey(toWinB);
        return markets;
    }
}
