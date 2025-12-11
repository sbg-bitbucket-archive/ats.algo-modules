package ats.algo.sport.darts;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.GamePeriod;
import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.core.baseclasses.MatchIncidentResult;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartMatchIncidentResult.DartMatchEventOutcome;
import ats.algo.sport.darts.LegWinResult.LegWinType;

/**
 * Holds the state for the whole match
 * 
 * @author Geoff
 * 
 */

public class DartMatchState extends MatchState {
    /**
     * the possible results from a player winning a single leg
     * 
     * @author Geoff
     * 
     */

    private static final long serialVersionUID = 1L;

    /**
     * if legWinType is matchWinner or Set winner and set based match then legsA, legsB holds score for set just
     * completed If legWinType is matchWinner or matchDraw then setsA,setsB holds the score in sets or legs (leg based
     * matches) for the match just completed
     * 
     * @author Geoff
     * 
     */

    public class PlayerScore implements Serializable {
        private static final long serialVersionUID = 1L;
        public int sets;
        public int legs;
        public int n180s; // holds n180s's up to the start of the current leg.
                          // currentLeg.n180sx holds the state for the
                          // currently active leg
        public int highestCheckout; // highest checkout score in legs completed
                                    // in the match so far

        public PlayerScore() {
            // required for JSON serialisation
        }

        public PlayerScore copy() {
            PlayerScore psCopy = new PlayerScore();
            psCopy.sets = this.sets;
            psCopy.legs = this.legs;
            psCopy.n180s = this.n180s;
            psCopy.highestCheckout = this.highestCheckout;
            return psCopy;
        }

        /**
         * Returns player set score
         * 
         * @return sets
         */
        public int getSets() {
            return sets;
        }

        /**
         *
         * 
         * @param sets Sets sets score
         * 
         */
        public void setSets(int sets) {
            this.sets = sets;
        }

        /**
         * Returns player legs score
         * 
         * @return legs
         */
        public int getLegs() {
            return legs;
        }

        /**
         *
         * 
         * @param legs Sets legs score
         * 
         */
        public void setLegs(int legs) {
            this.legs = legs;
        }

        /**
         * Returns n180s score
         * 
         * @return n180s
         */
        public int getN180s() {
            return n180s;
        }

        /**
         *
         * 
         * @param n180s Sets n180s score
         * 
         */
        public void setN180s(int n180s) {
            this.n180s = n180s;
        }

        /**
         * Returns highestCheckout score
         * 
         * @return highestCheckout
         */
        public int getHighestCheckout() {
            return highestCheckout;
        }

        /**
         *
         * 
         * @param highestCheckout Sets highestCheckout score
         * 
         */
        public void setHighestCheckout(int highestCheckout) {
            this.highestCheckout = highestCheckout;
        }

    }

    private int[][] setLeg;
    private int[][] setLegCheckOutScoreA;
    private int[][] setLeg180A;
    private int[][] setLegCheckOutScoreB;
    private int[][] setLeg180B;

    /**
     * Returns int[][] to display all the leg scores in sets of both teams
     * 
     * @return setLeg
     */
    @JsonIgnore
    public int[][] getSetLeg() {
        return setLeg;
    }

    /**
     * 
     * @param setLeg sets int[][] to record all the legs scores in sets
     * 
     */
    @JsonIgnore
    public void setSetLeg(int[][] setLeg) {
        this.setLeg = setLeg;
    }

    /**
     * Returns int[][] to display all the checkout scores in sets of player A
     * 
     * @return setLegCheckOutScoreA
     */
    @JsonIgnore
    public int[][] getSetLegCheckOutScoreA() {
        return setLegCheckOutScoreA;
    }

    /**
     * 
     * @param setLegCheckOutScoreA sets int[][] to record all the checkout scores in sets of player A
     * 
     */
    @JsonIgnore
    public void setSetLegCheckOutScoreA(int[][] setLegCheckOutScoreA) {
        this.setLegCheckOutScoreA = setLegCheckOutScoreA;
    }

    /**
     * Returns int[][] to display all the checkout scores in sets of player B
     * 
     * @return setLegCheckOutScoreB
     */
    @JsonIgnore
    public int[][] getSetLegCheckOutScoreB() {
        return setLegCheckOutScoreB;
    }

    /**
     * 
     * @param setLegCheckOutScoreB sets int[][] to record all the checkout scores in sets of player B
     * 
     */
    @JsonIgnore
    public void setSetLegCheckOutScoreB(int[][] setLegCheckOutScoreB) {
        this.setLegCheckOutScoreB = setLegCheckOutScoreB;
    }

    /**
     * Returns int[][] to display all the legs 180 scores in sets of player A
     * 
     * @return setLeg180A
     */
    @JsonIgnore
    public int[][] getSetLeg180A() {
        return setLeg180A;
    }

    /**
     * 
     * @param setLeg180A sets int[][] to record all the legs 180 scores in sets of player A
     * 
     */
    @JsonIgnore
    public void setSetLeg180A(int[][] setLeg180A) {
        this.setLeg180A = setLeg180A;
    }

    /**
     * Returns int[][] to display all the legs 180 scores in sets of player B
     * 
     * @return setLeg180B
     */
    @JsonIgnore
    public int[][] getSetLeg180B() {
        return setLeg180B;
    }

    /**
     * 
     * @param setLeg180B sets int[][] to record all the legs 180 scores in sets of player B
     * 
     */
    @JsonIgnore
    public void setSetLeg180B(int[][] setLeg180B) {
        this.setLeg180B = setLeg180B;
    }

    /**
     * Returns a new int[][] array with same No
     * 
     * @param array input array
     * @return copyString
     */
    @JsonIgnore
    public int[][] copy(final int[][] array) {
        if (array != null) {
            final int[][] copy = new int[array.length][];

            for (int i = 0; i < array.length; i++) {
                final int[] row = array[i];

                copy[i] = new int[row.length];
                System.arraycopy(row, 0, copy[i], 0, row.length);
            }

            return copy;
        }

        return null;
    }

    private PlayerScore playerAScore;
    private PlayerScore playerBScore;
    private LegState currentLeg;
    private TeamId playerAtOcheAtStartOfCurrentLeg;
    private int setScoreToWin;
    private int legScoreToWinSet;
    private int setScoreToDraw;
    private DartMatchIncidentResult matchEventResult;
    private boolean matchHas9DartFinish;

    /**
     * @return the playerAtOcheAtStartOfCurrentLeg
     */
    public TeamId getPlayerAtOcheAtStartOfCurrentLeg() {
        return playerAtOcheAtStartOfCurrentLeg;
    }

    /**
     * @param value the playerAtOcheAtStartOfCurrentLeg to set
     */
    public void setPlayerAtOcheAtStartOfCurrentLeg(TeamId value) {
        this.playerAtOcheAtStartOfCurrentLeg = value;
        currentLeg.playerAtOche = value;
    }

    /**
     * Returns player A score
     * 
     * @return playerAScore
     */
    public PlayerScore getPlayerAScore() {
        return playerAScore;
    }

    /**
     * @param playerAScore sets player A Score
     */
    public void setPlayerAScore(PlayerScore playerAScore) {
        this.playerAScore = playerAScore;
    }

    /**
     * Returns player B score
     * 
     * @return playerBScore
     */
    public PlayerScore getPlayerBScore() {
        return playerBScore;
    }

    /**
     * @param playerBScore sets player B Score
     */
    public void setPlayerBScore(PlayerScore playerBScore) {
        this.playerBScore = playerBScore;
    }

    /**
     * Returns current Leg
     * 
     * @return currentLeg
     */
    public LegState getCurrentLeg() {
        return currentLeg;
    }

    /**
     * @param currentLeg sets current Leg
     */
    public void setCurrentLeg(LegState currentLeg) {
        this.currentLeg = currentLeg;
    }

    /**
     * Returns match format
     * 
     * @return matchFormat
     */
    @JsonIgnore
    @Override
    public DartMatchFormat getMatchFormat() {
        return matchFormat;
    }

    /**
     * @param matchFormat sets match Format
     */
    public void setMatchFormat(DartMatchFormat matchFormat) {
        this.matchFormat = matchFormat;
    }

    private DartMatchFormat matchFormat;

    public DartMatchState() {
        this(new DartMatchFormat());
    }

    public DartMatchState(MatchFormat matchFormat) {
        super();
        this.matchFormat = (DartMatchFormat) matchFormat;
        this.playerAtOcheAtStartOfCurrentLeg = TeamId.UNKNOWN;
        currentLeg = new LegState(((DartMatchFormat) matchFormat).isDoubleReqdToStart());
        playerAScore = new PlayerScore();
        playerBScore = new PlayerScore();
        int nSets = ((DartMatchFormat) matchFormat).getnLegsOrSetsInMatch();
        int nLegs = ((DartMatchFormat) matchFormat).getnLegsPerSet();
        setLeg = new int[nSets][2];
        setLeg180A = new int[nSets][nLegs];
        setLegCheckOutScoreA = new int[nSets][nLegs];
        setLeg180B = new int[nSets][nLegs];
        setLegCheckOutScoreB = new int[nSets][nLegs];
        setScoreToWin = nSets / 2 + 1;
        legScoreToWinSet = nLegs / 2 + 1;
        if (2 * (nSets / 2) == nSets) {
            setScoreToDraw = nSets / 2; // nsets even so draw is possible
        } else
            setScoreToDraw = nSets; // suitable large
        matchEventResult = new DartMatchIncidentResult();
    }

    /**
     * creates a new copy of itself - all state copied.
     * 
     * @return the new copy
     */
    public DartMatchState copy() {
        DartMatchState cc;
        cc = new DartMatchState(matchFormat);
        cc.setEqualTo(this);
        return cc;
    }

    /**
     * Executed when a player has just won a leg
     * 
     * @param playerId id of the player winning the leg
     * @return whether a set or match winning event
     */
    @JsonIgnore
    LegWinResult legWon(TeamId playerId) {
        LegWinResult legWinResult;
        if (playerId == TeamId.A) {
            legWinResult = processLegWon(playerAScore, playerBScore, playerId);
        } else {
            legWinResult = processLegWon(playerBScore, playerAScore, playerId);
        }
        // playerAScore.n180s += currentLeg.playerA.N180sThrown;
        // playerBScore.n180s += currentLeg.playerB.N180sThrown;
        legWinResult.n180sA = playerAScore.n180s;
        legWinResult.n180sB = playerBScore.n180s;
        legWinResult.currentLegWinner = playerId;
        legWinResult.playerAtOcheAtStartOfLeg = playerAtOcheAtStartOfCurrentLeg;
        legWinResult.is9DartFinish = (playerId == TeamId.A && currentLeg.playerA.NDartsthrown == 9)
                        || (playerId == TeamId.B && currentLeg.playerB.NDartsthrown == 9);
        if (playerAtOcheAtStartOfCurrentLeg == TeamId.A) {
            currentLeg.startNewLeg(TeamId.B);
            playerAtOcheAtStartOfCurrentLeg = TeamId.B;
        } else {
            currentLeg.startNewLeg(TeamId.A);
            playerAtOcheAtStartOfCurrentLeg = TeamId.A;
        }
        return legWinResult;
    }

    /**
     * does the grunt work for the LegWon method
     * 
     * @param pScore sets p score
     * @param oppScore sets opp score
     * @param setBasedMatch sets set based match
     * @param nLegsPerSet sets n legs per set
     * @param nSetsPerMatch sets n sets per match
     * @param player sets player
     * @return legWinResult
     */
    @JsonIgnore
    LegWinResult processLegWon(PlayerScore pScore, PlayerScore oppScore, TeamId player) {
        LegWinResult legWinResult = new LegWinResult();
        legWinResult.matchWinner = TeamId.UNKNOWN;
        pScore.legs++;

        if (pScore.legs * 2 > matchFormat.getnLegsPerSet()) {
            legWinResult.lastSetLegsA = playerAScore.legs;
            legWinResult.lastSetLegsB = playerBScore.legs;
            pScore.sets++;
            playerAScore.legs = 0;
            playerBScore.legs = 0;
            if (pScore.sets * 2 > matchFormat.getnLegsOrSetsInMatch()) {
                legWinResult.matchScoreSetsOrLegsA = playerAScore.sets;
                legWinResult.matchScoreSetsOrLegsB = playerBScore.sets;
                legWinResult.matchWinner = player;
                legWinResult.legWinType = LegWinType.IsMatchWinner;
            } else if ((matchFormat.getnLegsPerSet() == 1) && (pScore.sets * 2 == matchFormat.getnLegsOrSetsInMatch())
                            && (pScore.sets == oppScore.sets)) {
                legWinResult.matchScoreSetsOrLegsA = playerAScore.sets;
                legWinResult.matchScoreSetsOrLegsB = playerBScore.sets;
                legWinResult.matchWinner = TeamId.UNKNOWN;
                legWinResult.legWinType = LegWinType.IsMatchDraw;
            } else
                legWinResult.legWinType = LegWinType.IsSetWinner;
        } else
            legWinResult.legWinType = LegWinType.IsNotMatchOrSetWinner;
        return legWinResult;
    }

    @Override
    public void setEqualTo(MatchState cc) {
        super.setEqualTo(cc);
        DartMatchState dms = (DartMatchState) cc;

        playerAtOcheAtStartOfCurrentLeg = dms.playerAtOcheAtStartOfCurrentLeg;
        playerAScore = dms.playerAScore.copy();
        playerBScore = dms.playerBScore.copy();
        currentLeg = dms.currentLeg.copy();

        int[][] temp_A = copy(dms.getSetLeg());
        this.setSetLeg(temp_A);
        int[][] temp_B = copy(dms.getSetLegCheckOutScoreA());
        this.setSetLegCheckOutScoreA(temp_B);
        int[][] temp_C = copy(dms.getSetLeg180A());
        this.setSetLeg180A(temp_C);
        int[][] temp_D = copy(dms.getSetLegCheckOutScoreB());
        this.setSetLegCheckOutScoreB(temp_D);
        int[][] temp_E = copy(dms.getSetLeg180B());
        this.setSetLeg180B(temp_E);
    }

    /**
     * Return dart MatchIncident Result after the match incident updated
     * 
     * @return matchEventResult
     */
    @Override
    public MatchIncidentResult updateStateForIncident(MatchIncident matchIncident,
                    boolean autosyncMatchStateToFeedOnMismatch) {
        if (!(matchIncident instanceof DartMatchIncident))
            return super.updateStateForIncident(matchIncident, autosyncMatchStateToFeedOnMismatch);
        else {
            switch (((DartMatchIncident) matchIncident).getIncidentSubType()) {
                case SET_PLAYER_STARTING_MATCH_AT_OCHE:
                    setPlayerAtOcheAtStartOfCurrentLeg(
                                    ((DartMatchIncident) matchIncident).getPlayerStartingMatchAtOche());
                    matchEventResult.setResult(playerAtOcheAtStartOfCurrentLeg, DartMatchEventOutcome.BEGININPLAY);
                    break;
                case DART_THROWN:
                    int multiplier = ((DartMatchIncident) matchIncident).getMultiplier();
                    int number = ((DartMatchIncident) matchIncident).getNumberHit();

                    DartTarget dt = new DartTarget(multiplier, number);
                    int beforeA = currentLeg.getPlayerA().N180sThrown;
                    int beforeB = currentLeg.getPlayerB().N180sThrown;
                    int beforeScoreA = currentLeg.getPlayerA().Points;
                    int beforeScoreB = currentLeg.getPlayerB().Points;
                    LegThrowResult throwResult = currentLeg.updateScore(dt);
                    int afterA = currentLeg.getPlayerA().N180sThrown;
                    int afterB = currentLeg.getPlayerB().N180sThrown;
                    if (afterA == (beforeA + 1)) {
                        playerAScore.n180s++;
                        setLeg180A[getSetNo() - 1][getLegNo() - 1]++;
                        currentLeg.first180InLeg = TeamId.UNKNOWN;
                        currentLeg.next180InLeg = false;
                    }
                    if (afterB == (beforeB + 1)) {
                        playerBScore.n180s++;
                        setLeg180B[getSetNo() - 1][getLegNo() - 1]++;
                        currentLeg.first180InLeg = TeamId.UNKNOWN;
                        currentLeg.next180InLeg = false;
                    }
                    DartMatchEventOutcome outcome;
                    TeamId playerAtOcheAtStartOfThisLeg = playerAtOcheAtStartOfCurrentLeg;
                    if (throwResult == LegThrowResult.ISLEGWINNINGDART) {
                        if (currentLeg.playerAtOche == TeamId.A && playerAScore.highestCheckout < beforeScoreA)
                            playerAScore.highestCheckout = beforeScoreA;
                        if (currentLeg.playerAtOche == TeamId.B && playerBScore.highestCheckout < beforeScoreB)
                            playerBScore.highestCheckout = beforeScoreB;
                        if (currentLeg.playerAtOche == TeamId.A) {
                            setLeg[getSetNo() - 1][0]++;
                            setLegCheckOutScoreA[getSetNo() - 1][getLegNo() - 1] = beforeScoreA;
                        }
                        if (currentLeg.playerAtOche == TeamId.B) {
                            setLeg[getSetNo() - 1][1]++;
                            setLegCheckOutScoreB[getSetNo() - 1][getLegNo() - 1] = beforeScoreB;
                        }
                        outcome = updateStateForLegWon(currentLeg.playerAtOche);

                    } else
                        outcome = DartMatchEventOutcome.WITHINLEG;
                    matchEventResult.setResult(playerAtOcheAtStartOfThisLeg, outcome);
                    break;
                default:
                    break;
            }
        }
        return matchEventResult;
    }

    /**
     * Return dart Match EventOutcome after leg won
     * 
     * @return outcome
     */
    @JsonIgnore
    DartMatchEventOutcome updateStateForLegWon(TeamId LegWinnerId) {
        DartMatchEventOutcome outcome;
        LegWinResult legWinResult = legWon(LegWinnerId);
        switch (legWinResult.legWinType) {
            case IsMatchWinner:
                if (legWinResult.currentLegWinner == TeamId.A)
                    outcome = DartMatchEventOutcome.MATCHWONA;
                else
                    outcome = DartMatchEventOutcome.MATCHWONB;
                break;
            case IsSetWinner:
                if (legWinResult.currentLegWinner == TeamId.A)
                    outcome = DartMatchEventOutcome.SETWONA;
                else
                    outcome = DartMatchEventOutcome.SETWONB;
                break;
            case IsMatchDraw:
                outcome = DartMatchEventOutcome.MATCHDRAWN;
                break;
            default:
                if (legWinResult.currentLegWinner == TeamId.A)
                    outcome = DartMatchEventOutcome.LEGWONA;
                else
                    outcome = DartMatchEventOutcome.LEGWONB;
                break;
        }
        return outcome;
    }

    private boolean requestedFirstPlayerAtOche;

    /**
     * Returns next matchIncident Prompt
     * 
     * @return matchIncidentPrompt
     */
    @JsonIgnore
    @Override
    public MatchIncidentPrompt getNextPrompt() {
        MatchIncidentPrompt prompt;
        if (isMatchCompleted()) {
            prompt = new MatchIncidentPrompt("Match won", "-");
        } else {
            if (getPlayerAtOcheAtStartOfCurrentLeg() == TeamId.UNKNOWN) {
                requestedFirstPlayerAtOche = true;
                prompt = new MatchIncidentPrompt("Enter id of player starting at oche (A/B): ", "A");
            } else {
                requestedFirstPlayerAtOche = false;
                prompt = new MatchIncidentPrompt(
                                "Enter dart throw result (e.g. T20 S15,B1/2/3) B1 first dart bounce out;B2 second dart bounce out; B3 both first and second darts bounce out.",
                                "T20");
            }
        }
        return prompt;
    }

    /**
     * Returns match incident
     * 
     * @return matchEvent
     */
    @JsonIgnore
    @Override
    public MatchIncident getMatchIncident(String response) {
        TeamId teamId;
        DartMatchIncident incident;
        if (requestedFirstPlayerAtOche) {
            switch (response.toUpperCase()) {
                case "A":
                    teamId = TeamId.A;
                    break;
                case "B":
                    teamId = TeamId.B;
                    break;
                default:
                    return null;
            }
            incident = new DartMatchIncident(0, teamId);
        } else {
            String s = response.toUpperCase();
            String sMultiplier;
            int number;
            int multiplier;
            try {
                if (s.length() == 0)
                    return null;
                sMultiplier = s.substring(0, 1);
                switch (sMultiplier) {
                    case "S":
                        number = Integer.parseInt(s.substring(1));
                        multiplier = 1;
                        break;
                    case "D":
                        number = Integer.parseInt(s.substring(1));
                        multiplier = 2;
                        break;
                    case "T":
                        number = Integer.parseInt(s.substring(1));
                        multiplier = 3;
                        break;
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                    case "9":
                        number = Integer.parseInt(s);
                        multiplier = 1;
                        break;
                    case "B":
                        number = -1 * Integer.parseInt(s.substring(1, 2));
                        multiplier = 0;
                        break;
                    default:
                        throw new Exception();
                }
            } catch (Exception e) {
                return null;
            }
            incident = new DartMatchIncident(0, multiplier, number);
        }
        return incident;
    }

    /**
     * Returns map
     * 
     * @return map
     */
    @JsonIgnore
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        if (((DartMatchFormat) matchFormat).getnLegsPerSet() > 1) {
            map.put("Sets", String.format("%d-%d", playerAScore.sets, playerBScore.sets));
            map.put("Legs", String.format("%d-%d", playerAScore.legs, playerBScore.legs));
        } else {
            map.put("Legs", String.format("%d-%d", playerAScore.sets, playerBScore.sets));
        }
        map.put("Current leg", String.format("%d-%d", currentLeg.playerA.Points, currentLeg.playerB.Points));
        map.put("Player at Oche at start of leg", getPlayerAtOcheAtStartOfCurrentLeg().toString());
        map.put("Player at Oche now", String.format("%s (%d/3)", currentLeg.playerAtOche.toString(),
                        currentLeg.threeDartSet.noDartsThrown + 1));
        map.put("Current three dart score", String.format("%d", currentLeg.threeDartSet.getTotal()));
        map.put("No of 180s so far in match (A-B)", String.format("%d-%d", playerAScore.n180s, playerBScore.n180s));
        map.put("Highest checkout in match so far (A-B)",
                        String.format("%d-%d", playerAScore.highestCheckout, playerBScore.highestCheckout));
        map.put("First 180 in current leg (UNKNOWN = None scored)", currentLeg.first180InLeg.toString());
        map.put("No of darts thrown in current leg (A-B)",
                        String.format("%d-%d", currentLeg.playerA.NDartsthrown, currentLeg.playerB.NDartsthrown));
        String s = "No";
        if (matchHas9DartFinish)
            s = "Yes";
        map.put("9 dart finish so far in match", s);
        map.put("all", String.format("%d-%d-%d-%d-%d-%d", setLeg[getSetNo() - 1][0], setLeg[getSetNo() - 1][1],
                        setLeg180A[getSetNo() - 1][getLegNo() - 1], setLeg180B[getSetNo() - 1][getLegNo() - 1],
                        setLegCheckOutScoreA[getSetNo() - 1][getLegNo() - 1],
                        setLegCheckOutScoreB[getSetNo() - 1][getLegNo() - 1]));
        return map;
    }

    /**
     * 
     * 
     * @param map sets null
     * 
     */
    @Override
    public String setFromMap(Map<String, String> map) {
        /*
         * don't allow score to be updated by the user, at least for now, so don't need this method to do anything
         */
        return null;
    }

    /**
     * Returns true or false if the match is completed
     * 
     * @return true or false
     */
    @JsonIgnore
    @Override
    public boolean isMatchCompleted() {
        boolean matchCompleted = false;
        matchCompleted = matchCompleted || (playerAScore.sets >= setScoreToWin) || (playerBScore.sets >= setScoreToWin);
        matchCompleted = matchCompleted || (playerAScore.sets == setScoreToDraw && playerBScore.sets == setScoreToDraw);
        return matchCompleted;
    }

    /**
     * Returns the match winner or UNKNOWN if draw. If match not finished then Returns null
     * 
     * @return teamId
     */
    @JsonIgnore
    TeamId getMatchOutcome() {
        TeamId teamId = null;
        if (isMatchCompleted()) {
            if (playerAScore.sets > playerBScore.sets)
                teamId = TeamId.A;
            else if (playerBScore.sets > playerAScore.sets)
                teamId = TeamId.B;
            else
                teamId = TeamId.UNKNOWN; // draw
        }
        return teamId;

    }

    /**
     * Returns true or false if the match has 9 Dart Finish
     * 
     * @return true or false
     */
    @JsonIgnore
    boolean getMatchHas9DartFinish() {
        return matchHas9DartFinish;
    }

    /**
     * Returns current game period
     * 
     * @return gamePeriod
     */
    @JsonIgnore
    @Override
    public GamePeriod getGamePeriod() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns true if in preMatch
     * 
     * @return true or false
     */
    @Override
    public boolean preMatch() {
        return (playerAtOcheAtStartOfCurrentLeg == TeamId.UNKNOWN);
    }

    /**
     * Returns no of secs to go within current period. Returns zero if pre Match or at half time etc.
     * 
     * @return secs
     */
    @Override
    public int secsLeftInCurrentPeriod() {
        int secs = -1;
        return secs;

    }

    /**
     * gets the sequence id to use for match based markets
     * 
     * @return "M"
     */
    @JsonIgnore
    public String getSequenceIdForMatch() {
        return "M";
    }

    /**
     * Returns the sequence id to use for set based markets
     * 
     * @param setOffset 0 = current set, 1 = next set etc
     * @return null if specified set can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdforSet(int setOffset) {
        int setNo = this.getSetNo() + setOffset;
        if (setNo > matchFormat.getnLegsOrSetsInMatch())
            return null;
        else
            return String.format("S%d", setNo);
    }

    /**
     * Returns the sequence id to use for leg based markets (within the currently active set)
     * 
     * @param legOffset 0 = current leg, 1 = next leg etc
     * @return null if specified game can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForLeg(int legOffset) {
        return String.format("L%d.%d", this.getSetNo(), this.getLegNo() + legOffset);
    }

    /**
     * Returns the sequence id to use for leg based markets (within the currently active set)
     * 
     * @param legOffset 0 = current leg, 1 = next leg etc
     * @return null if specified leg can't occur, else the sequence id
     */
    @JsonIgnore
    public String getSequenceIdForSetLeg(int legOffset) {
        return String.format("L%d.%d", this.getSetNo() + legOffset, this.getLegNo());
    }

    /**
     * 
     * @return set no in range 1-5
     */
    @JsonIgnore
    public int getSetNo() {
        return this.playerAScore.getSets() + this.playerBScore.getSets() + 1;
    }

    /**
     * 
     * @return 180 no
     */
    @JsonIgnore
    public int get180No() {
        return this.playerAScore.getN180s() + this.playerBScore.getN180s() + 1;
    }

    /**
     * 
     * @return leg no starting at 1 for first game of set
     */
    @JsonIgnore
    public int getLegNo() {
        return this.playerAScore.getLegs() + this.playerBScore.getLegs() + 1;
    }

    /**
     * Returns true if the leg with No n (starting at 1 for first game in set) has not already been played and might be
     *
     * @param n leg No
     * @return true or false
     */
    @JsonIgnore
    boolean isLegMayBePlayed(int n) {
        return n >= getLegNo() && (n <= (legScoreToWinSet * 2 - 1));
    }

    /**
     * Returns true if the Set with No n (starting at 1 for first set) has not already been played and might be
     *
     * @param n set No
     * @return true or false
     */
    @JsonIgnore
    boolean isSetMayBePlayed(int n) {
        return n >= getSetNo() && (n <= (setScoreToWin * 2 - 1));
    }

    @Override
    public MatchState generateSimpleMatchState() {
        DartSimpleMatchState simpleMatchState = new DartSimpleMatchState(this.preMatch(), this.isMatchCompleted(),
                        this.playerAScore.sets, this.playerBScore.sets, this.playerAScore.legs, this.playerBScore.legs,
                        this.currentLeg.playerA.Points, this.currentLeg.playerB.Points, this.playerAScore.n180s,
                        this.playerBScore.n180s, this.currentLeg.playerA.NDartsthrown,
                        this.currentLeg.playerB.NDartsthrown, getPlayerAtOcheAtStartOfCurrentLeg(),
                        this.currentLeg.playerAtOche, this.currentLeg.first180InLeg

        );
        return simpleMatchState;
    }
}
