package ats.algo.gateway.ppb;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import ats.algo.algomanager.AbstractPriceCalculator;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchEngineSavedState;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.common.TeamId;
import ats.algo.core.eventsettings.EventSettings;
import ats.algo.core.markets.Market;
import ats.algo.core.markets.MarketCategory;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.SuspensionStatus;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.requestresponse.ppb.PpbMarkets;
import ats.algo.requestresponse.ppb.PpbTennisMatchFormat;
import ats.algo.requestresponse.ppb.PpbTennisMatchParams;
import ats.algo.requestresponse.ppb.PpbTennisMatchState;
import ats.algo.requestresponse.ppb.PpbTennisPriceCalcRequest;
import ats.algo.requestresponse.ppb.PpbTennisPriceCalcResponse;
import ats.algo.sport.tennis.TennisMatchEngineSavedState;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.Surface;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncidentResult;
import ats.algo.sport.tennis.TennisMatchParams;
import ats.algo.sport.tennis.TennisMatchState;
import ats.core.util.json.JsonUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PpbPriceCalcGateway extends AbstractPriceCalculator {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /*
     * timeouts in secs
     */
    private static final int CONNECTION_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;
    private static final int ATTEMPT_TIMES = 3;
    private static final int maxWomenGameWinnerMarket = 30;
    private static final int maxMenGameWinnerMarket = 40;
    private static final String URL_PROPERTY_PREMATCH = "algo.tennis.PpbPriceCalcUrlPreMatch";
    private static final String URL_PROPERTY_INPLAY = "algo.tennis.PpbPriceCalcUrlInPlay";
    private static final String URL_PROPERTY_ENDOFMATCH = "algo.tennis.PpbPriceCalcUrlEndOfMatch";
    // private static final String DEFAULT_URL_PREMATCH =
    // "http://dubdc1-oattorc1.inhouse.paddypower.com:12348/setup";
    // private static final String DEFAULT_URL_INPLAY =
    // "http://dubdc1-oattorc1.inhouse.paddypower.com:12348/inrunning";
    private static final String DEFAULT_URL_PREMATCH = "http://10.109.188.192:12348/setup";
    private static final String DEFAULT_URL_INPLAY = "http://10.109.188.192:12348/inrunning";
    private static final String DEFAULT_URL_ENDPOINT = "http://10.109.188.192:12348/endofmatch";

    private static String urlPrematch;
    private static String urlInplay;
    private static String urlEndOfMatch;
    private static OkHttpClient client;
    private static String lineId = "";

    public PpbPriceCalcGateway() {
        urlPrematch = System.getProperty(URL_PROPERTY_PREMATCH);
        if (urlPrematch == null)
            urlPrematch = DEFAULT_URL_PREMATCH;
        urlInplay = System.getProperty(URL_PROPERTY_INPLAY);
        if (urlInplay == null)
            urlInplay = DEFAULT_URL_INPLAY;
        urlEndOfMatch = System.getProperty(URL_PROPERTY_ENDOFMATCH);
        if (urlEndOfMatch == null)
            urlEndOfMatch = DEFAULT_URL_ENDPOINT;
        info("PpbPriceCalcGateway will use url for prematch: %s for price calcs", urlPrematch);
        info("PpbPriceCalcGateway will use url for inplay: %s for price calcs", urlInplay);
        info("PpbPriceCalcGateway will use url for endofmatch: %s for price calcs", urlEndOfMatch);
    }

    public static void setUrlPrematch(String urlPrematch) {
        PpbPriceCalcGateway.urlPrematch = urlPrematch;
    }

    public static void setUrlInplay(String urlInplay) {
        PpbPriceCalcGateway.urlInplay = urlInplay;
    }

    public static void setUrlEndOfMatch(String urlEndOfMatch) {
        PpbPriceCalcGateway.urlEndOfMatch = urlEndOfMatch;
    }

    private static synchronized OkHttpClient httpClient() {
        if (null == client) {
            Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
            client = builder.build();
        }
        return client;
    }

    public PriceCalcResponse calculate(PriceCalcRequest request) {

        PriceCalcResponse response;
        Response httpResponse = null;
        Response httpResponseEndOfMatch = null;
        Request httpRequest;
        Request httpRequestEndOfMatch;
        PpbSavedState ppbSavedState;
        GenericMatchParams matchParams;
        boolean fatalError = false;
        String fatalErrorCause = null;

        TennisMatchFormat matchFormat = (TennisMatchFormat) request.getMatchFormat();
        if (matchFormat.getSurface().equals(TennisMatchFormat.Surface.CARPET)) {
            matchFormat.setSurface(Surface.IHARD);
        }
        TennisMatchState matchState = (TennisMatchState) request.getMatchState();
        boolean isMatchCompleted = matchState.isMatchCompleted();

        if (request.getCalcRequestCause().equals(CalcRequestCause.NEW_MATCH)) {
            matchParams = PpbTennisMatchParams.generatePpbGenericMatchParams(request.getMatchParams(), matchFormat);
            ppbSavedState = new PpbSavedState();
        } else {
            matchParams = request.getMatchParams();
            if (request.getMatchEngineSavedState() == null)
                ppbSavedState = new PpbSavedState();
            else
                ppbSavedState = JsonSerializer.deserialize(request.getMatchEngineSavedState().getSavedState(),
                                PpbSavedState.class);
        }

        if (request.getMatchIncident() instanceof TennisMatchIncident) {
            ppbSavedState.addPoint(matchState);
        }

        Markets markets = generateMarkets(request.getEventTier(), matchState, matchFormat);
        PpbTennisPriceCalcRequest ppbRequest = generatePpbPriceCalcRequest(request.getEventId(),
                        request.getUniqueRequestId(), matchFormat, matchState, matchParams, markets, ppbSavedState);
        String requestStr = JsonSerializer.serialize(ppbRequest, false);
        String paramsStr = JsonSerializer.serialize(ppbRequest.getPpbMatchParams(), false);
        String matchStateStr = JsonSerializer.serialize(ppbRequest.getPpbMatchState(), false);
        String marketsStr = JsonSerializer.serialize(ppbRequest.getPpbMarkets(), false);

        info("EventID: " + request.getEventId() + ". PPBPriceCalc request : " + requestStr);
        info("EventID: " + request.getEventId() + ". Match params are " + paramsStr);
        info("EventID: " + request.getEventId() + ". Match State is " + matchStateStr);
        info("EventID: " + request.getEventId() + ". Markets are " + marketsStr);
        info("EventID: " + request.getEventId() + ". EventTier is are " + request.getEventTier());
        RequestBody body = RequestBody.create(JSON, requestStr);

        try {
            if (isMatchCompleted) {
                OkHttpClient client = new OkHttpClient.Builder().readTimeout(1, TimeUnit.SECONDS).build();
                httpRequestEndOfMatch = new Request.Builder().url(urlEndOfMatch).post(body).build();
                info("EventID: " + request.getEventId() + ". POST the another request : " + request.getUniqueRequestId()
                                + " to: " + urlEndOfMatch + " when match is completed");
                info("EventID: " + request.getEventId() + ". PPBPriceCalc match comepleted request : " + requestStr);
                Call callEndOfMatch = client.newCall(httpRequestEndOfMatch);
                httpResponseEndOfMatch = callEndOfMatch.execute();
                if (httpResponseEndOfMatch.isSuccessful()) {
                    info("EventID: " + request.getEventId() + ". Sucessful match completed response: "
                                    + httpResponseEndOfMatch);
                } else {
                    info("EventID: " + request.getEventId() + ". Unsucessful match completed attempt: "
                                    + httpResponseEndOfMatch);
                }
                httpResponseEndOfMatch.body().close();
            }
        } catch (Exception e) {
            warn("EventID: " + request.getEventId() + ". Handle unexpected exception when match is completed " + e);
        } finally {
        }
        for (int attempt = 0; attempt < ATTEMPT_TIMES; attempt++) {
            try {
                info("EventID: " + request.getEventId() + ". This is the %d attempt", (attempt + 1));

                if (matchState.preMatch()) {
                    httpRequest = new Request.Builder().url(urlPrematch).post(body).build();
                    info("EventID: " + request.getEventId() + ". POST the request " + request.getUniqueRequestId()
                                    + " to: " + urlPrematch);
                } else {
                    httpRequest = new Request.Builder().url(urlInplay).post(body).build();
                    info("EventID: " + request.getEventId() + ". POST the request " + request.getUniqueRequestId()
                                    + " to: " + urlInplay);
                }

                Call call = httpClient().newCall(httpRequest);
                httpResponse = call.execute();

                if (!httpResponse.isSuccessful()) {
                    info("EventID: " + request.getEventId() + ". Unsucessful attempt: " + httpResponse);
                    continue;
                }

                String hrb = httpResponse.body().string();
                PpbTennisPriceCalcResponse ppbResponse =
                                JsonSerializer.deserialize(hrb, PpbTennisPriceCalcResponse.class);
                response = generatePriceCalcResponse(ppbResponse, markets, matchParams, ppbSavedState,
                                request.getUniqueRequestId());
                return response;
            } catch (Exception e) {
                error("Problem in " + e);
                fatalError = true;
                fatalErrorCause = e.getMessage();
            } finally {
                if (httpResponse != null)
                    httpResponse.body().close();
            }
        }
        MatchEngineSavedState savedState = new MatchEngineSavedState();
        String savedStateStr = JsonUtil.marshalJson(ppbSavedState);
        savedState.setSavedState(savedStateStr);
        if (fatalError) {
            if (request.getCalcRequestCause().equals(CalcRequestCause.NEW_MATCH)) {
                response = new PriceCalcResponse(request.getUniqueRequestId(), null, matchParams, savedState, null);
                info("EventID: %d " + request.getEventId()
                                + ". Fatal error but new match so pretend all good. Repsonse =  " + response);
            } else {
                response = PriceCalcResponse.generateFatalErrorResponse(request.getUniqueRequestId(), fatalErrorCause);
                response.setMatchParams(matchParams);
                response.setMatchEngineSavedState(savedState);
                info("Send back fatal error response " + response);
            }
        } else
            response = new PriceCalcResponse(request.getUniqueRequestId(), null, matchParams, savedState, null);
        return response;
    }

    public PpbTennisPriceCalcRequest generatePpbPriceCalcRequest(long eventId, String requestId,
                    TennisMatchFormat matchFormat, TennisMatchState matchState, GenericMatchParams matchParams,
                    Markets markets, PpbSavedState ppbSavedState) {

        PpbTennisPriceCalcRequest ppbRequest = new PpbTennisPriceCalcRequest(eventId, requestId,
                        PpbTennisMatchFormat.generatePbbTennisMatchFormat(matchFormat),
                        PpbTennisMatchState.generatePpbTennisMatchState(matchState, ppbSavedState.getPoints()),
                        PpbTennisMatchParams.generate(matchParams, matchFormat), new PpbMarkets(markets.getMarkets()),
                        ppbSavedState.getPpbState());
        return ppbRequest;
    }

    private PriceCalcResponse generatePriceCalcResponse(PpbTennisPriceCalcResponse ppbResponse, Markets markets,
                    GenericMatchParams matchParams, PpbSavedState ppbSavedState, String requestId) {
        ppbSavedState.setPpbState(ppbResponse.getPpbSavedState());
        MatchEngineSavedState savedState = new MatchEngineSavedState();
        String savedStateStr = JsonUtil.marshalJson(ppbSavedState);
        savedState.setSavedState(savedStateStr);
        Map<String, Market> markets2 = ppbResponse.getPpbMarkets().generateAlgoMarkets();
        markets2.values().forEach(market -> setMostBalancedLineId(market));
        markets.setMarkets(markets2);
        PriceCalcResponse response = new PriceCalcResponse(requestId, markets, matchParams, savedState, null);
        response.setTimePriceCalcRequestReceived(ppbResponse.getTimePriceCalcRequestReceived());
        response.setTimePriceCalcResponseIssued(ppbResponse.getTimePriceCalcResponseIssued());
        return response;
    }

    private static void setMostBalancedLineId(Market market) {
        if (market.getType().equals("FT:ALT:S:OU")) {
            market.setMarketDescription(market.getMarketDescription() + market.getLineId());
        }
        market.setBalancedLineId(market.getLineId());
    }

    public static Markets generateMarkets(long eventTier, TennisMatchState matchState, TennisMatchFormat matchFormat) {
        Markets markets = new Markets();
        int eventTierNo = Math.toIntExact(eventTier);
        if (!matchState.isMatchCompleted()) {
            switch (eventTierNo) {
                case 1:
                    markets.addMarketWithShortKey(generateMatchBettingMarket());
                    break;
                case 2:
                    markets = generateP2Market(matchState, matchFormat);
                    break;
                case 3:
                    markets = generateP3Market(matchState, matchFormat);
                    break;
                case 4:
                    markets = generateP4Market(matchState, matchFormat);
                    break;
                case 5:
                    markets = generateP5Market(matchState, matchFormat);
                    break;
                case 6:
                    markets = generateP6Market(matchState, matchFormat);
                    break;
                case 7:
                    markets = generateP01Market(matchState, matchFormat);
                    break;
                case 8:
                    markets = generateP8Market(matchState, matchFormat);
                    break;
                case 10:
                    int x = matchState.getSetNo();
                    int y = matchState.getGameNo();
                    int z = matchState.getPointNo();
                    for (int i = 0; i < 2; i++) {
                        if (y < 13)
                            markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));
                    }
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                    break;
                case 11:
                    markets = generateP11Market(matchState, matchFormat);
                    break;
                case 12:
                    markets = generateP12Market(matchState, matchFormat);
                    break;
                default:
                    markets = generateP01Market(matchState, matchFormat);
                    break;

            }

        }
        return markets;
    }

    private static Markets generateP12Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        int gamesPerSet = 6;
        int setsPerMatch = matchFormat.getSetsPerMatch();
        if (matchFormat.getTournamentLevel() == TournamentLevel.FAST4) {
            gamesPerSet = 4;
        }
        Markets markets = new Markets();

        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S
        markets.addMarketWithShortKey(generateMatchBettingMarket());// FT:ML
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));
        markets.addMarketWithShortKey(generateWinFirstSetAndMatchMarket());// FT:W1SML
        markets.addMarketWithShortKey(generatetLostOneSetWinMatchMarket());// FT:L1SML

        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "A"));
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "B"));

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());// FT:W1S:A
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());// FT:W1S:B

        if (!matchState.isInTieBreak() || matchState.getTieBreaksCounter() == 0) {
            markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        } // FT:TBIM

        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU
        if (matchState.getSetsA() == 0 || matchState.getSetsB() == 0)
            markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW

        if (!matchState.isInSuperTieBreak())
            markets.addMarketWithShortKey(generateTotalMatchMarket(matchState));// FT:OU

        int x = matchState.getSetNo();
        int y = matchState.getGameNo();
        int z = matchState.getPointNo();

        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !matchState.isInTieBreak()) {
            if (y < 7)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
            if (y < 6)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
            if (y < 5)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
        }
        if (y < 7 && !matchState.isInTieBreak())
            markets.addMarketWithShortKey(generateTotalMatchMarket(matchState)); // FT:OU
        if (!matchState.preMatch())
            if (y < 7)
                for (int i = 0; i < 6; i++) {
                    if (matchState.isInTieBreak() && (z + i) < 12) {
                        markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));// G:PW
                    } else if ((z + i) < 8) {
                        markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));// G:PW
                    }
                }

        if (matchFormat.getSetsPerMatch() == 3) {
            if (x != 3)
                markets.addMarketWithShortKey(generateToWinMarket(x));
            if (x + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(x + 1));
        } else {
            markets.addMarketWithShortKey(generateToWinMarket(x));
            if (x + 1 <= matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(x + 1));
        }
        markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, gamesPerSet, matchState, true)); // Pï¼šCS
        if (x + 1 <= matchState.getNoSetsInMatch())
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, gamesPerSet, matchState, true));

        if (x < setsPerMatch)
            markets.addMarketWithShortKey(generateTotalSetMarket(matchState));// FT:S:OU
        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();
        markets.addMarketWithShortKey(generateGameHandicapMarket(matchState));
        if (gameA == 3 && gameB == 3)
            markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));
        return markets;
    }

    private static Markets generateP11Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        int gamesPerSet = 6;
        int setsPerMatch = matchFormat.getSetsPerMatch();
        if (matchFormat.getTournamentLevel() == TournamentLevel.FAST4) {
            gamesPerSet = 4;
        }
        Markets markets = new Markets();
        int x = matchState.getSetNo();
        int y = matchState.getGameNo();

        markets.addMarketWithShortKey(generateMatchBettingMarket());// FT:ML
        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));
        markets.addMarketWithShortKey(generateWinFirstSetAndMatchMarket());// FT:W1SML
        markets.addMarketWithShortKey(generatetLostOneSetWinMatchMarket());// FT:L1SML

        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "A"));
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "B"));

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());// FT:W1S:A
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());// FT:W1S:B

        if (!matchState.isInTieBreak() || matchState.getTieBreaksCounter() == 0) {
            markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        } // FT:TBIM

        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU

        markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW

        if (y < 7)
            markets.addMarketWithShortKey(generateTotalMatchMarket(matchState)); // FT:OU
        markets.addMarketWithShortKey(generateToWinMarket(x));
        markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, gamesPerSet, matchState, true)); // P:CS
        if (x + 1 <= matchState.getNoSetsInMatch())
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, gamesPerSet, matchState, true));

        if (x < setsPerMatch)
            markets.addMarketWithShortKey(generateTotalSetMarket(matchState));// FT:S:OU
        markets.addMarketWithShortKey(generateGameHandicapMarket(matchState));
        return markets;
    }

    private static Market generateMatchBettingMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:ML", "M", "Match betting");
        market.setIsValid(true);

        market.put("A", 0.0);
        market.put("B", 0.0);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        return market;
    }

    // new ATP NEXT GEN Markets

    private static Markets generateP8Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        boolean inWimblendonFinalSet = matchState.isInWimblendon() && matchState.isInFinalSet();
        Markets markets = new Markets();
        int gamesPerSet = 6;
        if (matchFormat.getTournamentLevel() == TournamentLevel.ATP_NEXTGEN) {
            gamesPerSet = 4;
        }
        int x = matchState.getSetNo();
        int y = matchState.getGameNo();
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));
        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();
        if (generateMarket) {
            markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
            if (y < 7)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
            if (y < 6)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
        }

        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 3));

                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            } else if (inChampionshipTiebreak) {
                /*
                 * Do buggar all
                 */
            } else if (inWimblendonFinalSet) {
                if (y < 25)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 24)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 23)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
            } else if (tieBreak && !inChampionshipTiebreak) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
            }

        }

        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 1; i < 4; i++) {
                markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));
            }

        int setNo = matchState.getSetNo();
        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo != 3)
                markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        } else {
            markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 <= matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        }

        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();

        markets.addMarketWithFullKey(generateTSetXGameOverUnderNextGenMarket(x, gameA, gameB));
        if (!matchState.isInFinalSet())
            markets.addMarketWithFullKey(generateTSetXGameOverUnderNextGenMarket(x + 1));

        markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, gamesPerSet, matchState));
        if (!matchState.isInFinalSet())
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, gamesPerSet, matchState));
        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());
        return markets;
    }

    // PO/P1 Market

    private static Markets generateP01Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        boolean inWimblendonFinalSet = matchState.isInWimblendon() && matchState.isInFinalSet();
        Markets markets = new Markets();
        int gamesPerSet = 6;
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));

        int setNo = matchState.getSetNo();
        markets.addMarketWithShortKey(generateToWinMarket(setNo));
        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));

        } else if (setNo + 1 <= matchState.getNoSetsInMatch())
            markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));

        markets.addMarketWithShortKey(generateGameHandicapMarket(matchState));
        markets.addMarketWithShortKey(generateTotalMatchMarket(matchState));
        int x = matchState.getSetNo();
        int y = matchState.getGameNo();

        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();

        boolean inWimbledon = matchFormat.getFinalSetType() == FinalSetType.WIMBLEDON_TIE_BREAK;
        int nGamesinFinalSet = matchState.getnGamesInFinalSet();
        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !inChampionshipTiebreak) {
            if (inWimbledon && matchState.isInFinalSet()) {
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < (2 * nGamesinFinalSet - 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }

        }
        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {

                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 3));

                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            } else if (inChampionshipTiebreak) {
                /*
                 * Do buggar all
                 */
            } else if (inWimblendonFinalSet) {
                if (y < 25)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 24)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 23)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
            } else if (tieBreak && !inChampionshipTiebreak) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
                // 5-5 (11th game) creates 13th game winner and CS
                // 6-5 (12th game) generates 13th Game winner and No G:ML
                // 6-6 13th game CS created
            }

        }
        if (!matchState.isInTieBreak()) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(x));// P:SPRD
        }
        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 0; i < 6; i++) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));
            }
        // markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x,
        // y-1, z+1));

        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();
        if (!inChampionshipTiebreak) {
            Market market = generateTSetXGameOverUnderMarket(x, 6.5);
            if (gameA >= 1 && gameB >= 1)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 7.5);
            if (gameA >= 2 && gameB >= 2)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 8.5);
            if (gameA >= 3 && gameB >= 3)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 9.5);
            if (gameA >= 4 && gameB >= 4)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 10.5);
            if (gameA >= 5 && gameB >= 5)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 12.5);
            if (gameA == 6 && gameB == 6)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
        }
        if (!(gameA >= 5 && gameB >= 5)) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x));
        }
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 6.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 7.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 8.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 9.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 10.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 12.5));
        }
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1));
        if (!inChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, gamesPerSet, matchState));
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, gamesPerSet, matchState));

        markets.addMarketWithShortKey(generatetLostOneSetWinMatchMarket());
        if (matchState.isInTieBreak())
            markets.addMarketWithShortKey(generateTiebreakCorrectScoreMarket(matchState));

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());

        if (!(matchState.isInFinalSet())) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(matchState));
        }
        //
        if (!matchState.isPreMatch()) {// SG:AA,SG:BB
            if (matchState.getGameNo() == 1) {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
            } else {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "B"));
                if (!matchState.isPotentialNextSet(matchFormat) && matchState.getGameNo() != 12) {
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
                }
            }
        }
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "A"));
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "B"));
        markets.addMarketWithShortKey(generateTotalSetMarket(matchState));
        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU
        markets.addMarketWithShortKey(generateWinFirstSetAndMatchMarket());
        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S
        /*
         * stop create the tie break winner now.
         */
        // if (matchState.isInTieBreak())
        if (gameA >= 5 && gameB >= 5 && gameA < 7 && gameB < 7)
            markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));

        /*
         * please add P2 markets below
         *
         */
        if (matchState.isInFinalSet()) {

            if (matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) || !matchState.isTieBreakinFinalSet()
                            || matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)) {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, false));
            } else {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
            }
        } else {
            markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
        }
        markets.addMarketWithShortKey(generateWinningMarginMarket("A"));
        markets.addMarketWithShortKey(generateWinningMarginMarket("B"));
        markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW

        markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 2, matchState));
        markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 4, matchState));
        markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 6, matchState));
        markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo + 1, 2, matchState));
        markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo + 1, 4, matchState));
        markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo + 1, 6, matchState));

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());
        if (!matchState.isPreMatch()) {
            if (z < 7) {
                markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y));
            }
            markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y + 1));
            markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y + 2));
        }
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 1));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 2));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 3));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 4));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 5));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 6));
        markets.addMarketWithShortKey(generateAlternativeSetOverUnderMarket(2.5));

        if (y < 2)
            markets.addMarketWithShortKey(generateRacetoSetXGamesYMarket(x, 2));
        else if (y < 3)
            markets.addMarketWithShortKey(generateRacetoSetXGamesYMarket(x, 3));
        else if (y < 4)
            markets.addMarketWithShortKey(generateRacetoSetXGamesYMarket(x, 4));
        else if (y < 5)
            markets.addMarketWithShortKey(generateRacetoSetXGamesYMarket(x, 5));

        if (!matchState.isInTieBreak() || matchState.getTieBreaksCounter() == 0) {
            markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        }
        return markets;
    }

    private static Market generateRacetoSetXGamesYMarket(int x, int y) {
        String sequence = "S" + x + "." + y;;
        Market market = new Market(MarketCategory.GENERAL, "P:RTSG", sequence, "Set " + x + " Race to " + y + " Games");
        market.setIsValid(true);
        market.put("Player A", 0.0);
        market.put("Player B", 0.0);
        return market;
    }

    private static Markets generateP5Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        Markets markets = new Markets();
        int gamesPerSet = 6;
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));
        markets.addMarketWithShortKey(generateMostAcesMarket());
        markets.addMarketWithShortKey(generateNextAcesMarket(1));
        if (!matchState.isDoubleMatch()) {
            markets.addMarketWithShortKey(generateTotalAcesMarket());
            markets.addMarketWithShortKey(generateTotalPlayerAAcesMarket());
        }
        markets.addMarketWithShortKey(generateTotalPlayerBAcesMarket());

        int setNo = matchState.getSetNo();
        markets.addMarketWithShortKey(generateToWinMarket(setNo));
        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));

        } else if (setNo + 1 <= matchState.getNoSetsInMatch())
            markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));

        markets.addMarketWithShortKey(generateGameHandicapMarket(matchState));
        markets.addMarketWithShortKey(generateTotalMatchMarket(matchState));
        int x = matchState.getSetNo();
        int y = matchState.getGameNo();

        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();

        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !inChampionshipTiebreak) {
            if (y < 13)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
            if (y < 12)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
            if (y < 11)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
        }
        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {

                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            } else if (tieBreak && !inChampionshipTiebreak) {
            } else if (inChampionshipTiebreak) {
                /*
                 * Do buggar all
                 */
            } else {
                // 5-5 (11th game) creates 13th game winner and CS
                // 6-5 (12th game) generates 13th Game winner and No G:ML
                // 6-6 13th game CS created
            }

        }
        if (!matchState.isInTieBreak()) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(x));// P:SPRD
        }
        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 0; i < 6; i++) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));
            }
        // markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x,
        // y-1, z+1));

        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();
        if (!inChampionshipTiebreak) {
            Market market = generateTSetXGameOverUnderMarket(x, 6.5);
            if (gameA >= 1 && gameB >= 1)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 7.5);
            if (gameA >= 2 && gameB >= 2)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 8.5);
            if (gameA >= 3 && gameB >= 3)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 9.5);
            if (gameA >= 4 && gameB >= 4)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 10.5);
            if (gameA >= 5 && gameB >= 5)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 12.5);
            if (gameA == 6 && gameB == 6)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
        }
        if (!(gameA >= 5 && gameB >= 5)) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x));
        }
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 6.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 7.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 8.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 9.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 10.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 12.5));
        }
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1));
        if (!inChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, gamesPerSet, matchState));
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, gamesPerSet, matchState));

        markets.addMarketWithShortKey(generatetLostOneSetWinMatchMarket());
        // if (matchState.isInTieBreak())
        // markets.addMarketWithShortKey(generateTiebreakCorrectScoreMarket(matchState));
        markets.addMarketWithFullKey(generateSetXDoubleFaultOverUnderNextGenMarket(TeamId.A));
        markets.addMarketWithFullKey(generateSetXDoubleFaultOverUnderNextGenMarket(TeamId.B));
        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());

        if (!(matchState.isInFinalSet())) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(matchState));
        }
        //
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "A"));
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "B"));
        markets.addMarketWithShortKey(generateTotalSetMarket(matchState));
        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU
        markets.addMarketWithShortKey(generateWinFirstSetAndMatchMarket());
        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S
        /*
         * stop create the tie break winner now.
         */
        // if (matchState.isInTieBreak())
        // if (gameA >= 5 && gameB >= 5 && gameA < 7 && gameB < 7)
        // markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));

        /*
         * please add P2 markets below
         *
         */
        if (matchState.isInFinalSet()) {

            if (matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) || !matchState.isTieBreakinFinalSet()
                            || matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)) {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, false));
            } else {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
            }
        } else {
            markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
        }
        markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 1));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 2));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 3));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 4));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 5));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 6));
        markets.addMarketWithShortKey(generateAlternativeSetOverUnderMarket(2.5));
        markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        markets.addMarketWithShortKey(generateAlternativeSetHandicapMarket(matchState));
        markets.addMarketWithShortKey(generateWinningMarginMarket("A"));
        markets.addMarketWithShortKey(generateWinningMarginMarket("B"));
        if (!inChampionshipTiebreak) {
            markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 2, matchState));
            markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 4, matchState));
            markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 6, matchState));
        }
        markets.addMarketWithShortKey(generatePlayerABreakPointFirstInSetMarket(matchState));
        markets.addMarketWithShortKey(generatePlayerBBreakPointFirstInSetMarket(matchState));
        return markets;
    }

    private static Markets generateP6Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        boolean inWimblendonFinalSet = matchState.isInWimblendon() && matchState.isInFinalSet();
        Markets markets = new Markets();
        int gamesPerSet = 6;
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));
        markets.addMarketWithShortKey(generateMostAcesMarket());
        int aceNo = matchState.getTotalAcesA() + matchState.getTotalAcesB() + 2;
        markets.addMarketWithShortKey(generateNextAcesMarket(aceNo));
        if (!matchState.isDoubleMatch()) {
            markets.addMarketWithShortKey(generateTotalAcesMarket());
            markets.addMarketWithShortKey(generateTotalPlayerAAcesMarket());
        }
        markets.addMarketWithShortKey(generateTotalPlayerBAcesMarket());

        int setNo = matchState.getSetNo();
        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo != 3)
                markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        } else {
            markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 <= matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        }

        markets.addMarketWithShortKey(generateGameHandicapMarket(matchState));
        markets.addMarketWithShortKey(generateTotalMatchMarket(matchState));
        int x = matchState.getSetNo();
        int y = matchState.getGameNo();

        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();
        boolean inWimbledon = matchFormat.getFinalSetType() == FinalSetType.WIMBLEDON_TIE_BREAK;
        int nGamesinFinalSet = matchState.getnGamesInFinalSet();
        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !inChampionshipTiebreak) {
            if (inWimbledon && matchState.isInFinalSet()) {
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < (2 * nGamesinFinalSet - 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }

        }

        if (matchState.isInFinalSet() && matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET)) {
            if (matchFormat.getSetsPerMatch() == 3) {
                if (y < maxWomenGameWinnerMarket + 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
                if (y < maxWomenGameWinnerMarket)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < maxWomenGameWinnerMarket - 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }
            if (matchFormat.getSetsPerMatch() == 5) {
                if (y < maxMenGameWinnerMarket + 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
                if (y < maxMenGameWinnerMarket)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < maxMenGameWinnerMarket - 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }
        }

        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 3));

                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            } else if (inChampionshipTiebreak) {
                /*
                 * Do buggar all
                 */
            } else if (inWimblendonFinalSet) {
                if (y < 25)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 24)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 23)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
            } else if (tieBreak && !inChampionshipTiebreak) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
                // 5-5 (11th game) creates 13th game winner and CS
                // 6-5 (12th game) generates 13th Game winner and No G:ML
                // 6-6 13th game CS created
            }

        }
        if (!matchState.isInTieBreak()) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(x));// P:SPRD
        }
        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 0; i < 6; i++) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));
            }

        if (!matchState.preMatch()) {
            for (int i = 1; i < 4; i++) {
                if (y == 13) {
                    markets.addMarketWithShortKey(generateTSetXTiebreakPointZWinnerMarket(x, z + i));// G:TBPW
                }
            }
        }

        // markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x,
        // y-1, z+1));
        markets.addMarketWithFullKey(generateSetXDoubleFaultOverUnderNextGenMarket(TeamId.A));
        markets.addMarketWithFullKey(generateSetXDoubleFaultOverUnderNextGenMarket(TeamId.B));

        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();
        if (!inChampionshipTiebreak) {
            Market market = generateTSetXGameOverUnderMarket(x, 6.5);
            if (gameA >= 1 && gameB >= 1)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 7.5);
            if (gameA >= 2 && gameB >= 2)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 8.5);
            if (gameA >= 3 && gameB >= 3)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 9.5);
            if (gameA >= 4 && gameB >= 4)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 10.5);
            if (gameA >= 5 && gameB >= 5)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 12.5);
            if (gameA == 6 && gameB == 6)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
        }
        if (!(gameA >= 5 && gameB >= 5)) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x));
        } else {
            markets.addMarketWithFullKey(generateSuspendTSetXGameOverUnderMarket(x));
        }
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 6.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 7.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 8.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 9.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 10.5));
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1, 12.5));
        }
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1));
        if (!inChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, gamesPerSet, matchState));
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, gamesPerSet, matchState));

        markets.addMarketWithShortKey(generatetLostOneSetWinMatchMarket());
        if (matchState.isInTieBreak())
            markets.addMarketWithShortKey(generateTiebreakCorrectScoreMarket(matchState));

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());

        if (!(matchState.isInFinalSet())) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(matchState));
        }
        //
        if (!matchState.isPreMatch()) {
            if (matchState.getGameNo() == 1) {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
            } else if (!matchState.isInTieBreak()) {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "B"));
                if (!matchState.isPotentialNextSet(matchFormat) && matchState.getGameNo() != 12) {
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
                }
            }
        }
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "A"));
        markets.addMarketWithShortKey(generatePlayerTotalMatchMarket(matchState, "B"));
        markets.addMarketWithShortKey(generateTotalSetMarket(matchState));
        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU
        markets.addMarketWithShortKey(generateWinFirstSetAndMatchMarket());
        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S

        if (matchState.isInSuperTieBreak())
            markets.addMarketWithShortKey(generateSuperTiebreakCorrectScoreMarket(matchState));// P:CTBCS

        /*
         * stop create the tie break winner now.
         */
        // if (matchState.isInTieBreak())
        if (gameA >= 5 && gameB >= 5 && gameA < 7 && gameB < 7)
            markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));

        /*
         * please add P2 markets below
         *
         */
        if (matchState.isInFinalSet()) {

            if (matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) || !matchState.isTieBreakinFinalSet()
                            || matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)) {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, false));
            } else {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
            }
        } else {
            markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
        }

        markets.addMarketWithShortKey(generateWinningMarginMarket("A"));
        markets.addMarketWithShortKey(generateWinningMarginMarket("B"));
        markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW
        if (!inChampionshipTiebreak) {
            if (y <= 2)
                markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 2, matchState));
            if (y <= 4)
                markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 4, matchState));
            if (y <= 6)
                markets.addMarketWithShortKey(generateSetScoreAfterMarket(setNo, 6, matchState));
        }

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());
        if (!matchState.isPreMatch()) {
            if (gameToDeuceCriteria(matchState)) {
                if (z < 7) {
                    markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y));
                }
                markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y + 1));
                markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y + 2));
            }
        }
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 1));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 2));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 3));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 4));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 5));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 6));
        markets.addMarketWithShortKey(generateAlternativeSetOverUnderMarket(2.5));
        if (!matchState.isInTieBreak() || matchState.getTieBreaksCounter() == 0) {
            markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        }
        /*
         * 
         */
        markets.addMarketWithShortKey(generateAlternativeSetHandicapMarket(matchState));
        if (matchState.isInSuperTieBreak() && matchState.isInTieBreak()) {

        } else {
            markets.addMarketWithShortKey(generatePlayerABreakPointFirstInSetMarket(matchState));
            markets.addMarketWithShortKey(generatePlayerBBreakPointFirstInSetMarket(matchState));
        }
        return markets;
    }

    private static boolean gameToDeuceCriteria(TennisMatchState matchState) {
        boolean finalSetTieBreak = matchState.isFinalSetSuperTieBreak();
        boolean inFinalSet = matchState.isInFinalSet();
        return !(finalSetTieBreak && inFinalSet);
    }

    private static Markets generateP3Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket());// FT:ML
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));// FT:CS
        if (!(matchState.isInFinalSet())) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(matchState)); // FT:S:SPRD
        }
        int setNo = matchState.getSetNo();
        markets.addMarketWithShortKey(generateToWinMarket(setNo));
        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));

        } else if (setNo + 1 <= matchState.getNoSetsInMatch())
            markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));

        int x = matchState.getSetNo();
        int y = matchState.getGameNo();
        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();
        if (!matchState.isInTieBreak()) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(x));// P:SPRD
        }
        if (!(gameA >= 5 && gameB >= 5)) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x));// S:OU3
        }
        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();

        if (!inChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, 6, matchState));// P:CS

        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, 6, matchState));

        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !inChampionshipTiebreak) {
            if (y < 13)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
            if (y < 12)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
            if (y < 11)
                markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
        }
        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            }

        }
        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 0; i < 6; i++) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));// G:PW
            }

        // int gameA = matchState.getGamesA();
        // int gameB = matchState.getGamesB();

        // if (matchState.isInTieBreak())
        // markets.addMarketWithShortKey(generateTiebreakCorrectScoreMarket(matchState));//
        // P:TBCS

        if (!matchState.isPreMatch()) {// SG:AA,SG:BB
            if (matchState.getGameNo() == 1) {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
            } else {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "B"));
                if (!matchState.isPotentialNextSet(matchFormat) && matchState.getGameNo() != 12) {
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
                }
            }
        }
        /*
         * stop create the tie break winner now.
         */
        // if (gameA >= 5 && gameB >= 5 && gameA < 7 && gameB < 7)
        // markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));//
        // P:TBML

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());// FT:W1S:A
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());// FT:W1S:B
        /*
         * please add P2 markets below
         *
         */
        if (matchState.isInFinalSet()) {

            if (matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) || !matchState.isTieBreakinFinalSet()
                            || matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)) {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, false));
            } else {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
            }
        } else {
            markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
        }
        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU
        markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 1));// FT:ASPRD
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 2));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 3));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 4));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 5));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 6));
        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S
        if (!matchState.isInTieBreak() || matchState.getTieBreaksCounter() == 0) {
            markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        } // FT:TBIM
        return markets;
    }

    private static Markets generateP4Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        boolean inWimblendonFinalSet = matchState.isInWimblendon() && matchState.isInFinalSet();
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket());// FT:ML
        markets.addMarketWithShortKey(generateSetBettingMarket(matchState));// FT:CS
        if (!(matchState.isInFinalSet())) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(matchState)); // FT:S:SPRD
        }
        int setNo = matchState.getSetNo();
        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo != 3)
                markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        } else {
            markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 <= matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        }

        int x = matchState.getSetNo();
        int y = matchState.getGameNo();
        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();

        if (!matchState.isInTieBreak()) {
            markets.addMarketWithShortKey(generateSetHandicapMarket(x));// P:SPRD
        }
        if (!(gameA >= 5 && gameB >= 5)) {
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x));// S:OU3
        } else {
            markets.addMarketWithFullKey(generateSuspendTSetXGameOverUnderMarket(x));
        }

        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();

        if (!inChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x, 6, matchState));// P:CS
        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithFullKey(generateTSetXGameOverUnderMarket(x + 1));

        if (x + 1 <= matchState.getNoSetsInMatch() && !nextSetChampionshipTiebreak)
            markets.addMarketWithShortKey(generateTSetXCorrectScoreMarket(x + 1, 6, matchState));

        boolean inWimbledon = matchFormat.getFinalSetType() == FinalSetType.WIMBLEDON_TIE_BREAK;
        int nGamesinFinalSet = matchState.getnGamesInFinalSet();
        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !inChampionshipTiebreak) {
            if (inWimbledon && matchState.isInFinalSet()) {
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < (2 * nGamesinFinalSet - 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }

        }
        if (matchState.isInFinalSet() && matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET)) {
            if (matchFormat.getSetsPerMatch() == 3) {
                if (y < maxWomenGameWinnerMarket + 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
                if (y < maxWomenGameWinnerMarket)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < maxWomenGameWinnerMarket - 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }
            if (matchFormat.getSetsPerMatch() == 5) {
                if (y < maxMenGameWinnerMarket + 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
                if (y < maxMenGameWinnerMarket)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < maxMenGameWinnerMarket - 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }
        }

        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 0; i < 6; i++) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));// G:PW
            }

        if (!matchState.preMatch()) {
            for (int i = 1; i < 4; i++) {
                if (y == 13) {
                    markets.addMarketWithShortKey(generateTSetXTiebreakPointZWinnerMarket(x, z + i));// G:TBPW
                }
            }
        }

        if (matchState.isInTieBreak())
            markets.addMarketWithShortKey(generateTiebreakCorrectScoreMarket(matchState));// P:TBCS

        if (!matchState.isPreMatch()) {// SG:AA,SG:BB
            if (matchState.getGameNo() == 1) {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
            } else {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "B"));
                if (!matchState.isPotentialNextSet(matchFormat) && matchState.getGameNo() != 12) {
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
                }
            }
        }
        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x + 1, 3));

                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            } else if (inChampionshipTiebreak) {
                /*
                 * Do buggar all
                 */
            } else if (inWimblendonFinalSet) {
                if (y < 25)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 24)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 23)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
            } else if (tieBreak && !inChampionshipTiebreak) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateSetXGameYCSMarket(x, y + 2));
                // 5-5 (11th game) creates 13th game winner and CS
                // 6-5 (12th game) generates 13th Game winner and No G:ML
                // 6-6 13th game CS created
            }

        }
        /*
         * stop create the tie break winner now.
         */
        if (gameA >= 5 && gameB >= 5 && gameA < 7 && gameB < 7)
            markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));// P:TBML

        if (matchState.getSetsA() < 1)
            markets.addMarketWithShortKey(generateAToWinOneSetMarket());// FT:W1S:A
        if (matchState.getSetsB() < 1)
            markets.addMarketWithShortKey(generateBToWinOneSetMarket());// FT:W1S:B

        if (matchState.isInSuperTieBreak())
            markets.addMarketWithShortKey(generateSuperTiebreakCorrectScoreMarket(matchState));// P:TBCS

        /*
         * please add P2 markets below
         *
         */
        if (!inChampionshipTiebreak) {
            Market market = generateTSetXGameOverUnderMarket(x, 6.5);
            if (gameA >= 1 && gameB >= 1)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 7.5);
            if (gameA >= 2 && gameB >= 2)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 8.5);
            if (gameA >= 3 && gameB >= 3)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 9.5);
            if (gameA >= 4 && gameB >= 4)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 10.5);
            if (gameA >= 5 && gameB >= 5)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
            market = generateTSetXGameOverUnderMarket(x, 12.5);
            if (gameA == 6 && gameB == 6)
                market.setSuspensionStatus("Guarented outcome", SuspensionStatus.SUSPENDED_UNDISPLAY,
                                "Should suspend as a guarented outcome");
            markets.addMarketWithFullKey(market);
        }
        if (!matchState.isPreMatch()) {
            if (gameToDeuceCriteria(matchState)) {
                if (z < 7) {
                    markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y));
                }
                markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y + 1));
                markets.addMarketWithShortKey(generateSetXGameYGotoDeuceMarket(x, y + 2));
            }
        }
        if (matchState.isInFinalSet()) {

            if (matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) || !matchState.isTieBreakinFinalSet()
                            || matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)) {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, false));
            } else {
                markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
            }
        } else {
            markets.addMarketWithShortKey(generateTSetXCorrectScoreGroupMarket(x, true));
        }
        markets.addMarketWithShortKey(generateTotalTieBreaksMarket(matchState));// FT:TB:OU
        markets.addMarketWithShortKey(generatetBothTeamToWinOneSetMarket());// FT:BPTW
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 1));// FT:ASPRD
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 2));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 3));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 4));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 5));
        markets.addMarketWithShortKey(generateAlternativeGameHandicapMarket(matchState, 6));
        markets.addMarketWithShortKey(generateBothToWinASetMarket(matchState));// FT:BTW1S
        if (!matchState.isInTieBreak() || matchState.getTieBreaksCounter() == 0) {
            markets.addMarketWithShortKey(generateTieBreaksInMatchMarket());
        } // FT:TBIM
        return markets;
    }

    private static Markets generateP2Market(TennisMatchState matchState, TennisMatchFormat matchFormat) {
        boolean tieBreak = matchState.isInTieBreak();
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket());// FT:ML

        int setNo = matchState.getSetNo();

        if (matchFormat.getSetsPerMatch() == 3) {
            if (setNo != 3)
                markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 < matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        } else {
            markets.addMarketWithShortKey(generateToWinMarket(setNo));
            if (setNo + 1 <= matchState.getNoSetsInMatch())
                markets.addMarketWithShortKey(generateToWinMarket(setNo + 1));
        }

        int x = matchState.getSetNo();
        int y = matchState.getGameNo();

        boolean inChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isInFinalSet();
        boolean nextSetChampionshipTiebreak = matchFormat.getFinalSetType().equals(FinalSetType.CHAMPIONSHIP_TIE_BREAK)
                        && matchState.isNextSetFinalSet();

        boolean inWimbledon = matchFormat.getFinalSetType() == FinalSetType.WIMBLEDON_TIE_BREAK;
        int nGamesinFinalSet = matchState.getnGamesInFinalSet();
        boolean generateMarket = !matchState.isPreMatch() && matchState.isGameMayBePlayed(matchState.getGameNo());
        if (generateMarket && !inChampionshipTiebreak) {
            if (inWimbledon && matchState.isInFinalSet()) {
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < (2 * nGamesinFinalSet + 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < (2 * nGamesinFinalSet - 1))
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            } else {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));
                if (y < 12)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < 11)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }

        }

        if (matchState.isInFinalSet() && matchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET)) {
            if (matchFormat.getSetsPerMatch() == 3) {
                if (y < maxWomenGameWinnerMarket + 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
                if (y < maxWomenGameWinnerMarket)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < maxWomenGameWinnerMarket - 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }
            if (matchFormat.getSetsPerMatch() == 5) {
                if (y < maxMenGameWinnerMarket + 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y));// G:ML
                if (y < maxMenGameWinnerMarket)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 1));
                if (y < maxMenGameWinnerMarket - 1)
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x, y + 2));
            }
        }

        if (!matchState.isPreMatch()) {
            if (tieBreak && !matchState.isInFinalSet()) {
                if (!nextSetChampionshipTiebreak) {
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 1));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 2));
                    markets.addMarketWithShortKey(generateTSetXGameYWinnerMarket(x + 1, 3));
                }
            }

        }
        int z = matchState.getPointNo();
        if (!matchState.preMatch())
            for (int i = 0; i < 6; i++) {
                if (y < 13)
                    markets.addMarketWithShortKey(generateTSetXGameYPointZWinnerMarket(x, y, z + i));// G:PW
            }

        if (!matchState.preMatch()) {
            for (int i = 1; i < 4; i++) {
                if (y == 13) {
                    markets.addMarketWithShortKey(generateTSetXTiebreakPointZWinnerMarket(x, z + i));// G:TBPW
                }
            }
        }

        int gameA = matchState.getGamesA();
        int gameB = matchState.getGamesB();

        if (matchState.isInTieBreak())
            markets.addMarketWithShortKey(generateTiebreakCorrectScoreMarket(matchState));// P:TBCS

        if (!matchState.isPreMatch()) {// SG:AA,SG:BB
            if (matchState.getGameNo() == 1) {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
            } else {
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "A"));
                markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y - 1, "B"));
                if (!matchState.isPotentialNextSet(matchFormat) && matchState.getGameNo() != 12) {
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "A"));
                    markets.addMarketWithShortKey(generatePlayerWinSetXGameYandPlusOneMarket(x, y, "B"));
                }
            }
        }
        /*
         * stop create the tie break winner now.
         */
        if (gameA >= 5 && gameB >= 5 && gameA < 7 && gameB < 7)
            markets.addMarketWithShortKey(generateTieBreakMLMarket(matchState.getSetNo()));// P:TBML

        /*
         * please add P2 markets below
         *
         */
        return markets;
    }

    // PO/P1 Market

    @SuppressWarnings("unused")
    private static Markets generateDefaultMarket(TennisMatchState matchState) {
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        // markets.addMarketWithShortKey(generateGameHandicapMarket(matchState));
        return markets;
    }

    private static Market generateTieBreakMLMarket(int set) {
        String sequence = "S" + set;
        Market market = new Market(MarketCategory.GENERAL, "P:TBML", sequence,
                        String.format("Set %d Tiebreak Winner", set));
        market.setIsValid(true);

        market.put("A", 0.0);
        market.put("B", 0.0);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        return market;
    }

    private static Market generateAlternativeSetOverUnderMarket(double line) {
        String sequence = "M";
        Market market = new Market(MarketCategory.GENERAL, "FT:ALT:S:OU", sequence, ("Alternate Total Sets "));
        market.setLineId(Double.toString(line));
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over");
        market.setSelectionNameUnderOrB("Under");
        return market;
    }

    @SuppressWarnings("unused")
    private static Market generateTeamBreakPointFirstInSetMarket(TennisMatchState matchState) {
        String sequence = "S" + matchState.getSetNo();
        Market market = new Market(MarketCategory.GENERAL, "FT:FSB", sequence, "Team Break Point First");
        market.setIsValid(true);
        market.put("1", 0.0);
        market.put("2", 0.0);
        market.put("3", 0.0);
        market.put("4", 0.0);
        market.put("5", 0.0);
        market.put("6", 0.0);
        market.put("7", 0.0);
        market.put("8", 0.0);
        market.put("9", 0.0);
        market.put("10", 0.0);
        market.put("11", 0.0);
        market.put("None", 0.0);
        return market;
    }

    private static Market generatePlayerABreakPointFirstInSetMarket(TennisMatchState matchState) {
        String sequence = "S" + matchState.getSetNo();
        Market market = new Market(MarketCategory.GENERAL, "FT:FSB:A", sequence,
                        "Set " + matchState.getSetNo() + " Player A 1st Break Game");
        market.setIsValid(true);
        market.put("Doesn't break serve in set", 0.0);
        market.put("Games 1-4", 0.0);
        market.put("Games 5-8", 0.0);
        market.put("After Game 8", 0.0);
        return market;
    }

    private static Market generatePlayerBBreakPointFirstInSetMarket(TennisMatchState matchState) {
        String sequence = "S" + matchState.getSetNo();
        Market market = new Market(MarketCategory.GENERAL, "FT:FSB:B", sequence,
                        "Set " + matchState.getSetNo() + " Player B 1st Break Game");
        market.setIsValid(true);
        market.put("Doesn't break serve in set", 0.0);
        market.put("Games 1-4", 0.0);
        market.put("Games 5-8", 0.0);
        market.put("After Game 8", 0.0);
        return market;
    }

    private static Market generateBothToWinASetMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.GENERAL, "FT:BTW1S", "M", "Match and Both Players to win a Set");
        market.setIsValid(true);
        market.put("Player A wins the match and both players win a set", 0.0);
        market.put("Player B wins the match and both players win a set", 0.0);
        return market;
    }

    private static Market generateWinFirstSetAndMatchMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:W1SML", "M", "Win 1st Set and Win Match");
        market.setIsValid(true);

        market.put("A Yes", 0.0);
        market.put("B Yes", 0.0);
        market.setSelectionNameOverOrA("A Yes");
        market.setSelectionNameUnderOrB("B Yes");
        return market;
    }

    private static Market generateTotalSetMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.OVUN, "FT:S:OU", "M", "Total Sets");
        market.setLineId(lineId);
        market.setLineBase(2);
        double[] probs = {0.5848, 1.0};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);

        return market;
    }

    /*
     * BFAM-130
     */
    private static Market generateTotalTieBreaksMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.OVUN, "FT:TB:OU", "M", "Total Tie Breaks");
        market.setLineId("1.5");
        String lineId = "1.5";
        market.setLineBase(0);
        double[] probs = {0.5242, 0.8906, 0.9904, 1.0};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    private static Market generatePlayerTotalMatchMarket(TennisMatchState matchState, String string) {
        Market market = new Market(MarketCategory.OVUN, "FT:OU:" + string, "M", "Player " + string + " Total Games");

        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    private static Market generatePlayerWinSetXGameYandPlusOneMarket(int x, int y, String player) {
        String sequenceId = "S" + x + "." + y;
        String teamInMarketTypeCode = player.toUpperCase() + player.toUpperCase();
        Market market = new Market(MarketCategory.GENERAL, "SG:" + teamInMarketTypeCode, sequenceId,
                        "Player " + player + " To Win Set " + x + " Game " + y + ", " + (y + 1));
        market.setIsValid(true);
        market.put("Yes", 0.0);
        // market.put("No", 0.0);
        market.setSelectionNameOverOrA("Yes");
        market.setSelectionNameUnderOrB("No");
        return market;
    }

    private static Market generateSuperTiebreakCorrectScoreMarket(TennisMatchState matchState) {
        boolean nextGen = ((TennisMatchFormat) matchState.getMatchFormat())
                        .getTournamentLevel() == TournamentLevel.ATP_NEXTGEN;
        // String sequence = "S" + x;
        String marketName = "Championship Tie Break Correct Score";
        Market market;
        String teamA = "Player A ";
        String teamB = "Player B ";

        if (!nextGen) {
            market = new Market(MarketCategory.GENERAL, "FT:CTBCS", "M", marketName);
            // Championship tiebreak scenarios
            int win = 10;
            int losePoint = 8;
            int apoint = 0;
            int bpoint = 0;
            if (matchState.isInTieBreak()) {
                apoint = matchState.getPointsB();
                bpoint = matchState.getPointsA();
            }

            for (int sa = apoint; sa < win; sa++)
                if (sa <= losePoint)
                    market.put(teamA + win + "-" + sa, 0.0);

            for (int sb = bpoint; sb < win; sb++)
                if (sb <= losePoint)
                    market.put(teamB + win + "-" + sb, 0.0);

            market.put(teamA + "Any Other score", 0.0);
            market.put(teamB + "Any Other score", 0.0);

        } else {
            // no logic for the next gen matches
            market = new Market(MarketCategory.GENERAL, "FT:CTBCS", "M", marketName);
        }
        market.setIsValid(true);
        // market.put("B Any Other Score", 0.0);
        return market;
    }

    private static Market generateTiebreakCorrectScoreMarket(TennisMatchState matchState) {
        int x = matchState.getSetNo();
        boolean inSuperTieBreak = matchState.isInSuperTieBreak();
        boolean nextGen = ((TennisMatchFormat) matchState.getMatchFormat())
                        .getTournamentLevel() == TournamentLevel.ATP_NEXTGEN;
        String sequence = "S" + x;
        String marketName = "Set " + x + " Tiebreak Correct Score";
        Market market;
        String teamA = "Player A ";
        String teamB = "Player B ";

        if (!nextGen) {
            market = new Market(MarketCategory.GENERAL, "P:TBCS", sequence, marketName);
            // Championship tiebreak scenarios
            int win = (inSuperTieBreak ? 10 : 7);
            int losePoint = (inSuperTieBreak ? 9 : 6);
            int apoint = 0;
            int bpoint = 0;
            if (matchState.isInTieBreak()) {
                apoint = matchState.getPointsB();
                bpoint = matchState.getPointsA();
            }

            for (int sa = apoint; sa < win; sa++)
                if (sa < losePoint)
                    market.put(teamA + win + "-" + sa, 0.0);

            for (int sb = bpoint; sb < win; sb++)
                if (sb < losePoint)
                    market.put(teamB + win + "-" + sb, 0.0);
            market.put(teamA + "Any other Score", 0.0);
            market.put(teamB + "Any other Score", 0.0);

        } else {
            marketName += " (Next Gen)";
            market = new Market(MarketCategory.GENERAL, "P:CS", sequence, marketName);
            int win = 4;
            if (matchState.getSetNo() + 1 == x) {
                for (int sa = 0; sa < win; sa++)
                    market.put("Player A " + win + "-" + sa, 0.0);

                for (int sb = 0; sb < win; sb++)
                    market.put("Player B " + win + "-" + sb, 0.0);
            } else {
                for (int sa = matchState.getGamesA(); sa < win; sa++)
                    market.put("Player A " + win + "-" + sa, 0.0);

                for (int sb = matchState.getGamesB(); sb < win; sb++)
                    market.put("Player B " + win + "-" + sb, 0.0);
            }

        }
        market.setIsValid(true);
        // market.put("B Any Other Score", 0.0);
        return market;
    }

    private static Market generateSetHandicapMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.HCAP, "FT:S:SPRD", "M", "Set Handicap");
        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs1 = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs1);
        market.setIsValid(true);
        market.put("AH", 0.0);
        market.put("BH", 0.0);
        market.setSelectionNameOverOrA("AH");
        market.setSelectionNameUnderOrB("BH");
        return market;
    }

    private static Market generateAlternativeSetHandicapMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.HCAP, "FT:S:ASPRD", "M", "Alternative Set Handicap");
        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs1 = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs1);
        market.setIsValid(true);
        market.put("AH", 0.0);
        market.put("BH", 0.0);
        market.setSelectionNameOverOrA("AH");
        market.setSelectionNameUnderOrB("BH");
        return market;
    }

    private static Market generatetBothTeamToWinOneSetMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:BPTW", "M", "Both player to win a set");
        market.setIsValid(true);
        market.put("Yes", 0.0);
        market.put("No", 0.0);
        market.setSelectionNameOverOrA("Yes");
        market.setSelectionNameUnderOrB("No");
        return market;
    }

    private static Market generatetLostOneSetWinMatchMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:L1SML", "M", "Lose 1st Set and Win Match");
        market.setIsValid(true);
        market.put("A Yes", 0.0);
        market.put("B Yes", 0.0);
        market.setSelectionNameOverOrA("A Yes");
        market.setSelectionNameUnderOrB("B Yes");
        return market;
    }

    private static Market generateTSetXCorrectScoreMarket(int x, int gamesPerSet, TennisMatchState matchState) {
        String sequence = "S" + x;
        String marketName = "Set " + x + " Correct Score";
        Market market;

        if (gamesPerSet == 6) {
            market = new Market(MarketCategory.GENERAL, "P:CS", sequence, marketName);
            int win = 6;
            if (matchState.getSetNo() + 1 == x) {
                for (int sa = 0; sa < win - 1; sa++)
                    market.put("Player A " + win + "-" + sa, 0.0);
                for (int sb = 0; sb < win - 1; sb++)
                    market.put("Player B " + win + "-" + sb, 0.0);
                market.put("Player A 7-6", 0.0);
                market.put("Player B 7-6", 0.0);
                market.put("Player A 7-5", 0.0);
                market.put("Player B 7-5", 0.0);
            } else {
                for (int sa = matchState.getGamesB(); sa <= win; sa++)
                    if (sa < win - 1)
                        market.put("Player A " + win + "-" + sa, 0.0);
                    else
                        market.put("Player A 7-" + sa, 0.0);

                for (int sb = matchState.getGamesA(); sb <= win; sb++)
                    if (sb < win - 1)
                        market.put("Player B " + win + "-" + sb, 0.0);
                    else
                        market.put("Player B 7-" + sb, 0.0);
            }

        } else {
            marketName += " (Next Gen)";
            market = new Market(MarketCategory.GENERAL, "P:CS", sequence, marketName);
            int win = 4;
            if (matchState.getSetNo() + 1 == x) {
                for (int sa = 0; sa < win; sa++)
                    market.put("Player A " + win + "-" + sa, 0.0);

                for (int sb = 0; sb < win; sb++)
                    market.put("Player B " + win + "-" + sb, 0.0);
            } else {
                for (int sa = matchState.getGamesA(); sa < win; sa++)
                    market.put("Player A " + win + "-" + sa, 0.0);

                for (int sb = matchState.getGamesB(); sb < win; sb++)
                    market.put("Player B " + win + "-" + sb, 0.0);
            }

        }
        market.setIsValid(true);
        // market.put("B Any Other Score", 0.0);
        return market;
    }

    private static Market generateTSetXCorrectScoreMarket(int x, int gamesPerSet, TennisMatchState matchState,
                    boolean fast4) {
        String sequence = "S" + x;
        String marketName = "Set " + x + " Correct Score";
        if (matchState.getTournamentLevel().equals(TennisMatchFormat.TournamentLevel.FAST4)) {
            if (x == 1)
                marketName = "Correct Score " + x + "st Set";
            else if (x == 2)
                marketName = "Correct Score " + x + "nd Set";
            else if (x == 3)
                marketName = "Correct Score " + x + "rd Set";
            else if (x == 4)
                marketName = "Correct Score " + x + "th Set";
            else if (x == 5)
                marketName = "Correct Score " + x + "th Set";
        }
        Market market;
        market = new Market(MarketCategory.GENERAL, "P:CS", sequence, marketName);
        int win = 4;
        if (matchState.getSetNo() + 1 == x) {
            for (int sa = 0; sa < win; sa++)
                market.put("Player A " + win + "-" + sa, 0.0);

            for (int sb = 0; sb < win; sb++)
                market.put("Player B " + win + "-" + sb, 0.0);
        } else {
            for (int sa = matchState.getGamesB(); sa < win; sa++)
                market.put("Player A " + win + "-" + sa, 0.0);

            for (int sb = matchState.getGamesA(); sb < win; sb++)
                market.put("Player B " + win + "-" + sb, 0.0);
        }

        market.setIsValid(true);
        return market;

    }

    private static Market generateWinningMarginMarket(String teamId) {
        Market market = new Market(MarketCategory.GENERAL, "FT:WM:" + teamId, "M",
                        "Player " + teamId + " Exact Games Won Margin");
        market.setIsValid(true);
        market.put("Player " + teamId + " By 0 or Negative", 0.0);
        market.put("Player " + teamId + " By 1", 0.0);
        market.put("Player " + teamId + " By 2", 0.0);
        market.put("Player " + teamId + " By 3", 0.0);
        market.put("Player " + teamId + " By 4", 0.0);
        market.put("Player " + teamId + " By 5", 0.0);
        market.put("Player " + teamId + " By 6", 0.0);
        market.put("Player " + teamId + " By 7", 0.0);
        market.put("Player " + teamId + " By 8", 0.0);
        market.put("Player " + teamId + " By 9", 0.0);
        market.put("Player " + teamId + " By 10", 0.0);
        market.put("Player " + teamId + " By 11", 0.0);
        market.put("Player " + teamId + " By 12 or more", 0.0);
        return market;
    }

    private static Market generateTSetXCorrectScoreGroupMarket(int x, boolean standardTiebreakFinalSet) {
        String sequence = "S" + x;
        Market market = new Market(MarketCategory.GENERAL, "P:CSG", sequence,
                        "Set " + x + " Correct Score Groups Any Player");
        market.setIsValid(true);
        market.put("6-0 or 6-1", 0.0);
        market.put("6-2 or 6-3", 0.0);
        if (standardTiebreakFinalSet) {
            market.put("6-4, 7-5 or 7-6", 0.0);
        } else {
            market.put("6-4, 7-5 or Other Score", 0.0);
        }

        return market;
    }

    private static Market generateTSetXGameOverUnderMarket(int x, double line) {
        String sequence = "S" + x;
        Market market = new Market(MarketCategory.OVUN, "S:OU", sequence, "Set " + x + " Total Games Over/Under");
        market.setLineId(Double.toString(line));
        String lineId = Double.toString(line);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    private static Market generateSetXDoubleFaultOverUnderNextGenMarket(TeamId team) {
        String sequence = "M";
        String player = team == TeamId.A ? "A" : "B";
        Market market = new Market(MarketCategory.GENERAL, "FT:" + player + ":DFOU", sequence,
                        "Team " + player + " Total Double Faults in Match");
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        return market;
    }

    private static Market generateTSetXGameOverUnderNextGenMarket(int x) {
        String sequence = "S" + x;
        Market market = new Market(MarketCategory.GENERAL, "S:OUNG", sequence,
                        "Set " + x + " Total Games Over/Under(Next Gen)");
        market.setIsValid(true);
        market.put("Over 4.5", 0.0);
        market.put("Under 4.5", 0.0);
        market.put("Over 5.5", 0.0);
        market.put("Under 5.5", 0.0);
        market.put("Over 6.5", 0.0);
        market.put("Under 6.5", 0.0);
        return market;
    }

    private static Market generateTSetXGameOverUnderNextGenMarket(int x, int gameA, int gameB) {
        String sequence = "S" + x;
        Market market = new Market(MarketCategory.GENERAL, "S:OUNG", sequence,
                        "Set " + x + " Total Games Over/Under(Next Gen)");
        market.setIsValid(true);
        if (gameA >= 1 && gameB >= 1) {
        } else {
            market.put("Over 4.5", 0.0);
            market.put("Under 4.5", 0.0);
        }
        if (gameA >= 2 && gameB >= 2) {
        } else {
            market.put("Over 5.5", 0.0);
            market.put("Under 5.5", 0.0);
        }

        if (gameA >= 3 && gameB >= 3)
            market.setIsValid(false);
        else {
            market.put("Over 6.5", 0.0);
            market.put("Under 6.5", 0.0);
        }
        return market;
    }

    private static Market generateTSetXGameOverUnderMarket(int x) {
        String sequence = "S" + x;
        Market market = new Market(MarketCategory.GENERAL, "S:OU3", sequence, "Set " + x + " Total Games 3 Way");
        market.setIsValid(true);
        market.put("Over 10.5", 0.0);
        market.put("9 or 10", 0.0);
        market.put("Under 8.5", 0.0);
        market.setSelectionNameOverOrA("Over 10.5");
        market.setSelectionNameUnderOrB("Under 8.5");
        market.setSelectionNameDrawOrD("9 or 10");
        return market;
    }

    private static Market generateSuspendTSetXGameOverUnderMarket(int x) {
        String sequence = "S" + x;
        Market market = new Market(MarketCategory.GENERAL, "S:OU3", sequence, "Set " + x + " Total Games 3 Way");
        market.setIsValid(true);
        market.setSuspensionStatus("", SuspensionStatus.SUSPENDED_UNDISPLAY, "Suspend after both players reach 5");
        market.put("Over 10.5", 0.0);
        market.put("9 or 10", 0.0);
        market.put("Under 8.5", 0.0);
        market.setSelectionNameOverOrA("Over 10.5");
        market.setSelectionNameUnderOrB("Under 8.5");
        market.setSelectionNameDrawOrD("9 or 10");
        return market;
    }

    private static Market generateTSetXGameYPointZWinnerMarket(int x, int y, int z) {
        String sequenceId = "S" + x + "." + y + "." + z;
        Market market = new Market(MarketCategory.GENERAL, "G:PW", sequenceId,
                        "Set " + x + " Game " + y + " Point " + z + " Winner");
        market.setIsValid(true);
        market.put("A", 0.0);
        market.put("B", 0.0);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        return market;
    }

    private static Market generateTSetXTiebreakPointZWinnerMarket(int x, int z) {
        String sequenceId = "S" + x + ".13" + "." + z;
        Market market = new Market(MarketCategory.GENERAL, "G:TBPW", sequenceId,
                        "Set " + x + " Tiebreak " + "Point " + z + " Winner");
        market.setIsValid(true);
        market.put("Player A", 0.0);
        market.put("Player B", 0.0);
        market.setSelectionNameOverOrA("Player A");
        market.setSelectionNameUnderOrB("Player B");
        return market;
    }

    private static Market generateSetXGameYCSMarket(int x, int y) {
        String sequence = "S" + x + "." + y;
        Market market = new Market(MarketCategory.GENERAL, "G:CS", sequence,
                        "Set " + x + " Game " + y + " Correct Score");
        market.setIsValid(true);
        market.put("Player B To Win To 40", 0.0);
        market.put("Player B To Win To 30", 0.0);
        market.put("Player B To Win To 15", 0.0);
        market.put("Player B To Win To Love", 0.0);
        market.put("Player A To Win To Love", 0.0);
        market.put("Player A To Win To 15", 0.0);
        market.put("Player A To Win To 30", 0.0);
        market.put("Player A To Win To 40", 0.0);
        // market.put("0-4", 0.0);
        // market.put("1-4", 0.0);
        // market.put("2-4", 0.0);
        // market.put("3-5", 0.0);
        // market.put("4-0", 0.0);
        // market.put("4-1", 0.0);
        // market.put("4-2", 0.0);
        // market.put("5-3", 0.0);
        return market;
    }

    private static Market generateSetXGameYGotoDeuceMarket(int x, int y) {
        String sequence = "S" + x + "." + y;
        Market market = new Market(MarketCategory.GENERAL, "G:DEUCE", sequence,
                        "Set " + x + " Game " + y + " to Deuce");
        market.setIsValid(true);
        market.put("No", 0.0);
        market.put("Yes", 0.0);
        return market;
    }

    private static Market generateTSetXGameYWinnerMarket(int x, int y) {
        String sequenceId = "S" + x + "." + y;
        Market market = new Market(MarketCategory.GENERAL, "G:ML", sequenceId, "Set " + x + " Game " + y + " Winner");
        market.setIsValid(true);
        market.put("A", 0.0);
        market.put("B", 0.0);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        return market;
    }

    // P2 Market
    @SuppressWarnings("unused")
    private static Markets generateP012Market() {
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        markets.addMarketWithShortKey(generateSetHandicapMarket(1));
        return markets;
    }

    // P3 Market
    @SuppressWarnings("unused")
    private static Markets generateP0123Market() {
        Markets markets = new Markets();
        markets.addMarketWithShortKey(generateMatchBettingMarket());
        markets.addMarketWithShortKey(generateSetHandicapMarket(1));
        markets.addMarketWithShortKey(generateOvun3WayMarket());
        return markets;
    }

    /*
     * The only market needed for the day 1 release is match betting so comment out the others for now. can add back in
     * once they are needed
     */
    private static Market generateSetBettingMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.GENERAL, "FT:CS", "M", "Set betting");
        market.setIsValid(true);
        int win = (matchState.getNoSetsInMatch() + 1) / 2;
        for (int sa = matchState.getSetsA(); sa < win; sa++)
            market.put("B " + win + "-" + sa, 0.0);

        for (int sb = matchState.getSetsB(); sb < win; sb++)
            market.put("A " + win + "-" + sb, 0.0);

        return market;
    }

    private static Market generateSetScoreAfterMarket(int set, int game, TennisMatchState matchState) {
        String description = "";
        if (set < 4) {
            if (set == 1)
                description = set + "st Set Score after " + game + " games";
            if (set == 2)
                description = set + "nd Set Score after " + game + " games";
            if (set == 3)
                description = set + "rd Set Score after " + game + " games";
        } else
            description = "Set " + set + " Score after " + game + " games";

        Market market = new Market(MarketCategory.GENERAL, "P:CS" + game, "S" + set, description);

        /*
         * Generate All Possible Set game Scores from the MatchState
         */
        market.setIsValid(true);

        for (int sa = matchState.getGamesA() - 1; sa <= game; sa++) {
            for (int sb = matchState.getGamesB() - 1; sb <= game; sb++)
                if (sa + sb == game) {
                    if (sa > sb)
                        market.put("Player A " + sa + "-" + sb, 0.0);
                    else if (sa < sb)
                        market.put("Player B " + sb + "-" + sa, 0.0);
                    else
                        market.put(sa + "-" + sb, 0.0);
                }
        }

        return market;
    }

    private static Market generateToWinMarket(int setNo) {
        // int setNo = matchState.getSetNo();
        String sequenceId = "S" + setNo;
        String marketName;
        if (setNo == 1) {
            marketName = "To Win " + setNo + "st Set";
        } else {
            marketName = "Set " + setNo + " Winner";
        }
        Market market = new Market(MarketCategory.GENERAL, "P:ML", sequenceId, marketName);
        market.setIsValid(true);
        market.put("A", 0.0);
        market.put("B", 0.0);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        return market;
    }

    private static Market generateGameHandicapMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.HCAP, "FT:SPRD", "M", "Game Handicap");
        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("AH", 0.0);
        market.put("BH", 0.0);
        market.setSelectionNameOverOrA("AH");
        market.setSelectionNameUnderOrB("BH");
        return market;
    }

    private static Market generateAlternativeGameHandicapMarket(TennisMatchState matchState, int i) {
        Market market = new Market(MarketCategory.HCAP, "FT:ASPRD" + i, "M", "Alternative Game Handicap " + i);
        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("AH", 0.0);
        market.put("BH", 0.0);
        market.setSelectionNameOverOrA("AH");
        market.setSelectionNameUnderOrB("BH");
        return market;
    }

    private static Market generateTotalMatchMarket(TennisMatchState matchState) {
        Market market = new Market(MarketCategory.OVUN, "FT:OU", "M", "Total Match Games");

        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    private static Market generateSetHandicapMarket(int x) {
        Market market = new Market(MarketCategory.HCAP, "P:SPRD", "S" + x, " Set " + x + " Game Handicap");

        market.setIsValid(true);

        market.put("AH", 0.0);
        market.put("BH", 0.0);
        market.setSelectionNameOverOrA("AH");
        market.setSelectionNameUnderOrB("BH");
        return market;
    }

    private static Market generateOvun3WayMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:OU3", "M", "Total Match Games 3-Way");

        market.setIsValid(true);

        market.put("A", 0.0);
        market.put("B", 0.0);
        market.put("E", 0.0);
        market.setSelectionNameOverOrA("A");
        market.setSelectionNameUnderOrB("B");
        market.setSelectionNameDrawOrD("E");
        return market;
    }

    private static Market generateTieBreaksInMatchMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:TBIM", "M", "Tie Break In Match");
        market.setIsValid(true);
        market.put("Yes", 0.0);
        market.put("No", 0.0);
        market.setSelectionNameOverOrA("Yes");
        market.setSelectionNameUnderOrB("No");
        return market;
    }

    /*
     * The only market needed for the day 1 release is match betting so comment out the others for now. can add back in
     * once they are needed
     */
    // private static Market generateSetBettingMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "P:ML", "S1", "Set 1
    // betting");
    //
    // market.setIsValid(true);
    //
    // market.put("A", 0.0);
    // market.put("B", 0.0);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateToWinMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "FT:TW", "M", "To Win
    // X Set");
    // market.setIsValid(true);
    // market.put("A", 0.0);
    // market.put("B", 0.0);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateGameWinnerMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "G:ML", "P1.1", "Set 1
    // Game 1 Winner");
    // market.setIsValid(true);
    // market.put("A", 0.0);
    // market.put("B", 0.0);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    // private static Market generateGameCorrectScoreMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "P:CS", "P1.1", "Set 1
    // Game 1 Correct Score");
    // market.setIsValid(true);
    //
    //
    // market.put("6-0", 0.0);
    // market.put("0-6", 0.0);
    // market.put("6-1", 0.0);
    // market.put("1-6", 0.0);
    // market.put("6-3", 0.0);
    // market.put("3-6", 0.0);
    // market.put("6-4", 0.);
    // market.put("4-6", 0.0);
    // market.put("5-7", 0.0);
    // market.put("7-5", 0.0);
    // market.put("6-7", 0.0);
    // market.put("7-6", 0.0);
    // return market;
    // }
    //
    // private static Market generatePointWinnerMarket() {
    // Market market = new Market(MarketCategory.GENERAL, "G:PW", "P1.1.1", "Set
    // 1 Game 1 Point 1 Winner");
    // market.setIsValid(true);
    // market.put("A", 0.0);
    // market.put("B", 0.0);
    // market.setSelectionNameOverOrA("A");
    // market.setSelectionNameUnderOrB("B");
    // return market;
    // }
    //
    private static Market generateAToWinOneSetMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:W1S:A", "M", "Player A to win at least one set");
        market.setIsValid(true);
        market.put("A Yes", 0.0);
        market.put("A No", 0.0);
        return market;
    }

    private static Market generateBToWinOneSetMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:W1S:B", "M", "Player B to win at least one set");
        market.setIsValid(true);
        market.put("B Yes", 0.0);
        market.put("B No", 0.0);
        return market;
    }

    private static Market generateMostAcesMarket() {
        Market market = new Market(MarketCategory.GENERAL, "FT:AML", "M", "Player with Most Aces");
        market.setIsValid(true);
        market.put("Player A", 0.0);
        market.put("Player B", 0.0);
        return market;
    }

    private static Market generateNextAcesMarket(int x) {
        Market market = new Market(MarketCategory.GENERAL, "FT:AN", "A" + x, "Ace number " + x);
        market.setIsValid(true);
        market.put("Player A", 0.0);
        market.put("Player B", 0.0);
        return market;
    }

    private static Market generateTotalAcesMarket() {
        Market market = new Market(MarketCategory.OVUN, "FT:AOU", "M", "Total Aces");

        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    private static Market generateTotalPlayerAAcesMarket() {
        Market market = new Market(MarketCategory.OVUN, "FT:A:AOU", "M", "Total Player A Aces");

        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    private static Market generateTotalPlayerBAcesMarket() {
        Market market = new Market(MarketCategory.OVUN, "FT:B:AOU", "M", "Total Player B Aces");

        market.setLineId(lineId);
        market.setLineBase(-12);
        double[] probs = {0.0000052, 0.0000102, 0.0022444, 0.008442094, 0.021561398, 0.051869355, 0.097254343,
                0.161925181, 0.236490024, 0.29851382, 0.343560546, 0.377174168, 0.410988591, 0.446793999, 0.498762524,
                0.57748388, 0.684847312, 0.788772687, 0.874853539, 0.939621724, 0.973551607, 0.991669396, 0.997876368,
                0.999766309,};
        market.setLineProbs(probs);
        market.setIsValid(true);
        market.put("Over", 0.0);
        market.put("Under", 0.0);
        market.setSelectionNameOverOrA("Over " + lineId);
        market.setSelectionNameUnderOrB("Under " + lineId);
        return market;
    }

    public static void main(String[] args) {
        PpbPriceCalcGateway ppcg = new PpbPriceCalcGateway();
        String className = "TestClass";
        CalcRequestCause calcRequestCause = CalcRequestCause.NEW_MATCH;
        MatchFormat matchFormat = new TennisMatchFormat(false, Sex.MEN, Surface.HARD, TournamentLevel.ITF, 3,
                        FinalSetType.NORMAL_WITH_TIE_BREAK, false);
        MatchState matchState = new TennisMatchState(matchFormat);
        AlgoMatchParams matchParams = new TennisMatchParams();
        MatchIncident matchIncident = new TennisMatchIncident(0, TennisMatchIncidentType.MATCH_STARTING, TeamId.A);
        TennisMatchIncidentResult result =
                        new TennisMatchIncidentResult(true, true, 1, TennisMatchIncidentResultType.PREMATCH);
        MatchEngineSavedState savedState = new TennisMatchEngineSavedState();
        PriceCalcRequest request = new PriceCalcRequest(123L, EventSettings.generateEventSettingsForTesting(2),
                        className, calcRequestCause, matchFormat, matchState, matchParams.generateGenericMatchParams(),
                        matchIncident, result, savedState, System.currentTimeMillis());

        PriceCalcResponse response = ppcg.calculate(request);
        System.out.println(response);
    }

}
