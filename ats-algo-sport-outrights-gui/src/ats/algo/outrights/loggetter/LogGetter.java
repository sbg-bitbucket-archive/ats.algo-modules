package ats.algo.outrights.loggetter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ats.algo.genericsupportfunctions.JsonExtractor;
import ats.algo.outrights.loggetter.view.LogGetterDisplayController;
import ats.algo.outrights.view.AlertBox;
import ats.org.json.JSONException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SuppressWarnings("restriction")
public class LogGetter extends Application {
    /*
     * timeouts in secs
     */
    private static final int CONNECTION_TIMEOUT = 60;
    private static final int WRITE_TIMEOUT = 60;
    private static final int READ_TIMEOUT = 60;

    private static OkHttpClient client;

    private static final String URL_TEMPLATE =
                    "http://172.16.255.1:9200/filebeat-2017.10.03/_search?size=%s&pretty=true";

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


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

    private static List<String> getLogData(long nLines, String searchString) {
        Response httpResponse = null;
        String response = null;
        try {
            String json = String.format("{  \"query\": { \"match\": { \"message\": \"%s\"} }}", searchString);
            RequestBody body = RequestBody.create(JSON, json);
            String url = String.format(URL_TEMPLATE, Long.toString(nLines));
            Request httpRequest = new Request.Builder().url(url).post(body).build();
            info("POST the request to: " + url);
            info(json);

            Call call = httpClient().newCall(httpRequest);
            httpResponse = call.execute();
            if (httpResponse.isSuccessful()) {
                info("Sucessful Response: " + httpResponse);
            } else {
                info("Unsucessful response: " + httpResponse);
            }
            response = httpResponse.body().string();
            info("Http Response Body ready to deserialise");

        } catch (IOException e) {
            e.printStackTrace();
            AlertBox.displayError("Error connecting to server.  See console for details");
        } finally {
            if (httpResponse != null)
                httpResponse.body().close();
        }
        List<String> list = null;
        try {
            list = JsonExtractor.extractListProperty(response, "message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean failed = list == null;
        if (!failed)
            failed = list.size() == 0;

        if (failed) {
            info(response);
            AlertBox.displayError("No search results returned.  see console for details of error");
        }
        return list;
    }


    private static void info(String s) {
        System.out.println("INFO  " + s);
    }


    public void handleEventIdEntered(String eventId) {
        System.out.println("eventID entered");
        controller.updateLogRowDetail("");
        List<String> logRows = getLogData(100, eventId);
        controller.updateLogRows(logRows);
    }

    public void handleFreeTextEntered(String string) {
        System.out.println("free text entered");
        controller.updateLogRowDetail("");
        List<String> logRows = getLogData(100, string);
        controller.updateLogRows(logRows);
    }

    public void handleLogRowSelected(String row) {
        System.out.println("LogRow entered");
        controller.updateLogRowDetail(row);

    }

    Stage primaryStage;
    LogGetterDisplayController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Algo Log getter v1.0");
        showLogGetterDialog();
    }

    private void showLogGetterDialog() {
        Stage dialogStage = null;
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LogGetter.class.getResource("view/LogGetterDisplay.fxml"));
            BorderPane page = (BorderPane) loader.load();
            dialogStage = new Stage();
            dialogStage.setTitle("LogGetter");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.initialize(this);
            dialogStage.showAndWait();
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // LogGetter logGetter = new LogGetter();
        launch(args);
    }

}
