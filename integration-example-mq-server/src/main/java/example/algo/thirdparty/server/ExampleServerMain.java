package example.algo.thirdparty.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.jms.JMSException;

import ats.algo.core.common.SupportedSportType;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.ConsoleInput;
import ats.algo.genericsupportfunctions.JsonSerializer;

public class ExampleServerMain {

    public static void main(String[] args) {
        // LogUtil.initConsoleLogging(Level.INFO);
        System.out.println("Example server - sends canned responses to received requests");
        int option = ConsoleInput.readInt(
                        "Enter option: canned response football(1); canned response tennis(2); read football price calc response from file(3)",
                        1, false);
        PriceCalcResponse priceCalcResponse = null;
        ParamFindResponse paramFindResponse = null;
        SupportedSportType supportedSportType = null;
        switch (option) {
            case 1:
            default:
                supportedSportType = SupportedSportType.SOCCER;
                priceCalcResponse = CannedResponses.getCannedFootballPriceCalcResponse();
                paramFindResponse = CannedResponses.getCannedFootballParamFindResponse();
                break;
            case 2:
                supportedSportType = SupportedSportType.TENNIS;
                priceCalcResponse = CannedResponses.getCannedTennisPriceCalcResponse();
                paramFindResponse = CannedResponses.getCannedTennisParamFindResponse();
                break;
            case 3:
                supportedSportType = SupportedSportType.SOCCER;
                priceCalcResponse = getResponseFromFile();
                paramFindResponse = CannedResponses.getCannedFootballParamFindResponse();
                break;
        }
        String brokerUser = "admin";
        String brokerPassword = "admin";
        String brokerUrl = "tcp://localhost:61616";
        try {
            new ExampleServer(supportedSportType, brokerUser, brokerPassword, brokerUrl, priceCalcResponse,
                            paramFindResponse).start();
        } catch (JMSException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static PriceCalcResponse getResponseFromFile() {
        String json = null;
        do {
            String fileName = ConsoleInput.readString("Enter fileName containing PriceCalcResponse:",
                            "C:\\AAtmp\\PriceCalcResponse.json");
            try {
                json = new String(Files.readAllBytes(Paths.get(fileName)));
            } catch (IOException e) {
                System.out.println("File read error: " + e.getMessage());
            }
        } while (json == null);
        System.out.println("json successfully read from file");
        PriceCalcResponse response = null;
        try {
            response = JsonSerializer.deserialize(json, PriceCalcResponse.class);
        } catch (Exception e) {
            System.out.println("error deserializing json: " + e.getMessage());
            System.out.println("Can't continue");
            System.exit(1);
        }
        System.out.println("json successfully deserialized");
        System.out.println("Deserialized PriceCalcResponse:" + response);
        return response;
    }
}
