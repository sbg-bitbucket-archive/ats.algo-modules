package ats.algo.inplayGS;

import java.io.Serializable;
import java.util.Map;

public class AllInputOutputData implements Serializable {

    private static final long serialVersionUID = 1L;
    private Map<String, PlayerStatus> preMatchTeamSheet;
    private Map<String, Selection> preMatchFirstToScore;
    Map<String, Selection> preMatchCorrectScore;
    int matchDurationSecs;
    int matchClockSecs;
    Map<String, PlayerStatus> currentInPlayTeamSheet;
    Map<String, Selection> currentInPlayCorrectScore;
    String currentInPlayScore;
    private double nextGoalScorerMarginPerSelection;
    private double anytimeGoalScorerMarginPerSelection;
    private boolean reApplyInputMargin;

    private Map<String, Selection> inPlayNextGoalScorers;
    private Map<String, Selection> inPlayAnytimeGoalScorers;
    private OddsLadder oddsLadder;

    public Map<String, PlayerStatus> getPreMatchTeamSheet() {
        return preMatchTeamSheet;
    }

    public void setPreMatchTeamSheet(Map<String, PlayerStatus> preMatchTeamSheet) {
        this.preMatchTeamSheet = preMatchTeamSheet;
    }

    public Map<String, Selection> getPreMatchFirstToScore() {
        return preMatchFirstToScore;
    }

    public void setPreMatchFirstToScore(Map<String, Selection> preMatchFirstToScore) {
        this.preMatchFirstToScore = preMatchFirstToScore;
    }

    public Map<String, Selection> getPreMatchCorrectScore() {
        return preMatchCorrectScore;
    }

    public void setPreMatchCorrectScore(Map<String, Selection> preMatchCorrectScore) {
        this.preMatchCorrectScore = preMatchCorrectScore;
    }

    public int getMatchDurationSecs() {
        return matchDurationSecs;
    }

    public void setMatchDurationSecs(int matchDurationSecs) {
        this.matchDurationSecs = matchDurationSecs;
    }

    public int getMatchClockSecs() {
        return matchClockSecs;
    }

    public void setMatchClockSecs(int matchClockSecs) {
        this.matchClockSecs = matchClockSecs;
    }

    public Map<String, PlayerStatus> getCurrentInPlayTeamSheet() {
        return currentInPlayTeamSheet;
    }

    public void setCurrentInPlayTeamSheet(Map<String, PlayerStatus> currentInPlayTeamSheet) {
        this.currentInPlayTeamSheet = currentInPlayTeamSheet;
    }

    public Map<String, Selection> getCurrentInPlayCorrectScore() {
        return currentInPlayCorrectScore;
    }

    public void setCurrentInPlayCorrectScore(Map<String, Selection> currentInPlayCorrectScore) {
        this.currentInPlayCorrectScore = currentInPlayCorrectScore;
    }

    public String getCurrentInPlayScore() {
        return currentInPlayScore;
    }

    public void setCurrentInPlayScore(String currentInPlayScore) {
        this.currentInPlayScore = currentInPlayScore;
    }

    public double getNextGoalScorerMarginPerSelection() {
        return nextGoalScorerMarginPerSelection;
    }

    public void setNextGoalScorerMarginPerSelection(double nextGoalScorerMarginPerSelection) {
        this.nextGoalScorerMarginPerSelection = nextGoalScorerMarginPerSelection;
    }

    public double getAnytimeGoalScorerMarginPerSelection() {
        return anytimeGoalScorerMarginPerSelection;
    }

    public void setAnytimeGoalScorerMarginPerSelection(double anytimeGoalScorerMarginPerSelection) {
        this.anytimeGoalScorerMarginPerSelection = anytimeGoalScorerMarginPerSelection;
    }

    public boolean isReApplyInputMargin() {
        return reApplyInputMargin;
    }

    public void setReApplyInputMargin(boolean reApplyInputMargin) {
        this.reApplyInputMargin = reApplyInputMargin;
    }

    public Map<String, Selection> getInPlayNextGoalScorers() {
        return inPlayNextGoalScorers;
    }

    public void setInPlayNextGoalScorers(Map<String, Selection> inPlayNextGoalScorers) {
        this.inPlayNextGoalScorers = inPlayNextGoalScorers;
    }

    public Map<String, Selection> getInPlayAnytimeGoalScorers() {
        return inPlayAnytimeGoalScorers;
    }

    public void setInPlayAnytimeGoalScorers(Map<String, Selection> inPlayAnytimeGoalScorers) {
        this.inPlayAnytimeGoalScorers = inPlayAnytimeGoalScorers;
    }

    public OddsLadder getOddsLadder() {
        return oddsLadder;
    }

    public void setOddsLadder(OddsLadder oddsLadder) {
        this.oddsLadder = oddsLadder;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(anytimeGoalScorerMarginPerSelection);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((currentInPlayCorrectScore == null) ? 0 : currentInPlayCorrectScore.hashCode());
        result = prime * result + ((currentInPlayScore == null) ? 0 : currentInPlayScore.hashCode());
        result = prime * result + ((currentInPlayTeamSheet == null) ? 0 : currentInPlayTeamSheet.hashCode());
        result = prime * result + ((inPlayAnytimeGoalScorers == null) ? 0 : inPlayAnytimeGoalScorers.hashCode());
        result = prime * result + ((inPlayNextGoalScorers == null) ? 0 : inPlayNextGoalScorers.hashCode());
        result = prime * result + matchClockSecs;
        result = prime * result + matchDurationSecs;
        temp = Double.doubleToLongBits(nextGoalScorerMarginPerSelection);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((oddsLadder == null) ? 0 : oddsLadder.hashCode());
        result = prime * result + ((preMatchCorrectScore == null) ? 0 : preMatchCorrectScore.hashCode());
        result = prime * result + ((preMatchFirstToScore == null) ? 0 : preMatchFirstToScore.hashCode());
        result = prime * result + ((preMatchTeamSheet == null) ? 0 : preMatchTeamSheet.hashCode());
        result = prime * result + (reApplyInputMargin ? 1231 : 1237);
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
        AllInputOutputData other = (AllInputOutputData) obj;
        if (Double.doubleToLongBits(anytimeGoalScorerMarginPerSelection) != Double
                        .doubleToLongBits(other.anytimeGoalScorerMarginPerSelection))
            return false;
        if (currentInPlayCorrectScore == null) {
            if (other.currentInPlayCorrectScore != null)
                return false;
        } else if (!currentInPlayCorrectScore.equals(other.currentInPlayCorrectScore))
            return false;
        if (currentInPlayScore == null) {
            if (other.currentInPlayScore != null)
                return false;
        } else if (!currentInPlayScore.equals(other.currentInPlayScore))
            return false;
        if (currentInPlayTeamSheet == null) {
            if (other.currentInPlayTeamSheet != null)
                return false;
        } else if (!currentInPlayTeamSheet.equals(other.currentInPlayTeamSheet))
            return false;
        if (inPlayAnytimeGoalScorers == null) {
            if (other.inPlayAnytimeGoalScorers != null)
                return false;
        } else if (!inPlayAnytimeGoalScorers.equals(other.inPlayAnytimeGoalScorers))
            return false;
        if (inPlayNextGoalScorers == null) {
            if (other.inPlayNextGoalScorers != null)
                return false;
        } else if (!inPlayNextGoalScorers.equals(other.inPlayNextGoalScorers))
            return false;
        if (matchClockSecs != other.matchClockSecs)
            return false;
        if (matchDurationSecs != other.matchDurationSecs)
            return false;
        if (Double.doubleToLongBits(nextGoalScorerMarginPerSelection) != Double
                        .doubleToLongBits(other.nextGoalScorerMarginPerSelection))
            return false;
        if (oddsLadder == null) {
            if (other.oddsLadder != null)
                return false;
        } else if (!oddsLadder.equals(other.oddsLadder))
            return false;
        if (preMatchCorrectScore == null) {
            if (other.preMatchCorrectScore != null)
                return false;
        } else if (!preMatchCorrectScore.equals(other.preMatchCorrectScore))
            return false;
        if (preMatchFirstToScore == null) {
            if (other.preMatchFirstToScore != null)
                return false;
        } else if (!preMatchFirstToScore.equals(other.preMatchFirstToScore))
            return false;
        if (preMatchTeamSheet == null) {
            if (other.preMatchTeamSheet != null)
                return false;
        } else if (!preMatchTeamSheet.equals(other.preMatchTeamSheet))
            return false;
        if (reApplyInputMargin != other.reApplyInputMargin)
            return false;
        return true;
    }



}
