package ats.algo.sport.outrights;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.matchresult.MatchResultMap;


/**
 * AlgoMatchState class for Handball
 * 
 * @author Jin
 * 
 */
public class OutrightsMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private OutrightsMatchPeriod matchPeriod;

    /**
     * Json class constructor. Not for general use
     */
    public OutrightsMatchState() {
        this(new OutrightsMatchFormat());
    }

    /**
     * Class constructor
     * 
     * @param matchFormat
     */
    public OutrightsMatchState(MatchFormat matchFormat) {
        super();

    }

    /**
     * Get the ball holding information class
     * 
     * @return ballPossession
     */

    /**
     * Get the current matchPeriod
     * 
     * @return matchPeriod
     */
    public OutrightsMatchPeriod getMatchPeriod() {
        return matchPeriod;
    }

    /**
     * Set the current match period
     * 
     * @param matchPeriod
     */
    public void setMatchPeriod(OutrightsMatchPeriod matchPeriod) {
        this.matchPeriod = matchPeriod;
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        return matchPeriod;
    }

    @Override
    public AlgoMatchState copy() {
        OutrightsMatchState cc = new OutrightsMatchState();
        cc.setEqualTo(this);
        return cc;

    }

    @Override
    public void setEqualTo(AlgoMatchState matchState) {
        OutrightsMatchState other = (OutrightsMatchState) matchState;
        this.matchPeriod = other.matchPeriod;
    }

    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt matchIncidentPrompt = new MatchIncidentPrompt("-", "-");
        return matchIncidentPrompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        MatchIncident incident = new OutrightsMatchIncident();
        return incident;
    }

    private String MATCH_PERIOD_KEY = "Match period";

    @Override
    public LinkedHashMap<String, String> getAsMap() {

        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(MATCH_PERIOD_KEY, matchPeriod.toString());
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * do nothing - not allowing the user to manually update the score
         */
        return null;
    }

    /**
     * Returns true if at the end of match
     * 
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isMatchCompleted() {
        return (matchPeriod == OutrightsMatchPeriod.COMPETITION_COMPLETED);
    }

    @Override
    /**
     * updates the elapsed time in between receipt of matchIncidents. Called roughly once a second by a timer
     * 
     * @return if more than 10 seconds have elapsed since last price calc then returns true
     */
    public boolean updateElapsedTime() {
        boolean updatePrices = false;
        return updatePrices;
    }

    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;
        switch (matchPeriod) {
            case PRE_COMPETITION:
                gamePeriod = GamePeriod.PREMATCH;
                break;
            case IN_COMPETITION:
                gamePeriod = GamePeriod.FIRST_HALF;
                break;
            case COMPETITION_COMPLETED:
                gamePeriod = GamePeriod.POSTMATCH;
                break;
            default:
                break;

        }
        return gamePeriod;
    }


    /**
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (matchPeriod == OutrightsMatchPeriod.PRE_COMPETITION);
    }



    @Override
    public SimpleMatchState generateSimpleMatchState() {
        OutrightsSimpleMatchState sms = new OutrightsSimpleMatchState();
        sms.setMatchPeriod(matchPeriod);
        return sms;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return null;
    }

    @Override
    public int secsLeftInCurrentPeriod() {
        return 0;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }

}
