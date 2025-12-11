package ats.algo.sport.darts;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

public class DartMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private boolean playerAStartedAtOche;
    private DartMatchEventOutcome dartMatchEventOutcome;

    public DartMatchIncidentResult() {
        super();
    }

    enum DartMatchEventOutcome {
        BEGININPLAY,
        WITHINLEG,
        LEGWONA,
        LEGWONB,
        SETWONA,
        SETWONB,
        MATCHWONA,
        MATCHWONB,
        MATCHDRAWN
    }

    public void setResult(TeamId id, DartMatchEventOutcome dartMatchEventOutcome) {
        this.playerAStartedAtOche = id == TeamId.A;
        this.dartMatchEventOutcome = dartMatchEventOutcome;
    }

    public boolean isPlayerAStartedAtOche() {
        return playerAStartedAtOche;
    }

    public DartMatchEventOutcome getDartMatchEventOutcome() {
        return dartMatchEventOutcome;
    }



}
