package ats.algo.sport.outrights.calcengine.leagues;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Map.Entry;

import ats.algo.core.markets.Market;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarket;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.core.markets.Selection;
import ats.algo.genericsupportfunctions.PairOfIntegers;
import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.OutrightsFixtureStatus;
import ats.algo.sport.outrights.calcengine.core.AbstractMarketsResulter;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.core.CompetitionProperties;
import ats.algo.sport.outrights.calcengine.core.Fixture;
import ats.algo.sport.outrights.calcengine.core.FixtureType;
import ats.algo.sport.outrights.calcengine.core.Fixtures;
import ats.algo.sport.outrights.calcengine.core.Standing;
import ats.algo.sport.outrights.calcengine.core.Standings;
import ats.algo.sport.outrights.calcengine.core.Teams;
import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

/**
 * contains the logic needed to result
 * 
 * @author gicha
 *
 */
public class LeagueMarketsResulter extends AbstractMarketsResulter {

    private static final Logger log = LoggerFactory.getLogger(LeagueMarketsResulter.class);

    private CompetitionType competitionType;
    private LocalDateTime xmas;
    private LeagueFormat format;
    /*
     * Map from marketType to function used to determine how to result
     */
    private Map<String, BiFunction<String, Set<String>, Boolean>> winningResulters;
    private Map<String, BiFunction<String, Set<String>, Boolean>> losingResulters;
    List<ResultStanding> resultStandings;
    Map<String, ResultStanding> resultStandingsTeamNameMap;
    Map<String, ResultStanding> resultStandingsTeamIdMap;
    private int nMatchesPerTeam;
    private String playoffWinnerTeamName;
    private List<String> playoffLoserTeamNames;

    private static final int MAX_POINTS_PER_MATCH = 3;

    public LeagueMarketsResulter(Competition competition) {
        this.competitionType = competition.getCompetitionType();
        this.format = (LeagueFormat) CompetitionProperties.competitionMatchFormatInstance(competition);
        this.xmas = competition.getDateOfXmas();
        Standings standings = competition.generateStandings();
        Teams teams = competition.getTeams();
        if (format.getnPromotedViaPlayoff() > 0)
            establishPlayOffWinnerAndLosers(competition.getFixtures(), teams);
        int nTeams = teams.size();
        nMatchesPerTeam = 2 * (nTeams - 1);
        /*
         * generate the arrays and maps we need to support the various resulting functions
         */
        resultStandings = new ArrayList<>(nTeams);
        resultStandingsTeamNameMap = new HashMap<>(nTeams);
        resultStandingsTeamIdMap = new HashMap<>(nTeams);
        Fixtures fixtures = competition.getFixtures();
        for (Standing standing : standings.finishOrder()) {
            String teamId = standing.getTeamId();
            String selectionName = teams.get(teamId).getDisplayName();
            ResultStanding r = new ResultStanding(teamId, selectionName, standing, fixtures);
            resultStandings.add(r);
            resultStandingsTeamNameMap.put(selectionName, r);
            resultStandingsTeamIdMap.put(teamId, r);
        }
        updateMaxMinPosns(resultStandings, r -> asAtSeasonEnd(r));
        updateMaxMinPosns(resultStandings, r -> getXmasResultStanding(r));
        int nRelegated = ((LeagueFormat) format).nRelegated;
        /*
         * generate the maps of resulting functions for each market type
         */
        winningResulters = new HashMap<>();
        winningResulters.put("C:SU",
                        (thisSelName, selNames) -> inTopN(thisSelName, nTeams - nRelegated, r -> asAtSeasonEnd(r)));// stay
                                                                                                                    // up
        winningResulters.put("C:R",
                        (thisSelName, selNames) -> notInTopN(thisSelName, nTeams - nRelegated, r -> asAtSeasonEnd(r)));// relegated
        winningResulters.put("C:RB",
                        (thisSelName, selNames) -> notInTopN(thisSelName, nTeams - 1, r -> asAtSeasonEnd(r)));// rock
                                                                                                              // bottom
        winningResulters.put("C:LW", (thisSelName, selNames) -> inTopN(thisSelName, 1, r -> asAtSeasonEnd(r)));// league
                                                                                                               // winner
        winningResulters.put("C:T4", (thisSelName, selNames) -> inTopN(thisSelName, 4, r -> asAtSeasonEnd(r)));// top 4
        winningResulters.put("C:O4", (thisSelName, selNames) -> notInTopN(thisSelName, 4, r -> asAtSeasonEnd(r)));// outside
                                                                                                                  // top
                                                                                                                  // 4
        winningResulters.put("C:TH", (thisSelName, selNames) -> inTopN(thisSelName, nTeams / 2, r -> asAtSeasonEnd(r)));// top
                                                                                                                        // half
        winningResulters.put("C:BH",
                        (thisSelName, selNames) -> notInTopN(thisSelName, nTeams / 2, r -> asAtSeasonEnd(r)));// bottom
                                                                                                              // half
        winningResulters.put("C:MTW", (thisSelName, selNames) -> seasonMarketWinner(thisSelName, selNames));// season
                                                                                                            // winner
        winningResulters.put("C:OU", (thisSelName, selNames) -> true);// over under. If prob is 1 then OU mkt must be
                                                                      // resulted
        winningResulters.put("C:XMST",
                        (thisSelName, selNames) -> inTopN(thisSelName, 1, r -> getXmasResultStanding(r))); // xmas
                                                                                                           // winner
        winningResulters.put("C:WOM", (thisSelName, selNames) -> inTopNExSelections(thisSelName, 1,
                        r -> asAtSeasonEnd(r), s -> excludeManCity(s)));
        winningResulters.put("C:PR", (thisSelName, selNames) -> promoted(thisSelName));
        winningResulters.put("C:PO", (thisSelName, selNames) -> inPlayOffs(thisSelName));

        losingResulters = new HashMap<>();
        losingResulters.put("C:SU",
                        (thisSelName, selNames) -> notInTopN(thisSelName, nTeams - nRelegated, r -> asAtSeasonEnd(r)));// stay
                                                                                                                       // up
        losingResulters.put("C:R",
                        (thisSelName, selNames) -> inTopN(thisSelName, nTeams - nRelegated, r -> asAtSeasonEnd(r)));// relegated
        losingResulters.put("C:RB", (thisSelName, selNames) -> inTopN(thisSelName, nTeams - 1, r -> asAtSeasonEnd(r)));// rock
                                                                                                                       // bottom
        losingResulters.put("C:LW", (thisSelName, selNames) -> notInTopN(thisSelName, 1, r -> asAtSeasonEnd(r)));// league
                                                                                                                 // winner
        losingResulters.put("C:T4", (thisSelName, selNames) -> notInTopN(thisSelName, 4, r -> asAtSeasonEnd(r)));// top
                                                                                                                 // 4
        losingResulters.put("C:O4", (thisSelName, selNames) -> inTopN(thisSelName, 4, r -> asAtSeasonEnd(r)));// outside
                                                                                                              // top 4
        losingResulters.put("C:TH",
                        (thisSelName, selNames) -> notInTopN(thisSelName, nTeams / 2, r -> asAtSeasonEnd(r)));// top
                                                                                                              // half
        losingResulters.put("C:BH", (thisSelName, selNames) -> inTopN(thisSelName, nTeams / 2, r -> asAtSeasonEnd(r)));// bottom
                                                                                                                       // half
        losingResulters.put("C:MTW", (thisSelName, selNames) -> seasonMarketLoser(thisSelName, selNames));// season
                                                                                                          // winner
        losingResulters.put("C:OU", (thisSelName, selNames) -> true);// over under. If prob is 0 then OU mkt must be
                                                                     // resulted
        losingResulters.put("C:XMST",
                        (thisSelName, selNames) -> notInTopN(thisSelName, 1, r -> getXmasResultStanding(r)));
        losingResulters.put("C:WOM", (thisSelName, selNames) -> notInTopNExSelections(thisSelName, 1,
                        r -> asAtSeasonEnd(r), s -> excludeManCity(s)));
        losingResulters.put("C:PR", (thisSelName, selNames) -> notPromoted(thisSelName));
        losingResulters.put("C:PO", (thisSelName, selNames) -> notInPlayOffs(thisSelName));
    }

    private void establishPlayOffWinnerAndLosers(Fixtures fixtures, Teams teams) {
        Fixture fixture = fixtures.getByFixtureID(PlayoffsMgr.PLAY_OFF_FINAL);
        if (fixture != null) {
            String teamId = fixture.winner();
            if (teamId != null)
                this.playoffWinnerTeamName = teams.get(teamId).getDisplayName();
        }
        playoffLoserTeamNames = new ArrayList<>(2);
        String teamId1 = fixtures.loserOverTwoLegs(PlayoffsMgr.PLAY_OFF1_LEG2);
        if (teamId1 != null)
            this.playoffLoserTeamNames.add(teams.get(teamId1).getDisplayName());
        String teamId2 = fixtures.loserOverTwoLegs(PlayoffsMgr.PLAY_OFF2_LEG2);
        if (teamId2 != null)
            this.playoffLoserTeamNames.add(teams.get(teamId2).getDisplayName());
    }

    private void updateMaxMinPosns(List<ResultStanding> resultStandings, Function<ResultStanding, StandingAtDate> fn) {
        for (ResultStanding thisR : resultStandings) {
            PairOfIntegers p = getHighestLowest(thisR, resultStandings, fn, r -> false);
            StandingAtDate sd = fn.apply(thisR);
            sd.highestPossiblePosn = p.A;
            sd.lowestPossiblePosn = p.B;
        }
    }

    private PairOfIntegers getHighestLowest(ResultStanding thisR, List<ResultStanding> resultStandings,
                    Function<ResultStanding, StandingAtDate> fn, Predicate<String> excludeSelection) {
        PairOfIntegers result = new PairOfIntegers(1, 1);
        for (ResultStanding otherR : resultStandings) {
            if (thisR == otherR || excludeSelection.test(otherR.selName))
                continue;
            int n = compareResultStandingsByHighestAchievable(thisR, otherR, fn);
            if (n > 0)
                result.A++;
            int m = compareResultStandingsByLowestAchievable(thisR, otherR, fn);
            if (m >= 0)
                result.B++;
        }
        return result;
    }

    /**
     * calc # matches this team has to play before xmas
     * 
     * @param teamId
     * @param fixtures
     * @return
     */
    private int noMatchesToXmas(String teamId, Fixtures fixtures) {
        int n = 0;
        for (Fixture fixture : fixtures) {
            if (fixture.getStatus() != OutrightsFixtureStatus.COMPLETED
                            && fixture.getFixtureType().equals(FixtureType.LEAGUE))
                if ((fixture.getHomeTeamID().equals(teamId) || fixture.getAwayTeamID().equals(teamId)))
                    if (!fixture.getDateTime().isAfter(xmas))
                        n++;
        }
        return n;
    }

    @Override
    public ResultedMarkets execute(Markets markets) {
        ResultedMarkets resultedMarkets = new ResultedMarkets();
        switch (competitionType) {
            case PREMIER_LEAGUE:
            case CHAMPIONSHIP_LEAGUE:
            case LA_LIGA:
                break;
            default:
                log.error("Support for this competition type not yet implemented: " + competitionType);
                return resultedMarkets; // empty map
        }

        /*
         * re-use the winningSelections and losingSelections arrays rather than create new for efficiency
         */

        for (Entry<String, Market> e : markets.getMarkets().entrySet()) {
            List<String> winningSelections = new ArrayList<>();
            List<String> losingSelections = new ArrayList<>();
            Market market = e.getValue();
            String mktType = market.getType();
            Map<String, Selection> selections = market.getSelections();
            BiFunction<String, Set<String>, Boolean> losingResulter = getLosingResulter(mktType);
            BiFunction<String, Set<String>, Boolean> winningResulter = getWinningResulter(mktType);
            if (losingResulter == null || winningResulter == null)
                throw new IllegalArgumentException("Missing resulter for mktType: " + mktType);
            for (Entry<String, Selection> e2 : selections.entrySet()) {
                String selName = e2.getKey();
                Selection selection = e2.getValue();
                if (selection.getProb() < ZERO_THRESHOLD) {
                    if (losingResulter.apply(selName, selections.keySet())) {
                        losingSelections.add(selName);
                    }
                } else if (selection.getProb() > ONE_THRESHOLD) {
                    if (winningResulter.apply(selName, selections.keySet()))
                        winningSelections.add(selName);
                }
            }
            int nResultedSelections = winningSelections.size() + losingSelections.size();
            if (nResultedSelections > 0) {
                ResultedMarket resultedMarket = new ResultedMarket(market.getType(), market.getLineId(),
                                market.getCategory(), market.getSequenceId(), false, market.getMarketDescription(),
                                winningSelections, 0);
                resultedMarket.setLosingSelections(losingSelections);
                boolean fullyResulted = nResultedSelections == selections.size();
                resultedMarket.setFullyResulted(fullyResulted);
                resultedMarkets.addMarket(resultedMarket);
            }
        }
        return resultedMarkets;
    }

    private BiFunction<String, Set<String>, Boolean> getWinningResulter(String mktType) {
        String key = strippedMktType(mktType);
        return winningResulters.get(key);
    }

    private BiFunction<String, Set<String>, Boolean> getLosingResulter(String mktType) {
        String key = strippedMktType(mktType);
        return losingResulters.get(key);
    }

    /**
     * remove anything after a second ":" in the mktType
     * 
     * @param mktType
     * @return
     */
    private String strippedMktType(String mktType) {
        int n1 = mktType.indexOf(":");
        if (n1 == -1)
            return mktType;
        int n2 = mktType.indexOf(":", n1 + 1);
        if (n2 == -1)
            return mktType;
        return mktType.substring(0, n2);
    }

    /**
     * returns true if this selection will finish in top n teams, even if all other teams get max points from their
     * remaining matches
     * 
     * @param selectionName
     * @param n
     * @return
     */
    boolean inTopN(String selName, int n, Function<ResultStanding, StandingAtDate> fn) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        StandingAtDate sd = fn.apply(thisR);
        return sd.lowestPossiblePosn <= n;

    }

    /**
     * returns true if this selection will NOT finish in top n teams, even if it scores max possible points and all
     * other teams get zero points from their remaining matches
     * 
     * @param selectionName
     * @param n
     * @return
     */
    boolean notInTopN(String selName, int n, Function<ResultStanding, StandingAtDate> fn) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        StandingAtDate sd = fn.apply(thisR);
        return sd.highestPossiblePosn > n;
    }

    /**
     * returns true if this selection will finish in top n teams, even if all other teams get max points from their
     * remaining matches
     * 
     * @param selectionName
     * @param n
     * @return
     */
    boolean inTopNExSelections(String selName, int n, Function<ResultStanding, StandingAtDate> asAtDate,
                    Predicate<String> excludeSelection) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        PairOfIntegers p = getHighestLowest(thisR, resultStandings, asAtDate, excludeSelection);
        return p.B <= n;
    }

    boolean notInTopNExSelections(String selName, int n, Function<ResultStanding, StandingAtDate> asAtDate,
                    Predicate<String> excludeSelection) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        PairOfIntegers p = getHighestLowest(thisR, resultStandings, asAtDate, excludeSelection);
        return p.A > n;
    }

    boolean seasonMarketWinner(String selName, Set<String> selectionNames) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        PairOfIntegers p = getHighestLowest(thisR, resultStandings, r -> asAtSeasonEnd(r),
                        s -> excludeNamesNotInSet(s, selectionNames));
        return p.B == 1;

    }

    boolean seasonMarketLoser(String selName, Set<String> selectionNames) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        PairOfIntegers p = getHighestLowest(thisR, resultStandings, r -> asAtSeasonEnd(r),
                        s -> excludeNamesNotInSet(s, selectionNames));
        return p.A == 2;
    }

    private boolean excludeNamesNotInSet(String selName, Set<String> selectionNames) {
        return !selectionNames.contains(selName);
    }

    boolean excludeManCity(String selName) {
        return selName.equals(PremierLeagueMarketsFactory.MAN_CITY);
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("PosnsNow:\n");
        resultStandings.forEach(r -> s.append(r).append("\n"));
        return s.toString();
    }

    StandingAtDate asAtSeasonEnd(ResultStanding r) {
        return r.seasonEnd;
    }

    StandingAtDate getXmasResultStanding(ResultStanding r) {
        return r.xmas;
    }

    /**
     * compares current points of thisR against maxPoints of OtherR
     * 
     * @param r1
     * @param r2
     * @param by
     * @return
     */
    private int compareResultStandingsByHighestAchievable(ResultStanding thisR, ResultStanding otherR,
                    Function<ResultStanding, StandingAtDate> by) {
        StandingAtDate thisSd = by.apply(thisR);
        StandingAtDate otherSd = by.apply(otherR);

        int ptsDiff = otherR.pointsNow - thisSd.maxPoints;
        if (ptsDiff != 0)
            return ptsDiff;
        if (thisSd.matchesToGo == 0 && otherSd.matchesToGo == 0) {
            /*
             * only compare goaldiff etc if teams have finished all their matches
             */
            int goalsDiff = otherR.goalDiffNow - thisR.goalDiffNow;
            if (goalsDiff != 0)
                return goalsDiff;
            return otherR.goalsScoredNow - thisR.goalsScoredNow;
        } else {
            /*
             * return tie if not both finished
             */
            return 0;
        }
    }

    private int compareResultStandingsByLowestAchievable(ResultStanding thisR, ResultStanding otherR,
                    Function<ResultStanding, StandingAtDate> by) {
        StandingAtDate thisSd = by.apply(thisR);
        StandingAtDate otherSd = by.apply(otherR);

        int ptsDiff = otherSd.maxPoints - thisR.pointsNow;
        if (ptsDiff != 0)
            return ptsDiff;
        if (thisSd.matchesToGo == 0 && otherSd.matchesToGo == 0) {
            /*
             * only compare goaldiff etc if teams have finished all their matches
             */
            int goalsDiff = otherR.goalDiffNow - thisR.goalDiffNow;
            if (goalsDiff != 0)
                return goalsDiff;
            return otherR.goalsScoredNow - thisR.goalsScoredNow;
        } else {
            /*
             * return tie if not both finished
             */
            return 0;
        }
    }

    boolean inPlayOffs(String selName) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        StandingAtDate sd = thisR.seasonEnd;
        int highestRankInPlayoffs = format.nPromotedUnconditionally + 1;
        int lowestRankInPlayoffs = format.nPromotedUnconditionally + format.nTeamsInPlayoff;
        return sd.highestPossiblePosn >= highestRankInPlayoffs && sd.lowestPossiblePosn <= lowestRankInPlayoffs;

    }

    boolean notInPlayOffs(String selName) {
        ResultStanding thisR = resultStandingsTeamNameMap.get(selName);
        StandingAtDate sd = thisR.seasonEnd;
        int highestRankInPlayoffs = format.nPromotedUnconditionally + 1;
        int lowestRankInPlayoffs = format.nPromotedUnconditionally + format.nTeamsInPlayoff;
        return sd.lowestPossiblePosn < highestRankInPlayoffs || sd.highestPossiblePosn > lowestRankInPlayoffs;
    }

    /**
     * assumes only one team promoted via the play-offs
     * 
     * @param selName
     * @return
     */
    boolean promoted(String selName) {
        if (inTopN(selName, format.nPromotedUnconditionally, r -> asAtSeasonEnd(r)))
            return true;
        if (selName.equals(playoffWinnerTeamName))
            return true;
        return false;
    }

    /**
     * 
     * @param thisSelName
     * @return
     */
    boolean notPromoted(String selName) {
        if (notInTopN(selName, format.nPromotedUnconditionally + format.nTeamsInPlayoff, r -> asAtSeasonEnd(r)))
            return true;
        if (playoffWinnerTeamName != null)
            if (!selName.equals(playoffWinnerTeamName))
                return true;
        for (String loserName : playoffLoserTeamNames)
            if (selName.equals(loserName))
                return true;
        return false;
    }

    /**
     * returns the id of the team finishing exactly n'th if known
     * 
     * @param n
     * @return null if not known, else teamId
     */
    public String getTeamFinishingNth(int n) {
        ResultStanding r = resultStandings.get(n - 1);
        StandingAtDate s = r.seasonEnd;
        if (s.highestPossiblePosn == n && s.lowestPossiblePosn == n)
            return r.teamId;
        return null;
    }

    public int getHighestPossibleFinishPosn(String id) {
        ResultStanding r = resultStandingsTeamIdMap.get(id);
        if (r == null)
            return 0;
        return r.seasonEnd.highestPossiblePosn;
    }

    public int getLowestPossibleFinishPosn(String id) {
        ResultStanding r = resultStandingsTeamIdMap.get(id);
        if (r == null)
            return 0;
        return r.seasonEnd.lowestPossiblePosn;
    }


    class ResultStanding {
        String teamId;
        String selName;
        int nPlayedNow;
        int goalDiffNow;
        int goalsScoredNow;
        int pointsNow;
        StandingAtDate seasonEnd;
        StandingAtDate xmas;

        /**
         * 
         * @param selectionName
         * @param pointsNow
         * @param pointsMax
         * @param leaguePositionNow
         * @param leaguePositionMax
         */
        public ResultStanding(String teamId, String selName, Standing standing, Fixtures fixtures) {
            this.teamId = teamId;
            this.selName = selName;
            this.nPlayedNow = standing.getPlayed();
            this.pointsNow = standing.getPoints();
            this.goalsScoredNow = standing.getGoalsFor();
            this.goalDiffNow = standing.getGoalsDiff();
            seasonEnd = new StandingAtDate(nMatchesPerTeam - nPlayedNow, pointsNow);
            xmas = new StandingAtDate(noMatchesToXmas(standing.getTeamId(), fixtures), pointsNow);
        }

        @Override
        public String toString() {
            return "ResultStanding [teamId=" + teamId + ", selName=" + selName + ", nPlayedNow=" + nPlayedNow
                            + ", goalDiffNow=" + goalDiffNow + ", goalsScoredNow=" + goalsScoredNow + ", pointsNow="
                            + pointsNow + ", seasonEnd=" + seasonEnd + ", xmas=" + xmas + "]";
        }
    }

    class StandingAtDate {
        int matchesToGo;
        int maxPoints;
        int highestPossiblePosn;
        int lowestPossiblePosn;

        public StandingAtDate(int matchesToGo, int pointsNow) {
            super();
            this.matchesToGo = matchesToGo;
            this.maxPoints = pointsNow + matchesToGo * MAX_POINTS_PER_MATCH;
        }

        @Override
        public String toString() {
            return "StandingAtDate [matchesToGo=" + matchesToGo + ", maxPoints=" + maxPoints + ", highestPossiblePosn="
                            + highestPossiblePosn + ", lowestPossiblePosn=" + lowestPossiblePosn + "]";
        }

    }


}
