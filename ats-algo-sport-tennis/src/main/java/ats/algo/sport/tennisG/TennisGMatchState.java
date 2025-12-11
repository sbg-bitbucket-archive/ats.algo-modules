package ats.algo.sport.tennisG;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.baseclasses.AlgoMatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.genericsupportfunctions.GCMath;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tennisG.TennisGMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennisG.TennisGMatchIncidentResult.TennisMatchIncidentResultType;

/**
 * implements a simple best of n set tennis match
 * 
 * @author Geoff
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TennisGMatchState extends AlgoMatchState {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TennisGMatchFormat matchFormat;
    private int matchWinningSetScore;
    private int setsA;
    private int setsB;
    private int gamesA;
    private int gamesB;
    private PairOfIntegers[] gameScoreInSetN;
    private boolean inTieBreak;
    private Game game;
    private TieBreak tieBreak;
    private int lastGameOrTieBreakPointsA;
    private int lastGameOrTieBreakPointsB;
    private TeamId lastPointPlayedOutcome;
    private TeamId lastGamePlayedOutcome;

    /*
     * counters
     */

    public int getSetsA() {
        return setsA;
    }

    public void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    public int getSetsB() {
        return setsB;
    }

    public void setSetsB(int setsB) {
        this.setsB = setsB;
    }

    public int getGamesA() {
        return gamesA;
    }

    public void setGamesA(int gamesA) {
        this.gamesA = gamesA;
    }

    public int getGamesB() {
        return gamesB;
    }

    public void setGamesB(int gamesB) {
        this.gamesB = gamesB;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public TieBreak getTieBreak() {
        return tieBreak;
    }

    public void setTieBreak(TieBreak tieBreak) {
        this.tieBreak = tieBreak;
    }

    public TeamId getOnServeNow() {
        if (inTieBreak)
            return tieBreak.onServeNow;
        else
            return game.onServeNow;
    }

    public void setOnServeNow(TeamId id) {
        if (inTieBreak)
            tieBreak.onServeNow = id;
        else
            game.onServeNow = id;
    }

    /**
     * 
     * @return set no in range 1-5
     */
    @JsonIgnore
    public int getSetNo() {
        return setsA + setsB + 1;
    }

    /**
     * 
     * @return game no starting at 1 for first game of set
     */
    @JsonIgnore
    public int getGameNo() {
        return gamesA + gamesB + 1;
    }

    /**
     * 
     * @return point no starting at 1 for first point of game or tie break
     */
    @JsonIgnore
    public int getPointNo() {
        int pointNo;
        if (inTieBreak)
            pointNo = tieBreak.pointsA + tieBreak.pointsB;
        else
            pointNo = game.pointNo;
        return pointNo + 1;
    }

    /**
     * 
     * @param i i = 0 for the first set
     * @return
     */
    public PairOfIntegers getGameScoreInSetN(int i) {
        return gameScoreInSetN[i];
    }

    /**
     * 
     * @param setNo =0 for first set
     * @param gamesA
     * @param gamesB
     */
    public void setGameScoreInSetN(int setNo, int gamesA, int gamesB) {
        gameScoreInSetN[setNo].A = gamesA;
        gameScoreInSetN[setNo].B = gamesB;
    }

    public boolean isInTieBreak() {
        return inTieBreak;
    }

    public int getPointsA() {
        if (inTieBreak)
            return tieBreak.pointsA;
        else
            return game.pointsA;
    }

    public int getPointsB() {
        if (inTieBreak)
            return tieBreak.pointsB;
        else
            return game.pointsB;
    }

    public boolean isPreMatch() {
        return getOnServeNow() == TeamId.UNKNOWN;
    }

    /**
     * returns true if in a tiebreak now or one has been played in sets alredy completed
     * 
     * @return
     */
    public boolean isTieBreakAlreadyPlayedInMatch() {
        if (inTieBreak)
            return true;
        for (int i = 0; i < this.getSetNo(); i++) {
            PairOfIntegers s = gameScoreInSetN[i];
            if (s.A + s.B == 13)

                return true;
        }
        return false;
    }

    /**
     * returns true i A has already won a set in the match
     * 
     * @return
     */
    @JsonIgnore
    public boolean isAlreadyWonASetA() {

        return setsA > 0;
    }

    /**
     * returns true if B has already won a set in the match
     * 
     * @return
     */
    @JsonIgnore
    public boolean isAlreadyWonASetB() {

        return setsB > 0;

    }

    /**
     * returns true of either player has scored 5 or more games in the current set
     * 
     * @return
     */
    @JsonIgnore
    public boolean isAlreadyOver4GamesInCurrentSet() {
        return (gamesA >= 5 || gamesB >= 5);
    }

    /**
     * returns true if the game with No n (starting at 1 for first game in set) has not already been played and might be
     * 
     * @param n
     * @return
     */
    public boolean isGameMayBePlayed(int n) {
        return n >= getGameNo() && (inAdvantageSet() || n <= 12);
    }

    /**
     * returns true if within a game and point no n (starting at 1 for first point in game) might be played
     * 
     * @param n
     * @return
     */
    public boolean isPointMayBePlayed(int n) {
        return !isInTieBreak() && n >= getPointNo();

    }

    /**
     * class constructor
     * 
     * @param nGamesInMatch best of 3 or 5 sets
     */
    public TennisGMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (TennisGMatchFormat) matchFormat;
        game = new Game();
        tieBreak = new TieBreak();
        int nSetsInMatch = this.matchFormat.getSetsPerMatch();
        setScore(0, 0, 0, 0, TeamId.UNKNOWN);
        gameScoreInSetN = new PairOfIntegers[nSetsInMatch];
        for (int i = 0; i < nSetsInMatch; i++)
            gameScoreInSetN[i] = new PairOfIntegers();
        matchWinningSetScore = nSetsInMatch / 2 + 1;
    }

    // required for JSON serialisation
    @SuppressWarnings("unused")
    private TennisGMatchState() {}

    /**
     * sets the starting score. Starting points assumed to be zero
     * 
     * @param setsA
     * @param setsB
     * @param gamesA
     * @param gamesB
     * @param onServeNow may be A,B or unknown
     */
    public void setScore(int setsA, int setsB, int gamesA, int gamesB, TeamId onServeNow) {
        this.setsA = setsA;
        this.setsB = setsB;
        this.gamesA = gamesA;
        this.gamesB = gamesB;
        inTieBreak = gamesA == 6 && gamesB == 6;
        if (inTieBreak)
            tieBreak.onServeNow = onServeNow;
        else
            game.onServeNow = onServeNow;
        this.game.onServeNow = onServeNow;
        game.startNewGame(onServeNow);
        tieBreak.startNewTieBreak(onServeNow);
    }

    @Override
    public void setEqualTo(AlgoMatchState ms) {
        super.setEqualTo(ms);
        TennisGMatchState ts = (TennisGMatchState) ms;
        this.setScore(ts.getSetsA(), ts.getSetsB(), ts.getGamesA(), ts.getGamesB(), ts.getOnServeNow());
        this.getGame().setEqualTo(ts.getGame());
        this.getTieBreak().setEqualTo(ts.getTieBreak());
        this.lastGameOrTieBreakPointsA = ts.getLastGameOrTieBreakPointsA();
        this.lastGameOrTieBreakPointsB = ts.getLastGameOrTieBreakPointsB();
        for (int i = 0; i < matchFormat.getSetsPerMatch(); i++) {
            PairOfIntegers s = ts.getGameScoreInSetN(i);
            this.gameScoreInSetN[i].A = s.A;
            this.gameScoreInSetN[i].B = s.B;
        }
    }

    @Override
    public AlgoMatchState copy() {
        TennisGMatchState cc = new TennisGMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    private enum GameOrTieBreakEventResult {
        POINTWONA,
        POINTWONB,
        GAMEWONA,
        GAMEWONB
    }

    /**
     * holds the state for the currently active game
     * 
     * @author Geoff
     * 
     */
    public class Game {
        int pointsA;
        int pointsB;
        int pointNo;
        TeamId onServeNow;

        public Game() {} // required for JSON serialisation

        public int getPointsA() {
            return pointsA;
        }

        public void setPointsA(int pointsA) {
            this.pointsA = pointsA;
        }

        public int getPointsB() {
            return pointsB;
        }

        public void setPointsB(int pointsB) {
            this.pointsB = pointsB;
        }

        public int getPointNo() {
            return pointNo;
        }

        public void setPointNo(int pointNo) {
            this.pointNo = pointNo;
        }

        public TeamId getOnServeNow() {
            return onServeNow;
        }

        public void setOnServeNow(TeamId onServeNow) {
            this.onServeNow = onServeNow;
        }

        void startNewGame(TeamId onServeNow) {
            pointsA = 0;
            pointsB = 0;
            pointNo = 0;
            this.onServeNow = onServeNow;
        }

        public void setEqualTo(Game game) {
            this.pointsA = game.pointsA;
            this.pointsB = game.pointsB;
            this.pointNo = game.pointNo;
            this.onServeNow = game.onServeNow;
        }

        /**
         * updates score for point played, and returns result
         * 
         * @param pointWonByA
         * @return
         */
        GameOrTieBreakEventResult updateForPointPlayed(boolean pointWonByA) {
            GameOrTieBreakEventResult pointWonOutcome;
            pointNo++;
            if (pointWonByA) {
                pointWonOutcome = GameOrTieBreakEventResult.POINTWONA;
                pointsA++;
            } else {
                pointWonOutcome = GameOrTieBreakEventResult.POINTWONB;
                pointsB++;
            }
            if (pointsA - pointsB >= 2 && pointsA >= 4) {
                pointWonOutcome = GameOrTieBreakEventResult.GAMEWONA;
            }
            if (pointsB - pointsA >= 2 && pointsB >= 4) {
                pointWonOutcome = GameOrTieBreakEventResult.GAMEWONB;
            }
            if (pointsA == 4 && pointsB == 4) {
                pointsA = 3;
                pointsB = 3;
            }
            return pointWonOutcome;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((onServeNow == null) ? 0 : onServeNow.hashCode());
            result = prime * result + pointNo;
            result = prime * result + pointsA;
            result = prime * result + pointsB;
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
            Game other = (Game) obj;
            if (onServeNow != other.onServeNow)
                return false;
            if (pointNo != other.pointNo)
                return false;
            if (pointsA != other.pointsA)
                return false;
            if (pointsB != other.pointsB)
                return false;
            return true;
        }
    }

    /**
     * implements tie break logic
     * 
     * @author Geoff
     * 
     */
    private class TieBreak {
        int pointsA;
        int pointsB;
        TeamId onServeNow;
        TeamId onServeAtStartOfTieBreak;

        public TieBreak() {} // required for JSON serialisation

        void startNewTieBreak(TeamId onServeNow) {
            pointsA = 0;
            pointsB = 0;
            this.onServeNow = onServeNow;
            this.onServeAtStartOfTieBreak = onServeNow;
        }

        public void setEqualTo(TieBreak tieBreak) {
            this.pointsA = tieBreak.pointsA;
            this.pointsB = tieBreak.pointsB;
            this.onServeNow = tieBreak.onServeNow;
            this.onServeAtStartOfTieBreak = tieBreak.onServeAtStartOfTieBreak;
        }

        GameOrTieBreakEventResult updateForPointPlayed(boolean pointWonByA) {
            GameOrTieBreakEventResult gameOutcome;
            if (pointWonByA) {
                gameOutcome = GameOrTieBreakEventResult.POINTWONA;
                pointsA++;
            } else {
                gameOutcome = GameOrTieBreakEventResult.POINTWONB;
                pointsB++;
            }
            if (pointsA - pointsB >= 2 && pointsA >= 7) {
                gameOutcome = GameOrTieBreakEventResult.GAMEWONA;
            }
            if (pointsB - pointsA >= 2 && pointsB >= 7) {
                gameOutcome = GameOrTieBreakEventResult.GAMEWONB;
            }
            if (GCMath.isOdd(pointsA + pointsB))
                onServeNow = switchServer(onServeNow);
            return gameOutcome;
        }

        @SuppressWarnings("unused")
        public int getPointsA() {
            return pointsA;
        }

        @SuppressWarnings("unused")
        public void setPointsA(int pointsA) {
            this.pointsA = pointsA;
        }

        @SuppressWarnings("unused")
        public int getPointsB() {
            return pointsB;
        }

        @SuppressWarnings("unused")
        public void setPointsB(int pointsB) {
            this.pointsB = pointsB;
        }

        @SuppressWarnings("unused")
        public TeamId getOnServeNow() {
            return onServeNow;
        }

        @SuppressWarnings("unused")
        public void setOnServeNow(TeamId onServeNow) {
            this.onServeNow = onServeNow;
        }

        @SuppressWarnings("unused")
        public TeamId getOnServeAtStartOfTieBreak() {
            return onServeAtStartOfTieBreak;
        }

        @SuppressWarnings("unused")
        public void setOnServeAtStartOfTieBreak(TeamId onServeAtStartOfTieBreak) {
            this.onServeAtStartOfTieBreak = onServeAtStartOfTieBreak;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((onServeAtStartOfTieBreak == null) ? 0 : onServeAtStartOfTieBreak.hashCode());
            result = prime * result + ((onServeNow == null) ? 0 : onServeNow.hashCode());
            result = prime * result + pointsA;
            result = prime * result + pointsB;
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
            TieBreak other = (TieBreak) obj;
            if (onServeAtStartOfTieBreak != other.onServeAtStartOfTieBreak)
                return false;
            if (onServeNow != other.onServeNow)
                return false;
            if (pointsA != other.pointsA)
                return false;
            if (pointsB != other.pointsB)
                return false;
            return true;
        }
    }

    /**
     * updates the matchState as each point is played
     * 
     * @param pointWonByA
     * @return
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        if (!(matchIncident instanceof TennisGMatchIncident)) {
            super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
            return null;
        }

        TennisGMatchIncident tennisMatchIncident = (TennisGMatchIncident) matchIncident;
        /*
         * check for start of match
         */
        if (tennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.MATCH_STARTING) {
            setOnServeNow(tennisMatchIncident.getServerAtStartOfMatch());
            return new TennisGMatchIncidentResult(TennisMatchIncidentResultType.FIRSTSERVERINMATCHSET);
        }
        /*
         * to get here must be point played type event
         */
        boolean pointWonByA = tennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.POINT_WON
                        && tennisMatchIncident.getPointWinner() == TeamId.A;

        lastPointPlayedOutcome = tennisMatchIncident.getPointWinner();
        TeamId onServeNow;
        GameOrTieBreakEventResult gameEventResult;
        TennisGMatchIncidentResult tennisMatchIncidentResult;

        TeamId serverAtStartOfGameOrTieBreak;
        if (inTieBreak) {
            onServeNow = tieBreak.onServeNow;
            gameEventResult = tieBreak.updateForPointPlayed(pointWonByA);
            serverAtStartOfGameOrTieBreak = tieBreak.onServeAtStartOfTieBreak;
            lastGameOrTieBreakPointsA = tieBreak.pointsA;
            lastGameOrTieBreakPointsB = tieBreak.pointsB;
        } else {
            onServeNow = game.onServeNow;
            gameEventResult = game.updateForPointPlayed(pointWonByA);
            serverAtStartOfGameOrTieBreak = game.onServeNow;
            lastGameOrTieBreakPointsA = game.pointsA;
            lastGameOrTieBreakPointsB = game.pointsB;
        }
        switch (gameEventResult) {
            case POINTWONA:
                tennisMatchIncidentResult = new TennisGMatchIncidentResult(onServeNow == TeamId.A, true,
                                TennisMatchIncidentResultType.POINTWONONLY);
                return (MatchIncidentResult) tennisMatchIncidentResult;
            case POINTWONB:
                tennisMatchIncidentResult = new TennisGMatchIncidentResult(onServeNow == TeamId.A, false,
                                TennisMatchIncidentResultType.POINTWONONLY);
                return (MatchIncidentResult) tennisMatchIncidentResult;
            case GAMEWONA:
                lastGamePlayedOutcome = TeamId.A;
                tennisMatchIncidentResult = new TennisGMatchIncidentResult(onServeNow == TeamId.A, true,
                                TennisMatchIncidentResultType.GAMEWONA);
                gamesA++;
                if (isSetWinningGameScore(gamesA, gamesB)) {
                    tennisMatchIncidentResult.setTennisMatchIncidentResultType(TennisMatchIncidentResultType.SETWONA);
                    int setNo = setsA + setsB;
                    gameScoreInSetN[setNo].A = gamesA;
                    gameScoreInSetN[setNo].B = gamesB;
                    gamesA = 0;
                    gamesB = 0;
                    setsA++;
                    if (setsA == matchWinningSetScore)
                        tennisMatchIncidentResult
                                        .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONA);
                }
                break;
            case GAMEWONB:
                lastGamePlayedOutcome = TeamId.B;
                tennisMatchIncidentResult = new TennisGMatchIncidentResult(onServeNow == TeamId.A, false,
                                TennisMatchIncidentResultType.GAMEWONB);
                gamesB++;
                if (isSetWinningGameScore(gamesB, gamesA)) {
                    tennisMatchIncidentResult.setTennisMatchIncidentResultType(TennisMatchIncidentResultType.SETWONB);
                    int setNo = setsA + setsB;
                    gameScoreInSetN[setNo].A = gamesA;
                    gameScoreInSetN[setNo].B = gamesB;
                    gamesA = 0;
                    gamesB = 0;
                    setsB++;
                    if (setsB == matchWinningSetScore)
                        tennisMatchIncidentResult
                                        .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONB);
                }
                break;
            default:
                throw new IllegalArgumentException("Should not get here");
        }
        TeamId newServer = switchServer(serverAtStartOfGameOrTieBreak);
        inTieBreak = (!inAdvantageSet()) && (gamesA == 6) && (gamesB == 6);
        if (inTieBreak)
            tieBreak.startNewTieBreak(newServer);
        else
            game.startNewGame(newServer);
        return (MatchIncidentResult) tennisMatchIncidentResult;
    }

    private TeamId switchServer(TeamId currentServer) {
        TeamId newServer = null;
        switch (currentServer) {
            case A:
                newServer = TeamId.B;
                break;
            case B:
                newServer = TeamId.A;
                break;
            case UNKNOWN:
                throw new IllegalArgumentException("Should not get here");
        }
        return newServer;
    }

    boolean inAdvantageSet() {
        boolean inFinalSet = setsA + setsB == matchFormat.getSetsPerMatch() - 1;
        return (inFinalSet && !matchFormat.isTieBreakInFinalSet());
    }

    /**
     * returns true if the games score is a set winning score
     * 
     * @param gamesWinner
     * @param gamesLoser
     * @return
     */
    private boolean isSetWinningGameScore(int gamesWinner, int gamesLoser) {

        if (inAdvantageSet())
            return (gamesWinner >= 6) && (gamesWinner - gamesLoser >= 2);
        else
            return (gamesWinner == 7) || (gamesWinner == 6 && gamesLoser <= 4);
    }

    /**
     * returns the point score in the game or tie break just completed
     * 
     * @return
     */
    int getLastGameOrTieBreakPointsA() {
        return lastGameOrTieBreakPointsA;
    }

    int getLastGameOrTieBreakPointsB() {
        return lastGameOrTieBreakPointsB;
    }

    private boolean requestedFirstPlayerToServe;
    private String defaultPrompt;

    @Override
    @JsonIgnore
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt prompt;
        if (isMatchCompleted()) {
            prompt = new MatchIncidentPrompt("Match won", "-");
        } else {
            if (getOnServeNow() == TeamId.UNKNOWN) {
                requestedFirstPlayerToServe = true;
                prompt = new MatchIncidentPrompt("Enter id of first player to serve (A/B): ", "A");
            } else {
                requestedFirstPlayerToServe = false;
                prompt = new MatchIncidentPrompt("Next point winner (A/B): ", defaultPrompt);
            }
        }
        return prompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        TeamId teamId;
        TennisMatchIncidentType incidentType;
        if (requestedFirstPlayerToServe)
            incidentType = TennisMatchIncidentType.MATCH_STARTING;
        else
            incidentType = TennisMatchIncidentType.POINT_WON;
        switch (response.toUpperCase()) {
            case "A":
                teamId = TeamId.A;
                defaultPrompt = "A";
                break;
            case "B":
                teamId = TeamId.B;
                defaultPrompt = "B";
                break;
            default:
                /*
                 * error in input supplied
                 */
                return null;
        }
        requestedFirstPlayerToServe = false; // only ask this once
        return new TennisGMatchIncident(0, incidentType, teamId);
    }

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put("Sets", String.format("%d-%d", setsA, setsB));
        map.put("Games", String.format("%d-%d", gamesA, gamesB));
        String pointsA;
        String pointsB;
        String inTb;
        if (isInTieBreak()) {
            pointsA = Integer.toString(tieBreak.pointsA);
            pointsB = Integer.toString(tieBreak.pointsB);
            inTb = "Yes";
        } else {
            pointsA = convertToTennisPoints(game.pointsA);
            pointsB = convertToTennisPoints(game.pointsB);
            inTb = "No";
        }
        map.put("Points", String.format("%s-%s", pointsA, pointsB));

        map.put("In tie break?", inTb);
        map.put("Player on serve", getOnServeNow().toString());
        if (matchFormat != null) { // can currently null when deserialised from
                                   // json
            for (int i = 0; i < matchFormat.getSetsPerMatch(); i++) {
                PairOfIntegers p = gameScoreInSetN[i];
                map.put(String.format("Game score in set no %d", i + 1), String.format("%d-%d", p.A, p.B));
            }
        }
        String currentPointId = String.format("S%d.%d.%d", getSetNo(), getGameNo(), getPointNo());
        map.put("Current point id", currentPointId);
        return map;
    }

    private String convertToTennisPoints(int points) {
        String s = "";
        switch (points) {
            case 0:
                s = "0";
                break;
            case 1:
                s = "15";
                break;
            case 2:
                s = "30";
                break;
            case 3:
                s = "40";
                break;
            case 4:
                s = "Adv";
                break;
        }
        return s;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * don't allow score to be updated by the user, at least for now, so don't need this method to do anything
         */
        return null;
    }

    @Override
    public boolean isMatchCompleted() {
        return (setsA == matchWinningSetScore) || (setsB == matchWinningSetScore);
    }

    public TeamId getLastPointPlayedOutcome() {
        return lastPointPlayedOutcome;
    }

    public TeamId getLastGamePlayedOutcome() {
        return lastGamePlayedOutcome;
    }

    @JsonIgnore
    @Override
    public MatchFormat getMatchFormat() {
        return matchFormat;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + gamesA;
        result = prime * result + gamesB;
        result = prime * result + (inTieBreak ? 1231 : 1237);
        result = prime * result + setsA;
        result = prime * result + setsB;
        result = prime * result + ((tieBreak == null) ? 0 : tieBreak.hashCode());
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
        TennisGMatchState other = (TennisGMatchState) obj;
        if (gamesA != other.gamesA)
            return false;
        if (gamesB != other.gamesB)
            return false;
        if (inTieBreak != other.inTieBreak)
            return false;
        if (setsA != other.setsA)
            return false;
        if (setsB != other.setsB)
            return false;
        if (tieBreak == null) {
            if (other.tieBreak != null)
                return false;
        } else if (!tieBreak.equals(other.tieBreak))
            return false;
        return true;
    }

    /**
     * gets the sequence id to use for match based markets
     * 
     * @return
     */
    public String getSequenceIdForMatch() {
        return "M";
    }

    /**
     * gets the sequence id to use for set based markets
     * 
     * @param setOffset 0 = current set, 1 = next set etc
     * @return null if specified set can't occur, else the sequence id
     */
    public String getSequenceIdforSet(int setOffset) {
        int setNo = this.getSetNo() + setOffset;
        if (setNo > matchFormat.getSetsPerMatch())
            return null;
        else
            return String.format("S%d", setNo);
    }

    /**
     * gets the sequence id to use for game based markets (within the currently active set)
     * 
     * @param gameOffset 0 = current game, 1 = next game etc
     * @return null if specified game can't occur, else the sequence id
     */
    public String getSequenceIdForGame(int gameOffset) {
        if (this.isInTieBreak())
            return null;
        int gameNo = this.getGameNo() + gameOffset;
        if (this.isGameMayBePlayed(gameNo))
            return String.format("S%d.%d", this.getSetNo(), gameNo);
        else
            return null;
    }

    /**
     * gets the sequence id to use for point based markets (within the currently active game or tie break)
     * 
     * @param pointOffset 0 = current game, 1 = next game etc
     * @return null if specified point can't occur, else the sequence id
     */
    public String getSequenceIdForPoint(int pointOffset) {
        int pointNo = this.getPointNo() + pointOffset;
        if (this.isPointMayBePlayed(pointNo))
            return String.format("S%d.%d.%d", this.getSetNo(), this.getGameNo(), pointNo);
        else
            return null;
    }

    /**
     * gets the sequence id to use for point based markets (within the currently active game or tie break)
     * 
     * @param pointNo 1 = first point in next game etc
     * @return null if specified point can't occur, else the sequence id
     */
    public String getSequenceIdForNextGamePoint(int pointNo) {
        int gameNo = this.getGameNo() + 1;
        if (this.isGameMayBePlayed(gameNo))
            return String.format("S%d.%d.%d", this.getSetNo(), gameNo, pointNo);
        else
            return null;
    }

    @Override
    public GamePeriod getGamePeriod() {
        GamePeriod gamePeriod = null;;
        if (this.isPreMatch())
            gamePeriod = GamePeriod.PREMATCH;
        else {
            switch (this.getSetNo()) {
                case 1:
                    gamePeriod = GamePeriod.FIRST_SET;
                    break;
                case 2:
                    gamePeriod = GamePeriod.SECOND_SET;
                    break;
                case 3:
                    gamePeriod = GamePeriod.THIRD_SET;
                    break;
                case 4:
                    gamePeriod = GamePeriod.FOURTH_SET;
                    break;
                case 5:
                    gamePeriod = GamePeriod.FIFTH_SET;
                    break;
            }
        }
        return gamePeriod;
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
    public SimpleMatchState generateSimpleMatchState() {
        return null;
    }

    @Override
    public AlgoMatchState generateMatchStateForMatchResult(MatchResultMap matchResultMap) {
        /*
         * not yet supported for this sport
         */
        return null;
    }

}
