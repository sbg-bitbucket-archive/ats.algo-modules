package ats.algo.sport.outrights.calcengine.core;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchState;

/**
 * handles those methods of the MatchState class that are not needed for competitions (which do not use MatchState
 * within MatchRunner)
 * 
 * @author gicha
 *
 */
public abstract class AbstractState extends MatchState {

    private static final long serialVersionUID = 1L;

    protected Competition competition; // n.b. competition and all the objects it points to should be left unchanged
                                       // when simulating a match

    public AbstractState(Competition competition) {
        super();
        this.competition = competition;
    }

    @Override
    public void setEqualTo(MatchState matchState) {
        super.setEqualTo(matchState);
    }

    /**
     * get the next match to be played
     * 
     * @return
     */
    public abstract Fixture nextFixture();

    /**
     * This method needs to be coded as efficiently as possible since gets called hundreds of thousands of times in
     * every simulation run. Avoid creating any new java classes in this method.
     * 
     * @param result
     */
    public abstract void updateStateForFixtureResult(Fixture fixture, FixtureResult result);

    /**
     * 
     * @return must not return null - return empty object with ok set to true of ok
     */
    public abstract CompetitionWarnings checkStateForErrors();

    /**
     * returns true if the competition has finished
     * 
     * @return
     */
    public abstract boolean competitionCompleted();

    @Override
    public MatchIncidentPrompt getNextPrompt() {
        return null;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        return null;
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
        return null;
    }

    @Override
    public boolean preMatch() {
        return false;
    }

    @Override
    public int secsLeftInCurrentPeriod() {
        return 0;
    }

    @Override
    public abstract MatchState copy();

    @Override
    public MatchState generateSimpleMatchState() {
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((competition == null) ? 0 : competition.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractState other = (AbstractState) obj;
        if (competition == null) {
            if (other.competition != null)
                return false;
        } else if (!competition.equals(other.competition))
            return false;
        return true;
    }

}
