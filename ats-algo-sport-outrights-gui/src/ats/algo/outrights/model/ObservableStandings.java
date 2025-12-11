package ats.algo.outrights.model;

import org.boon.primitive.Int;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.server.api.StandingsList;
import ats.algo.sport.outrights.server.api.StandingsListEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the Markets class to convert the data to/from the data format needed for a tree list view so it can be
 * displayed in the GUI
 * 
 * @author Rob
 * 
 */
public class ObservableStandings {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableStanding> observableStandings;

    public ObservableStandings() {
        observableStandings = FXCollections.observableArrayList();
    }

    public void update(StandingsList standings) {

        observableStandings.clear();
        for (StandingsListEntry entry : standings.getStandingsList()) {
            Standing standing = entry.getStanding();
            ObservableStanding observableStanding = new ObservableStanding(standing,
                            entry.getHighestPossibleFinishPosn(), entry.getLowestPossibleFinishPosn(),
                            entry.getManualPointsAdj(), entry.getManualtieBreakAdj());
            observableStandings.add(observableStanding);
        }
    }

    public void update(StandingsList standings, String marketFilter) {
        observableStandings.clear();
    }

    public ObservableList<ObservableStanding> getData() {
        return observableStandings;
    }

    public class ObservableStanding {
        /*
         * Stuff to display
         */
        private StringProperty teamID;
        private StringProperty played;
        private StringProperty won;
        private StringProperty drawn;
        private StringProperty lost;
        private StringProperty goalsFor;
        private StringProperty goalsAgainst;
        private StringProperty goalDiff;
        private StringProperty points;
        private StringProperty highestPossibleFinishPosn;
        private StringProperty lowestPossibleFinishPosn;
        private StringProperty manualPointsAdj;
        private StringProperty manualtieBreakAdj;
        @JsonIgnore
        private StringProperty standingUpdate;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableStanding(Standing standing, int highestPossibleFinishPosn, int lowestPossibleFinishPosn,
                        int manualPointsAdj, int manualtieBreakAdj) {
            this.teamID = new SimpleStringProperty(standing.getTeamId());
            this.played = new SimpleStringProperty(String.valueOf(standing.getPlayed()));
            this.won = new SimpleStringProperty(String.valueOf(standing.getWon()));
            this.drawn = new SimpleStringProperty(String.valueOf(standing.getDrawn()));
            this.lost = new SimpleStringProperty(String.valueOf(standing.getLost()));
            this.goalsFor = new SimpleStringProperty(String.valueOf(standing.getGoalsFor()));
            this.goalsAgainst = new SimpleStringProperty(String.valueOf(standing.getGoalsAgainst()));
            this.goalDiff = new SimpleStringProperty(String.valueOf(standing.getGoalsDiff()));
            this.points = new SimpleStringProperty(String.valueOf(standing.getPoints()));
            this.highestPossibleFinishPosn = new SimpleStringProperty(String.valueOf(highestPossibleFinishPosn));
            this.lowestPossibleFinishPosn = new SimpleStringProperty(String.valueOf(lowestPossibleFinishPosn));
            this.manualPointsAdj = new SimpleStringProperty(String.valueOf(manualPointsAdj));
            this.manualtieBreakAdj = new SimpleStringProperty(String.valueOf(manualtieBreakAdj));
            this.standingUpdate = new SimpleStringProperty("No");
        }

        /**
         * Constructor for rows other than the first row of a market
         * 
         * @param key
         * @param sequenceId
         * @param selectionName
         * @param nSelections
         * @param prob
         */
        public ObservableStanding(String teamID, int played, int won, int drawn, Int lost, int goalsFor,
                        int goalsAgainst, int goalDiff, int points) {
            this.teamID = new SimpleStringProperty(teamID);
            this.played = new SimpleStringProperty(String.valueOf(played));
            this.won = new SimpleStringProperty(String.valueOf(won));
            this.drawn = new SimpleStringProperty(String.valueOf(drawn));
            this.lost = new SimpleStringProperty(String.valueOf(lost));
            this.goalsFor = new SimpleStringProperty(String.valueOf(goalsFor));
            this.goalsAgainst = new SimpleStringProperty(String.valueOf(goalsAgainst));
            this.goalDiff = new SimpleStringProperty(String.valueOf(goalDiff));
            this.points = new SimpleStringProperty(String.valueOf(points));
            this.standingUpdate = new SimpleStringProperty("No");
        }

        public StringProperty playedProperty() {
            return played;
        }

        public StringProperty teamIDProperty() {
            return teamID;
        }

        public StringProperty wonProperty() {
            return won;
        }

        public StringProperty drawnProperty() {
            return drawn;
        }

        public StringProperty lostProperty() {
            return lost;
        }

        public StringProperty goalsForProperty() {
            return goalsFor;
        }

        public StringProperty goalsAgainstProperty() {
            return goalsAgainst;
        }

        public StringProperty goalDiffProperty() {
            return goalDiff;
        }

        public StringProperty manualtieBreakAdjProperty() {
            return manualtieBreakAdj;
        }

        public StringProperty highestPossibleFinishPosnProperty() {
            return highestPossibleFinishPosn;
        }

        public StringProperty lowestPossibleFinishPosnProperty() {
            return lowestPossibleFinishPosn;
        }

        public StringProperty manualPointsAdjProperty() {
            return manualPointsAdj;
        }

        public StringProperty pointsProperty() {
            return points;
        }


        public StringProperty standingUpdateProperty() {
            return standingUpdate;
        }

        public String getTeamID() {
            return teamID.getValue();
        }

        public int getPlayed() {
            return Integer.valueOf(played.getValue());
        }

        public int getWon() {
            return Integer.valueOf(won.getValue());
        }

        public int getDrawn() {
            return Integer.valueOf(drawn.getValue());
        }

        public int getLost() {
            return Integer.valueOf(lost.getValue());
        }

        public int getGoalsFor() {
            return Integer.valueOf(goalsFor.getValue());
        }

        public int getGoalsAgainst() {
            return Integer.valueOf(goalsAgainst.getValue());
        }

        public int getGoalDiff() {
            return Integer.valueOf(goalDiff.getValue());
        }

        public int getHighestPossibleFinishPosn() {
            return Integer.valueOf(highestPossibleFinishPosn.getValue());
        }

        public int getLowestPossibleFinishPosn() {
            return Integer.valueOf(lowestPossibleFinishPosn.getValue());
        }


        public int getManualPointsAdj() {
            return Integer.valueOf(manualPointsAdj.getValue());
        }

        public int getManualtieBreakAdj() {
            return Integer.valueOf(manualtieBreakAdj.getValue());
        }

        public int getPoints() {
            return Integer.valueOf(points.getValue());
        }


        public String getStangdingUpdate() {
            return standingUpdate.getValue();
        }

        public void setPlayed(String played) {
            this.played = new SimpleStringProperty(played);
        }

        public void setWon(String won) {
            this.won = new SimpleStringProperty(won);
        }

        public void setDrawn(String drawn) {
            this.drawn = new SimpleStringProperty(drawn);
        }

        public void setLost(String lost) {
            this.lost = new SimpleStringProperty(lost);
        }


        public void setGoalsFor(String goalsFor) {
            this.goalsFor = new SimpleStringProperty(goalsFor);
        }

        public void setGoalDiff(String goalDiff) {
            this.goalDiff = new SimpleStringProperty(goalDiff);
        }

        public void setGoalsAgainst(String goalsAgainst) {
            this.goalsAgainst = new SimpleStringProperty(goalsAgainst);
        }

        public void setHighestPossibleFinishPosn(String highestPossibleFinishPosn) {
            this.highestPossibleFinishPosn = new SimpleStringProperty(highestPossibleFinishPosn);;
        }

        public void setLowestPossibleFinishPosn(String lowestPossibleFinishPosn) {
            this.lowestPossibleFinishPosn = new SimpleStringProperty(lowestPossibleFinishPosn);;
        }

        public void setManualPointsAdj(String manualPointsAdj) {
            this.manualPointsAdj = new SimpleStringProperty(manualPointsAdj);;
        }

        public void setManualtieBreakAdj(String manualtieBreakAdj) {
            this.manualtieBreakAdj = new SimpleStringProperty(manualtieBreakAdj);;
        }

        public void setPoints(String points) {
            this.points = new SimpleStringProperty(points);;
        }

        public void setStandingUpdate(String standingUpdate) {
            this.standingUpdate = new SimpleStringProperty(standingUpdate);;
        }

        public Standing getStanding() {
            Standing standing = new Standing(teamID.getValue(), getPlayed(), getWon(), getDrawn(), getGoalsFor(),
                            getGoalsAgainst(), getPoints());
            standing.setManualTieBreakAdjustment(getManualtieBreakAdj());
            return standing;
        }

        public StandingsListEntry getStandingListEntry() {
            Standing standing = new Standing(teamID.getValue(), getPlayed(), getWon(), getDrawn(), getGoalsFor(),
                            getGoalsAgainst(), getPoints());
            StandingsListEntry listEntry = new StandingsListEntry(standing, getHighestPossibleFinishPosn(),
                            getLowestPossibleFinishPosn(), getManualPointsAdj(), getManualtieBreakAdj());
            return listEntry;
        }

        @Override
        public String toString() {
            return "ObservableStanding [teamID=" + teamID + ", played=" + played + ", won=" + won + ", drawn=" + drawn
                            + ", lost=" + lost + ", goalsFor=" + goalsFor + ", goalsAgainst=" + goalsAgainst
                            + ", goalDiff=" + goalDiff + ", points=" + points + ", highestPossibleFinishPosn="
                            + highestPossibleFinishPosn + ", lowestPossibleFinishPosn=" + lowestPossibleFinishPosn
                            + ", manualPointsAdj=" + manualPointsAdj + ", manualtieBreakAdj=" + manualtieBreakAdj
                            + ", standingUpdate=" + standingUpdate + "]";
        }


    }

}
