package ats.algo.outrights.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureType;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.server.api.FixturesList;
import ats.algo.sport.outrights.server.api.FixturesListEntry;
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
@SuppressWarnings("restriction")
public class ObservableFixtures {

    /*
     * these two data objects need always to be kept in sync - if one is changed the other must be also
     */

    private ObservableList<ObservableFixture> observableFixtures;

    public ObservableFixtures() {
        observableFixtures = FXCollections.observableArrayList();
    }

    public void update(FixturesList fixturesList) {

        observableFixtures.clear();
        for (FixturesListEntry fixture : fixturesList.getFixturesList()) {
            ObservableFixture observableTeam = new ObservableFixture(fixture);
            observableFixtures.add(observableTeam);
        }
    }

    public void update(Fixtures fixtures, String marketFilter) {
        observableFixtures.clear();
    }

    public ObservableList<ObservableFixture> getData() {
        return observableFixtures;
    }

    public class ObservableFixture {
        /*
         * Stuff to display
         */
        private StringProperty date;
        private StringProperty fixtureType;
        private StringProperty fixtureID;
        private StringProperty status;
        private StringProperty homeTeamID;
        private StringProperty awayTeamID;
        private StringProperty eventID;
        private StringProperty tag;
        private StringProperty playedAtNeutralGround;
        private StringProperty goalsHome;
        private StringProperty goalsAway;
        private StringProperty probsSourcedfromATS;

        private StringProperty pHome;
        private StringProperty pAway;
        private StringProperty pDraw;
        @JsonIgnore
        private StringProperty fixtureUpdate;

        /**
         * Constructor for a single row market display where there are no valid selections
         * 
         * @param market
         * @param msg
         */
        public ObservableFixture(FixturesListEntry fixturesListEntry) {
            this.fixtureID = new SimpleStringProperty(fixturesListEntry.getFixture().getFixtureID());
            this.fixtureType = new SimpleStringProperty(fixturesListEntry.getFixture().getFixtureType().toString());
            this.eventID = new SimpleStringProperty(String.valueOf(fixturesListEntry.getFixture().getEventID()));
            this.homeTeamID = new SimpleStringProperty(fixturesListEntry.getFixture().getHomeTeamID());
            this.awayTeamID = new SimpleStringProperty(fixturesListEntry.getFixture().getAwayTeamID());
            this.tag = new SimpleStringProperty(fixturesListEntry.getFixture().getTag());
            this.date = new SimpleStringProperty(fixturesListEntry.getFixture().getDate());
            this.playedAtNeutralGround = new SimpleStringProperty(
                            String.valueOf(fixturesListEntry.getFixture().isPlayedAtNeutralGround()));
            this.status = new SimpleStringProperty(String.valueOf(fixturesListEntry.getFixture().getStatus()));
            this.probsSourcedfromATS = new SimpleStringProperty(
                            String.valueOf(fixturesListEntry.getFixture().isProbsSourcedfromATS()));
            this.pHome = new SimpleStringProperty(String.valueOf(fixturesListEntry.getpHome()));
            this.pAway = new SimpleStringProperty(String.valueOf(fixturesListEntry.getpAway()));
            this.pDraw = new SimpleStringProperty(String.valueOf(fixturesListEntry.getpDraw()));
            this.goalsHome = new SimpleStringProperty(String.valueOf(fixturesListEntry.getFixture().getGoalsHome()));
            this.goalsAway = new SimpleStringProperty(String.valueOf(fixturesListEntry.getFixture().getGoalsAway()));
            this.fixtureUpdate = new SimpleStringProperty("No");
        }

        public ObservableFixture(String date, String fixtureType, String fixtureID, String status, String homeTeamID,
                        String awayTeamID, String eventID, String tag, boolean playedAtNeutralGround, int goalsHome,
                        int goalsAway, boolean probsSourcedfromATS, double pHome, double pAway, double pDraw) {
            this.date = new SimpleStringProperty(date);
            this.fixtureType = new SimpleStringProperty(fixtureType);
            this.fixtureID = new SimpleStringProperty(fixtureID);
            this.status = new SimpleStringProperty(status);
            this.homeTeamID = new SimpleStringProperty(homeTeamID);
            this.awayTeamID = new SimpleStringProperty(awayTeamID);
            this.eventID = new SimpleStringProperty(eventID);
            this.tag = new SimpleStringProperty(tag);
            this.playedAtNeutralGround = new SimpleStringProperty(String.valueOf(playedAtNeutralGround));
            this.goalsHome = new SimpleStringProperty(String.valueOf(goalsHome));
            this.goalsAway = new SimpleStringProperty(String.valueOf(goalsAway));
            this.probsSourcedfromATS = new SimpleStringProperty(String.valueOf(probsSourcedfromATS));
            this.pHome = new SimpleStringProperty(String.valueOf(pHome));
            this.pAway = new SimpleStringProperty(String.valueOf(pAway));
            this.pDraw = new SimpleStringProperty(String.valueOf(pDraw));
            this.fixtureUpdate = new SimpleStringProperty("No");
        }

        public StringProperty goalsHomeProperty() {
            return goalsHome;
        }

        public StringProperty goalsAwayProperty() {
            return goalsAway;
        }

        public StringProperty pHomeProperty() {
            return pHome;
        }

        public StringProperty pAwayProperty() {
            return pAway;
        }

        public StringProperty pDrawProperty() {
            return pDraw;
        }

        public StringProperty dateProperty() {
            return date;
        }

        public StringProperty eventIDProperty() {
            return eventID;
        }

        public StringProperty fixtureIDProperty() {
            return fixtureID;
        }

        public StringProperty fixtureTypeProperty() {
            return fixtureType;
        }

        public StringProperty homeTeamIDProperty() {
            return homeTeamID;
        }

        public StringProperty awayTeamIDProperty() {
            return awayTeamID;
        }

        public StringProperty tagProperty() {
            return tag;
        }

        public StringProperty statusProperty() {
            return status;
        }

        public StringProperty playedAtNeutralGroundProperty() {
            return playedAtNeutralGround;
        }

        public StringProperty probsSourcedfromATSProperty() {
            return probsSourcedfromATS;
        }

        public StringProperty fixtureUpdateProperty() {
            return fixtureUpdate;
        }

        public String getDate() {
            return date.getValue();
        }

        public String getHomeTeamID() {
            return homeTeamID.getValue();
        }

        public String getAwayTeamID() {
            return awayTeamID.getValue();
        }

        public Long getEventID() {
            return Long.parseLong(eventID.getValue());
        }

        public String getFixtureID() {
            return fixtureID.getValue();
        }

        public String getFixtureType() {
            return fixtureType.getValue();
        }

        public String getTag() {
            return tag.getValue();
        }

        public double getPhome() {
            return Double.valueOf(pHome.getValue());
        }

        public double getPaway() {
            return Double.valueOf(pAway.getValue());
        }

        public double getPdraw() {
            return Double.valueOf(pDraw.getValue());
        }

        public int getGoalsHome() {
            return Integer.valueOf(goalsHome.getValue());
        }

        public int getGoalsAway() {
            return Integer.valueOf(goalsAway.getValue());
        }

        public boolean getPlayedAtNeutralGround() {
            return Boolean.valueOf(playedAtNeutralGround.getValue());
        }

        public boolean getStatus() {
            return Boolean.valueOf(status.getValue());
        }

        public StringProperty getpHome() {
            return pHome;
        }

        public void setpHome(StringProperty pHome) {
            this.pHome = pHome;
        }

        public String getFixtureUpdate() {
            return fixtureUpdate.getValue();
        }

        public void setFixtureUpdate(StringProperty fixtureUpdate) {
            this.fixtureUpdate = fixtureUpdate;
        }

        public void setFixtureUpdate(String fixtureUpdate) {
            this.fixtureUpdate = new SimpleStringProperty(fixtureUpdate);
        }

        public StringProperty getpAway() {
            return pAway;
        }

        public void setpAway(StringProperty pAway) {
            this.pAway = pAway;
        }

        public StringProperty getpDraw() {
            return pDraw;
        }

        public void setpDraw(StringProperty pDraw) {
            this.pDraw = pDraw;
        }

        public void setDate(StringProperty date) {
            this.date = date;
        }

        public void setpHome(String pHome) {
            this.pHome = new SimpleStringProperty(String.valueOf(pHome));
        }

        public void setpAway(String pAway) {
            this.pAway = new SimpleStringProperty(String.valueOf(pAway));
        }

        public void setpDraw(String pDraw) {
            this.pDraw = new SimpleStringProperty(String.valueOf(pDraw));
        }

        public void setDate(String date) {
            this.date = new SimpleStringProperty(date);
        }

        public void setStatus(StringProperty status) {
            this.status = status;
        }

        public void setEventID(StringProperty eventID) {
            this.eventID = eventID;
        }

        public void setGoalsHome(StringProperty goalsHome) {
            this.goalsHome = goalsHome;
        }

        public void setGoalsAway(StringProperty goalsAway) {
            this.goalsAway = goalsAway;
        }

        public void setStatus(String status) {
            this.status = new SimpleStringProperty(String.valueOf(status));
        }

        public void setEventID(long eventID) {
            this.eventID = new SimpleStringProperty(String.valueOf(eventID));
        }

        public void setGoalsHome(String goalsHome) {
            this.goalsHome = new SimpleStringProperty(goalsHome);
        }

        public void setGoalsAway(String goalsAway) {
            this.goalsAway = new SimpleStringProperty(goalsAway);
        }

        public FixturesListEntry getFixturesListEntry() {
            FixtureType type = null;
            String fixtureType = getFixtureType().toUpperCase();
            switch (fixtureType) {
                case "LEAGUE":
                    type = FixtureType.LEAGUE;
                    break;
                case "LEAGUE_PLAYOFF_SF":
                    type = FixtureType.LEAGUE_PLAYOFF_SF;
                    break;
                case "LEAGUE_PLAY_OFF_FINAL":
                    type = FixtureType.LEAGUE_PLAY_OFF_FINAL;
                    break;
                case "KNOCKOUT_R128":
                    type = FixtureType.KNOCKOUT_R128;
                    break;

                case "KNOCKOUT_R64":
                    type = FixtureType.KNOCKOUT_R64;
                    break;
                case "KNOCKOUT_R32":
                    type = FixtureType.KNOCKOUT_R32;
                    break;

                case "KNOCKOUT_R16":
                    type = FixtureType.KNOCKOUT_R16;
                    break;
                case "KNOCKOUT_QF":
                    type = FixtureType.KNOCKOUT_QF;
                    break;

                case "KNOCKOUT_SF":
                    type = FixtureType.KNOCKOUT_SF;
                    break;
                case "KNOCKOUT_FINAL":
                    type = FixtureType.KNOCKOUT_FINAL;
                    break;

                case "KNOCKOUT_THIRD_PLACE":
                    type = FixtureType.KNOCKOUT_THIRD_PLACE;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid fixture type: " + type);

            }
            Fixture fixture = new Fixture(getDate(), type, getFixtureID(), getHomeTeamID(), getAwayTeamID(), getTag(),
                            getPlayedAtNeutralGround());

            fixture.setEventID(getEventID());
            FixturesListEntry entry = new FixturesListEntry(fixture, getPhome(), getPaway(), getPdraw());
            return entry;
        }

        @Override
        public String toString() {
            return "ObservableFixture [date=" + date.getValue() + ", fixtureID=" + fixtureID.getValue() + ", status="
                            + status.getValue() + ", homeTeamID=" + homeTeamID.getValue() + ", awayTeamID="
                            + awayTeamID.getValue() + ", eventID=" + eventID.getValue() + ", tag=" + tag.getValue()
                            + ", playedAtNeutralGround=" + playedAtNeutralGround.getValue() + ", goalsHome="
                            + goalsHome.getValue() + ", goalsAway=" + goalsAway.getValue() + ", probsSourcedfromATS="
                            + probsSourcedfromATS.getValue() + ", pHome=" + pHome.getValue() + ", pAway="
                            + pAway.getValue() + ", pDraw=" + pDraw.getValue() + "]";
        }

    }

}
