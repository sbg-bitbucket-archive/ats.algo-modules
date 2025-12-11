package ats.algo.sport.ufc;

import ats.algo.core.baseclasses.MatchResultMarkets;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.TeamId;
import ats.algo.core.markets.Market;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class UfcMatchResultMarkets extends MatchResultMarkets {
    List<String> notToWinInRoundList3 = new ArrayList<String>(Arrays.asList("A Not to Win in Round 1",
                    "A Not to Win in Round 2", "A Not to Win in Round 3",
                    "A Not to Win Not to win on by Decision/Tech Dec", "B Not to Win in Round 1",
                    "B Not to Win in Round 2", "B Not to Win in Round 3",
                    "B Not to Win Not to win on by Decision/Tech Dec", "Fight not to be a Draw or Tech Draw"));
    List<String> notToWinInRoundList5 = Lists.newArrayList(Iterables.concat(notToWinInRoundList3,
                    new ArrayList<String>(Arrays.asList("A Not to Win in Round 5", "B Not to Win in Round 5",
                                    "A Not to Win in Round 4", "B Not to Win in Round 4"))));

    @Override
    protected CheckMarketResultedOutcome checkMarketResulted(Market market, MatchState previousMatchState,
                    MatchState currentMatchState) {
        UfcMatchState ufcMatchState = (UfcMatchState) currentMatchState;
        UfcMatchFormat ufcMatchFormat = (UfcMatchFormat) ufcMatchState.getMatchFormat();
        String winningSelection = "";
        switch (market.getType()) {
            case "FT:ML": // Match winner
                if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                    winningSelection = "A";
                else if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                    winningSelection = "B";
                else
                    winningSelection = "Draw";
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:WR": // Match winner
                String lineId = market.getLineId();
                Double lineIdD = Double.valueOf(lineId);
                int line = (int) (lineIdD - 0.5);
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                    if (ufcMatchState.getRoundPlayed() < line) {
                        winningSelection = "A and Under " + lineIdD;
                    } else if (ufcMatchState.getRoundPlayed() == line && ufcMatchState.isFirstHalf()) {
                        winningSelection = "A and Under " + lineIdD;
                    } else if (ufcMatchState.getRoundPlayed() == line && !ufcMatchState.isFirstHalf()) {
                        winningSelection = "A and Over " + lineIdD;
                    } else {
                        winningSelection = "A and Over " + lineIdD;
                    }
                } else {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                        if (ufcMatchState.getRoundPlayed() < line) {
                            winningSelection = "B and Under " + lineIdD;
                        } else if (ufcMatchState.getRoundPlayed() == line && ufcMatchState.isFirstHalf()) {
                            winningSelection = "B and Under " + lineIdD;
                        } else if (ufcMatchState.getRoundPlayed() == line && !ufcMatchState.isFirstHalf()) {
                            winningSelection = "B and Over " + lineIdD;
                        } else {
                            winningSelection = "B and Over " + lineIdD;
                        }
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection, ufcMatchState.getRoundPlayed());

            case "FT:FFM":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.isDecision()) {
                    winningSelection = "Either Fighter on by Decision/Tech Dec";
                } else
                    winningSelection = "Either Fighter in Round " + ufcMatchState.getRoundPlayed();
                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:GRB1":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.isDecision()) {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                        winningSelection = "A by Decision/Tech Dec";
                    else if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                        winningSelection = "B by Decision/Tech Dec";
                } else {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                        if (ufcMatchState.getRoundPlayed() < 3)
                            winningSelection = "A Round 1-2";
                        else if (ufcMatchState.getRoundPlayed() == 3 && ufcMatchFormat.getRoundsPerMatch() == 3)
                            winningSelection = "A Round 3";
                        else if (ufcMatchState.getRoundPlayed() < 5 && ufcMatchFormat.getRoundsPerMatch() == 5)
                            winningSelection = "A Round 3-4";
                        else
                            winningSelection = "A Round 5";
                    }
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                        if (ufcMatchState.getRoundPlayed() < 3)
                            winningSelection = "B Round 1-2";
                        else if (ufcMatchState.getRoundPlayed() == 3 && ufcMatchFormat.getRoundsPerMatch() == 3)
                            winningSelection = "B Round 3";
                        else if (ufcMatchState.getRoundPlayed() < 5 && ufcMatchFormat.getRoundsPerMatch() == 5)
                            winningSelection = "B Round 3-4";
                        else
                            winningSelection = "B Round 5";
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:GRB2":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.isDecision()) {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                        winningSelection = "A by Decision/Tech Dec";
                    else if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                        winningSelection = "B by Decision/Tech Dec";
                } else {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                        if (ufcMatchState.getRoundPlayed() < 2)
                            winningSelection = "A Round 1";
                        else if (ufcMatchState.getRoundPlayed() < 4)
                            winningSelection = "A Round 2-3";
                        else
                            winningSelection = "A Round 4-5";
                    }
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                        if (ufcMatchState.getRoundPlayed() < 2)
                            winningSelection = "B Round 1";
                        else if (ufcMatchState.getRoundPlayed() < 4)
                            winningSelection = "B Round 2-3";
                        else
                            winningSelection = "B Round 4-5";
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:GRB3":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.isDecision()) {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                        winningSelection = "A by Decision/Tech Dec";
                    else if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                        winningSelection = "B by Decision/Tech Dec";
                } else {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                        if (ufcMatchState.getRoundPlayed() < 3)
                            winningSelection = "A Round 1-2";
                        else if (ufcMatchState.getRoundPlayed() <= 5)
                            winningSelection = "A Round 3-5";
                    }
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                        if (ufcMatchState.getRoundPlayed() < 3)
                            winningSelection = "B Round 1-2";
                        else if (ufcMatchState.getRoundPlayed() <= 5)
                            winningSelection = "B Round 3-5";
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:GRB4":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.isDecision()) {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                        winningSelection = "A by Decision/Tech Dec";
                    else if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                        winningSelection = "B by Decision/Tech Dec";
                } else {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                        if (ufcMatchState.getRoundPlayed() < 4)
                            winningSelection = "A Round 1-3";
                        else
                            winningSelection = "A Round 4-5";
                    }
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                        if (ufcMatchState.getRoundPlayed() < 4)
                            winningSelection = "B Round 1-3";
                        else
                            winningSelection = "B Round 4-5";
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:NTWR":
                List<String> notToWinInRoundList;
                if (ufcMatchFormat.getRoundsPerMatch() == 3) {
                    notToWinInRoundList = notToWinInRoundList3;
                } else {
                    notToWinInRoundList = notToWinInRoundList5;
                }
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    notToWinInRoundList.remove("Fight not to be a Draw or Tech Draw");
                } else if (ufcMatchState.isDecision()) {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                        notToWinInRoundList.remove("A Not to Win Not to win on by Decision/Tech Dec");
                    else if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                        notToWinInRoundList.remove("B Not to Win Not to win on by Decision/Tech Dec");
                } else {
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.A))
                        notToWinInRoundList.remove("A Not to Win in Round " + ufcMatchState.getRoundPlayed());
                    if (ufcMatchState.getWinnningTeam().equals(TeamId.B))
                        notToWinInRoundList.remove("B Not to Win in Round " + ufcMatchState.getRoundPlayed());
                }
                return new CheckMarketResultedOutcome(notToWinInRoundList);

            case "FT:GD":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Yes";
                } else if (ufcMatchState.isDecision()) {
                    winningSelection = "Yes";
                } else
                    winningSelection = "No";
                return new CheckMarketResultedOutcome(winningSelection);

            case "FT:MV":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                    if (ufcMatchState.isDecision()) {
                        winningSelection = "A by Decision/Tech Dec";
                    } else {
                        winningSelection = "A by KO, TKO, DQ or Submission";
                    }
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                    if (ufcMatchState.isDecision()) {
                        winningSelection = "B by Decision/Tech Dec";
                    } else {
                        winningSelection = "B by KO, TKO, DQ or Submission";
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:RB":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                    if (ufcMatchState.isDecision()) {
                        winningSelection = "A by Decision/Tech Dec";
                    } else {
                        winningSelection = String.format("A Round %d", ufcMatchState.getRoundPlayed());
                    }
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                    if (ufcMatchState.isDecision()) {
                        winningSelection = "B by Decision/Tech Dec";
                    } else {
                        winningSelection = String.format("B Round %d", ufcMatchState.getRoundPlayed());
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:FOER":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    return new CheckMarketResultedOutcome(new ArrayList<String>(market.getSelections().keySet()), true,
                                    true);
                } else if (ufcMatchState.getRoundPlayed() % 2 == 0) {
                    winningSelection = "Even";
                } else {
                    winningSelection = "Odd";
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:HFNW":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    return new CheckMarketResultedOutcome(new ArrayList<String>(market.getSelections().keySet()));
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                    if (ufcMatchState.isDecision()) {
                        winningSelection = "B Not to Win by Decision/Tech Dec";
                    } else {
                        winningSelection = "B Not to Win by KO, TKO, DQ or Submission";
                    }
                } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                    if (ufcMatchState.isDecision()) {
                        winningSelection = "A Not to Win by Decision/Tech Dec";
                    } else {
                        winningSelection = "A Not to Win by KO, TKO, DQ or Submission";
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection);
            case "FT:OU":
                Double lineIdOU = Double.valueOf(market.getLineId());
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Over " + lineIdOU;
                } else {
                    String[] marketDescription = market.getMarketDescription().split(" ");
                    if (marketDescription.length > 2) {
                        if (ufcMatchState.getRoundPlayed() > Double
                                        .valueOf(marketDescription[marketDescription.length - 1])) {
                            winningSelection = "Over " + lineIdOU;
                        } else {
                            winningSelection = "Under " + lineIdOU;
                        }
                    }
                }
                return new CheckMarketResultedOutcome(winningSelection, ufcMatchState.getRoundPlayed());
            case "FT:RTF":
                String sequenceID = market.getSequenceId();
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    winningSelection = "Draw or Tech Draw";
                } else if (ufcMatchState.isDecision()) {
                    winningSelection = "Either Fighter on by Decision/Tech Dec";
                } else
                    switch (sequenceID) {
                        case "1":
                            if (ufcMatchState.getRoundPlayed() < 3) {
                                winningSelection = "Either Fighter in Round 1 or 2";
                            } else if (ufcMatchState.getRoundPlayed() == 3 || ufcMatchState.getRoundPlayed() == 4) {
                                if (ufcMatchFormat.getRoundsPerMatch() == 5)
                                    winningSelection = "Either Fighter in Round 3 or 4";
                                else
                                    winningSelection = "Either Fighter in Round 3";
                            } else if (ufcMatchState.getRoundPlayed() == 5) {
                                winningSelection = "Either Fighter in Round 5";
                            }
                            break;
                        case "2":
                            if (ufcMatchState.getRoundPlayed() == 1) {
                                winningSelection = "Either Fighter in Round 1";
                            } else if (ufcMatchState.getRoundPlayed() == 2 || ufcMatchState.getRoundPlayed() == 3) {
                                winningSelection = "Either Fighter in Round 2 or 3";
                            } else if (ufcMatchState.getRoundPlayed() == 4 || ufcMatchState.getRoundPlayed() == 5) {
                                winningSelection = "Either Fighter in Round 4 or 5";
                            }
                            break;
                        case "3":
                            if (ufcMatchState.getRoundPlayed() < 4) {
                                winningSelection = "Either Fighter in Round 1, 2 or 3";
                            } else if (ufcMatchState.getRoundPlayed() > 3) {
                                winningSelection = "Either Fighter in Round 4 or 5";
                            }
                            break;
                        case "4":
                            if (ufcMatchState.getRoundPlayed() < 3) {
                                winningSelection = "Either Fighter in Round 1 or 2";
                            } else if (ufcMatchState.getRoundPlayed() > 2) {
                                winningSelection = "Either Fighter in Round 3, 4 or 5";
                            }
                            break;
                    }
                return new CheckMarketResultedOutcome(winningSelection, Integer.parseInt(sequenceID));
            case "FT:DC":
                if (ufcMatchState.getWinnningTeam().equals(TeamId.UNKNOWN)) {
                    return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()), true, true);
                }
                sequenceID = market.getSequenceId();
                switch (sequenceID) {
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        if (ufcMatchState.getRoundPlayed() > Integer.valueOf(sequenceID)) {
                            return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()),
                                            true, true);
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                            winningSelection = String.format("A in Round %d or by Decision",
                                            ufcMatchState.getRoundPlayed());
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                            winningSelection = String.format("B in Round %d or by Decision",
                                            ufcMatchState.getRoundPlayed());
                        }
                        break;
                    case "6":
                        if (ufcMatchState.getRoundPlayed() > 2) {
                            return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()),
                                            true, true);
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                            winningSelection = "A in Round 1-2 or by Decision";
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                            winningSelection = "B in Round 1-2 or by Decision";
                        }
                        break;
                    case "7":
                        if (ufcMatchState.getRoundPlayed() > 3) {
                            return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()),
                                            true, true);
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                            winningSelection = "A in Round 1-3 or by Decision";
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                            winningSelection = "B in Round 1-3 or by Decision";
                        }
                        break;
                    case "8":
                        if (ufcMatchState.getRoundPlayed() > 3 || ufcMatchState.getRoundPlayed() == 1) {
                            return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()),
                                            true, true);
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                            winningSelection = "A in Round 2-3 or by Decision";
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                            winningSelection = "B in Round 2-3 or by Decision";
                        }
                        break;
                    case "9":
                        if (ufcMatchState.getRoundPlayed() > 4 || ufcMatchState.getRoundPlayed() == 1) {
                            return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()),
                                            true, true);
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                            winningSelection = "A in Round 2-4 or by Decision";
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                            winningSelection = "B in Round 2-4 or by Decision";
                        }
                        break;
                    case "10":
                        if (ufcMatchState.getRoundPlayed() > 4 || ufcMatchState.getRoundPlayed() < 3) {
                            return new CheckMarketResultedOutcome(new ArrayList<>(market.getSelections().keySet()),
                                            true, true);
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.A)) {
                            winningSelection = "A in Round 3-4 or by Decision";
                        } else if (ufcMatchState.getWinnningTeam().equals(TeamId.B)) {
                            winningSelection = "B in Round 3-4 or by Decision";
                        }
                        break;
                }
                return new CheckMarketResultedOutcome(winningSelection, Integer.parseInt(sequenceID));
        }

        return null;
    }
}
