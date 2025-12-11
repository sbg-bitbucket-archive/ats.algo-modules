package ats.algo.sport.football;

import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.matchresult.MatchResultElement;
import ats.algo.core.matchresult.MatchResultEntryType;
import ats.algo.core.matchresult.MatchResultMap;
import ats.algo.core.matchresult.MatchResulter;
import ats.algo.genericsupportfunctions.PairOfIntegers;


public class FootballMatchResulter extends MatchResulter {

    @Override
    public MatchResultMap generateProforma(MatchFormat matchFormat) {

        MatchResultMap result = new MatchResultMap();

        result.put("firstHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("secondHalfGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("totalGoals", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

        result.put("firstHalfCorners", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("secondHalfCorners", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("totalCorners", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

        result.put("firstHalfYellowCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("secondHalfYellowCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("totalYellowCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

        result.put("firstHalfRedCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("secondHalfRedCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        result.put("totalRedCards", new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

        if (((FootballMatchFormat) matchFormat).getExtraTimeMinutes() > 0) {
            result.put("extraTimeFirstHalfGoals",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
            result.put("extraTimeSecondHalfGoals",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

            result.put("extraTimeFirstHalfCorners",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
            result.put("extraTimeSecondHalfCorners",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

            result.put("extraTimeFirstHalfYellowCards",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
            result.put("extraTimeSecondHalfYellowCards",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));

            result.put("extraTimeFirstHalfRedCards",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
            result.put("extraTimeSecondHalfRedCards",
                            new MatchResultElement(MatchResultEntryType.INT_HYPHEN_INT, 0, 50, "-1-0"));
        }
        return result;
    }

    @Override
    public MatchState generateMatchStateForMatchResult(MatchState matchState, MatchResultMap matchManualResult,
                    boolean useSimpleMatchState) {

        boolean halfGoalsInput = false;
        boolean halfCornersInput = false;
        boolean halfYellowCardsInput = false;
        boolean halfRedCardsInput = false;

        boolean fullGoalsInput = false;
        boolean fullCornersInput = false;
        boolean fullYellowCardsInput = false;
        boolean fullRedCardsInput = false;

        Map<String, MatchResultElement> map = matchManualResult.getMap();
        int numOfScores = 2;

        if (((FootballMatchFormat) matchState.getMatchFormat()).getExtraTimeMinutes() > 0)
            numOfScores = 4;

        PairOfIntegers[] halfResultGoals = new PairOfIntegers[numOfScores];
        PairOfIntegers[] halfResultCorners = new PairOfIntegers[numOfScores];
        PairOfIntegers[] halfResultYellowCards = new PairOfIntegers[numOfScores];
        PairOfIntegers[] halfResultRedCards = new PairOfIntegers[numOfScores];

        PairOfIntegers[] ftResultGoals = new PairOfIntegers[1];
        PairOfIntegers[] ftResultCorners = new PairOfIntegers[1];
        PairOfIntegers[] ftResultYellowCards = new PairOfIntegers[1];
        PairOfIntegers[] ftResultRedCards = new PairOfIntegers[1];

        if (map.get("firstHalfGoals") != null && map.get("secondHalfGoals") != null)
            if (map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultGoals[0] = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfResultGoals[1] = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultGoals[0].A > -1 || halfResultGoals[1].A > -1 || halfResultGoals[0].B > -1
                                || halfResultGoals[1].B > -1)) {
                    halfGoalsInput = true;
                }
            } else if (map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultGoals[0] = map.get("firstHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfResultGoals[1] = map.get("secondHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfGoalsInput = true;
            }
        if (map.get("totalGoals") != null)
            if (map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultGoals[0] = map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultGoals[0].A > -1 || ftResultGoals[1].B > -1) {
                    fullGoalsInput = true;
                }
            } else if (map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultGoals[0] = map.get("totalGoals").valueAsPairOfIntegersCheckingNegatives();
                fullGoalsInput = true;
            }

        if (map.get("firstHalfCorners") != null && map.get("secondHalfCorners") != null)
            if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultCorners[0].A > -1 || halfResultCorners[1].A > -1 || halfResultCorners[0].B > -1
                                || halfResultCorners[1].B > -1)) {
                    halfCornersInput = true;
                }
            } else if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfCornersInput = true;
            }
        if (map.get("totalCorners") != null)
            if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultCorners[0].A > -1 || ftResultCorners[1].B > -1) {
                    fullCornersInput = true;
                }
            } else if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();
                fullCornersInput = true;
            }
        if (map.get("firstHalfCorners") != null && map.get("secondHalfCorners") != null)
            if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultCorners[0].A > -1 || halfResultCorners[1].A > -1 || halfResultCorners[0].B > -1
                                || halfResultCorners[1].B > -1)) {
                    halfCornersInput = true;
                }
            } else if (map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultCorners[0] = map.get("firstHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfResultCorners[1] = map.get("secondHalfCorners").valueAsPairOfIntegersCheckingNegatives();
                halfCornersInput = true;
            }
        if (map.get("totalCorners") != null)
            if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultCorners[0].A > -1 || ftResultCorners[1].B > -1) {
                    fullCornersInput = true;
                }
            } else if (map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultCorners[0] = map.get("totalCorners").valueAsPairOfIntegersCheckingNegatives();
                fullCornersInput = true;
            }

        if (map.get("firstHalfYellowCards") != null && map.get("secondHalfYellowCards") != null)
            if (map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultYellowCards[0] = map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultYellowCards[1] = map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultYellowCards[0].A > -1 || halfResultYellowCards[1].A > -1
                                || halfResultYellowCards[0].B > -1 || halfResultYellowCards[1].B > -1)) {
                    halfYellowCardsInput = true;
                }
            } else if (map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultYellowCards[0] = map.get("firstHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultYellowCards[1] = map.get("secondHalfYellowCards").valueAsPairOfIntegersCheckingNegatives();
                halfYellowCardsInput = true;
            }

        if (map.get("totalYellowCards") != null)
            if (map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultYellowCards[0] = map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultYellowCards[0].A > -1 || ftResultYellowCards[1].B > -1) {
                    fullYellowCardsInput = true;
                }
            } else if (map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultYellowCards[0] = map.get("totalYellowCards").valueAsPairOfIntegersCheckingNegatives();
                fullYellowCardsInput = true;
            }
        if (map.get("firstHalfRedCards") != null)
            if (map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultRedCards[0] = map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultRedCards[1] = map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                if ((halfResultRedCards[0].A > -1 || halfResultRedCards[1].A > -1 || halfResultRedCards[0].B > -1
                                || halfResultRedCards[1].B > -1)) {
                    halfRedCardsInput = true;
                }
            } else if (map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null
                            && map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                halfResultRedCards[0] = map.get("firstHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                halfResultRedCards[1] = map.get("secondHalfRedCards").valueAsPairOfIntegersCheckingNegatives();
                halfRedCardsInput = true;
            }
        if (map.get("totalRedCards") != null)
            if (map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultRedCards[0] = map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives();

                if (ftResultRedCards[0].A > -1 || ftResultRedCards[1].B > -1) {
                    fullRedCardsInput = true;
                }
            } else if (map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives() != null) {

                ftResultRedCards[0] = map.get("totalRedCards").valueAsPairOfIntegersCheckingNegatives();
                fullRedCardsInput = true;
            }

        if (numOfScores == 4) {
            if (map.get("extraTimeFirstHalfGoals") != null && map.get("extraTimeSecondHalfGoals") != null) {
                halfResultGoals[2] = map.get("extraTimeFirstHalfGoals").valueAsPairOfIntegersCheckingNegatives();
                halfResultGoals[3] = map.get("extraTimeSecondHalfGoals").valueAsPairOfIntegersCheckingNegatives();
            } else
                numOfScores = 2;
        }

        FootballMatchState endMatchState = (FootballMatchState) matchState.copy();

        if (fullGoalsInput) {
            if (halfGoalsInput) {
                updateMatchStateForPeriodGoals(endMatchState, halfResultGoals, numOfScores);
            } else {
                updateMatchStateForFullTimeGoals(endMatchState, ftResultGoals);
            }
        } else if (halfGoalsInput) {
            updateMatchStateForPeriodGoals(endMatchState, halfResultGoals, numOfScores);
        }
        if (fullCornersInput) {
            if (halfCornersInput) {
                updateMatchStateForPeriodCorners(endMatchState, halfResultCorners, numOfScores);
            } else {
                updateMatchStateForFullTimeCorners(endMatchState, ftResultCorners);
            }
        } else if (halfCornersInput) {
            updateMatchStateForPeriodCorners(endMatchState, halfResultCorners, numOfScores);
        }
        if (fullRedCardsInput || fullYellowCardsInput) {
            if (halfRedCardsInput || halfYellowCardsInput) {
                updateMatchStateForPeriodCards(endMatchState, halfResultRedCards, halfRedCardsInput,
                                halfResultYellowCards, halfYellowCardsInput, numOfScores);
            } else {
                updateMatchStateForFullTimeCards(endMatchState, ftResultRedCards, fullRedCardsInput,
                                ftResultYellowCards, fullYellowCardsInput);
            }
        } else if (halfRedCardsInput || halfYellowCardsInput) {
            updateMatchStateForPeriodCards(endMatchState, halfResultRedCards, halfRedCardsInput, halfResultYellowCards,
                            halfYellowCardsInput, numOfScores);
        }

        endMatchState.setMatchPeriod(FootballMatchPeriod.MATCH_COMPLETED);
        if (useSimpleMatchState) {
            MatchState simpleMatchState = endMatchState.generateSimpleMatchState();
            return simpleMatchState;
        }
        return endMatchState;
    }

    private void updateMatchStateForFullTimeCards(FootballMatchState endMatchState, PairOfIntegers[] ftResultRedCards,
                    boolean fullRedCardsInput, PairOfIntegers[] ftResultYellowCards, boolean fullYellowCardsInput) {

        int totalYellowCardsA = 0;
        int totalYellowCardsB = 0;
        int totalRedCardsA = 0;
        int totalRedCardsB = 0;

        if (fullYellowCardsInput) {
            totalYellowCardsA = ftResultYellowCards[0].A;
            totalYellowCardsB = ftResultYellowCards[0].B;

            endMatchState.setYellowCardsA(totalYellowCardsA);
            endMatchState.setYellowCardsB(totalYellowCardsB);
        }

        if (fullRedCardsInput) {
            totalRedCardsA = ftResultRedCards[0].A;
            totalRedCardsB = ftResultRedCards[0].B;

            endMatchState.setRedCardsA(totalRedCardsA);
            endMatchState.setRedCardsB(totalRedCardsB);
        }

        if (fullYellowCardsInput && fullRedCardsInput) {
            endMatchState.setCardsA(totalRedCardsA + totalYellowCardsA);
            endMatchState.setCardsB(totalRedCardsB + totalYellowCardsB);
        }

    }

    private void updateMatchStateForFullTimeCorners(FootballMatchState endMatchState,
                    PairOfIntegers[] ftResultCorners) {
        int totalCornersA = ftResultCorners[0].A;
        int totalCornersB = ftResultCorners[0].B;

        endMatchState.setCornersA(totalCornersA);
        endMatchState.setCornersB(totalCornersB);

    }

    private void updateMatchStateForFullTimeGoals(FootballMatchState endMatchState, PairOfIntegers[] ftResultGoals) {

        int totalGoalsA = ftResultGoals[0].A;
        int totalGoalsB = ftResultGoals[0].B;

        endMatchState.setGoalsA(totalGoalsA);
        endMatchState.setGoalsB(totalGoalsB);
    }

    private void updateMatchStateForPeriodCards(FootballMatchState endMatchState, PairOfIntegers[] halfResultRedCards,
                    boolean redCardsInput, PairOfIntegers[] halfResultYellowCards, boolean yellowCardsInput,
                    int numOfScores) {

        int totalRedCardsAForNormalTime = 0;
        int totalRedCardsBForNormalTime = 0;

        int totalRedCardsAForExtraTimeFirstHalf = 0;
        int totalRedCardsAForExtraTimeSecondHalf = 0;

        int totalRedCardsBForExtraTimeFirstHalf = 0;
        int totalRedCardsBForExtraTimeSecondHalf = 0;

        if (redCardsInput) {
            for (int i = 0; i < numOfScores; i++) {

                int halfRedCardsTeamA = halfResultRedCards[i].A;
                int halfRedCardsTeamB = halfResultRedCards[i].B;

                endMatchState.setRedCardsInPeriodN(i, halfRedCardsTeamA, halfRedCardsTeamB);

                if (i < 2) {
                    totalRedCardsAForNormalTime += halfRedCardsTeamA;
                    totalRedCardsBForNormalTime += halfRedCardsTeamB;
                }

                if (i > 1) {
                    if (i == 2) {
                        totalRedCardsAForExtraTimeFirstHalf = halfRedCardsTeamA;
                        totalRedCardsBForExtraTimeFirstHalf = halfRedCardsTeamB;
                    }

                    if (i == 3) {
                        totalRedCardsAForExtraTimeSecondHalf = halfRedCardsTeamA;
                        totalRedCardsBForExtraTimeSecondHalf = halfRedCardsTeamB;
                    }
                }
            }
        }

        int totalRedCardsA = totalRedCardsAForNormalTime + totalRedCardsAForExtraTimeFirstHalf
                        + totalRedCardsAForExtraTimeSecondHalf;
        int totalRedCardsB = totalRedCardsBForNormalTime + totalRedCardsBForExtraTimeFirstHalf
                        + totalRedCardsBForExtraTimeSecondHalf;

        endMatchState.setRedCardsA(totalRedCardsA);
        endMatchState.setRedCardsB(totalRedCardsB);

        endMatchState.setNormalTimeRedCardsA(totalRedCardsAForNormalTime);
        endMatchState.setNormalTimeRedCardsB(totalRedCardsBForNormalTime);

        endMatchState.setExtraTimeFHRedCardsA(totalRedCardsAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHRedCardsB(totalRedCardsBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHRedCardsA(totalRedCardsAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHRedCardsB(totalRedCardsBForExtraTimeSecondHalf);

        if (redCardsInput) {
            endMatchState.setPreviousPeriodRedCardsA(halfResultRedCards[0].A);
            endMatchState.setPreviousPeriodRedCardsB(halfResultRedCards[0].B);
            endMatchState.setCurrentPeriodRedCardsA(halfResultRedCards[1].A);
            endMatchState.setCurrentPeriodRedCardsB(halfResultRedCards[1].B);
        }

        int totalYellowCardsAForNormalTime = 0;
        int totalYellowCardsBForNormalTime = 0;

        int totalYellowCardsAForExtraTimeFirstHalf = 0;
        int totalYellowCardsAForExtraTimeSecondHalf = 0;

        int totalYellowCardsBForExtraTimeFirstHalf = 0;
        int totalYellowCardsBForExtraTimeSecondHalf = 0;

        if (yellowCardsInput) {
            for (int i = 0; i < numOfScores; i++) {

                int halfYellowCardsTeamA = halfResultYellowCards[i].A;
                int halfYellowCardsTeamB = halfResultYellowCards[i].B;

                endMatchState.setYellowCardsInPeriodN(i, halfYellowCardsTeamA, halfYellowCardsTeamB);

                if (i < 2) {
                    totalYellowCardsAForNormalTime += halfYellowCardsTeamA;
                    totalYellowCardsBForNormalTime += halfYellowCardsTeamB;
                }

                if (i > 1) {
                    if (i == 2) {
                        totalYellowCardsAForExtraTimeFirstHalf = halfYellowCardsTeamA;
                        totalYellowCardsBForExtraTimeFirstHalf = halfYellowCardsTeamB;
                    }

                    if (i == 3) {
                        totalYellowCardsAForExtraTimeSecondHalf = halfYellowCardsTeamA;
                        totalYellowCardsBForExtraTimeSecondHalf = halfYellowCardsTeamB;
                    }
                }
            }
        }

        int totalYellowCardsA = totalYellowCardsAForNormalTime + totalYellowCardsAForExtraTimeFirstHalf
                        + totalYellowCardsAForExtraTimeSecondHalf;
        int totalYellowCardsB = totalYellowCardsBForNormalTime + totalYellowCardsBForExtraTimeFirstHalf
                        + totalYellowCardsBForExtraTimeSecondHalf;

        endMatchState.setYellowCardsA(totalYellowCardsA);
        endMatchState.setYellowCardsB(totalYellowCardsB);

        endMatchState.setNormalTimeYellowCardsA(totalYellowCardsAForNormalTime);
        endMatchState.setNormalTimeYellowCardsB(totalYellowCardsBForNormalTime);

        endMatchState.setExtraTimeFHYellowCardsA(totalYellowCardsAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHYellowCardsB(totalYellowCardsBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHYellowCardsA(totalYellowCardsAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHYellowCardsB(totalYellowCardsBForExtraTimeSecondHalf);

        if (yellowCardsInput) {
            endMatchState.setPreviousPeriodYellowCardsA(halfResultYellowCards[0].A);
            endMatchState.setPreviousPeriodYellowCardsB(halfResultYellowCards[0].B);
            endMatchState.setCurrentPeriodYellowCardsA(halfResultYellowCards[1].A);
            endMatchState.setCurrentPeriodYellowCardsB(halfResultYellowCards[1].B);
        }

        endMatchState.setCardsA(endMatchState.getCardsA());
        endMatchState.setCardsB(endMatchState.getCardsB());

        endMatchState.setNormalTimeCardsA(endMatchState.getNormalTimeCardsA());
        endMatchState.setNormalTimeCardsB(endMatchState.getNormalTimeCardsA());

        endMatchState.setExtraTimeFHCardsA(endMatchState.getExtraTimeFHCardsA());
        endMatchState.setExtraTimeFHCardsB(endMatchState.getExtraTimeFHCardsB());

        endMatchState.setExtraTimeSHCardsA(endMatchState.getExtraTimeSHCardsA());
        endMatchState.setExtraTimeSHCardsB(endMatchState.getExtraTimeSHCardsB());

        endMatchState.setPreviousPeriodCardsA(endMatchState.getPreviousPeriodCardsA());
        endMatchState.setPreviousPeriodCardsB(endMatchState.getPreviousPeriodCardsB());
        endMatchState.setCurrentPeriodCardsA(endMatchState.getCurrentPeriodCardsA());
        endMatchState.setCurrentPeriodCardsB(endMatchState.getCurrentPeriodCardsB());

    }

    private void updateMatchStateForPeriodCorners(FootballMatchState endMatchState, PairOfIntegers[] halfResultCorners,
                    int numOfScores) {

        int totalCornersAForNormalTime = 0;
        int totalCornersBForNormalTime = 0;

        int totalCornersAForExtraTimeFirstHalf = 0;
        int totalCornersAForExtraTimeSecondHalf = 0;

        int totalCornersBForExtraTimeFirstHalf = 0;
        int totalCornersBForExtraTimeSecondHalf = 0;

        for (int i = 0; i < numOfScores; i++) {

            int halfCornersTeamA = halfResultCorners[i].A;
            int halfCornersTeamB = halfResultCorners[i].B;

            endMatchState.setCornersInPeriodN(i, halfCornersTeamA, halfCornersTeamB);

            if (i < 2) {
                totalCornersAForNormalTime += halfCornersTeamA;
                totalCornersBForNormalTime += halfCornersTeamB;
            }

            if (i > 1) {
                if (i == 2) {
                    totalCornersAForExtraTimeFirstHalf = halfCornersTeamA;
                    totalCornersBForExtraTimeFirstHalf = halfCornersTeamB;
                }

                if (i == 3) {
                    totalCornersAForExtraTimeSecondHalf = halfCornersTeamA;
                    totalCornersBForExtraTimeSecondHalf = halfCornersTeamB;
                }
            }
        }

        int totalCornersA = totalCornersAForNormalTime + totalCornersAForExtraTimeFirstHalf
                        + totalCornersAForExtraTimeSecondHalf;
        int totalCornersB = totalCornersBForNormalTime + totalCornersBForExtraTimeFirstHalf
                        + totalCornersBForExtraTimeSecondHalf;

        endMatchState.setCornersA(totalCornersA);
        endMatchState.setCornersB(totalCornersB);

        endMatchState.setNormalTimeCornersA(totalCornersAForNormalTime);
        endMatchState.setNormalTimeCornersB(totalCornersBForNormalTime);

        endMatchState.setExtraTimeFHCornersA(totalCornersAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHCornersB(totalCornersBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHCornersA(totalCornersAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHCornersB(totalCornersBForExtraTimeSecondHalf);

        endMatchState.setPreviousPeriodCornersA(halfResultCorners[0].A);
        endMatchState.setPreviousPeriodCornersB(halfResultCorners[0].B);
        endMatchState.setCurrentPeriodCornersA(halfResultCorners[1].A);
        endMatchState.setCurrentPeriodCornersB(halfResultCorners[1].B);

    }

    private void updateMatchStateForPeriodGoals(FootballMatchState endMatchState, PairOfIntegers[] halfResultGoals,
                    int numOfScores) {

        int totalGoalsAForNormalTime = 0;
        int totalGoalsBForNormalTime = 0;

        int totalGoalsAForExtraTimeFirstHalf = 0;
        int totalGoalsAForExtraTimeSecondHalf = 0;

        int totalGoalsBForExtraTimeFirstHalf = 0;
        int totalGoalsBForExtraTimeSecondHalf = 0;

        for (int i = 0; i < numOfScores; i++) {

            int halfGoalsTeamA = halfResultGoals[i].A;
            int halfGoalsTeamB = halfResultGoals[i].B;

            endMatchState.setGoalScoreInPeriodN(i, halfGoalsTeamA, halfGoalsTeamB);

            if (i < 2) {
                totalGoalsAForNormalTime += halfGoalsTeamA;
                totalGoalsBForNormalTime += halfGoalsTeamB;
            }

            if (i > 1) {
                if (i == 2) {
                    totalGoalsAForExtraTimeFirstHalf = halfGoalsTeamA;
                    totalGoalsBForExtraTimeFirstHalf = halfGoalsTeamB;
                }

                if (i == 3) {
                    totalGoalsAForExtraTimeSecondHalf = halfGoalsTeamA;
                    totalGoalsBForExtraTimeSecondHalf = halfGoalsTeamB;
                }
            }
        }

        int totalGoalsA =
                        totalGoalsAForNormalTime + totalGoalsAForExtraTimeFirstHalf + totalGoalsAForExtraTimeSecondHalf;
        int totalGoalsB =
                        totalGoalsBForNormalTime + totalGoalsBForExtraTimeFirstHalf + totalGoalsBForExtraTimeSecondHalf;

        endMatchState.setGoalsA(totalGoalsA);
        endMatchState.setGoalsB(totalGoalsB);

        endMatchState.setNormalTimeGoalsA(totalGoalsAForNormalTime);
        endMatchState.setNormalTimeGoalsB(totalGoalsBForNormalTime);

        endMatchState.setExtraTimeFHGoalsA(totalGoalsAForExtraTimeFirstHalf);
        endMatchState.setExtraTimeFHGoalsB(totalGoalsBForExtraTimeFirstHalf);

        endMatchState.setExtraTimeSHGoalsA(totalGoalsAForExtraTimeSecondHalf);
        endMatchState.setExtraTimeSHGoalsB(totalGoalsBForExtraTimeSecondHalf);

        endMatchState.setPreviousPeriodGoalsA(halfResultGoals[0].A);
        endMatchState.setPreviousPeriodGoalsB(halfResultGoals[0].B);
        endMatchState.setCurrentPeriodGoalsA(halfResultGoals[1].A);
        endMatchState.setCurrentPeriodGoalsB(halfResultGoals[1].B);

    }


}
