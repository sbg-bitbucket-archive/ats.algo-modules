package ats.algo.sport.outrights.calcengine.core;

import ats.algo.sport.outrights.CompetitionType;
import ats.algo.sport.outrights.calcengine.core.AbstractFormat;
import ats.algo.sport.outrights.calcengine.core.AbstractMarketsFactory;
import ats.algo.sport.outrights.calcengine.core.AbstractState;
import ats.algo.sport.outrights.calcengine.core.Competition;
import ats.algo.sport.outrights.calcengine.leagues.LeagueFormat;
import ats.algo.sport.outrights.calcengine.leagues.LeagueMarketsFactory;
import ats.algo.sport.outrights.calcengine.leagues.LeagueState;

public class CompetitionProperties {

    /**
     * gets an instance of the CompetitionState class for this particular competition
     * 
     * @param competition
     * @return
     */
    static AbstractState competitionStateInstance(Competition competition, AbstractFormat format) {
        AbstractState state = null;
        switch (competition.getCompetitionType()) {
            case PREMIER_LEAGUE:
            case CHAMPIONSHIP_LEAGUE:
            case EFL_DIVISION_1:
            case EFL_DIVISION_2:
                state = new LeagueState(competition, (LeagueFormat) format);
                break;

            default:
                throw new IllegalArgumentException("unknown enum");
        }
        return state;
    }

    /**
     * 
     * @param competition
     * @return
     */
    static AbstractMarketsFactory competitionMarketsFactoryInstance(Competition competition) {
        LeagueMarketsFactory factory = null;
        switch (competition.getCompetitionType()) {
            case PREMIER_LEAGUE:
            case CHAMPIONSHIP_LEAGUE:
            case EFL_DIVISION_1:
            case EFL_DIVISION_2:
                factory = new LeagueMarketsFactory(competition);
                break;

            default:
                throw new IllegalArgumentException("unsupported enum");
        }
        return factory;
    }

    /**
     * 
     * @param competition
     * @return
     */
    public static AbstractFormat competitionMatchFormatInstance(Competition competition) {
        AbstractFormat format = null;
        switch (competition.getCompetitionType()) {
            case PREMIER_LEAGUE:
                format = new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 0, 0, 0, 3);
                break;
            case CHAMPIONSHIP_LEAGUE:
                format = new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 2, 1, 4, 3);
                break;
            case EFL_DIVISION_1:
                format = new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 2, 1, 4, 3);
                break;
            case EFL_DIVISION_2:
                format = new LeagueFormat(CompetitionType.PREMIER_LEAGUE, 3, 1, 4, 3);
                break;
            default:
                throw new IllegalArgumentException("unknown enum");
        }

        return format;
    }

}
