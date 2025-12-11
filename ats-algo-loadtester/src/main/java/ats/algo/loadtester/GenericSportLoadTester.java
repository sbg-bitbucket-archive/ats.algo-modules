package ats.algo.loadtester;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerPublishable;
import ats.algo.algomanager.EventStateBlob;
import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.GenericMatchParams;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.core.traderalert.TraderAlert;
import ats.algo.sport.afl.AflMatchFormat;
import ats.algo.sport.americanfootball.AmericanfootballMatchFormat;
import ats.algo.sport.badminton.BadmintonMatchFormat;
import ats.algo.sport.baseball.BaseballMatchFormat;
import ats.algo.sport.basketball.BasketballMatchFormat;
import ats.algo.sport.beachvolleyball.BeachVolleyballMatchFormat;
import ats.algo.sport.bowls.BowlsMatchFormat;
import ats.algo.sport.cricket.CricketMatchFormat;
import ats.algo.sport.darts.DartMatchFormat;
import ats.algo.sport.fieldhockey.FieldhockeyMatchFormat;
import ats.algo.sport.floorball.FloorballMatchFormat;
import ats.algo.sport.football.FootballMatchFormat;
import ats.algo.sport.futsal.FutsalMatchFormat;
import ats.algo.sport.handball.HandballMatchFormat;
import ats.algo.sport.icehockey.IcehockeyMatchFormat;
import ats.algo.sport.rugbyleague.RugbyLeagueMatchFormat;
import ats.algo.sport.rugbyunion.RugbyUnionMatchFormat;
import ats.algo.sport.snooker.SnookerMatchFormat;
import ats.algo.sport.squash.SquashMatchFormat;
import ats.algo.sport.tabletennis.TabletennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennisG.TennisGMatchFormat;
import ats.algo.sport.testcricket.TestCricketMatchFormat;
import ats.algo.sport.ufc.UfcMatchFormat;
import ats.algo.sport.volleyball.VolleyballMatchFormat;
import ats.core.AtsBean;

public class GenericSportLoadTester extends AtsBean implements AlgoManagerPublishable {

    private AlgoManager algoManager;
    protected GenericMatchParams publishedMatchParams;
    protected SimpleMatchState publishedMatchState;
    protected Markets publishedMarkets;
    protected ResultedMarkets publishedResultedMarkets;
    protected ParamFindResults publishedParamFinderResults;
    protected MatchResultMap matchResultProforma;
    protected Boolean publishedNotifyEventCompleted;
    protected Set<String> keysForDiscontinuedMarkets;
    protected Recording recording;

    private volatile boolean finished;
    volatile long startTime;
    volatile long endTime;
    volatile int iterationNo;

    private Map<SupportedSportType, Class<? extends MatchFormat>> matchFormatMap;

    GenericSportLoadTester() {
        recording = new Recording();
        matchFormatMap = new HashMap<SupportedSportType, Class<? extends MatchFormat>>();
        matchFormatMap.put(SupportedSportType.TENNIS, TennisMatchFormat.class);
        matchFormatMap.put(SupportedSportType.DARTS, DartMatchFormat.class);
        matchFormatMap.put(SupportedSportType.ICE_HOCKEY, IcehockeyMatchFormat.class);
        matchFormatMap.put(SupportedSportType.SOCCER, FootballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.HANDBALL, HandballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.FLOORBALL, FloorballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.VOLLEYBALL, VolleyballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.SNOOKER, SnookerMatchFormat.class);
        matchFormatMap.put(SupportedSportType.FIELD_HOCKEY, FieldhockeyMatchFormat.class);
        matchFormatMap.put(SupportedSportType.TABLE_TENNIS, TabletennisMatchFormat.class);
        matchFormatMap.put(SupportedSportType.BADMINTON, BadmintonMatchFormat.class);
        matchFormatMap.put(SupportedSportType.BASKETBALL, BasketballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.BEACH_VOLLEYBALL, BeachVolleyballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.SQUASH, SquashMatchFormat.class);
        matchFormatMap.put(SupportedSportType.BOWLS, BowlsMatchFormat.class);
        matchFormatMap.put(SupportedSportType.CRICKET, CricketMatchFormat.class);
        matchFormatMap.put(SupportedSportType.RUGBY_UNION, RugbyUnionMatchFormat.class);
        matchFormatMap.put(SupportedSportType.RUGBY_LEAGUE, RugbyLeagueMatchFormat.class);
        matchFormatMap.put(SupportedSportType.AMERICAN_FOOTBALL, AmericanfootballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.TESTCRICKET, TestCricketMatchFormat.class);
        matchFormatMap.put(SupportedSportType.TEST_TENNISG, TennisGMatchFormat.class);
        matchFormatMap.put(SupportedSportType.BASEBALL, BaseballMatchFormat.class);
        matchFormatMap.put(SupportedSportType.FUTSAL, FutsalMatchFormat.class);
        matchFormatMap.put(SupportedSportType.UFC, UfcMatchFormat.class);
        matchFormatMap.put(SupportedSportType.AUSSIE_RULES, AflMatchFormat.class);
    }



    private static final int N_ITERATIONS = 10;



    AlgoManager getAlgoManager() {
        return algoManager;
    }

    void setAlgoManager(AlgoManager algoManager) {
        this.algoManager = algoManager;
    }

    public void executeLoadTest(SupportedSportType sportType) {

        // Class c = AussieRulesMatchEngine.class; //check to see that it is within scope
        finished = false;
        iterationNo = 0;
        Class<? extends MatchFormat> matchFormatClass = matchFormatMap.get(sportType);
        MatchFormat matchFormat = null;
        try {
            matchFormat = matchFormatClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                        | IllegalArgumentException | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        algoManager.handleNewEventCreation(sportType, 1001L, matchFormat);
        startTime = System.currentTimeMillis();
        while (!finished) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double timeTaken = ((double) (endTime - startTime)) / 1000.0;
        double avgTime = timeTaken / N_ITERATIONS;
        System.out.printf("TEST FINISHED. %d iterations in %.2f secs.  Avg time per price calc: %.3f.\n", N_ITERATIONS,
                        timeTaken, avgTime);
    }

    private void handleNextAction() {
        iterationNo++;
        if (iterationNo == N_ITERATIONS) {
            endTime = System.currentTimeMillis();
            finished = true;
        } else {
            algoManager.handleSetMatchParams(publishedMatchParams);
            System.out.println("Iteration no: " + iterationNo);
        }
    }



    @Override
    public void publishMatchParams(long eventId, GenericMatchParams matchParams) {
        this.publishedMatchParams = matchParams;
        handleNextAction();
    }

    @Override
    public void publishMatchState(long eventId, SimpleMatchState matchState) {
        this.publishedMatchState = matchState;
    }

    @Override
    public void publishMarkets(long eventId, Markets markets, Set<String> keysForDiscontinuedMarkets,
                    TimeStamp timeStamp, String sequenceId) {
        this.publishedMarkets = markets;
        this.keysForDiscontinuedMarkets = keysForDiscontinuedMarkets;

    }

    @Override
    public void publishResultedMarkets(long eventId, ResultedMarkets resultedMarkets) {
        this.publishedResultedMarkets = resultedMarkets;
    }

    @Override
    public void publishParamFindResults(long eventId, ParamFindResults paramFindResults, GenericMatchParams matchParams,
                    long elapsedtimeMs) {
        this.publishedParamFinderResults = paramFindResults;
    }

    @Override
    public void notifyEventCompleted(long eventId, boolean isCompleted) {
        this.publishedNotifyEventCompleted = isCompleted;
    }


    @Override
    public void publishRecordedItem(long eventId, RecordedItem recordedItem) {
        recording.add(recordedItem);
    }

    @Override
    public void notifyFatalError(long eventId, String requestId, String errorCause) {}

    @Override
    public void publishTraderAlert(long eventId, TraderAlert traderAlert) {}

    @Override
    public void publishEventState(long eventId, EventStateBlob eventStateBlob) {

    }

    @Override
    public void publishEventSuspensionStatus(long eventId, boolean suspend, Set<MarketGroup> marketGroups) {

    }

    @Override
    public void publishEventProperties(long eventId, Map<String, String> properties) {

    }



}
