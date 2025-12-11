package ats.algo.sport.tennis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.AbandonMatchIncident;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.AbandonMatchIncident.AbandonMatchIncidentType;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.ElapsedTimeMatchIncident;
import ats.algo.core.common.GenderId;
import ats.algo.core.common.SequenceId;
import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.tennis.TennisMatchFormat.FinalSetType;
import ats.algo.sport.tennis.TennisMatchFormat.Sex;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;
import ats.algo.sport.tennis.TennisMatchIncidentResult.TennisMatchIncidentResultType;

/**
 * implements a singles and doubles tennis match
 *
 * @author Geoff & Jin
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TennisMatchState extends MatchState {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private EnumMap<PlayerId, GenderId> genderMap; // NOT INSTANCIANED IN
                                                   // SIMULATION COPIES!! NO
                                                   // SET EQUAL TO METHOD OR
                                                   // GETTER/SETTER
    private SuspensionStatus suspensionStatus;
    private boolean faultFlag = false;
    private TennisMatchFormat matchFormat;
    private int matchWinningSetScore;
    private int setsA;
    private int setsB;
    private int gamesA;
    private int gamesB;
    private int tieBreaksCounter;

    private int[] firstServingBreakInSetN;
    private int[] firstServingBreakAInSetN;
    private int[] firstServingBreakBInSetN;
    private PairOfIntegers[] gameScoreInSetN;
    boolean inTieBreak;
    private boolean inSuperTieBreak;
    private boolean breakPoint;
    private boolean gamePoint;
    private boolean dangerPoint;
    private boolean inFinalSet;
    private int nSetsInMatch;
    private int nGamesInMatch;
    private PlayerId onServePlayerInFirstGameOfSet = PlayerId.UNKNOWN;
    private PlayerId onServePlayerInSecondGameOfSet = PlayerId.UNKNOWN;
    private PlayerId startOfMatchServer = PlayerId.UNKNOWN;

    int finalSetSuperTBNo = 0; // 0 + tb false for adv 0 + tb true for
                               // tb in
                               // final set, 7+ tb true or 10+ tb true
    private boolean tieBreakinFinalSet;
    private boolean tieBreakinRegularSet;
    private boolean finalSetSuperTieBreak;
    private boolean newSetSwitchServePlayer = false;
    boolean doublesMatch;
    private Game game;
    private TieBreak tieBreak;
    private SuperTieBreak superTieBreak;
    private int lastGameOrTieBreakPointsA;
    private int lastGameOrTieBreakPointsB;
    private TeamId lastPointPlayedOutcome;
    private TeamId lastGamePlayedOutcome;
    private TeamId teamBreakFirst = TeamId.UNKNOWN;

    private TennisMatchIncident doubletennisMatchIncident;
    private LastIncidentDetails lastIncidentDetails = new LastIncidentDetails();
    private int serveNumber = 1; // if last serve if fault serveNumber becomes 2
    private TennisMatchIncidentResult mostRecentMatchIncidentResult; // the
                                                                     // state

    public TennisMatchIncidentResult getMostRecentMatchIncidentResult() {
        return mostRecentMatchIncidentResult;
    }

    public void setMostRecentMatchIncidentResult(TennisMatchIncidentResult mostRecentMatchIncidentResult) {
        this.mostRecentMatchIncidentResult = mostRecentMatchIncidentResult;
    }



    public int getMatchWinningSetScore() {
        return matchWinningSetScore;
    }


    public int getTieBreaksCounter() {
        return tieBreaksCounter;
    }


    public void setTieBreaksCounter(int tieBreaksCounter) {
        this.tieBreaksCounter = tieBreaksCounter;
    }


    private PairOfIntegers previousGameScore;


    public int[] getFirstServingBreakInSetN() {
        return firstServingBreakInSetN;
    }


    public PairOfIntegers[] getGameScoreInSetN() {
        return gameScoreInSetN;
    }

    public void setGameScoreInSetN(PairOfIntegers[] gameScoreInSetN) {
        this.gameScoreInSetN = gameScoreInSetN;
    }

    public int getnSetsInMatch() {
        return nSetsInMatch;
    }

    public void setnSetsInMatch(int nSetsInMatch) {
        this.nSetsInMatch = nSetsInMatch;
    }

    public int getnGamesInMatch() {
        return nGamesInMatch;
    }

    public void setnGamesInMatch(int nGamesInMatch) {
        this.nGamesInMatch = nGamesInMatch;
    }

    public boolean isDoublesMatch() {
        return doublesMatch;
    }

    public void setDoublesMatch(boolean doublesMatch) {
        this.doublesMatch = doublesMatch;
    }

    public boolean isRequestedFirstPlayerToServe() {
        return requestedFirstPlayerToServe;
    }

    public void setRequestedFirstPlayerToServe(boolean requestedFirstPlayerToServe) {
        this.requestedFirstPlayerToServe = requestedFirstPlayerToServe;
    }

    public void setMatchFormat(TennisMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    public void setMatchWinningSetScore(int matchWinningSetScore) {
        this.matchWinningSetScore = matchWinningSetScore;
    }

    public void setLastGameOrTieBreakPointsA(int lastGameOrTieBreakPointsA) {
        this.lastGameOrTieBreakPointsA = lastGameOrTieBreakPointsA;
    }

    public void setLastGameOrTieBreakPointsB(int lastGameOrTieBreakPointsB) {
        this.lastGameOrTieBreakPointsB = lastGameOrTieBreakPointsB;
    }

    public void setLastPointPlayedOutcome(TeamId lastPointPlayedOutcome) {
        this.lastPointPlayedOutcome = lastPointPlayedOutcome;
    }

    public void setLastGamePlayedOutcome(TeamId lastGamePlayedOutcome) {
        this.lastGamePlayedOutcome = lastGamePlayedOutcome;
    }

    public void setFirstServingBreakInSetN(int[] firstServingBreakInSetN) {
        this.firstServingBreakInSetN = firstServingBreakInSetN;
    }

    public int[] getFirstServingBreakAInSetN() {
        return firstServingBreakAInSetN;
    }

    public void setFirstServingBreakAInSetN(int[] firstServingBreakAInSetN) {
        this.firstServingBreakAInSetN = firstServingBreakAInSetN;
    }

    public int[] getFirstServingBreakBInSetN() {
        return firstServingBreakBInSetN;
    }

    public void setFirstServingBreakBInSetN(int[] firstServingBreakBInSetN) {
        this.firstServingBreakBInSetN = firstServingBreakBInSetN;
    }

    public SuspensionStatus getSuspensionStatus() {
        return suspensionStatus;
    }

    public void setSuspensionStatus(SuspensionStatus suspensionStatus) {
        this.suspensionStatus = suspensionStatus;
    }

    public PairOfIntegers getPreviousGameScore() {
        return previousGameScore;
    }

    public void setPreviousGameScore(PairOfIntegers previousGameScore) {
        this.previousGameScore = previousGameScore;
    }

    public int getServeNumber() {
        return serveNumber;
    }

    public void setServeNumber(int serveNumber) {
        this.serveNumber = serveNumber;
    }

    public LastIncidentDetails getLastIncidentDetails() {
        return lastIncidentDetails;
    }

    public void setLastIncidentDetails(LastIncidentDetails lastIncidentDetails) {
        this.lastIncidentDetails = lastIncidentDetails;
    }

    public TennisMatchIncident getDoubletennisMatchIncident() {
        return doubletennisMatchIncident;
    }

    public void setDoubletennisMatchIncident(TennisMatchIncident doubletennisMatchIncident) {
        this.doubletennisMatchIncident = doubletennisMatchIncident;
    }

    public EnumMap<PlayerId, GenderId> getGenderMap() {
        return genderMap;
    }

    public void setGenderMap(EnumMap<PlayerId, GenderId> genderMap) {
        this.genderMap = genderMap;
    }

    /* trading rules required */
    public boolean isBreakPoint() {
        return breakPoint;
    }

    public void setBreakPoint(boolean breakPoint) {
        this.breakPoint = breakPoint;
    }

    public boolean isGamePoint() {
        return gamePoint;
    }

    public void setGamePoint(boolean gamePoint) {
        this.gamePoint = gamePoint;
    }

    public boolean isDangerPoint() {
        return dangerPoint;
    }

    public void setDangerPoint(boolean dangerPoint) {
        this.dangerPoint = dangerPoint;
    }

    public TeamId getTeamBreakFirst() {
        return teamBreakFirst;
    }

    public void setTeamBreakFirst(TeamId teamBreakFirst) {
        this.teamBreakFirst = teamBreakFirst;
    }

    /*
     * Power Points for IPTL only
     */

    public boolean isPowerPointCurrentPoint() {
        return game.isPowerPointCurrentPoint();
    }


    public void setPowerPointCurrentPoint(boolean powerPointCurrentPoint) {
        game.setPowerPointCurrentPoint(powerPointCurrentPoint);
    }

    /*
     * Ppb dummy params
     */

    public PlayerId getStartOfMatchServer() {
        return startOfMatchServer;
    }


    public void setStartOfMatchServer(PlayerId startOfMatchServer) {
        this.startOfMatchServer = startOfMatchServer;
    }

    /*
     * Poker star asked public methods
     */

    public PlayerId getOnServePlayerInFirstGameOfSet() {
        return onServePlayerInFirstGameOfSet;
    }



    public void setOnServePlayerInFirstGameOfSet(PlayerId onServePlayerInFirstGameOfSet) {
        this.onServePlayerInFirstGameOfSet = onServePlayerInFirstGameOfSet;
    }


    public PlayerId getOnServePlayerInSecondGameOfSet() {
        return onServePlayerInSecondGameOfSet;
    }


    public void setOnServePlayerInSecondGameOfSet(PlayerId onServePlayerInSecondGameOfSet) {
        this.onServePlayerInSecondGameOfSet = onServePlayerInSecondGameOfSet;
    }


    public TeamId getOnServeTeamInFirstGameOfSet() {

        if (onServePlayerInFirstGameOfSet == PlayerId.A1 || onServePlayerInFirstGameOfSet == PlayerId.A2) {
            return TeamId.A;
        }
        if (onServePlayerInFirstGameOfSet == PlayerId.B1 || onServePlayerInFirstGameOfSet == PlayerId.B2) {
            return TeamId.B;
        }

        return TeamId.UNKNOWN;
    }


    public TeamId getOnServeTeamInSecondGameOfSet() {

        if (onServePlayerInSecondGameOfSet == PlayerId.A1 || onServePlayerInSecondGameOfSet == PlayerId.A2) {
            return TeamId.A;
        }
        if (onServePlayerInSecondGameOfSet == PlayerId.B1 || onServePlayerInSecondGameOfSet == PlayerId.B2) {
            return TeamId.B;
        }

        return TeamId.UNKNOWN;
    }

    public boolean isDoubleMatch() {
        return doublesMatch;
    }

    public boolean isInSuperTieBreak() {
        return inSuperTieBreak;
    }

    public boolean isFaultFlag() {
        return faultFlag;
    }

    public void setFaultFlag(boolean faultFlag) {
        this.faultFlag = faultFlag;
    }

    void setInSuperTieBreak(boolean inSuperTieBreak) {
        this.inSuperTieBreak = inSuperTieBreak;
    }

    void setInTieBreak(boolean inTieBreak) {
        this.inTieBreak = inTieBreak;
    }

    public boolean isFinalSetSuperTieBreak() {
        return finalSetSuperTieBreak;
    }

    void setFinalSetSuperTieBreak(boolean isFinalSetTieBreakTenOrSeven) {
        this.finalSetSuperTieBreak = isFinalSetTieBreakTenOrSeven;
    }

    public boolean isInFinalSet() {
        return inFinalSet;
    }

    void setInFinalSet(boolean inFinalSet) {
        this.inFinalSet = inFinalSet;
    }

    public int getFinalSetSuperTBNo() {
        return finalSetSuperTBNo;
    }

    void setFinalSetSuperTBNo(int finalSetTBNo) {
        this.finalSetSuperTBNo = finalSetTBNo;
    }

    public boolean isTieBreakinFinalSet() {
        return tieBreakinFinalSet;
    }

    public boolean isTieBreakinRegularSet() {
        return tieBreakinRegularSet;
    }

    void setTieBreakinRegularSet(boolean isTieBreakRegularSet) {
        this.tieBreakinRegularSet = isTieBreakRegularSet;
    }

    void setTieBreakinFinalSet(boolean isTieBreakFinalSet) {
        this.tieBreakinFinalSet = isTieBreakFinalSet;
    }

    /*
     * counters
     */

    public boolean isNoAdvantageGameFormat() {
        return game.isNoAdvantageGameFormat();
    }

    public boolean isNewSetSwitchServePlayer() {
        return newSetSwitchServePlayer;
    }

    void setNewSetSwitchServePlayer(boolean newGameSwitchServePlayer) {
        this.newSetSwitchServePlayer = newGameSwitchServePlayer;
    }

    public int getSetsA() {
        return setsA;
    }

    void setSetsA(int setsA) {
        this.setsA = setsA;
    }

    public int getSetsB() {
        return setsB;
    }

    void setSetsB(int setsB) {
        this.setsB = setsB;
    }

    public int getGamesA() {
        return gamesA;
    }

    void setGamesA(int gamesA) {
        this.gamesA = gamesA;
    }

    public int getGamesB() {
        return gamesB;
    }

    void setGamesB(int gamesB) {
        this.gamesB = gamesB;
    }

    public Game getGame() {
        return game;
    }

    void setGame(Game game) {
        this.game = game;
    }

    public int getNoSetsInMatch() {
        return nSetsInMatch;
    }

    public void setNoSetsInMatch(int nSetsInMatch) {
        this.nSetsInMatch = nSetsInMatch;
    }

    public TieBreak getTieBreak() {
        return tieBreak;
    }

    void setTieBreak(TieBreak tieBreak) {
        this.tieBreak = tieBreak;
    }

    public SuperTieBreak getSuperTieBreak() {
        return superTieBreak;
    }

    void setSuperTieBreak(SuperTieBreak superTieBreak) {
        this.superTieBreak = superTieBreak;
    }

    /**
     * returns the id of the player currently on serve
     *
     * @return
     */

    public PlayerId getOnServePlayerNow() {
        PlayerId id = null;
        if (getOnServeTeamNow() != null) {
            switch (getOnServeTeamNow()) {
                case A:
                    if (getOnServePlayerTeamANow() == 1)
                        id = PlayerId.A1;
                    else if (getOnServePlayerTeamANow() == 2)
                        id = PlayerId.A2;
                    else
                        id = PlayerId.UNKNOWN;
                    break;
                case B:
                    if (getOnServePlayerTeamBNow() == 1)
                        id = PlayerId.B1;
                    else if (getOnServePlayerTeamBNow() == 2)
                        id = PlayerId.B2;
                    else
                        id = PlayerId.UNKNOWN;
                    break;
                case UNKNOWN:
                    id = PlayerId.UNKNOWN;
                    break;
            }
        } else
            id = PlayerId.UNKNOWN;

        return id;
    }


    int getOnServePlayerTeamANow() {
        if (inTieBreak) {
            if (inSuperTieBreak)
                return superTieBreak.onServePlayerTeamANow;
            else
                return tieBreak.onServePlayerTeamANow;
        } else {
            return game.onServePlayerTeamANow;
        }

    }


    int getOnServePlayerTeamBNow() {
        if (inTieBreak)
            if (inSuperTieBreak)
                return superTieBreak.onServePlayerTeamBNow;
            else
                return tieBreak.onServePlayerTeamBNow;
        else {
            return game.onServePlayerTeamBNow;
        }

    }

    void setOnServePlayerTeamANow(int player) {
        if (inTieBreak)
            if (inSuperTieBreak)
                superTieBreak.onServePlayerTeamANow = player;
            else
                tieBreak.onServePlayerTeamANow = player;
        else {
            game.onServePlayerTeamANow = player;
        }
    }

    void setOnServePlayerTeamBNow(int player) {
        if (inTieBreak) {
            if (inSuperTieBreak)
                superTieBreak.onServePlayerTeamBNow = player;
            else
                tieBreak.onServePlayerTeamBNow = player;
        } else {
            game.onServePlayerTeamBNow = player;
        }
    }

    public TeamId getOnServeTeamNow() {
        if (inTieBreak) {
            if (inSuperTieBreak)
                return superTieBreak.onServeSideNow;
            else
                return tieBreak.onServeSideNow;
        } else
            return game.onServeSideNow;
    }

    // used only for current json message
    public TeamId getOnServeNow() {
        if (inTieBreak) {
            if (inSuperTieBreak)
                return superTieBreak.onServeSideNow;
            else
                return tieBreak.onServeSideNow;
        } else
            return game.onServeSideNow;
    }

    void setOnServeSideNow(TeamId id) {
        if (inTieBreak)
            if (inSuperTieBreak)
                superTieBreak.onServeSideNow = id;
            else
                tieBreak.onServeSideNow = id;
        else
            game.onServeSideNow = id;
    }

    /**
     *
     * @return if break point still not happend and wether it is possible
     */

    public boolean isBreakPointStillPossible() {
        if ((this.isInTieBreak() && this.isInFinalSet()) || !this.getTeamBreakFirst().equals(TeamId.UNKNOWN)
                        || this.isInSuperTieBreak())
            return false;

        return true;
    }

    /**
     *
     * @return set no in range 1-5
     */

    public int getSetNo() {
        return setsA + setsB + 1;
    }

    /**
     *
     * @return game no starting at 1 for first game of set
     */

    public int getGameNo() {
        return gamesA + gamesB + 1;
    }

    /**
     *
     * @return point no starting at 1 for first point of game or tie break
     */

    public int getPointNo() {
        int pointNo;
        if (inTieBreak) {
            if (inSuperTieBreak)
                pointNo = superTieBreak.pointsA + superTieBreak.pointsB;
            else
                pointNo = tieBreak.pointsA + tieBreak.pointsB;
        } else {
            pointNo = game.pointNo;
        }
        return pointNo + 1;
    }

    /**
     *
     * @param i i = 0 for the first set
     * @return
     */
    public PairOfIntegers getGameScoreInSetN(int i) {
        if (i < matchFormat.getSetsPerMatch())
            return gameScoreInSetN[i];
        else
            return null;
    }

    /**
     *
     * @param setNo =0 for first set
     * @param gamesA
     * @param gamesB
     */
    void setGameScoreInSetN(int setNo, int gamesA, int gamesB) {
        gameScoreInSetN[setNo].A = gamesA;
        gameScoreInSetN[setNo].B = gamesB;
    }

    public boolean isInTieBreak() {
        return inTieBreak;
    }

    /**
     * returns current points for A
     *
     * @return
     */
    public int getPointsA() {
        if (inTieBreak)
            if (inSuperTieBreak)
                return superTieBreak.pointsA;
            else
                return tieBreak.pointsA;
        else
            return game.pointsA;
    }

    /**
     * returns current points for B
     *
     * @return
     */
    public int getPointsB() {
        if (inTieBreak)
            if (inSuperTieBreak)
                return superTieBreak.pointsB;
            else
                return tieBreak.pointsB;
        else
            return game.pointsB;
    }

    public boolean isPreMatch() {
        return getOnServeTeamNow() == TeamId.UNKNOWN;
    }

    /**
     * returns true if in a tiebreak now or one has been played in sets alredy completed
     *
     * @return
     */
    boolean isTieBreakAlreadyPlayedInMatch() {
        if (inTieBreak && !finalSetSuperTieBreak)
            return true;
        int setTotal = this.getSetNo();
        if (this.isMatchCompleted())
            setTotal = setTotal - 1;
        for (int i = 0; i < setTotal; i++) {
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

    boolean isAlreadyWonASetA() {

        return setsA > 0;
    }

    /**
     * returns true if B has already won a set in the match
     *
     * @return
     */

    boolean isAlreadyWonASetB() {

        return setsB > 0;

    }

    /**
     * returns true of either player has scored 5 or more games in the current set
     *
     * @return
     */

    boolean isAlreadyOver4GamesInCurrentSet() {
        return (gamesA >= 5 || gamesB >= 5);
    }

    /**
     * returns true if the game with No n (starting at 1 for first game in set) has not already been played and might be
     *
     * @param n
     * @return
     */
    public boolean isGameMayBePlayed(int n) {
        return n >= getGameNo() && (inAdvantageSet() || n <= nGamesInMatch * 2);
    }

    /**
     * returns true if the Set with No n (starting at 1 for first set) has not already been played and might be
     *
     * @param n
     * @return
     */
    boolean isSetMayBePlayed(int n) {
        return n >= getSetNo() && n <= nSetsInMatch;
    }

    /**
     * returns true if point no n (starting at 1 for first point in game) might be played
     *
     * @param n
     * @return
     */
    boolean isPointMayBePlayed(int n) {
        return n >= getPointNo();

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

    /**
     * class constructor
     *
     * @param matchFormat
     */
    public TennisMatchState(MatchFormat format) {
        super();
        TennisMatchFormat matchFormat = (TennisMatchFormat) format;
        nGamesInMatch = 6;
        if (matchFormat.getTournamentLevel() == TournamentLevel.ATP_NEXTGEN)
            nGamesInMatch = 3;
        suspensionStatus = new SuspensionStatus();
        mostRecentMatchIncidentResult = new TennisMatchIncidentResult(TennisMatchIncidentResultType.PREMATCH);

        if (!matchFormat.getSex().equals(TennisMatchFormat.Sex.MIXED)) {
            GenderId genderId;
            if (matchFormat.getSex().equals(TennisMatchFormat.Sex.MEN))
                genderId = GenderId.MALE;
            else
                genderId = GenderId.FEMALE;

            genderMap = new EnumMap<PlayerId, GenderId>(PlayerId.class);

            genderMap.put(PlayerId.A1, genderId);
            genderMap.put(PlayerId.B1, genderId);
            if (this.doublesMatch) {
                genderMap.put(PlayerId.A2, genderId);
                genderMap.put(PlayerId.B2, genderId);
            }
        } else {
            genderMap = new EnumMap<PlayerId, GenderId>(PlayerId.class);
        }

        this.matchFormat = matchFormat;
        this.doublesMatch = matchFormat.isDoublesMatch();
        this.newSetSwitchServePlayer = false;
        this.finalSetSuperTieBreak = false;
        this.tieBreakinFinalSet = true;
        this.tieBreakinRegularSet = true;
        this.previousGameScore = PairOfIntegers.generateFromString("0-0");
        game = new Game(matchFormat.isNoAdvantageGameFormat());
        tieBreak = new TieBreak(doublesMatch, matchFormat.isNoAdvantageTieBreakFormat());
        superTieBreak = new SuperTieBreak(doublesMatch);
        this.nSetsInMatch = matchFormat.getSetsPerMatch();
        setScore(0, 0, 0, 0, TeamId.UNKNOWN, 0, 0);
        gameScoreInSetN = new PairOfIntegers[nSetsInMatch];
        this.firstServingBreakInSetN = new int[nSetsInMatch];
        this.firstServingBreakAInSetN = new int[nSetsInMatch];
        this.firstServingBreakBInSetN = new int[nSetsInMatch];
        for (int i = 0; i < nSetsInMatch; i++)
            gameScoreInSetN[i] = new PairOfIntegers();
        matchWinningSetScore = nSetsInMatch / 2 + 1;
        switch (matchFormat.getFinalSetType()) {
            case ADVANTAGE_SET:
                this.tieBreakinFinalSet = false;
                break;
            case CHAMPIONSHIP_TIE_BREAK:
                this.finalSetSuperTieBreak = true;
                this.finalSetSuperTBNo = 10;
                break;
            case NORMAL_WITH_TIE_BREAK:
                break;
            default:
                break;

        }

        tieBreak.setStoreLastGameScore(
                        (StoreLastGameScore & Serializable) (int a, int b) -> this.storeLastGameScore(a, b));
        superTieBreak.setStoreLastGameScore(
                        (StoreLastGameScore & Serializable) (int a, int b) -> this.storeLastGameScore(a, b));

    }

    // required for JSON serialisation
    public TennisMatchState() {
        this(new TennisMatchFormat());
    }

    /**
     * sets the starting score. Starting points assumed to be zero
     *
     * @param setsA
     * @param setsB
     * @param gamesA
     * @param gamesB
     * @param onServeSideNow may be A,B or unknown
     * @param onServePlayerTeamANow set current player who serves for team A may be 1 or 2, in single tennis matches
     *        always set to 1
     * 
     * @param onServePlayerTeamANow set current player who serves for team B may be 1 or 2, in single tennis matches
     *        always set to 1
     * 
     */

    boolean checkTieBreaks(int setsA, int setsB, int gamesA, int gamesB, int pointsA, int pointsB,
                    boolean inFinalSetNow) {
        boolean out = false;

        if (inFinalSetNow) {
            if (!finalSetSuperTieBreak) {
                out = gamesA == nGamesInMatch && gamesB == nGamesInMatch && tieBreakinFinalSet && finalSetSuperTBNo == 0
                                && (pointsA == 7 || pointsB == 7);
            } else {
                out = gamesA == nGamesInMatch && gamesB == nGamesInMatch && tieBreakinFinalSet && finalSetSuperTBNo == 0
                                && (pointsA == 10 || pointsB == 10);
                inSuperTieBreak = finalSetSuperTieBreak;
            }
        } else {
            out = ((gamesA == (nGamesInMatch + 1) && gamesB == nGamesInMatch)
                            || (gamesA == (nGamesInMatch) && gamesB == (nGamesInMatch + 1))) && tieBreakinRegularSet
                            && (pointsA == 7 || pointsB == 7);
            if (finalSetSuperTieBreak && inFinalSet) {
                inSuperTieBreak = finalSetSuperTieBreak;
            }
        }
        return out;
    }

    public void setScore(int setsA, int setsB, int gamesA, int gamesB, TeamId onServeSideNow, int onServePlayerTeamANow,
                    int onServePlayerTeamBNow) {
        this.setsA = setsA;
        this.setsB = setsB;
        this.gamesA = gamesA;
        this.gamesB = gamesB;

        updateIfInFinalSetNow();
        if (inFinalSet) {
            if (!finalSetSuperTieBreak) {
                inTieBreak = gamesA == nGamesInMatch && gamesB == nGamesInMatch && tieBreakinFinalSet
                                && finalSetSuperTBNo == 0;
            } else {
                inTieBreak = true;
                inSuperTieBreak = finalSetSuperTieBreak;
            }
        } else {
            inTieBreak = gamesA == nGamesInMatch && gamesB == nGamesInMatch && tieBreakinRegularSet;
        }

        if (inTieBreak && !inSuperTieBreak) {
            tieBreak.onServeSideNow = onServeSideNow;
            tieBreak.onServePlayerTeamANow = onServePlayerTeamANow;
            tieBreak.onServePlayerTeamBNow = onServePlayerTeamBNow;
        } else if (inTieBreak && inSuperTieBreak) {
            superTieBreak.onServeSideNow = onServeSideNow;
            superTieBreak.onServePlayerTeamANow = onServePlayerTeamANow;
            superTieBreak.onServePlayerTeamBNow = onServePlayerTeamBNow;
        } else {
            game.onServeSideNow = onServeSideNow;
            game.onServePlayerTeamANow = onServePlayerTeamANow;
            game.onServePlayerTeamBNow = onServePlayerTeamBNow;
        }
        this.game.onServeSideNow = onServeSideNow;
        this.game.onServePlayerTeamANow = onServePlayerTeamANow;
        this.game.onServePlayerTeamBNow = onServePlayerTeamBNow;

        game.startNewGame(onServeSideNow, onServePlayerTeamANow, onServePlayerTeamBNow);
        tieBreak.startNewTieBreak(onServeSideNow, onServePlayerTeamANow, onServePlayerTeamBNow);
        superTieBreak.startNewSuperTieBreak(onServeSideNow, onServePlayerTeamANow, onServePlayerTeamBNow);
    }

    // Jin resp: For updating the indicator if the current set is final set?
    private void updateIfInFinalSetNow() {
        inFinalSet = setsA + setsB == matchFormat.getSetsPerMatch() - 1;
    }

    public boolean isNextSetFinalSet() {
        return setsA + setsB == matchFormat.getSetsPerMatch() - 2;
    }

    @Override
    public void setEqualTo(MatchState ms) {
        /*
         * check to see if actually the same object. If yes then nothing to do
         */
        if (this == ms)
            return;
        super.setEqualTo(ms);
        TennisMatchState ts = (TennisMatchState) ms;
        this.setFaultFlag(ts.isFaultFlag());
        this.setScore(ts.getSetsA(), ts.getSetsB(), ts.getGamesA(), ts.getGamesB(), ts.getOnServeTeamNow(),
                        ts.getOnServePlayerTeamANow(), ts.getOnServePlayerTeamBNow());
        this.getGame().setEqualTo(ts.getGame());
        this.getTieBreak().setEqualTo(ts.getTieBreak());
        this.getSuperTieBreak().setEqualTo(ts.getSuperTieBreak());
        this.matchWinningSetScore = ((TennisMatchFormat) ms.getMatchFormat()).getSetsPerMatch() / 2 + 1;
        this.lastGameOrTieBreakPointsA = ts.getLastGameOrTieBreakPointsA();
        this.lastGameOrTieBreakPointsB = ts.getLastGameOrTieBreakPointsB();
        this.lastPointPlayedOutcome = ts.getLastPointPlayedOutcome();
        this.lastGamePlayedOutcome = ts.getLastGamePlayedOutcome();
        this.newSetSwitchServePlayer = ts.isNewSetSwitchServePlayer();
        this.inTieBreak = ts.isInTieBreak();
        this.inSuperTieBreak = ts.isInSuperTieBreak();
        this.tieBreakinRegularSet = ts.tieBreakinRegularSet;
        this.doublesMatch = ts.isDoubleMatch();
        this.breakPoint = ts.isBreakPoint();
        this.gamePoint = ts.isGamePoint();
        this.dangerPoint = ts.isDangerPoint();
        this.inFinalSet = ts.isInFinalSet();
        this.nSetsInMatch = ts.nSetsInMatch;
        this.nGamesInMatch = ts.nGamesInMatch;
        this.finalSetSuperTieBreak = ts.finalSetSuperTieBreak;
        this.teamBreakFirst = ts.getTeamBreakFirst();
        this.lastIncidentDetails = ts.getLastIncidentDetails().copy();
        this.suspensionStatus = ts.getSuspensionStatus().copy();
        this.firstServingBreakInSetN =
                        Arrays.copyOf(ts.getFirstServingBreakInSetN(), ts.getFirstServingBreakInSetN().length);
        this.firstServingBreakAInSetN =
                        Arrays.copyOf(ts.getFirstServingBreakAInSetN(), ts.getFirstServingBreakAInSetN().length);
        this.firstServingBreakBInSetN =
                        Arrays.copyOf(ts.getFirstServingBreakBInSetN(), ts.getFirstServingBreakBInSetN().length);

        this.setTieBreakinFinalSet(ts.isTieBreakinFinalSet());
        this.setFinalSetSuperTBNo(ts.getFinalSetSuperTBNo());
        this.setDoubletennisMatchIncident(ts.getDoubletennisMatchIncident());
        this.setTieBreaksCounter(ts.getTieBreaksCounter());
        for (int i = 0; i < matchFormat.getSetsPerMatch(); i++) {
            if (i < ts.getGamesScoreMap().size()) {
                PairOfIntegers s = ts.getGameScoreInSetN(i);
                this.gameScoreInSetN[i].A = s.A;
                this.gameScoreInSetN[i].B = s.B;
            } else {
                PairOfIntegers s = PairOfIntegers.generateFromString("0-0");
                this.gameScoreInSetN[i].A = s.A;
                this.gameScoreInSetN[i].B = s.B;
            }

        }

        for (Entry<PlayerId, GenderId> entry : ts.getGenderMap().entrySet()) {
            this.genderMap.put(entry.getKey(), entry.getValue());
        }
        this.setStartOfMatchServer(ts.getStartOfMatchServer());
        this.setOnServePlayerInFirstGameOfSet(ts.getOnServePlayerInFirstGameOfSet());
        this.setOnServePlayerInSecondGameOfSet(ts.getOnServePlayerInSecondGameOfSet());
        this.setTeamBreakFirst(ts.getTeamBreakFirst());
        this.setPowerPointCurrentPoint(ts.isPowerPointCurrentPoint());
        this.setServeNumber(ts.getServeNumber());
        this.previousGameScore = PairOfIntegers
                        .generateFromString(ts.getPreviousGameScore().A + "-" + ts.getPreviousGameScore().B);
        this.game.setPreviousGameScore(ts.game.getPreviousGameScore());
        this.tieBreak.setStoreLastGameScore(
                        (StoreLastGameScore & Serializable) (int a, int b) -> ts.storeLastGameScore(a, b));
        this.superTieBreak.setStoreLastGameScore(
                        (StoreLastGameScore & Serializable) (int a, int b) -> ts.storeLastGameScore(a, b));

        if (ts.getMostRecentMatchIncidentResult() != null)
            this.setMostRecentMatchIncidentResult(ts.getMostRecentMatchIncidentResult().clone());
    }

    @Override
    public MatchState copy() {
        TennisMatchState cc = new TennisMatchState(this.matchFormat);
        cc.setEqualTo(this);
        return cc;
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
        mostRecentMatchIncidentResult = null;
        lastIncidentDetails.setLastIncident(doubletennisMatchIncident);
        if (matchIncident instanceof AbandonMatchIncident) {
            lastIncidentDetails.setLastIncident(null);
            mostRecentMatchIncidentResult = new TennisMatchIncidentResult(TennisMatchIncidentResultType.MATCHABANDONED);
            mostRecentMatchIncidentResult.setAbandonMatchWinner(matchAbandonedWonTeam);
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        }
        if ((matchIncident instanceof ElapsedTimeMatchIncident)) {
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        }
        if ((matchIncident instanceof DatafeedMatchIncident)) {
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        }
        doubletennisMatchIncident = (TennisMatchIncident) matchIncident;
        suspensionStatus.updateSuspension(doubletennisMatchIncident);
        // FIXME: group update last incident and place it before every returns;
        lastIncidentDetails.setLastIncident(doubletennisMatchIncident);
        lastIncidentDetails.setPowerPoint(isPowerPointCurrentPoint());
        lastIncidentDetails.setServingPlayer(getOnServePlayerNow());
        lastIncidentDetails.setServingTeam(getOnServeNow());
        lastIncidentDetails.setPointNo(getPointNo());
        lastIncidentDetails.setServeNo(getServeNumber());
        lastIncidentDetails.setPointResult(doubletennisMatchIncident.getPointResult());
        // ONLY increment serveNumber after saved to the lastIncidentDetails, if
        // it is a double fault, still reset
        // serveNumber
        if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.FAULT && serveNumber == 1)
            serveNumber = 2;
        else
            serveNumber = 1;

        TennisMatchIncidentResult doubletennisMatchIncidentResult;
        GameOrTieBreakEventResult gameEventResult = null;
        TennisMatchStateFromFeed stateFromFeed = null;
        if (doubletennisMatchIncident.getTennisMatchStateFromFeed() != null)
            stateFromFeed = doubletennisMatchIncident.getTennisMatchStateFromFeed().copy();
        // FIXME: A issue reported only happening when it is a tie break, feed
        // some times provide eg. 0-0,6-6,4-7
        // scores, need the below logic to tidy up the feed state.
        boolean isDataFeedStateTieBreakResult = false;
        boolean inFinalSetCurrently = inFinalSet;
        boolean inSuperTieBreakCurrently = inSuperTieBreak;
        TennisMatchIncidentResultType feedMatchIncidentResultType = TennisMatchIncidentResultType.POINTWONONLY;
        if (stateFromFeed != null) {
            inFinalSet = (stateFromFeed.getSetsA() + stateFromFeed.getSetsB()) == (matchFormat.getSetsPerMatch() - 1);
            isDataFeedStateTieBreakResult = checkTieBreaks(stateFromFeed.getSetsA(), stateFromFeed.getSetsB(),
                            stateFromFeed.getGamesA(), stateFromFeed.getGamesB(), stateFromFeed.getPointsA(),
                            stateFromFeed.getPointsB(), inFinalSetCurrently);
            if (isDataFeedStateTieBreakResult) {
                // int gA= stateFromFeed.getGamesA();
                // int gB= stateFromFeed.getGamesB();
                int sA = stateFromFeed.getSetsA();
                int sB = stateFromFeed.getSetsB();

                if (shouldUpdateSetWonAtTieBreak(stateFromFeed.getPointsA(), stateFromFeed.getPointsB(),
                                this.matchFormat.isNoAdvantageTieBreakFormat())) { // assume
                                                                                   // point/set
                                                                                   // won
                                                                                   // by
                                                                                   // A
                                                                                   // stateFromFeed.getPointsA()
                                                                                   // ==
                                                                                   // 7
                    if (sA <= this.getSetsA())
                        sA++;
                    stateFromFeed = new TennisMatchStateFromFeed(sA, sB, 0, 0, 0, 0, TeamId.A);
                    if ((sA == matchWinningSetScore) || (sB == matchWinningSetScore))
                        feedMatchIncidentResultType = TennisMatchIncidentResultType.MATCHWONA;
                    else
                        feedMatchIncidentResultType = TennisMatchIncidentResultType.SETWON;

                } else if (shouldUpdateSetWonAtTieBreak(stateFromFeed.getPointsB(), stateFromFeed.getPointsA(),
                                this.matchFormat.isNoAdvantageTieBreakFormat())) {
                    if (sB <= this.getSetsB())
                        sB++;
                    stateFromFeed = new TennisMatchStateFromFeed(sA, sB, 0, 0, 0, 0, TeamId.B);
                    if ((sA == matchWinningSetScore) || (sB == matchWinningSetScore))
                        feedMatchIncidentResultType = TennisMatchIncidentResultType.MATCHWONB;
                    else
                        feedMatchIncidentResultType = TennisMatchIncidentResultType.SETWON;
                }
                // else do nothing
            } else if (isInSuperTieBreak()) {
                int sA = stateFromFeed.getSetsA();
                int sB = stateFromFeed.getSetsB();

                if (stateFromFeed.getPointsA() == 10) { // assume point/set won
                                                        // by A
                    sA++;
                    stateFromFeed = new TennisMatchStateFromFeed(sA, sB, 0, 0, 0, 0, TeamId.A);
                } else if (stateFromFeed.getPointsB() == 10) {
                    sB++;
                    stateFromFeed = new TennisMatchStateFromFeed(sA, sB, 0, 0, 0, 0, TeamId.B);
                }
                // else do nothing
            }

            // Provisional solution to end the match
            if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.MATCH_END) {
                // Update score board only
                TennisMatchIncidentResultType type;
                if (stateFromFeed.getSetsA() > stateFromFeed.getSetsB()) {
                    type = TennisMatchIncidentResultType.MATCHWONA;
                } else if (stateFromFeed.getSetsA() < stateFromFeed.getSetsB()) {
                    type = TennisMatchIncidentResultType.MATCHWONB;
                } else {
                    type = null;
                    throw new IllegalArgumentException("Match State From Feeds can not end a match ");
                }
                TennisMatchIncidentResult doubletennisMatchIncidentResultME = new TennisMatchIncidentResult(type);

                super.setDatafeedStateMismatch(
                                mismatchWithStateFromFeed(doubletennisMatchIncident.getTennisMatchStateFromFeed(),
                                                doubletennisMatchIncidentResultME));
                copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);
                return (MatchIncidentResult) doubletennisMatchIncidentResultME;
            }
        }
        faultFlag = false;
        if (doubletennisMatchIncident.getIncidentSubType().equals(TennisMatchIncidentType.INJURY_STATUS_REVERSE)) {
            doubletennisMatchIncidentResult =
                            new TennisMatchIncidentResult(TennisMatchIncidentResultType.INJURY_STATUS_REVERSE);

            DatafeedMatchIncidentType status = this.getDataFeedStatus();
            if (status == DatafeedMatchIncidentType.OK)
                this.setDataFeedStatus(DatafeedMatchIncidentType.BET_STOP);
            else
                this.setDataFeedStatus(DatafeedMatchIncidentType.OK);
        } else if (doubletennisMatchIncident.getIncidentSubType().equals(TennisMatchIncidentType.FAULT)) {
            // do nothing for us, but for ps
            faultFlag = true;
            if (doubletennisMatchIncident.getSourceSystem() != null)
                if (!isInTieBreak() && doubletennisMatchIncident.getSourceSystem().equals("IMG")) {
                    updateDangerPointStatus();
                }
            doubletennisMatchIncidentResult = new TennisMatchIncidentResult(TennisMatchIncidentResultType.FAULT);

        } else if (doubletennisMatchIncident.getIncidentSubType().equals(TennisMatchIncidentType.POINT_START)) {
            // do nothing for us, but for ppb
            doubletennisMatchIncidentResult = new TennisMatchIncidentResult(TennisMatchIncidentResultType.POINT_START);

        } else if (doubletennisMatchIncident.getIncidentSubType().equals(TennisMatchIncidentType.CHALLENGER_BALLMARK)) {
            // do nothing for us, but for ppb
            doubletennisMatchIncidentResult =
                            new TennisMatchIncidentResult(TennisMatchIncidentResultType.CHALLENGER_BALLMARK);
            if (isGamePoint()) {
                dangerPoint = true;
            } else
                dangerPoint = false;

        } else {

            /*
             * On Serve Player Info
             */
            @SuppressWarnings("unused")
            boolean pointWonAndServingIncidence = checkAndSetIfDelayedServingOrder(doubletennisMatchIncident);
            /*
             * breakpoint information for the trading rules.
             */
            breakPoint = false;
            gamePoint = false;
            dangerPoint = false;
            // in case order and won points feed comes together, boolean type
            // prepared to be used in future.
            if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.MATCH_STARTING) {
                if (doublesMatch) {

                    // FIXME: hack the code, only for ppb before feeds issue
                    // solved, this should be removed in the
                    // future
                    if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 0) {
                        doubletennisMatchIncident.setServerPlayerAtStartOfMatch(1);
                    }

                    if (onServePlayerInFirstGameOfSet == PlayerId.UNKNOWN) {
                        if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.A) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 1)
                                this.onServePlayerInFirstGameOfSet = PlayerId.A1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 2)
                                this.onServePlayerInFirstGameOfSet = PlayerId.A2;

                        }

                        if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.B) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 1)
                                this.onServePlayerInFirstGameOfSet = PlayerId.B1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 2)
                                this.onServePlayerInFirstGameOfSet = PlayerId.B2;
                        }
                    } else if (onServePlayerInFirstGameOfSet != PlayerId.UNKNOWN
                                    && onServePlayerInSecondGameOfSet == PlayerId.UNKNOWN) {
                        if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.A) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 1)
                                this.onServePlayerInSecondGameOfSet = PlayerId.A1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 2)
                                this.onServePlayerInSecondGameOfSet = PlayerId.A2;
                        }

                        if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.B) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 1)
                                this.onServePlayerInSecondGameOfSet = PlayerId.B1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfMatch() == 2)
                                this.onServePlayerInSecondGameOfSet = PlayerId.B2;
                        }

                    }

                } else { // single games
                    if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.A) {
                        this.onServePlayerInFirstGameOfSet = PlayerId.A1;
                        this.onServePlayerInSecondGameOfSet = PlayerId.B1;
                    }

                    if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.B) {
                        this.onServePlayerInFirstGameOfSet = PlayerId.B1;
                        this.onServePlayerInSecondGameOfSet = PlayerId.A1;
                    }

                }

            } else if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.SERVING_ORDER) {
                if (doublesMatch) {

                    // FIXME: hack the code, only for ppb before feeds issue
                    // solved, this should be removed in the
                    // future
                    if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 0) {
                        doubletennisMatchIncident.setServerPlayerAtStartOfCurrentGame(1);
                    }

                    if (onServePlayerInFirstGameOfSet == PlayerId.UNKNOWN) {
                        if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.A) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 1)
                                this.onServePlayerInFirstGameOfSet = PlayerId.A1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 2)
                                this.onServePlayerInFirstGameOfSet = PlayerId.A2;

                        }

                        if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.B) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 1)
                                this.onServePlayerInFirstGameOfSet = PlayerId.B1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 2)
                                this.onServePlayerInFirstGameOfSet = PlayerId.B2;
                        }
                    } else if (onServePlayerInFirstGameOfSet != PlayerId.UNKNOWN
                                    && onServePlayerInSecondGameOfSet == PlayerId.UNKNOWN) {
                        if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.A) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 1)
                                this.onServePlayerInSecondGameOfSet = PlayerId.A1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 2)
                                this.onServePlayerInSecondGameOfSet = PlayerId.A2;
                        }

                        if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.B) {
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 1)
                                this.onServePlayerInSecondGameOfSet = PlayerId.B1;
                            if (doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame() == 2)
                                this.onServePlayerInSecondGameOfSet = PlayerId.B2;
                        }

                    }

                } else { // single games
                    if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.A) {
                        this.onServePlayerInFirstGameOfSet = PlayerId.A1;
                        this.onServePlayerInSecondGameOfSet = PlayerId.B1;
                    }

                    if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.B) {
                        this.onServePlayerInFirstGameOfSet = PlayerId.B1;
                        this.onServePlayerInSecondGameOfSet = PlayerId.A1;
                    }

                }

            } else if (onServePlayerInFirstGameOfSet == PlayerId.UNKNOWN) {
                if (!doublesMatch) {
                    // single games USED TO do nothing, fix for ticket
                    // implemented
                    if (game.onServeSideNow == TeamId.A) {
                        this.onServePlayerInFirstGameOfSet = PlayerId.A1;
                        this.onServePlayerInSecondGameOfSet = PlayerId.B1;
                        this.game.onServePlayerTeamANow = 1;
                        this.game.onServePlayerTeamBNow = 1;
                    } else if (game.onServeSideNow == TeamId.B) {
                        this.onServePlayerInFirstGameOfSet = PlayerId.B1;
                        this.onServePlayerInSecondGameOfSet = PlayerId.A1;
                        this.game.onServePlayerTeamANow = 1;
                        this.game.onServePlayerTeamBNow = 1;
                    }
                } else {
                    if (game.onServeSideNow == TeamId.A) {
                        if (game.onServePlayerTeamANow == 1)
                            this.onServePlayerInFirstGameOfSet = PlayerId.A1;
                        if (game.onServePlayerTeamANow == 2)
                            this.onServePlayerInFirstGameOfSet = PlayerId.A2;
                    } else if (game.onServeSideNow == TeamId.B) {
                        if (game.onServePlayerTeamBNow == 1)
                            this.onServePlayerInFirstGameOfSet = PlayerId.B1;
                        if (game.onServePlayerTeamBNow == 2)
                            this.onServePlayerInFirstGameOfSet = PlayerId.B2;
                    }

                }
            } else if (onServePlayerInFirstGameOfSet != PlayerId.UNKNOWN
                            && onServePlayerInSecondGameOfSet == PlayerId.UNKNOWN && (gamesA + gamesB == 1)) {
                if (game.onServeSideNow == TeamId.A) {
                    if (game.onServePlayerTeamANow == 1)
                        this.onServePlayerInSecondGameOfSet = PlayerId.A1;
                    if (game.onServePlayerTeamANow == 2)
                        this.onServePlayerInSecondGameOfSet = PlayerId.A2;
                } else if (game.onServeSideNow == TeamId.B) {
                    if (game.onServePlayerTeamBNow == 1)
                        this.onServePlayerInSecondGameOfSet = PlayerId.B1;
                    if (game.onServePlayerTeamBNow == 2)
                        this.onServePlayerInSecondGameOfSet = PlayerId.B2;
                }

            }

            /*
             * set startOfMatchServe
             */
            if (startOfMatchServer == PlayerId.UNKNOWN) {
                if (this.setsA + this.setsB == 0) {
                    startOfMatchServer = onServePlayerInFirstGameOfSet;
                }
            } else {
                if ((this.setsA + this.setsB == 0) && (this.gamesA + this.gamesB == 0) && (getPointNo() < 2)) {
                    startOfMatchServer = onServePlayerInFirstGameOfSet;
                }
            }

            /*
             * check for start of match
             */
            if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.MATCH_STARTING) {
                setOnServeSideNow(doubletennisMatchIncident.getServerSideAtStartOfMatch());

                if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.A) {
                    setOnServePlayerTeamANow(doubletennisMatchIncident.getServerPlayerAtStartOfMatch());

                    if (!doublesMatch) {
                        setOnServePlayerTeamBNow(1);
                    }
                } else if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.B) {
                    setOnServePlayerTeamBNow(doubletennisMatchIncident.getServerPlayerAtStartOfMatch());
                    if (!doublesMatch) {
                        setOnServePlayerTeamANow(1);
                    }
                } else {
                    throw new IllegalArgumentException(" should not go here");
                }
                TennisMatchIncidentResult result =
                                new TennisMatchIncidentResult(TennisMatchIncidentResultType.FIRSTSERVERINMATCHSET);
                super.setDatafeedStateMismatch(mismatchWithStateFromFeed(
                                doubletennisMatchIncident.getTennisMatchStateFromFeed(), result));

                /**
                 * only instiance if doubl mix
                 **/
                if (matchFormat.getSex().equals(Sex.MIXED)) {
                    if (this.genderMap == null)
                        genderMap = new EnumMap<PlayerId, GenderId>(PlayerId.class);

                    PlayerId playerKey = this.getOnServePlayerNow();
                    if (genderMap.get(playerKey) == null) {
                        GenderId genderId = doubletennisMatchIncident.getGenderId();
                        genderMap.put(playerKey, genderId);

                        PlayerId oppositPlayerId = (PlayerId) getItemOpposit(playerKey);
                        if (oppositPlayerId == null) {
                            throw new IllegalArgumentException(
                                            "oppositPlayerId is null, current player key" + playerKey);
                        }
                        genderMap.put(oppositPlayerId, (GenderId) getItemOpposit(genderId));
                    }
                }
                copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);
                return result;
            } else if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.SERVING_ORDER) {
                setOnServeSideNow(doubletennisMatchIncident.getServerSideAtStartOfCurrentGame());

                if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.A) {
                    setOnServePlayerTeamANow(doubletennisMatchIncident.getServerPlayerAtStartOfCurrentGame());

                    if (!doublesMatch) {
                        setOnServePlayerTeamBNow(1);
                    }
                } else if (doubletennisMatchIncident.getServerSideAtStartOfCurrentGame() == TeamId.B) {
                    setOnServePlayerTeamBNow(doubletennisMatchIncident.getServerPlayerAtStartOfMatch());
                    if (!doublesMatch) {
                        setOnServePlayerTeamANow(1);
                    }
                } else {
                    throw new IllegalArgumentException(" should not go here");
                }
                TennisMatchIncidentResult result =
                                new TennisMatchIncidentResult(TennisMatchIncidentResultType.FIRSTSERVERINMATCHSET);
                super.setDatafeedStateMismatch(mismatchWithStateFromFeed(
                                doubletennisMatchIncident.getTennisMatchStateFromFeed(), result));

                /**
                 * only instiance if doubl mix
                 **/
                if (matchFormat.getSex().equals(Sex.MIXED)) {
                    if (this.genderMap == null)
                        genderMap = new EnumMap<PlayerId, GenderId>(PlayerId.class);

                    PlayerId playerKey = this.getOnServePlayerNow();
                    if (genderMap.get(playerKey) == null) {
                        GenderId genderId = doubletennisMatchIncident.getGenderId();
                        genderMap.put(playerKey, genderId);

                        PlayerId oppositPlayerId = (PlayerId) getItemOpposit(playerKey);
                        if (oppositPlayerId == null) {
                            throw new IllegalArgumentException(
                                            "oppositPlayerId is null, current player key" + playerKey);
                        }
                        genderMap.put(oppositPlayerId, (GenderId) getItemOpposit(genderId));
                    }
                }
                copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);
                return result;
            } else if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.HEAT_DELAY
                            || doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.TOILET_BREAK
                            || doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.RAIN
                            || doubletennisMatchIncident
                                            .getIncidentSubType() == TennisMatchIncidentType.ON_COURT_COACHING
                            || doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.MEDICAL_TIMEOUT
                            || doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.CHALLENGE) {

                return new TennisMatchIncidentResult(TennisMatchIncidentResultType.DELAY);

            }

            /* if iptl power point incident */

            if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.IPTL_POWERPOINT) {
                // tempory stores power point team id in point winner param
                if (doubletennisMatchIncident.getPointWinner().equals(game.onServeSideNow)) {
                    throw new IllegalArgumentException(" Power Point Should Only Being called by Receiving Team");
                } else if (inTieBreak || inSuperTieBreak) {
                    throw new IllegalArgumentException(" No Power Point in TieBreaks");
                }
                if (matchFormat.getTournamentLevel().equals(TournamentLevel.IPTL)) {
                    game.setPowerPointCurrentPoint(true);
                } else {
                    throw new IllegalArgumentException(" Power Point Only Allowed for IPTL Matches");
                }
                TennisMatchIncidentResult result =
                                new TennisMatchIncidentResult(TennisMatchIncidentResultType.POWERPOINTSTART);
                super.setDatafeedStateMismatch(mismatchWithStateFromFeed(
                                doubletennisMatchIncident.getTennisMatchStateFromFeed(), result));
                copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);
                return result;
            }

            if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.NEW_SET_STARTING) {
                if (this.isDoubleMatch()) {
                    /*
                     * only need to set the server if playing double
                     */
                    if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.A) {
                        setOnServePlayerTeamANow(doubletennisMatchIncident.getServerPlayerAtStartOfMatch());
                    } else if (doubletennisMatchIncident.getServerSideAtStartOfMatch() == TeamId.B) {
                        setOnServePlayerTeamBNow(doubletennisMatchIncident.getServerPlayerAtStartOfMatch());
                    } else {
                        throw new IllegalArgumentException(" TeamId not specified in NEW_SET_STARTING matchIncident");
                    }
                }

                TennisMatchIncidentResult result =
                                new TennisMatchIncidentResult(TennisMatchIncidentResultType.FIRSTSERVERINMATCHSET);
                super.setDatafeedStateMismatch(mismatchWithStateFromFeed(
                                doubletennisMatchIncident.getTennisMatchStateFromFeed(), result));
                copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);
                return result;

            }

            /*
             * to get here must be point played type event
             */

            // Penalty point/game/match won here
            TeamId onServeSideNow = null;
            int onServePlayerNow = 0;
            TeamId serverSideAtStartOfGameOrTieBreak = null;
            int serverPlayerTeamAAtStartOfGameOrTieBreak = 0;
            int serverPlayerTeamBAtStartOfGameOrTieBreak = 0;

            if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.PENALTY_POINT_WON
                            || doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.POINT_WON) {
                boolean pointWonByA = doubletennisMatchIncident.getPointWinner() == TeamId.A; // FIXME:
                                                                                              // in
                                                                                              // case
                                                                                              // of
                                                                                              // penalty
                                                                                              // pointwinner
                                                                                              // can
                                                                                              // be
                                                                                              // different

                lastPointPlayedOutcome = doubletennisMatchIncident.getPointWinner();

                // TennisMatchIncidentResult doubletennisMatchIncidentResult;

                if (inTieBreak) {
                    if (!inSuperTieBreakCurrently) {
                        onServeSideNow = tieBreak.onServeSideNow;
                        if (onServeSideNow == TeamId.A) {
                            onServePlayerNow = tieBreak.onServePlayerTeamANow;
                        } else if (onServeSideNow == TeamId.B) {
                            onServePlayerNow = tieBreak.onServePlayerTeamBNow;
                        } else {
                            throw new IllegalArgumentException(" should no go here");
                        }

                        gameEventResult = tieBreak.updateForPointPlayed(pointWonByA);
                        serverSideAtStartOfGameOrTieBreak = tieBreak.onServeAtStartOfTieBreak;
                        serverPlayerTeamAAtStartOfGameOrTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
                        serverPlayerTeamBAtStartOfGameOrTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
                        lastGameOrTieBreakPointsA = tieBreak.pointsA;
                        lastGameOrTieBreakPointsB = tieBreak.pointsB;
                    } else {
                        onServeSideNow = superTieBreak.onServeSideNow;
                        if (onServeSideNow == TeamId.A) {
                            onServePlayerNow = superTieBreak.onServePlayerTeamANow;
                        } else if (onServeSideNow == TeamId.B) {
                            onServePlayerNow = superTieBreak.onServePlayerTeamBNow;
                        } else {
                            throw new IllegalArgumentException(" should no go here");
                        }
                        gameEventResult = superTieBreak.updateForPointPlayed(pointWonByA);
                        serverSideAtStartOfGameOrTieBreak = superTieBreak.onServeAtStartOfSuperTieBreak;
                        serverPlayerTeamAAtStartOfGameOrTieBreak =
                                        superTieBreak.onServePlayerTeamAAtStartOfSuperTieBreak;
                        serverPlayerTeamBAtStartOfGameOrTieBreak =
                                        superTieBreak.onServePlayerTeamAAtStartOfSuperTieBreak;
                        lastGameOrTieBreakPointsA = superTieBreak.pointsA;
                        lastGameOrTieBreakPointsB = superTieBreak.pointsB;

                    }
                } else {
                    onServeSideNow = game.onServeSideNow;
                    if (onServeSideNow.equals(TeamId.A)) {
                        onServePlayerNow = game.onServePlayerTeamANow;
                    } else if (onServeSideNow.equals(TeamId.B)) {
                        onServePlayerNow = game.onServePlayerTeamBNow;
                    } else {
                        throw new IllegalArgumentException(String
                                        .format("Invalid value of onServeSideNow.  Value is:  %s", onServeSideNow));
                    }

                    gameEventResult = game.updateForPointPlayed(pointWonByA);
                    serverSideAtStartOfGameOrTieBreak = game.onServeSideNow;

                    serverPlayerTeamAAtStartOfGameOrTieBreak = game.onServePlayerTeamANow;
                    serverPlayerTeamBAtStartOfGameOrTieBreak = game.onServePlayerTeamBNow;

                    lastGameOrTieBreakPointsA = game.pointsA;
                    lastGameOrTieBreakPointsB = game.pointsB;

                    /*
                     * Reset power point status
                     */
                    game.setPowerPointCurrentPoint(false);

                }

            } else if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.PENALTY_GAME_WON) {
                boolean wonByA = doubletennisMatchIncident.getPointWinner() == TeamId.A; // FIXME:
                                                                                         // in
                                                                                         // case
                                                                                         // of
                                                                                         // penalty
                                                                                         // pointwinner
                                                                                         // can
                                                                                         // be
                                                                                         // different

                lastPointPlayedOutcome = doubletennisMatchIncident.getPointWinner();

                if (inTieBreak) {
                    if (!inSuperTieBreak) {
                        onServeSideNow = tieBreak.onServeSideNow;
                        if (onServeSideNow == TeamId.A) {
                            onServePlayerNow = tieBreak.onServePlayerTeamANow;
                        } else if (onServeSideNow == TeamId.B) {
                            onServePlayerNow = tieBreak.onServePlayerTeamBNow;
                        } else {
                            throw new IllegalArgumentException(" should no go here");
                        }

                        if (wonByA)
                            gameEventResult = GameOrTieBreakEventResult.GAMEWONA;
                        else
                            gameEventResult = GameOrTieBreakEventResult.GAMEWONB;

                        serverSideAtStartOfGameOrTieBreak = tieBreak.onServeAtStartOfTieBreak;
                        serverPlayerTeamAAtStartOfGameOrTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
                        serverPlayerTeamBAtStartOfGameOrTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
                        lastGameOrTieBreakPointsA = tieBreak.pointsA;
                        lastGameOrTieBreakPointsB = tieBreak.pointsB;
                    } else {
                        onServeSideNow = superTieBreak.onServeSideNow;
                        if (onServeSideNow == TeamId.A) {
                            onServePlayerNow = superTieBreak.onServePlayerTeamANow;
                        } else if (onServeSideNow == TeamId.B) {
                            onServePlayerNow = superTieBreak.onServePlayerTeamBNow;
                        } else {
                            throw new IllegalArgumentException(" should no go here");
                        }
                        if (wonByA)
                            gameEventResult = GameOrTieBreakEventResult.GAMEWONA;
                        else
                            gameEventResult = GameOrTieBreakEventResult.GAMEWONB;

                        serverSideAtStartOfGameOrTieBreak = superTieBreak.onServeAtStartOfSuperTieBreak;
                        serverPlayerTeamAAtStartOfGameOrTieBreak =
                                        superTieBreak.onServePlayerTeamAAtStartOfSuperTieBreak;
                        serverPlayerTeamBAtStartOfGameOrTieBreak =
                                        superTieBreak.onServePlayerTeamAAtStartOfSuperTieBreak;
                        lastGameOrTieBreakPointsA = superTieBreak.pointsA;
                        lastGameOrTieBreakPointsB = superTieBreak.pointsB;

                    }
                } else {
                    onServeSideNow = game.onServeSideNow;
                    if (onServeSideNow.equals(TeamId.A)) {
                        onServePlayerNow = game.onServePlayerTeamANow;
                    } else if (onServeSideNow.equals(TeamId.B)) {
                        onServePlayerNow = game.onServePlayerTeamBNow;
                    } else {
                        throw new IllegalArgumentException(String
                                        .format("Invalid value of onServeSideNow.  Value is:  %s", onServeSideNow));
                    }

                    if (wonByA)
                        gameEventResult = GameOrTieBreakEventResult.GAMEWONA;
                    else
                        gameEventResult = GameOrTieBreakEventResult.GAMEWONB;

                    serverSideAtStartOfGameOrTieBreak = game.onServeSideNow;

                    serverPlayerTeamAAtStartOfGameOrTieBreak = game.onServePlayerTeamANow;
                    serverPlayerTeamBAtStartOfGameOrTieBreak = game.onServePlayerTeamBNow;

                    lastGameOrTieBreakPointsA = game.pointsA;
                    lastGameOrTieBreakPointsB = game.pointsB;

                    /*
                     * Reset power point status
                     */
                    game.setPowerPointCurrentPoint(false);

                }
            } else if (doubletennisMatchIncident.getIncidentSubType() == TennisMatchIncidentType.PENALTY_MATCH_WON) {
                boolean wonByA = doubletennisMatchIncident.getPointWinner() == TeamId.A;

                lastPointPlayedOutcome = doubletennisMatchIncident.getPointWinner();

                if (inTieBreak) {
                    if (!inSuperTieBreak) {
                        onServeSideNow = tieBreak.onServeSideNow;
                        if (onServeSideNow == TeamId.A) {
                            onServePlayerNow = tieBreak.onServePlayerTeamANow;
                        } else if (onServeSideNow == TeamId.B) {
                            onServePlayerNow = tieBreak.onServePlayerTeamBNow;
                        } else {
                            throw new IllegalArgumentException(" should no go here");
                        }

                        if (wonByA)
                            gameEventResult = GameOrTieBreakEventResult.PENALTYMATCHWONA;
                        else
                            gameEventResult = GameOrTieBreakEventResult.PENALTYMATCHWONB;

                        serverSideAtStartOfGameOrTieBreak = tieBreak.onServeAtStartOfTieBreak;
                        serverPlayerTeamAAtStartOfGameOrTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
                        serverPlayerTeamBAtStartOfGameOrTieBreak = tieBreak.onServePlayerTeamAAtStartOfTieBreak;
                        lastGameOrTieBreakPointsA = tieBreak.pointsA;
                        lastGameOrTieBreakPointsB = tieBreak.pointsB;
                    } else {
                        onServeSideNow = superTieBreak.onServeSideNow;
                        if (onServeSideNow == TeamId.A) {
                            onServePlayerNow = superTieBreak.onServePlayerTeamANow;
                        } else if (onServeSideNow == TeamId.B) {
                            onServePlayerNow = superTieBreak.onServePlayerTeamBNow;
                        } else {
                            throw new IllegalArgumentException(" should no go here");
                        }
                        if (wonByA)
                            gameEventResult = GameOrTieBreakEventResult.PENALTYMATCHWONA;
                        else
                            gameEventResult = GameOrTieBreakEventResult.PENALTYMATCHWONB;

                        serverSideAtStartOfGameOrTieBreak = superTieBreak.onServeAtStartOfSuperTieBreak;
                        serverPlayerTeamAAtStartOfGameOrTieBreak =
                                        superTieBreak.onServePlayerTeamAAtStartOfSuperTieBreak;
                        serverPlayerTeamBAtStartOfGameOrTieBreak =
                                        superTieBreak.onServePlayerTeamBAtStartOfSuperTieBreak;
                        lastGameOrTieBreakPointsA = superTieBreak.pointsA;
                        lastGameOrTieBreakPointsB = superTieBreak.pointsB;

                    }
                } else {
                    onServeSideNow = game.onServeSideNow;
                    if (onServeSideNow.equals(TeamId.A)) {
                        onServePlayerNow = game.onServePlayerTeamANow;
                    } else if (onServeSideNow.equals(TeamId.B)) {
                        onServePlayerNow = game.onServePlayerTeamBNow;
                    } else {
                        throw new IllegalArgumentException(String
                                        .format("Invalid value of onServeSideNow.  Value is:  %s", onServeSideNow));
                    }

                    if (wonByA)
                        gameEventResult = GameOrTieBreakEventResult.PENALTYMATCHWONA;
                    else
                        gameEventResult = GameOrTieBreakEventResult.PENALTYMATCHWONB;

                    serverSideAtStartOfGameOrTieBreak = game.onServeSideNow;

                    serverPlayerTeamAAtStartOfGameOrTieBreak = game.onServePlayerTeamANow;
                    serverPlayerTeamBAtStartOfGameOrTieBreak = game.onServePlayerTeamBNow;

                    lastGameOrTieBreakPointsA = game.pointsA;
                    lastGameOrTieBreakPointsB = game.pointsB;

                    /*
                     * Reset power point status
                     */
                    game.setPowerPointCurrentPoint(false);

                }
            }

            switch (gameEventResult) {

                case POINTWONA:
                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.A, true,
                                    onServePlayerNow, TennisMatchIncidentResultType.POINTWONONLY);
                    breakPoint = checkBreakPointStatus();
                    gamePoint = checkGamePointStatus();
                    if (doubletennisMatchIncident.getSourceSystem() != null)
                        if (!isInTieBreak() && doubletennisMatchIncident.getSourceSystem().equals("BETRADAR")
                                        || doubletennisMatchIncident.getSourceSystem().equals("TRADER")) {
                            updateDangerPointStatus();
                        }
                    super.setDatafeedStateMismatch(
                                    mismatchWithStateFromFeed(doubletennisMatchIncident.getTennisMatchStateFromFeed(),
                                                    doubletennisMatchIncidentResult));

                    copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);

                    if (isDataFeedStateTieBreakResult) {
                        doubletennisMatchIncidentResult.setTennisMatchIncidentResultType(feedMatchIncidentResultType);
                    }

                    return (MatchIncidentResult) doubletennisMatchIncidentResult;
                case POINTWONB:
                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.A, false,
                                    onServePlayerNow, TennisMatchIncidentResultType.POINTWONONLY);

                    breakPoint = checkBreakPointStatus();
                    gamePoint = checkGamePointStatus();
                    if (doubletennisMatchIncident.getSourceSystem() != null)
                        if (!isInTieBreak() && (doubletennisMatchIncident.getSourceSystem().equals("BETRADAR")
                                        || doubletennisMatchIncident.getSourceSystem().equals("TRADER"))) {
                            updateDangerPointStatus();
                        }
                    super.setDatafeedStateMismatch(
                                    mismatchWithStateFromFeed(doubletennisMatchIncident.getTennisMatchStateFromFeed(),
                                                    doubletennisMatchIncidentResult));
                    copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);

                    if (isDataFeedStateTieBreakResult) {
                        doubletennisMatchIncidentResult.setTennisMatchIncidentResultType(feedMatchIncidentResultType);
                    }
                    return (MatchIncidentResult) doubletennisMatchIncidentResult;

                case GAMEWONA:
                    updateFirstServeBreak(onServeSideNow, TeamId.A, this.getGameNo(), this.getSetNo(),
                                    this.isInTieBreak());
                    updateFirstServeBreakAB(onServeSideNow, TeamId.A, this.getGameNo(), this.getSetNo(),
                                    this.isInTieBreak());
                    lastGamePlayedOutcome = TeamId.A;

                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.A, true,
                                    onServePlayerNow, TennisMatchIncidentResultType.GAMEWONA);
                    gamesA++;
                    if (inFinalSet && inSuperTieBreak) {
                        doubletennisMatchIncidentResult
                                        .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.SETWON);

                        if (doublesMatch) {
                            onServePlayerInFirstGameOfSet = PlayerId.UNKNOWN;
                            onServePlayerInSecondGameOfSet = PlayerId.UNKNOWN;
                        } else {
                            if (getOnServePlayerNow() == PlayerId.A1) {
                                onServePlayerInFirstGameOfSet = PlayerId.B1;
                                onServePlayerInSecondGameOfSet = PlayerId.A1;
                            } else {
                                onServePlayerInFirstGameOfSet = PlayerId.A1;
                                onServePlayerInSecondGameOfSet = PlayerId.B1;
                            }
                        }

                        int setNo = setsA + setsB;
                        gameScoreInSetN[setNo].A = gamesA;
                        gameScoreInSetN[setNo].B = gamesB;
                        gamesA = 0;
                        gamesB = 0;
                        // lastSetWonA = setsA;
                        setsA++;
                        if (doublesMatch) {
                            newSetSwitchServePlayer = true;
                        }
                        if (setsA == matchWinningSetScore)
                            doubletennisMatchIncidentResult
                                            .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONA);

                    } else if (isSetWinningGameScore(gamesA, gamesB)) { // set won
                        doubletennisMatchIncidentResult
                                        .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.SETWON);

                        if (doublesMatch) {
                            onServePlayerInFirstGameOfSet = PlayerId.UNKNOWN;
                            onServePlayerInSecondGameOfSet = PlayerId.UNKNOWN;
                        } else {
                            if (!inTieBreak) {
                                if (getOnServePlayerNow() == PlayerId.A1) {
                                    onServePlayerInFirstGameOfSet = PlayerId.B1;
                                    onServePlayerInSecondGameOfSet = PlayerId.A1;
                                } else {
                                    onServePlayerInFirstGameOfSet = PlayerId.A1;
                                    onServePlayerInSecondGameOfSet = PlayerId.B1;
                                }
                            } else {
                                flipOverOnServePlayerInFirstGameOfSet();
                            }
                        }

                        int setNo = setsA + setsB;
                        gameScoreInSetN[setNo].A = gamesA;
                        gameScoreInSetN[setNo].B = gamesB;
                        gamesA = 0;
                        gamesB = 0;
                        // lastSetWonA = setsA;
                        setsA++;
                        if (doublesMatch) {
                            newSetSwitchServePlayer = true;
                        }
                        if (setsA == matchWinningSetScore)
                            doubletennisMatchIncidentResult
                                            .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONA);
                    }
                    break;
                case PENALTYMATCHWONA:
                    int setNo = setsA + setsB;
                    gameScoreInSetN[setNo].A = gamesA;
                    gameScoreInSetN[setNo].B = gamesB;
                    gamesA = 0;
                    gamesB = 0;
                    setsA = matchWinningSetScore;
                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.UNKNOWN,
                                    true, onServePlayerNow, TennisMatchIncidentResultType.MATCHWONA);
                    break;
                case PENALTYMATCHWONB:
                    setNo = setsA + setsB;
                    gameScoreInSetN[setNo].A = gamesA;
                    gameScoreInSetN[setNo].B = gamesB;
                    gamesA = 0;
                    gamesB = 0;
                    setsB = matchWinningSetScore;
                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.UNKNOWN,
                                    true, onServePlayerNow, TennisMatchIncidentResultType.MATCHWONB);
                    break;
                case GAMEWONB:
                    updateFirstServeBreak(onServeSideNow, TeamId.B, this.getGameNo(), this.getSetNo(),
                                    this.isInTieBreak());
                    updateFirstServeBreakAB(onServeSideNow, TeamId.B, this.getGameNo(), this.getSetNo(),
                                    this.isInTieBreak());
                    lastGamePlayedOutcome = TeamId.B;

                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.A, false,
                                    onServePlayerNow, TennisMatchIncidentResultType.GAMEWONB);
                    gamesB++;
                    if (inFinalSet && inSuperTieBreak) {
                        // set won
                        doubletennisMatchIncidentResult
                                        .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.SETWON);

                        if (doublesMatch) {
                            onServePlayerInFirstGameOfSet = PlayerId.UNKNOWN;
                            onServePlayerInSecondGameOfSet = PlayerId.UNKNOWN;
                        } else {
                            if (getOnServePlayerNow() == PlayerId.A1) {
                                onServePlayerInFirstGameOfSet = PlayerId.B1;
                                onServePlayerInSecondGameOfSet = PlayerId.A1;
                            } else {
                                onServePlayerInFirstGameOfSet = PlayerId.A1;
                                onServePlayerInSecondGameOfSet = PlayerId.B1;
                            }
                        }

                        setNo = setsA + setsB;
                        gameScoreInSetN[setNo].A = gamesA;
                        gameScoreInSetN[setNo].B = gamesB;
                        gamesA = 0;
                        gamesB = 0;
                        // lastSetWonB = setsB;
                        setsB++;
                        if (doublesMatch) {
                            newSetSwitchServePlayer = true;
                        }
                        if (setsB == matchWinningSetScore)
                            doubletennisMatchIncidentResult
                                            .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONB);

                    } else if (isSetWinningGameScore(gamesB, gamesA)) { // set won
                        doubletennisMatchIncidentResult
                                        .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.SETWON);

                        if (doublesMatch) {
                            onServePlayerInFirstGameOfSet = PlayerId.UNKNOWN;
                            onServePlayerInSecondGameOfSet = PlayerId.UNKNOWN;

                        } else {
                            if (!inTieBreak) {
                                if (getOnServePlayerNow() == PlayerId.A1) {
                                    onServePlayerInFirstGameOfSet = PlayerId.B1;
                                    onServePlayerInSecondGameOfSet = PlayerId.A1;
                                } else {
                                    onServePlayerInFirstGameOfSet = PlayerId.A1;
                                    onServePlayerInSecondGameOfSet = PlayerId.B1;
                                }
                            } else {
                                flipOverOnServePlayerInFirstGameOfSet();
                            }
                        }

                        setNo = setsA + setsB;
                        gameScoreInSetN[setNo].A = gamesA;
                        gameScoreInSetN[setNo].B = gamesB;
                        gamesA = 0;
                        gamesB = 0;
                        // lastSetWonB = setsB;
                        setsB++;
                        if (doublesMatch) {
                            newSetSwitchServePlayer = true;
                        }
                        if (setsB == matchWinningSetScore)
                            doubletennisMatchIncidentResult
                                            .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONB);
                    }
                    break;
                case SUPERTIEBREAKSETWONA:
                    lastGamePlayedOutcome = TeamId.A;
                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.A, true,
                                    onServePlayerNow, TennisMatchIncidentResultType.GAMEWONA);

                    // lastSetWonA = setsA;
                    gamesA++;
                    setNo = setsA + setsB;
                    gameScoreInSetN[setNo].A = gamesA;
                    gameScoreInSetN[setNo].B = gamesB;
                    setsA++;
                    gamesA = 0;
                    gamesB = 0;
                    if (doublesMatch) {
                        newSetSwitchServePlayer = false;
                    }
                    doubletennisMatchIncidentResult
                                    .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONA);

                    break;
                case SUPERTIEBREAKSETWONB:
                    lastGamePlayedOutcome = TeamId.B;
                    doubletennisMatchIncidentResult = new TennisMatchIncidentResult(onServeSideNow == TeamId.A, false,
                                    onServePlayerNow, TennisMatchIncidentResultType.GAMEWONB);
                    // lastSetWonB = setsB;
                    gamesB++;
                    setNo = setsA + setsB;
                    gameScoreInSetN[setNo].A = gamesA;
                    gameScoreInSetN[setNo].B = gamesB;
                    setsB++;
                    gamesA = 0;
                    gamesB = 0;
                    // lastSetWonB = setsB;
//                    setsB++;
                    if (doublesMatch) {
                        newSetSwitchServePlayer = false;
                    }
                    doubletennisMatchIncidentResult
                                    .setTennisMatchIncidentResultType(TennisMatchIncidentResultType.MATCHWONB);

                    break;
                default:
                    throw new IllegalArgumentException("Should not get here");
            }
            TeamId newServerSide = switchServer(serverSideAtStartOfGameOrTieBreak);
            int newServerPlayerTeamA = 0;
            int newServerPlayerTeamB = 0;
            if ((gamesA + gamesB) % 2 == 0 && (gamesA + gamesB >= 2)) { // In
                                                                        // new
                                                                        // set
                                                                        // game
                                                                        // 1
                                                                        // and
                                                                        // 2
                                                                        // need
                                                                        // serving
                                                                        // player
                                                                        // info
                newServerPlayerTeamA = switchPlayer(doublesMatch, serverPlayerTeamAAtStartOfGameOrTieBreak);
                newServerPlayerTeamB = switchPlayer(doublesMatch, serverPlayerTeamBAtStartOfGameOrTieBreak);
            } else if (gamesA == 0 && gamesB == 0 && doublesMatch) { // NEW SET,
                                                                     // RESET
                                                                     // PLAYER
                                                                     // SERVER
                                                                     // ORDERS
                newServerPlayerTeamA = 0;
                newServerPlayerTeamB = 0;
            } else {
                newServerPlayerTeamA = serverPlayerTeamAAtStartOfGameOrTieBreak;
                newServerPlayerTeamB = serverPlayerTeamBAtStartOfGameOrTieBreak;
            }

            /*
             * Update doubletennisMatchIncidentResult Type since now use feeds info, since been asked only check this
             * when tiebreaks, or super tiebreaks
             */
            if (isDataFeedStateTieBreakResult) {
                doubletennisMatchIncidentResult.setTennisMatchIncidentResultType(feedMatchIncidentResultType);
            }
            /**/

            /**
             * Determin the next tie breaks status for the rest of match, and if new set switch serve player
             */
            newSetSwitchServePlayer = false;
            if (doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()
                            .equals(TennisMatchIncidentResultType.MATCHWONB)
                            || doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()
                                            .equals(TennisMatchIncidentResultType.MATCHWONA)
                            || doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()
                                            .equals(TennisMatchIncidentResultType.SETWON)) {
                if (doublesMatch)
                    newSetSwitchServePlayer = true;
            }

            updateTieBreakStatus(doubletennisMatchIncidentResult);

            if (inTieBreak)
                if (inSuperTieBreak)
                    superTieBreak.startNewSuperTieBreak(newServerSide, newServerPlayerTeamA, newServerPlayerTeamB);
                else
                    tieBreak.startNewTieBreak(newServerSide, newServerPlayerTeamA, newServerPlayerTeamB);
            else {
                if (newSetSwitchServePlayer) {
                    // In the set(s) that follow, teams can decide which player
                    // will serve first for their respective
                    // team
                    // and establish a new rotation.
                    game.startNewGame(newServerSide, 0, 0);
                } else {
                    game.startNewGame(newServerSide, newServerPlayerTeamA, newServerPlayerTeamB);
                }
            }

        }
        /*
         * check for mismatch of state with the datafeed and set or clear the flag
         */
        boolean isMisMatch = mismatchWithStateFromFeed(doubletennisMatchIncident.getTennisMatchStateFromFeed(),
                        doubletennisMatchIncidentResult);
        super.setDatafeedStateMismatch(isMisMatch);

        if (isMisMatch)
            copyMatchStateFromFeed(stateFromFeed, autosyncMatchStateToFeedOnMismatch);

        return (MatchIncidentResult) doubletennisMatchIncidentResult;
    }

    private boolean shouldUpdateSetWonAtTieBreak(int pointsA, int pointsB, boolean noAdvantageTieBreakFormat) {
        if (noAdvantageTieBreakFormat)
            return pointsA == 7;
        else
            return pointsA >= 7 && pointsA - pointsB >= 2;
    }

    private void updateDangerPointStatus() {
        if (!isNoAdvantageGameFormat()) {
            if ((this.game.getPointsA() == 3 && this.game.getPointsB() == 2)
                            || (this.game.getPointsB() == 3 && this.game.getPointsA() == 2)) {
                this.dangerPoint = true;
                this.gamePoint = true;
            }
        } else {
            if (this.game.getPointsA() == 3 || this.game.getPointsB() == 3) {
                this.dangerPoint = true;
                this.gamePoint = true;
            }
        }
    }

    private void updateFirstServeBreak(TeamId onServeSideNow, TeamId TeamWon, int gameNo, int setNo, boolean tieBreak) {
        if (TeamWon != onServeSideNow && this.getFirstServingBreakInSetN()[setNo - 1] == 0 && !tieBreak) {
            this.getFirstServingBreakInSetN()[setNo - 1] = gameNo;
        }
    }

    private void updateFirstServeBreakAB(TeamId onServeSideNow, TeamId TeamWon, int gameNo, int setNo,
                    boolean tieBreak) {
        if (TeamWon == TeamId.A) {
            if (TeamWon != onServeSideNow && this.getFirstServingBreakAInSetN()[setNo - 1] == 0 && !tieBreak)

                this.getFirstServingBreakAInSetN()[setNo - 1] = gameNo;
        }
        if (TeamWon == TeamId.B) {
            if (TeamWon != onServeSideNow && this.getFirstServingBreakBInSetN()[setNo - 1] == 0 && !tieBreak)

                this.getFirstServingBreakBInSetN()[setNo - 1] = gameNo;
        }
    }

    /*
     * This method copy the match state and reset the data feed state mismatch alarm
     */
    private void copyMatchStateFromFeed(TennisMatchStateFromFeed stateFromFeed,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        if (stateFromFeed != null && autosyncMatchStateToFeedOnMismatch) {
            resetTieBreakStatus(stateFromFeed); // must reset tie break status
                                                // BEFORE apply scores
            setServingOrders(this, stateFromFeed);
            setMatchStateEqualToFeedState(this, stateFromFeed);
            super.setDatafeedStateMismatch(false);
        }

    }

    private void setServingOrders(TennisMatchState tennisMatchState, TennisMatchStateFromFeed stateFromFeed) {
        TeamId teamServing = stateFromFeed.getTeamServingNow();
        this.setOnServeSideNow(teamServing);
        // FIXME: might worth to reset player serving now
        // if(doublesMatch){
        // if(stateFromFeed.getGamesA()+stateFromFeed.getGamesB()==0){
        //
        // }
        // }
    }

    private void resetTieBreakStatus(TennisMatchStateFromFeed stateFromFeed) {
        int fGameA = stateFromFeed.getGamesA();
        int fGameB = stateFromFeed.getGamesB();
        int fGetA = stateFromFeed.getSetsA();
        int fGetB = stateFromFeed.getSetsB();

        this.finalSetSuperTieBreak = false;
        this.tieBreakinFinalSet = true;

        if (fGetA < this.matchWinningSetScore - 1 && fGetB < this.matchWinningSetScore - 1) {// not
                                                                                             // in
                                                                                             // final
                                                                                             // set
            if (this.tieBreakinRegularSet) { // tie break format
                if (fGameA == nGamesInMatch && fGameB == nGamesInMatch)
                    inTieBreak = true;
            } else { // no tie break format
                inTieBreak = false;
            }
        } else {// final set
            if (this.finalSetSuperTieBreak) { // super tie break mode
                if (fGameA == nGamesInMatch && fGameB == nGamesInMatch) {
                    inTieBreak = true;
                    inSuperTieBreak = true;
                }
            } else { // super tie break format
                if (fGameA == nGamesInMatch && fGameB == nGamesInMatch) {
                    inTieBreak = true;
                    inSuperTieBreak = false;
                }
            }
        }

    }

    // private void resetServingOrdersFromPointsJump(TennisMatchStateFromFeed
    // stateFromFeed) {
    // PlayerId playerId = this.getOnServePlayerInFirstGameOfSet();
    // int gamesSoFar = stateFromFeed.getGamesA() + stateFromFeed.getGamesB();
    //
    // PlayerId playerStartOfGame = servingStartOfCurrentGame(playerId,
    // gamesSoFar);
    // TeamId team = obtainTeamServingNow(playerStartOfGame,
    // stateFromFeed.getPointsA(), stateFromFeed.getPointsB());
    //
    // if (this.inTieBreak || this.inSuperTieBreak)
    // this.setOnServeSideNow(team);
    //
    // }

    // private TeamId obtainTeamServingNow(PlayerId playerStartOfGame, int
    // pointsA, int pointsB) {
    // // inputs: player who start the game, pointsA, pointsB
    // PlayerId player = null;
    // if (this.inTieBreak) {
    // if (pointsA + pointsB == 0)
    // player = playerStartOfGame;
    // else if (Math.ceil((pointsA + pointsB) / 2.0) % 2 == 0) { // rotated back
    // case
    // player = playerStartOfGame;
    // } else {
    // if (playerStartOfGame == PlayerId.A1 || playerStartOfGame == PlayerId.A2)
    // player = PlayerId.B1;
    // else
    // player = PlayerId.A1;
    // }
    // } else {
    // if (playerStartOfGame == PlayerId.A1 || playerStartOfGame == PlayerId.A2)
    // player = PlayerId.A1;
    // else
    // player = PlayerId.B1;
    // }
    // TeamId teamId = ((player == PlayerId.A1 || player == PlayerId.A2) ?
    // TeamId.A : TeamId.B);
    // return teamId;
    // }

    // private PlayerId servingStartOfCurrentGame(PlayerId playerId, int
    // gamesSoFar) {
    // PlayerId player = null;
    // if (doublesMatch) {
    // if (gamesSoFar % 2 == 1) {// FIXME: FIX LOGIC FOR DOUBLE TENNIS
    // if (playerId == PlayerId.A1)
    // player = PlayerId.B1;
    // else
    // player = PlayerId.A1;
    // }
    // } else {
    // if (gamesSoFar % 2 == 1) {
    // if (playerId == PlayerId.A1)
    // player = PlayerId.B1;
    // else
    // player = PlayerId.A1;
    // } else {
    // if (playerId == PlayerId.A1)
    // player = PlayerId.A1;
    // else
    // player = PlayerId.B1;
    //
    //
    // }
    // }
    // return player;
    // }


    private <e> Enum<?> getItemOpposit(Enum<?> genderId) {
        if (genderId.getClass() == GenderId.class) {
            if (genderId == GenderId.FEMALE)
                return GenderId.MALE;
            else if (genderId == GenderId.MALE)
                return GenderId.FEMALE;
            else
                return GenderId.UNKNOWN;
        } else if (genderId.getClass() == PlayerId.class) {
            if (genderId == PlayerId.A1)
                return PlayerId.A2;
            else if (genderId == PlayerId.A2)
                return PlayerId.A1;
            else if (genderId == PlayerId.B1)
                return PlayerId.B2;
            else if (genderId == PlayerId.B2)
                return PlayerId.B1;
            else
                return PlayerId.UNKNOWN;
        }
        return null;
    }

    private boolean mismatchWithStateFromFeed(TennisMatchStateFromFeed sf,
                    TennisMatchIncidentResult matchIncidentResult) {
        /*
         * have to deal with feed not resetting points and games at the end of each set
         */
        boolean stateOk = true;
        if (sf != null) {
            int fSetsA = sf.getSetsA();
            int fSetsB = sf.getSetsB();
            int fGamesA = sf.getGamesA();
            int fGamesB = sf.getGamesB();
            int fPointsA = sf.getPointsA();
            int fPointsB = sf.getPointsB();
            switch (matchIncidentResult.getTennisMatchIncidentResultType()) {
                case FAULT:
                case POINT_START:
                case CHALLENGER_BALLMARK:
                case FIRSTSERVERINMATCHSET:
                case INJURY_STATUS_REVERSE:
                case POINTWONONLY:
                case POWERPOINTSTART:
                case DELAY:
                    /*
                     * do nothing
                     */
                    break;
                case GAMEWONA:
                case GAMEWONB:
                    fPointsA = 0;
                    fPointsB = 0;
                    break;
                case SETWON:
                    fPointsA = 0;
                    fPointsB = 0;
                    fGamesA = 0;
                    fGamesB = 0;
                    break;
                case MATCHWONA:
                case MATCHWONB:
                    fPointsA = 0;
                    fPointsB = 0;
                    fGamesA = 0;
                    fGamesB = 0;
                    break;
                case MATCHABANDONED:
                    break;
                case PREMATCH:
                    break;
                default:
                    break;
            }
            stateOk = fSetsA == this.getSetsA() && fSetsB == this.getSetsB() && fGamesA == this.getGamesA()
                            && fGamesB == this.getGamesB() && fPointsA == this.getPointsA()
                            && fPointsB == this.getPointsB();
        }
        return !stateOk;

    }

    private void flipOverOnServePlayerInFirstGameOfSet() {
        if (onServePlayerInFirstGameOfSet == PlayerId.A1) {
            onServePlayerInFirstGameOfSet = PlayerId.B1;
            onServePlayerInSecondGameOfSet = PlayerId.A1;
        } else {
            onServePlayerInFirstGameOfSet = PlayerId.A1;
            onServePlayerInSecondGameOfSet = PlayerId.B1;
        }

    }

    private boolean checkBreakPointStatus() {
        if (inTieBreak) {
            return false;
        }
        if (inSuperTieBreak) {
            return false;
        }
        if (game.onServeSideNow == TeamId.A) { // if comes into here, must be
                                               // point won by B
            if (game.getPointsB() >= 3 && (game.getPointsA() < game.getPointsB())) {
                if (teamBreakFirst.equals(TeamId.UNKNOWN))
                    teamBreakFirst = TeamId.B;
                return true;
            }
        }

        if (game.onServeSideNow == TeamId.B) { // if comes into here, must be
                                               // point won by A
            if (game.getPointsA() >= 3 && (game.getPointsB() < game.getPointsA())) {
                if (teamBreakFirst.equals(TeamId.UNKNOWN))
                    teamBreakFirst = TeamId.A;
                return true;
            }
        }

        if (game.getPointsB() == 3 && game.getPointsA() == 3 && this.matchFormat.isNoAdvantageGameFormat()) {
            return true;
        }

        return false;
    }

    private boolean checkGamePointStatus() {
        if (inTieBreak) {
            return false;
        }
        if (inSuperTieBreak) {
            return false;
        }
        if (game.getPointsA() >= 3 && (game.getPointsA() > game.getPointsB())) {
            return true;
        }

        if (game.getPointsB() >= 3 && (game.getPointsB() > game.getPointsA())) {
            return true;
        }

        if (game.getPointsB() == 3 && game.getPointsA() == 3 && this.matchFormat.isNoAdvantageGameFormat()) {
            return true;
        }

        return false;
    }

    /**
     * This method check if input incident is point won & current serving order is whether known or not
     **/
    private boolean checkAndSetIfDelayedServingOrder(TennisMatchIncident matchIncident) {
        boolean checkIfDelayedServingOrder = false;

        if (matchIncident.getIncidentSubType() == TennisMatchIncidentType.POINT_WON) {
            if (doublesMatch) {
                if (this.getOnServeTeamNow() == TeamId.UNKNOWN || this.getOnServePlayerNow() == PlayerId.UNKNOWN) {
                    int servingPlayerNow = 1;
                    TeamId servingTeamNow = decideServingTeamFromPoints(matchIncident.getServerSideAtStartOfMatch());
                    if (doublesMatch)
                        servingPlayerNow = decideServingPlayerFromPoints(matchIncident.getServerSideAtStartOfMatch(),
                                        matchIncident.getServerPlayerAtStartOfMatch());

                    this.setOnServeSideNow(servingTeamNow);
                    if (servingTeamNow.equals(TeamId.A)) {
                        this.setOnServePlayerTeamANow(servingPlayerNow);
                    } else if (servingTeamNow.equals(TeamId.B)) {
                        this.setOnServePlayerTeamBNow(servingPlayerNow);
                    } else {
                        throw new IllegalArgumentException(
                                        "Serving order not known while point won, match incident illegal");
                    }
                    checkIfDelayedServingOrder = true;
                }

            } else {
                // Single Tennis
                if (this.getOnServeTeamNow() == TeamId.UNKNOWN) {
                    TeamId servingTeamNow = decideServingTeamFromPoints(matchIncident.getServerSideAtStartOfMatch());
                    this.setOnServeSideNow(servingTeamNow);
                    if (servingTeamNow.equals(TeamId.A)) {
                        this.setOnServePlayerTeamANow(1);
                    } else if (servingTeamNow.equals(TeamId.B)) {
                        this.setOnServePlayerTeamBNow(1);
                    } else {
                        throw new IllegalArgumentException(
                                        "Serving order not known while point won, match incident illegal");
                    }
                    checkIfDelayedServingOrder = true;
                }
            }
        }
        return checkIfDelayedServingOrder;
    }

    private int decideServingPlayerFromPoints(TeamId serverSideAtStartOfMatch, int serverPlayerAtStartOfMatch) {
        return 1;
    }

    private TeamId decideServingTeamFromPoints(TeamId serverSideAtStartOfMatch) {
        TeamId serverTeamNow = serverSideAtStartOfMatch;
        int numberOfGames = 0;
        if (!inTieBreak) {
            for (int i = 0; i < gameScoreInSetN.length; i++)
                numberOfGames += (gameScoreInSetN[i].A + gameScoreInSetN[i].B);
            if (numberOfGames % 2 != 0) { // switch team id
                if (serverSideAtStartOfMatch == TeamId.A) {
                    serverTeamNow = TeamId.B;
                } else if (serverSideAtStartOfMatch == TeamId.B) {
                    serverTeamNow = TeamId.A;
                }
            }
        } else {
            // if in a tie break
            for (int i = 0; i < gameScoreInSetN.length; i++)
                numberOfGames += (gameScoreInSetN[i].A + gameScoreInSetN[i].B);
            if (numberOfGames % 2 != 0) { // switch team id
                if (serverSideAtStartOfMatch == TeamId.A) {
                    serverTeamNow = TeamId.B;
                } else if (serverSideAtStartOfMatch == TeamId.B) {
                    serverTeamNow = TeamId.A;
                }
            }
            int totalPoints = this.tieBreak.getPointsA() + this.tieBreak.getPointsB();
            if ((totalPoints / 2 + totalPoints % 2) % 2 == 0) {
                // do nothing
            } else {
                // swap serving team
                if (serverTeamNow == TeamId.A) {
                    serverTeamNow = TeamId.B;
                } else {
                    serverTeamNow = TeamId.A;
                }
            }
        }
        return serverTeamNow;
    }

    private void updateTieBreakStatus(TennisMatchIncidentResult doubletennisMatchIncidentResult) {
        if (doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()
                        .equals(TennisMatchIncidentResultType.MATCHWONA)
                        || doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()
                                        .equals(TennisMatchIncidentResultType.MATCHWONB)
                        || doubletennisMatchIncidentResult.getTennisMatchIncidentResultType()
                                        .equals(TennisMatchIncidentResultType.MATCHABANDONED)) {
            this.inTieBreak = false;
            this.inSuperTieBreak = false;
            return;
        }
        boolean tiebreakLastIncident = this.inTieBreak;
        inFinalSet = setsA + setsB == matchFormat.getSetsPerMatch() - 1;
        if (inFinalSet)
            if (finalSetSuperTieBreak) {
                inSuperTieBreak = true;
                inTieBreak = true;
            } else {
                inSuperTieBreak = false;
                inTieBreak = gamesA == nGamesInMatch && gamesB == nGamesInMatch && tieBreakinFinalSet;
            }
        else {// in regular set
            if (!tieBreakinRegularSet) {
                inTieBreak = false;
                inSuperTieBreak = false;
            } else {
                if ((gamesA == nGamesInMatch) && (gamesB == nGamesInMatch)) {
                    inTieBreak = true;
                    inSuperTieBreak = false;
                } else {
                    inTieBreak = false;
                    inSuperTieBreak = false;
                }
            }
        }

        if (tiebreakLastIncident == false && inTieBreak == true)
            this.tieBreaksCounter++;
    }

    static TeamId switchServer(TeamId currentServer) {
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

    static int switchPlayer(boolean doubles, int currentServerPlayer) {
        int newServer = 1;
        if (doubles) {
            switch (currentServerPlayer) {
                case 1:
                    newServer = 2;
                    break;
                case 2:
                    newServer = 1;
                    break;
                case 0:
                    newServer = 0;
                    break; // for cases where new sets start
            }
        }
        return newServer;
    }

    boolean inAdvantageSet() {
        inFinalSet = setsA + setsB == matchFormat.getSetsPerMatch() - 1;
        if (inFinalSet)
            if (!finalSetSuperTieBreak)
                return (inFinalSet && !tieBreakinFinalSet && finalSetSuperTBNo == 0);
            else {
                inSuperTieBreak = true;
                return (false);
            }
        else// in regular set
            return (!tieBreakinRegularSet);

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
            return (gamesWinner >= nGamesInMatch) && (gamesWinner - gamesLoser >= 2);
        else if (nGamesInMatch == 6)
            return (gamesWinner == (nGamesInMatch + 1))
                            || (gamesWinner == nGamesInMatch && gamesLoser <= (nGamesInMatch - 2));
        else
            return (gamesWinner == (nGamesInMatch + 1));
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

    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt prompt;
        if (isMatchCompleted()) {
            prompt = new MatchIncidentPrompt("Match won", "-");
        } else {
            if (getOnServeTeamNow() == TeamId.UNKNOWN) {
                requestedFirstPlayerToServe = true;
                if (doublesMatch)
                    prompt = new MatchIncidentPrompt("Enter id of first player to serve (A1,A2,B1,B2): ", "A1");
                else
                    prompt = new MatchIncidentPrompt("Enter id of first player to serve (A, B): ", "A");
            } else if (getOnServeTeamNow() != TeamId.UNKNOWN) {
                if (getOnServeTeamNow() == TeamId.A && getOnServePlayerTeamANow() == 0 && doublesMatch) {
                    requestedFirstPlayerToServe = true;
                    prompt = new MatchIncidentPrompt("Enter id of first player to serve (B1,B2): ", "A1");
                } else if (getOnServeTeamNow() == TeamId.B && getOnServePlayerTeamBNow() == 0 && doublesMatch) {
                    requestedFirstPlayerToServe = true;
                    prompt = new MatchIncidentPrompt("Enter id of first player to serve (B1,B2): ", "B1");
                } else {
                    prompt = new MatchIncidentPrompt(
                                    "Next point winner Ar/Br, r=R(ally), A(ce), D(ouble fault), U(nknown): ",
                                    defaultPrompt);
                }
            } else if (newSetSwitchServePlayer == true && doublesMatch) {
                prompt = new MatchIncidentPrompt("Enter id of first player to serve (A1,A2,B1,B2): ", "A1");
            } else {
                requestedFirstPlayerToServe = false;
                prompt = new MatchIncidentPrompt("Next point winner (A1,A2,B1,B2): ", defaultPrompt);
            }
        }
        return prompt;
    }

    @Override
    public MatchIncident getMatchIncident(String response) {
        TeamId teamId;
        int playerId = 0;
        GenderId genderId = null;
        TennisMatchIncidentType incidentType;
        try {

            if (response.toUpperCase().equals("PS")) {
                incidentType = TennisMatchIncidentType.POINT_START;

                teamId = this.getOnServeNow();
                TennisMatchIncident tennisMatchIncident = new TennisMatchIncident(0, incidentType, teamId, playerId);
                tennisMatchIncident.setSourceSystem("IMG");
                return tennisMatchIncident;
            }
            if (response.toUpperCase().equals("CH")) {
                incidentType = TennisMatchIncidentType.CHALLENGER_BALLMARK;
                teamId = this.getOnServeNow();
                TennisMatchIncident tennisMatchIncident = new TennisMatchIncident(0, incidentType, teamId, playerId);
                tennisMatchIncident.setSourceSystem("IMG");
                return tennisMatchIncident;
            }
            if (requestedFirstPlayerToServe) {
                incidentType = TennisMatchIncidentType.MATCH_STARTING;
                if (doublesMatch) {
                    if (response.length() < 2)
                        return null; // expecting two char response
                    playerId = Integer.parseInt(response.substring(1, 2));
                    if (this.matchFormat.getSex().equals(TennisMatchFormat.Sex.MIXED))
                        if (response.substring(2, 3) != null) {
                            if (response.substring(2, 3).toUpperCase().equals("F"))
                                genderId = GenderId.FEMALE;
                            else
                                genderId = GenderId.MALE;
                        }

                } else {
                    playerId = 1;
                }
            } else if (newSetSwitchServePlayer && doublesMatch) {
                if (response.length() != 2)
                    return null;
                playerId = Integer.parseInt(response.substring(1, 2));
                incidentType = TennisMatchIncidentType.NEW_SET_STARTING;
            } else
                incidentType = TennisMatchIncidentType.POINT_WON;
            TennisPointResult pointResult = null;
            switch (response.toUpperCase().substring(0, 1)) {
                case "X":
                    teamId = TeamId.UNKNOWN;
                    if (response.toUpperCase().substring(1, 2) == "A")
                        teamId = TeamId.A;
                    else if (response.toUpperCase().substring(1, 2) == "B")
                        teamId = TeamId.B;
                    return new AbandonMatchIncident(AbandonMatchIncidentType.WALKOVER, teamId);
                case "A":
                    teamId = TeamId.A;
                    pointResult = getPointResult(response);
                    defaultPrompt = "AR";
                    break;
                case "B":
                    teamId = TeamId.B;
                    pointResult = getPointResult(response);
                    defaultPrompt = "BR";
                    break;
                // check
                case "C":
                    teamId = TeamId.A;
                    playerId = 1;
                    defaultPrompt = "A";
                    incidentType = TennisMatchIncidentType.SERVING_ORDER;
                    break;
                case "P":
                    if (response.toUpperCase().substring(1, 2).equals("A"))
                        teamId = TeamId.A;
                    else
                        teamId = TeamId.B;
                    incidentType = TennisMatchIncidentType.IPTL_POWERPOINT;
                    break;

                // penalty wons
                case "W":
                    String t = response.substring(2, 3).toUpperCase();
                    if (t.equals("A")) {
                        teamId = TeamId.A;

                        defaultPrompt = "A";
                    } else {
                        teamId = TeamId.B;

                        defaultPrompt = "B";
                    }
                    String wType = response.substring(1, 2).toUpperCase();
                    if (wType.equals("P"))
                        incidentType = TennisMatchIncidentType.PENALTY_POINT_WON;
                    if (wType.equals("G"))
                        incidentType = TennisMatchIncidentType.PENALTY_GAME_WON;
                    if (wType.equals("M"))
                        incidentType = TennisMatchIncidentType.PENALTY_MATCH_WON;
                    break;
                case "F":
                    if (response.toUpperCase().substring(1, 2) == "A")
                        teamId = TeamId.A;
                    else
                        teamId = TeamId.B;
                    incidentType = TennisMatchIncidentType.FAULT;
                    break;
                case "I":
                    incidentType = TennisMatchIncidentType.INJURY_STATUS_REVERSE;
                    teamId = TeamId.UNKNOWN;
                    break;
                default:
                    /*
                     * error in input supplied
                     */
                    return null;
            }
            requestedFirstPlayerToServe = false; // only ask this once
            newSetSwitchServePlayer = false; // only reset again when entering
                                             // next
            TennisMatchIncident incident;
            if (genderId == null)
                incident = new TennisMatchIncident(0, incidentType, teamId, playerId);
            else
                incident = new TennisMatchIncident(0, incidentType, teamId, playerId, genderId);
            incident.setPointResult(pointResult);
            return incident;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private TennisPointResult getPointResult(String response) {
        TennisPointResult result = null;
        if (response.length() >= 2) {
            String pointResultStr = response.substring(1, 2).toUpperCase();
            switch (pointResultStr) {
                case "R":
                    result = TennisPointResult.RALLY;
                    break;
                case "A":
                    result = TennisPointResult.ACE;
                    break;
                case "D":
                    result = TennisPointResult.DOUBLE_FAULT;
                    break;
                default:
                    result = TennisPointResult.UNKNOWN;
                    break;
            }
        }
        return result;
    }

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String preMatch;
        if (this.isPreMatch())
            preMatch = "Yes";
        else
            preMatch = "No";
        map.put("Pre-match?", preMatch);
        map.put("Fault Status", String.valueOf(faultFlag));
        map.put("Sets", String.format("%d-%d", setsA, setsB));
        map.put("Games", String.format("%d-%d", gamesA, gamesB));
        map.put("Last Game Scores", String.format("%d-%d", previousGameScore.A, previousGameScore.B));
        String pointsA;
        String pointsB;
        String inTb;
        if (isInSuperTieBreak()) {
            pointsA = Integer.toString(superTieBreak.pointsA);
            pointsB = Integer.toString(superTieBreak.pointsB);
            inTb = "Yes";
        } else if (isInTieBreak()) {
            pointsA = Integer.toString(tieBreak.pointsA);
            pointsB = Integer.toString(tieBreak.pointsB);
            inTb = "Yes";
        } else {
            pointsA = convertToTennisPoints(game.pointsA);
            pointsB = convertToTennisPoints(game.pointsB);
            inTb = "No";
        }
        map.put("Points", String.format("%s-%s", pointsA, pointsB));
        map.put("Is break point?", String.valueOf(this.breakPoint));
        map.put("Is game point?", String.valueOf(this.gamePoint));
        map.put("Is danger point?", String.valueOf(this.dangerPoint));
        map.put("In tie break?", inTb);
        map.put("Team on serve", this.getOnServeTeamNow().toString());
        String gender1 = "N/A";
        String gender2 = "N/A";
        String gender3 = "N/A";
        if (genderMap != null && this.getOnServePlayerNow() != null) {
            if (genderMap.get(this.getOnServePlayerNow()) != null)
                gender1 = genderMap.get(this.getOnServePlayerNow()).toString();
            if (genderMap.get(this.getOnServePlayerInFirstGameOfSet()) != null)
                gender2 = genderMap.get(this.getOnServePlayerInFirstGameOfSet()).toString();
            if (genderMap.get(this.getOnServePlayerInSecondGameOfSet()) != null)
                gender3 = genderMap.get(this.getOnServePlayerInSecondGameOfSet()).toString();
        }
        map.put("Player on serve", this.getOnServePlayerNow().toString() + " Gender:" + gender1);
        map.put("On serve in game 1 of this set",
                        this.getOnServePlayerInFirstGameOfSet().toString() + " Gender:" + gender2);
        map.put("On serve in game 2 of this set",
                        this.getOnServePlayerInSecondGameOfSet().toString() + " Gender:" + gender3);

        if (matchFormat != null) { // can currently null when deserialised from
                                   // json
            for (int i = 0; i < matchFormat.getSetsPerMatch() - 1; i++) {
                PairOfIntegers p = gameScoreInSetN[i];
                map.put(String.format("Game score in set no %d", i + 1), String.format("%d-%d", p.A, p.B));
            }
        }
        String currentPointId = this.getSequenceIdForPoint(0);
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
        if (matchWinningSetScore == 0) {
            throw new IllegalArgumentException("Match winning set score can not be zero");
        } else {
            return (setsA == matchWinningSetScore) || (setsB == matchWinningSetScore);
        }
    }

    public TeamId getLastPointPlayedOutcome() {
        return lastPointPlayedOutcome;
    }

    public TeamId getLastGamePlayedOutcome() {
        return lastGamePlayedOutcome;
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
        TennisMatchState other = (TennisMatchState) obj;
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
     * returns true if in preMatch
     */
    @Override
    public boolean preMatch() {
        return (isPreMatch());
    }

    /**
     * calculates no of secs to go within current period. Returns zero if pre Match or at half time etc.
     *
     * @return no of secs remaining in current period (including actual or estimated injury time).
     */
    @Override
    public int secsLeftInCurrentPeriod() {
        int secs = -1;
        return secs;
    }

    /**
     * returns total no of games scored by player A in the match
     *
     * @return
     */
    public int getTotalGamesA() {
        int nSets = getSetsA() + getSetsB();
        int nGames = 0;
        for (int i = 0; i < nSets; i++) {
            nGames += gameScoreInSetN[i].A;
        }
        return nGames;
    }

    /**
     * returns total no of games scored by player B in the match
     *
     * @return
     */
    public int getTotalGamesB() {
        int nSets = getSetsA() + getSetsB();
        int nGames = 0;
        for (int i = 0; i < nSets; i++) {
            nGames += gameScoreInSetN[i].B;
        }
        return nGames;
    }

    @Override
    /**
     * looks for sequence of the form "Gn" and converts to form "Sn.m". If not of the form "Gn" then null is returned.
     * If of the form "Gn" but "n" implies that this refers to a tie break then "Sx" is returned
     * 
     */

    public String convertSequenceIdToAlgoMgrStd(String sequenceId) {
        if (sequenceId == null) {
            return null;
        }
        char x = sequenceId.charAt(0);
        if (x != 'G') {
            return null;
        }
        String gameNoStr = sequenceId.substring(1);
        int gameNo;
        String newSequenceId = null;
        try {
            gameNo = Integer.parseInt(gameNoStr);
            int setNo = 0;
            int gameNoInSet = 0;
            setNo = setsA + setsB + 1;
            int nGamesInCompletedSets = 0;
            for (int i = 0; i < setNo - 1; i++)
                nGamesInCompletedSets += gameScoreInSetN[i].A + gameScoreInSetN[i].B;
            gameNoInSet = gameNo - nGamesInCompletedSets;
            if (!inAdvantageSet() && gameNoInSet == 13)
                newSequenceId = "Sx";
            else
                newSequenceId = String.format("S%d.%d", setNo, gameNoInSet);

        } catch (NumberFormatException e) {
            return "Sx";
        }

        return newSequenceId;
    }

    @Override
    public String getSequenceId(String mktSequenceId, int offset) {
        int[] idElements = SequenceId.getSequenceIdElements(mktSequenceId);
        if (idElements == null)
            return this.getSequenceIdForMatch();
        switch (idElements.length) {
            case 1:
                return this.getSequenceIdforSet(offset);
            case 2:
                return this.getSequenceIdForGame(offset);
            case 3:
                return this.getSequenceIdForPoint(offset);
            default:
                throw new IllegalArgumentException(String.format("Not a valid tennis sequence id: %s", mktSequenceId));
        }
    }


    private LinkedHashMap<String, PairOfIntegers> getGamesScoreMap() {
        LinkedHashMap<String, PairOfIntegers> scoreMap = new LinkedHashMap<String, PairOfIntegers>();
        for (int i = 1; i < getSetNo(); i++) {
            scoreMap.put("scoreInSet" + Integer.toString(i), gameScoreInSetN[i - 1]);
        }
        return scoreMap;
    }

    @Override
    public TennisSimpleMatchState generateSimpleMatchState() {
        TennisSimpleMatchState simpleMatchState = new TennisSimpleMatchState(isPreMatch(), isMatchCompleted(),
                        getSetsA(), getSetsB(), getGamesA(), getGamesB(), getPointsA(), getPointsB(), getOnServeNow(),
                        getSetNo(), isInTieBreak(), isInSuperTieBreak(), getGamesScoreMap());
        return simpleMatchState;
    }


    public void setMatchStateEqualToFeedState(MatchState matchState, TennisMatchStateFromFeed matchIncidentState) {
        // System.out.println("Match State From Feeds: " + matchIncidentState);
        // System.out.println("Match State For Present: " + this);

        this.setSetsA(matchIncidentState.getSetsA());
        this.setSetsB(matchIncidentState.getSetsB());
        this.setGamesA(matchIncidentState.getGamesA());
        this.setGamesB(matchIncidentState.getGamesB());

        if (this.isInTieBreak()) {
            if (this.isInSuperTieBreak()) {//// in super tie break
                this.superTieBreak.setPointsA(matchIncidentState.getPointsA());
                this.superTieBreak.setPointsB(matchIncidentState.getPointsB());
            } else {// normal tie break
                this.tieBreak.setPointsA(matchIncidentState.getPointsA());
                this.tieBreak.setPointsB(matchIncidentState.getPointsB());
            }
        } else {
            this.game.setPointsA(matchIncidentState.getPointsA());
            this.game.setPointsB(matchIncidentState.getPointsB());

        }
    }


    // private int getCurrentServeNumber() {
    //
    // int player = -1;
    //
    // if (this.getOnServeNow() == TeamId.A) {
    // player = this.getOnServePlayerTeamANow();
    // } else if (this.getOnServeNow() == TeamId.B) {
    // player = this.getOnServePlayerTeamBNow();
    // } else {
    // // do nothing
    // }
    //
    // return player;
    // }

    public void storeLastGameScore(int pointsA, int pointsB) {
        String value = pointsA + "-" + pointsB;
        this.previousGameScore = PairOfIntegers.generateFromString(value);
    }

    public boolean isServingBrokenInSet(int setNo) {
        return this.getFirstServingBreakInSetN()[setNo - 1] != 0;
    }

    public boolean isServingBrokenInSetA(int setNo) {
        return this.getFirstServingBreakAInSetN()[setNo - 1] != 0;
    }

    public boolean isServingBrokenInSetB(int setNo) {
        return this.getFirstServingBreakBInSetN()[setNo - 1] != 0;
    }

    public boolean isPotentialNextSet(TennisMatchFormat tennisMatchFormat) {
        boolean finalSet = this.isInFinalSet();
        int gamesA = this.getGamesA();
        int gamesB = this.getGamesB();
        int gameNo = this.getGameNo();
        boolean potentialNextSet = true;
        if (gamesA < 5) {
            potentialNextSet = false;
        } else {
            if (finalSet) {
                if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) && gameNo > 10
                                && gameNo % 2 == 0) {
                    potentialNextSet = false;
                } else if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.NORMAL_WITH_TIE_BREAK)
                                && gameNo == 11) {
                    potentialNextSet = false;
                }
            } else if (gameNo == 11) {
                potentialNextSet = false;
            }
        }
        if (potentialNextSet) {
            return true;
        }
        potentialNextSet = true;
        if (gamesB < 5) {
            potentialNextSet = false;
        } else {
            if (finalSet) {
                if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.ADVANTAGE_SET) && gameNo > 10
                                && gameNo % 2 == 0) {
                    potentialNextSet = false;
                } else if (tennisMatchFormat.getFinalSetType().equals(FinalSetType.NORMAL_WITH_TIE_BREAK)
                                && gameNo == 11) {
                    potentialNextSet = false;
                }
            } else if (gameNo == 11) {
                potentialNextSet = false;
            }
        }
        if (potentialNextSet) {
            return true;
        }

        return potentialNextSet;
    }
}
