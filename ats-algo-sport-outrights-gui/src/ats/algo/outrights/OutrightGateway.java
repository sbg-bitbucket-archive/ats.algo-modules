package ats.algo.outrights;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ats.algo.sport.outrights.calcengine.core.FcastStandings;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.algo.sport.outrights.server.api.Alerts;
import ats.algo.sport.outrights.server.api.CompetitionsList;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.MarketsList;
import ats.algo.sport.outrights.server.api.StandingsList;
import ats.algo.sport.outrights.server.api.TargetPointsEntry;
import ats.algo.sport.outrights.server.api.TargetPointsList;
import ats.core.AtsBean;
import ats.core.util.json.JsonUtil;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.OkHttpClient.Builder;

public class OutrightGateway extends AtsBean {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /*
     * timeouts in secs
     */
    private static final int CONNECTION_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;
    private static final String URL_PROPERTY = "algo.outright.server.url";
    // server http://enamwdev2algo06:8080/
    private static final String DEFAULT_SERVER_URL_PREMATCH = "http://localhost:8098/";
    // private static final String DEFAULT_SERVER_URL_PREMATCH = "http://enamwdev2algo06:8080/";
    private static String urlOutright;
    private static OkHttpClient client;

    public OutrightGateway() {
        urlOutright = System.getProperty(URL_PROPERTY);
        if (urlOutright == null)
            urlOutright = DEFAULT_SERVER_URL_PREMATCH;
        info("OurrightGateway will use url for : %s ", urlOutright);
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

    public String getHttpResponse(String mapping, Map<String, String> params) {
        String response = "";
        Response httpResponse = null;
        Request httpRequest;

        String newUrlOutright = urlOutright + mapping;

        HttpUrl.Builder httpBuider = HttpUrl.parse(newUrlOutright).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        httpRequest = new Request.Builder().url(httpBuider.build()).build();
        info("connecting to outright server : " + httpBuider.build().toString());
        Call call = httpClient().newCall(httpRequest);
        try {
            httpResponse = call.execute();
            response = httpResponse.body().string();
            info("Response from outright server : " + response);
            return response;
        } catch (Exception e) {
            error("Problem in " + e);
        } finally {
            if (httpResponse != null)
                httpResponse.body().close();
        }
        return response;
    }

    public String postHttpResponse(String request, String mapping, Map<String, String> params) {
        String response = "";
        Response httpResponse = null;
        Request httpRequest;
        RequestBody body = RequestBody.create(JSON, request);

        String newUrlOutright = urlOutright + mapping;
        HttpUrl.Builder httpBuider = HttpUrl.parse(newUrlOutright).newBuilder();
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                httpBuider.addQueryParameter(param.getKey(), param.getValue());
            }
        }
        info("Post update :" + request);
        info("to Outrights Server Url : " + httpBuider.build().toString());
        httpRequest = new Request.Builder().url(httpBuider.build()).post(body).build();
        Call call = httpClient().newCall(httpRequest);
        try {
            httpResponse = call.execute();
            response = httpResponse.body().string();
            info("Response : " + response);
            return response;
        } catch (Exception e) {
            error("Problem in Response " + e);
        } finally {
            if (httpResponse != null)
                httpResponse.body().close();
        }
        return response;
    }

    public String getUrl() {
        return urlOutright;
    }

    public static void main(String[] args) throws IOException {
        String eventId = String.valueOf(999111L);
        Map<String, String> params = new HashMap<>();
        params.put("eventID", eventId);
        OutrightGateway outrightGateway = new OutrightGateway();

        System.out.println("OurrightGateway will use url for :  " + urlOutright);
        String httpResponse = outrightGateway.getHttpResponse("outrights", params);
        System.out.println(httpResponse);

        httpResponse = outrightGateway.getHttpResponse("alerts", null);
        System.out.println(httpResponse);
        Alerts alerts = JsonUtil.unmarshalJson(httpResponse, Alerts.class);
        System.out.println("Alerts" + alerts);

        httpResponse = outrightGateway.getHttpResponse("outrights", params);
        System.out.println(httpResponse);
        CompetitionsList comp = JsonUtil.unmarshalJson(httpResponse, CompetitionsList.class);
        System.out.println("outrights : " + JsonUtil.marshalJson(comp));
        httpResponse = outrightGateway.getHttpResponse("standings", params);
        System.out.println(httpResponse);
        StandingsList standing = JsonUtil.unmarshalJson(httpResponse, StandingsList.class);
        System.out.println("standings : " + standing);
        httpResponse = outrightGateway.getHttpResponse("teams", params);
        System.out.println("teams : " + httpResponse);
        Teams teams = JsonUtil.unmarshalJson(httpResponse, Teams.class);
        System.out.println("teams : " + JsonUtil.marshalJson(teams));

        httpResponse = outrightGateway.getHttpResponse("fixtures", params);
        System.out.println("fixtures : " + httpResponse);
        FixturesList fixturesList = JsonUtil.unmarshalJson(httpResponse, FixturesList.class);
        System.out.println("fixtures : " + fixturesList);
        httpResponse = outrightGateway.getHttpResponse("markets", params);
        System.out.println("markets : " + httpResponse);
        MarketsList marketsList = JsonUtil.unmarshalJson(httpResponse, MarketsList.class);
        System.out.println("marketsList : " + marketsList);
        httpResponse = outrightGateway.getHttpResponse("outrights", null);
        System.out.println("url : " + httpResponse);

        httpResponse = outrightGateway.getHttpResponse("fcaststandings", params);
        System.out.println(httpResponse);
        FcastStandings fcaststandings = JsonUtil.unmarshalJson(httpResponse, FcastStandings.class);
        System.out.println("fcaststandings : " + fcaststandings);


        // fcaststandings

        TargetPointsEntry entry = new TargetPointsEntry();
        List<TargetPointsEntry> entries = new ArrayList<>();
        entry.setTeamID("T1");
        entry.setTargetPoints(32.5);
        entries.add(entry);
        TargetPointsList tPointsList = new TargetPointsList();
        tPointsList.setEntries(entries);
        tPointsList.setEventID(456789l);
        String requestBody = JsonUtil.marshalJson(tPointsList);
        System.out.println(requestBody);
        String response = outrightGateway.postHttpResponse(requestBody, "updatetargetpoints", null);
        System.out.println(response);

        response = outrightGateway.postHttpResponse(requestBody, "paramfind", null);
        System.out.println(response);
        // TeamObject teamObject = new TeamObject();
        // teamObject.setEventId(456789L);
        // teamObject.setCompetitionName("English Premier league 17/18");
        // Team team = new Team("T4", "Burnley", "Burnley", 2.0, 0.5);
        // teamObject.setTeam(team);
        // String response = outrightGateway.postHttpResponse(teamObject.toString(), "team", null);
        // System.out.println("response : " + response);
    }
}
