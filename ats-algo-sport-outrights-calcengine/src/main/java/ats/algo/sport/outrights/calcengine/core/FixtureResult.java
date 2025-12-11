package ats.algo.sport.outrights.calcengine.core;

import ats.algo.core.common.TeamId;
import ats.core.util.json.JsonUtil;

public class FixtureResult {
    private int scoreA; // scores at end of full time
    private int scoreB;
    private TeamId winningTeamID; // may be set to a team even if scores are drawn if match is decided on overtime
                                  // or penalties

    public FixtureResult() {}

    public FixtureResult(int scoreA, int scoreB, TeamId winningTeamID) {
        super();
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.winningTeamID = winningTeamID;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public TeamId getWinningTeamID() {
        return winningTeamID;
    }

    public void setWinningTeamID(TeamId winningTeamID) {
        this.winningTeamID = winningTeamID;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + scoreA;
        result = prime * result + scoreB;
        result = prime * result + ((winningTeamID == null) ? 0 : winningTeamID.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FixtureResult other = (FixtureResult) obj;
        if (scoreA != other.scoreA)
            return false;
        if (scoreB != other.scoreB)
            return false;
        if (winningTeamID != other.winningTeamID)
            return false;
        return true;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
