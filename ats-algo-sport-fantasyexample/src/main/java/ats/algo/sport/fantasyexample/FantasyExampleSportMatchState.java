package ats.algo.sport.fantasyexample;

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
import ats.algo.sport.fantasyexample.FantasyExampleSportMatchIncident.FantasyExampleSportMatchIncidentType;

public class FantasyExampleSportMatchState extends AlgoMatchState {

    private static final long serialVersionUID = 1L;
    private int goals;
    private int yellowCards;
    private int ptsPlayer1;
    private int ptsPlayer2;
    private int ptsPlayer3;


    FantasyExampleSportMatchState() {}

    FantasyExampleSportMatchState(FantasyExampleSportMatchFormat format) {

        goals = 0;
        yellowCards = 0;
        ptsPlayer1 = 0;
        ptsPlayer2 = 0;
        ptsPlayer3 = 0;
    }


    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getYellowCards() {
        return yellowCards;
    }

    public void setYellowCards(int yellowCards) {
        this.yellowCards = yellowCards;
    }

    public int getPtsPlayer1() {
        return ptsPlayer1;
    }

    public void setPtsPlayer1(int ptsPlayer1) {
        this.ptsPlayer1 = ptsPlayer1;
    }

    public int getPtsPlayer2() {
        return ptsPlayer2;
    }

    public void setPtsPlayer2(int ptsPlayer2) {
        this.ptsPlayer2 = ptsPlayer2;
    }

    public int getPtsPlayer3() {
        return ptsPlayer3;
    }

    public void setPtsPlayer3(int ptsPlayer3) {
        this.ptsPlayer3 = ptsPlayer3;
    }

    @Override
    public AlgoMatchState copy() {
        FantasyExampleSportMatchState state = new FantasyExampleSportMatchState();

        state.goals = this.goals;
        state.yellowCards = this.yellowCards;
        state.ptsPlayer1 = this.ptsPlayer1;
        state.ptsPlayer2 = this.ptsPlayer2;
        state.ptsPlayer3 = this.ptsPlayer3;
        return state;
    }

    @Override
    public void setEqualTo(AlgoMatchState other) {

        this.goals = ((FantasyExampleSportMatchState) other).goals;
        this.yellowCards = ((FantasyExampleSportMatchState) other).yellowCards;
        this.ptsPlayer1 = ((FantasyExampleSportMatchState) other).ptsPlayer1;
        this.ptsPlayer2 = ((FantasyExampleSportMatchState) other).ptsPlayer2;
        this.ptsPlayer3 = ((FantasyExampleSportMatchState) other).ptsPlayer3;

    }

    @Override
    @JsonIgnore
    public MatchIncidentPrompt getNextPrompt() {
        if (!isMatchCompleted())
            return new MatchIncidentPrompt("Enter goal to n (Gn) or yellow card to n (Yn)", "");
        else
            return new MatchIncidentPrompt("Match over");
    }

    @Override
    @JsonIgnore
    public MatchIncident getMatchIncident(String response) {
        try {
            String d = response.substring(0, 1).toUpperCase();
            switch (d) {
                case "G":
                    int n = Integer.valueOf(response.substring(1, 2));
                    return FantasyExampleSportMatchIncident.generatePlayerIncident(123L,
                                    FantasyExampleSportMatchIncidentType.GOAL, n);
                case "Y":
                    int n2 = Integer.valueOf(response.substring(1, 2));
                    return FantasyExampleSportMatchIncident.generatePlayerIncident(123L,
                                    FantasyExampleSportMatchIncidentType.YELLOW_CARD, n2);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        return null;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        return null;
    }

    @Override
    public boolean isMatchCompleted() {
        return false;
    }

    @Override
    public MatchFormat getMatchFormat() {
        return null;
    }

    @Override
    public GamePeriod getGamePeriod() {
        if (preMatch())
            return GamePeriod.PREMATCH;
        if (isMatchCompleted())
            return GamePeriod.POSTMATCH;
        return GamePeriod.PERIOD1;
    }

    @Override
    public boolean preMatch() {
        return (goals == 0 && this.yellowCards == 0);
    }

    @Override
    public int secsLeftInCurrentPeriod() {
        return 0;
    }

    @Override
    public SimpleMatchState generateSimpleMatchState() {
        return new FantasyExampleSportSimpleMatchState(preMatch(), isMatchCompleted(), goals, yellowCards, ptsPlayer1,
                        ptsPlayer2, ptsPlayer3);
    }

    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        if (matchIncident.getClass() == FantasyExampleSportMatchIncident.class) {
            FantasyExampleSportMatchIncident incident = (FantasyExampleSportMatchIncident) matchIncident;
            switch ((FantasyExampleSportMatchIncidentType) incident.getIncidentSubType()) {

                case YELLOW_CARD:
                    this.yellowCards++;
                    switch (incident.getPlayerNo()) {
                        case 1:
                            ptsPlayer1 -= 1;
                            break;
                        case 2:
                            ptsPlayer2 -= 1;
                            break;
                        case 3:
                            ptsPlayer3 -= 1;
                            break;
                        default:
                            break;
                    }
                    break;
                case GOAL:
                    this.goals++;
                    switch (incident.getPlayerNo()) {
                        case 1:
                            ptsPlayer1 += 3;
                            break;
                        case 2:
                            ptsPlayer2 += 3;
                            break;
                        case 3:
                            ptsPlayer3 += 3;
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;

            }

        }
        return new FantasyExampleSportMatchIncidentResult();
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }

}
