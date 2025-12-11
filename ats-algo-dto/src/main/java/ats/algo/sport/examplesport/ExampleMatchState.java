package ats.algo.sport.examplesport;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.sport.examplesport.ExampleMatchIncident.ExampleMatchIncidentType;

public class ExampleMatchState extends MatchState {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int legsA;
    private int legsB;
    private final int legScoreForWin;
    private final int legScoreForDraw;
    private ExampleMatchIncidentResult currentMatchState; // the state following
                                                          // the most recent
                                                          // MatchIncident;

    public int getLegsA() {
        return legsA;
    }

    public void setLegsA(int legsA) {
        this.legsA = legsA;
    }

    public int getLegsB() {
        return legsB;
    }

    public void setLegsB(int legsB) {
        this.legsB = legsB;
    }

    protected ExampleMatchIncidentResult getCurrentMatchState() {
        return currentMatchState;
    }

    protected void setCurrentMatchState(ExampleMatchIncidentResult currentMatchState) {
        this.currentMatchState = currentMatchState;
    }

    public ExampleMatchState(ExampleMatchFormat matchFormat) {
        super();
        int n = matchFormat.getNoLegsInMatch();
        legScoreForWin = n / 2 + 1;
        if (2 * (n / 2) == n) // i.e. if n is even
            legScoreForDraw = n / 2;
        else
            legScoreForDraw = n; // set high enough so it doesn't ever get hit
        currentMatchState = ExampleMatchIncidentResult.PREMATCH;
    }

    /**
     * used by the copy method
     * 
     * @param legScoreforWin
     * @param legScoreforDraw
     */
    private ExampleMatchState(int legScoreforWin, int legScoreforDraw) {
        this.legScoreForWin = legScoreforWin;
        this.legScoreForDraw = legScoreforDraw;
        currentMatchState = ExampleMatchIncidentResult.PREMATCH;
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        ExampleMatchIncidentResult matchEventResult = null;
        switch (((ExampleMatchIncident) matchIncident).getIncidentSubType()) {
            case LEGWONBYA:
                matchEventResult = ExampleMatchIncidentResult.LEGWONBYA;
                legsA++;
                if (legsA == legScoreForWin)
                    matchEventResult = ExampleMatchIncidentResult.MATCHWONBYA;
                break;
            case LEGWONBYB:
                matchEventResult = ExampleMatchIncidentResult.LEGWONBYB;
                legsB++;
                if (legsB == legScoreForWin)
                    matchEventResult = ExampleMatchIncidentResult.MATCHWONBYB;
                break;
        }
        if ((legsA == legScoreForDraw) && (legsB == legScoreForDraw))
            matchEventResult = ExampleMatchIncidentResult.DRAW;
        currentMatchState = matchEventResult;
        return matchEventResult;
    }

    @Override
    public MatchState copy() {
        ExampleMatchState cc = new ExampleMatchState(legScoreForWin, legScoreForDraw);
        cc.setLegsA(this.getLegsA());
        cc.setLegsB(this.getLegsB());
        cc.setCurrentMatchState(this.getCurrentMatchState());
        return cc;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        this.setLegsA(((ExampleMatchState) matchState).getLegsA());
        this.setLegsB(((ExampleMatchState) matchState).getLegsB());
        this.setCurrentMatchState(((ExampleMatchState) matchState).getCurrentMatchState());
    }

    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = null;
        String lastLegWinner = "A"; // set default prompt to be whoever on the
                                    // last leg
        String matchWinner = "A";
        switch (currentMatchState) {
            case PREMATCH:
            case LEGWONBYB:
                lastLegWinner = "B";
            case LEGWONBYA:
                matchIncidentPrompt = new MatchIncidentPrompt("Enter winner of next leg (A/B)", lastLegWinner);
                break;
            case DRAW:
            case MATCHWONBYB:
                matchWinner = "B";
            case MATCHWONBYA:
                matchIncidentPrompt =
                                new MatchIncidentPrompt(String.format("Match is finished - won by %s", matchWinner));
        }
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        ExampleMatchIncidentType incidentType = null;
        switch (response.toUpperCase()) {
            case "A":
                incidentType = ExampleMatchIncidentType.LEGWONBYA;
                break;
            case "B":
                incidentType = ExampleMatchIncidentType.LEGWONBYB;
                break;
            default:
                return null; // invalid input so return null
        }
        ExampleMatchIncident incident = new ExampleMatchIncident();
        incident.setExampleMatchIncidentType(incidentType);
        return incident;
    }

    private static final String legScoreKey = "Leg score";

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(legScoreKey, String.format("%d-%d", legsA, legsB));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    @Override
    public boolean isMatchCompleted() {

        return (currentMatchState == ExampleMatchIncidentResult.DRAW
                        || currentMatchState == ExampleMatchIncidentResult.MATCHWONBYA
                        || currentMatchState == ExampleMatchIncidentResult.MATCHWONBYB);
    }

    @Override
    public MatchFormat getMatchFormat() {
        return null;
    }

    @Override
    public GamePeriod getGamePeriod() {
        return null;
    }

    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return false;
    }

    /**
     * calculates no of secs to go within current period. Returns zero if pre Match or at half time etc.
     * 
     * @return no of secs remaining in current period (including actual or estimated injury time).
     */
    @Override
    public int secsLeftInCurrentPeriod() {
        int secs = -1;
        return secs;

    }

    /**
     * the no of seconds since the last goal was scored
     * 
     * @return -1 if no goal scored or scored during a previous period, otherwise the no of secs
     */
    public int secsSinceLastGoal() {
        int secs = -1;
        return secs;
    }

    @Override
    public MatchState generateSimpleMatchState() {
        return null;
    }
}
