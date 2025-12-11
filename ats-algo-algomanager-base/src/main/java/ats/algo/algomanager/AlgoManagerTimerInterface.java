package ats.algo.algomanager;

public interface AlgoManagerTimerInterface {

    void handleTimerInitiatedCalc(EventDetails eventDetails, long now);

    void handleDelayedPublishResultedMarkets(EventDetails eventDetails, long now);

    int getTimerFrequencyMs();

    void runTimerRelatedTradingRules(EventDetails eventDetails, long now);


}
