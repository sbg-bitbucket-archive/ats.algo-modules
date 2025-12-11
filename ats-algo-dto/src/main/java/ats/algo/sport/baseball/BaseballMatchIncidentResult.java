package ats.algo.sport.baseball;

import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.common.TeamId;

public class BaseballMatchIncidentResult implements MatchIncidentResult {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BaseballMatchPeriod baseballMatchPeriod;
    private TeamId teamId;

    public BaseballMatchIncidentResult(BaseballMatchPeriod baseballMatchPeriod, TeamId teamId) {
        super();
        this.baseballMatchPeriod = baseballMatchPeriod;
        this.teamId = teamId;
    }

    public void setBaseballMatchPeriod(BaseballMatchPeriod baseballMatchPeriod) {
        this.baseballMatchPeriod = baseballMatchPeriod;
    }

    public BaseballMatchPeriod getBaseballMatchPeriod() {
        return baseballMatchPeriod;
    }

    public void setTeamId(TeamId teamId) {
        this.teamId = teamId;
    }

    public TeamId getTeamId() {
        return teamId;
    }

    public BaseballMatchPeriod getNextBaseballMatchPeriod() {
        return BaseballMatchPeriod.values()[baseballMatchPeriod.getNextPeriod()];
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((baseballMatchPeriod == null) ? 0 : baseballMatchPeriod.hashCode());
        result = prime * result + ((teamId == null) ? 0 : teamId.hashCode());
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
        BaseballMatchIncidentResult other = (BaseballMatchIncidentResult) obj;
        if (baseballMatchPeriod != other.baseballMatchPeriod)
            return false;
        if (teamId != other.teamId)
            return false;
        return true;
    }

    public enum BaseballMatchPeriod {
        PREMATCH(0),
        FIRST_INNING_FIRST_HALF(1),
        FIRST_INNING_SECOND_HALF(2),
        SECOND_INNING_FIRST_HALF(3),
        SECOND_INNING_SECOND_HALF(4),
        THIRD_INNING_FIRST_HALF(5),
        THIRD_INNING_SECOND_HALF(6),
        FOURTH_INNING_FIRST_HALF(7),
        FOURTH_INNING_SECOND_HALF(8),
        FIFTH_INNING_FIRST_HALF(9),
        FIFTH_INNING_SECOND_HALF(10),
        SIXTH_INNING_FIRST_HALF(11),
        SIXTH_INNING_SECOND_HALF(12),
        SEVENTH_INNING_FIRST_HALF(13),
        SEVENTH_INNING_SECOND_HALF(14),
        EIGHTH_INNING_FIRST_HALF(15),
        EIGHTH_INNING_SECOND_HALF(16),
        NINTH_INNING_FIRST_HALF(17),
        NINTH_INNING_SECOND_HALF(18),
        MATCH_COMPLETED(19),
        DRAW(20),
        EXTRA_INNING_FIRST_HALF(21),
        EXTRA_INNING_SECOND_HALF(22);
        private BaseballMatchPeriod(int value) {
            this.period = value;
        }

        private final int period;

        public int getPeriod() {
            return period;
        }

        public int getNextPeriod() {
            return period + 1;
        }
    }

}
