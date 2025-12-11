package ats.algo.requestresponse.ppb;

import java.io.Serializable;

import ats.algo.sport.tennis.TennisMatchFormat;
import ats.algo.sport.tennis.TennisMatchFormat.TournamentLevel;

public class PpbTennisMatchFormat implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String matchType;
    private String surface;
    private String matchTeamsGender;
    private boolean advantagePlayedNormalGame;
    private boolean advantagePlayedTieBreakGame;
    private int numSets;
    private String lastSetFormat;
    private int gamesInSetFirstTo;
    private int tieBreakPlayedAfterXGames;
    private int pointsInTieBreakFirstTo;
    private int pointsInRaceSetFirstTo;
    private String tournamentType;

    /**
     * Sets all parameters to sensible defaults. Required for JunitSerilisation test
     */
    public PpbTennisMatchFormat() {

    }

    /**
     * generate an instance of PpbTennisMatchformat from the contents of MatchFormat.
     * 
     * @param format
     * @return
     */

    public static PpbTennisMatchFormat generatePbbTennisMatchFormat(TennisMatchFormat format) {
        /*
         * These properties should be set to the default values shown. is_advantage_played_tie_break_game (default to
         * true) games_in_set_first_to (default to 6) tie_break_played_after_x_games (default to 6)
         * points_in_race_set_first_to (default to 10)
         * 
         * points_in_tie_break_first_to (set to 7 for normal tie break, 10 for champ tb)
         * 
         * We should be able to set all other properties correctly from the contents of format
         */

        PpbTennisMatchFormat.Builder builder = new PpbTennisMatchFormat.Builder();
        if (format.getTournamentLevel() == TournamentLevel.ATP_NEXTGEN) {
            builder.gamesInSetFirstTo(4);
            builder.advantagePlayedTieBreakGame(true);
            builder.tieBreakPlayedAfterXGames(3);
            builder.pointsInTieBreakFirstTo(7);
            builder.pointsInRaceSetFirstTo(10);
        } else if (format.getTournamentLevel() == TournamentLevel.FAST4) {
            builder.gamesInSetFirstTo(4);
            builder.advantagePlayedTieBreakGame(false);
            builder.tieBreakPlayedAfterXGames(3);
            builder.pointsInTieBreakFirstTo(5);
            builder.pointsInRaceSetFirstTo(10);
        } else {
            builder.gamesInSetFirstTo(6);
            builder.advantagePlayedTieBreakGame(true);
            builder.tieBreakPlayedAfterXGames(6);
            builder.pointsInTieBreakFirstTo(7);
            builder.pointsInRaceSetFirstTo(10);
        }


        // if (format.getFinalSetType() == TennisMatchFormat.FinalSetType.CHAMPIONSHIP_TIE_BREAK)
        // if(true)

        // else
        // builder.pointsInTieBreakFirstTo(5);

        //
        if (format.isDoublesMatch()) {
            builder.matchType("doubles");
        } else {
            builder.matchType("singles");
            // builder.advantagePlayedNormalGame("yes");
        }

        if (format.isNoAdvantageGameFormat()) {
            builder.advantagePlayedNormalGame(false);

        } else {
            builder.advantagePlayedNormalGame(true);
        }

        if (format.isNoAdvantageTieBreakFormat()) {
            builder.advantagePlayedTieBreakGame(false);

        } else {
            builder.advantagePlayedTieBreakGame(true);
        }

        builder.surface(format.getSurface().toString().toLowerCase());


        switch (format.getSex()) {
            case MEN:
                builder.matchTeamsGender("male");
                break;
            case WOMEN:
                builder.matchTeamsGender("female");
                break;
            case MIXED:
                builder.matchTeamsGender("mixed");
                break;
            // case UNKNOWN:
            // builder.matchTeamsGender("unknown");
        }


        builder.numSets(format.getSetsPerMatch());

        switch (format.getFinalSetType()) {
            case NORMAL_WITH_TIE_BREAK:
                builder.lastSetFormat("Tiebreaks in all sets");
                break;
            case ADVANTAGE_SET:
                builder.lastSetFormat("No tiebreak in last set");
                break;
            case CHAMPIONSHIP_TIE_BREAK:
            case SHORT_TIEBREAK:
                builder.lastSetFormat("Race set");
                break;
            case WIMBLEDON_TIE_BREAK:
                builder.lastSetFormat("12 Games and tie break in last set");
                break;
            case AUSTRALIAN_OPEN:
                builder.lastSetFormat("6 Games and 10 point tiebreak in last set");
        }
        // 'atp_wta', 'atp_wta_qualifier', 'challenger', 'challenger_qualifier', 'itf', 'itf_qualifier'
        switch (format.getTournamentLevel()) {
            case CHALLENGER:
                builder.tournamentType("challenger");
                break;
            case ITF:
                builder.tournamentType("itf");
                break;
            case ATP:
                builder.tournamentType("atp_wta");
                break;
            case CHALLENGER_QUALIFIER:
                builder.tournamentType("challenger_qualifier");
                break;
            case ITF_QUALIFIER:
                builder.tournamentType("itf_qualifier");
                break;
            case ATP_QUALIFIER:
                builder.tournamentType("atp_wta_qualifier");
                break;
            case WTA:
                builder.tournamentType("atp_wta");
                break;
            case WTA_QUALIFIER:
                builder.tournamentType("atp_wta_qualifier");
                break;
            case FAST4:
                builder.tournamentType("atp_wta");
                break;
            default:
                builder.tournamentType("atp_next_gen");
                break;
        }

        PpbTennisMatchFormat pbbTennisMatchFormat = builder.build();
        return pbbTennisMatchFormat;
    }

    public String getMatchType() {
        return matchType;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public String getSurface() {
        return surface;
    }

    public String getMatchTeamsGender() {
        return matchTeamsGender;
    }

    public boolean isAdvantagePlayedNormalGame() {
        return advantagePlayedNormalGame;
    }

    public boolean isAdvantagePlayedTieBreakGame() {
        return advantagePlayedTieBreakGame;
    }

    public int getNumSets() {
        return numSets;
    }

    public String getLastSetFormat() {
        return lastSetFormat;
    }

    public int getGamesInSetFirstTo() {
        return gamesInSetFirstTo;
    }

    public int getTieBreakPlayedAfterXGames() {
        return tieBreakPlayedAfterXGames;
    }

    public int getPointsInTieBreakFirstTo() {
        return pointsInTieBreakFirstTo;
    }

    public int getPointsInRaceSetFirstTo() {
        return pointsInRaceSetFirstTo;
    }

    public static class Builder {
        private String matchType;
        private String surface;
        private String matchTeamsGender;
        private boolean advantagePlayedNormalGame;
        private boolean advantagePlayedTieBreakGame;
        private int numSets;
        private String lastSetFormat;
        private int gamesInSetFirstTo;
        private int tieBreakPlayedAfterXGames;
        private int pointsInTieBreakFirstTo;
        private int pointsInRaceSetFirstTo;
        private String tournamentType;

        public Builder matchType(String val) {
            matchType = val;
            return this;
        }

        public Builder tournamentType(String val) {
            tournamentType = val;
            return this;
        }

        public Builder surface(String val) {
            surface = val;
            return this;
        }

        public Builder matchTeamsGender(String val) {
            matchTeamsGender = val;
            return this;
        }

        public Builder advantagePlayedNormalGame(boolean val) {
            advantagePlayedNormalGame = val;
            return this;
        }

        public Builder advantagePlayedTieBreakGame(boolean val) {
            advantagePlayedTieBreakGame = val;
            return this;
        }

        public Builder numSets(int val) {
            numSets = val;
            return this;
        }

        public Builder lastSetFormat(String val) {
            lastSetFormat = val;
            return this;
        }

        public Builder gamesInSetFirstTo(int val) {
            gamesInSetFirstTo = val;
            return this;
        }

        public Builder tieBreakPlayedAfterXGames(int val) {
            tieBreakPlayedAfterXGames = val;
            return this;
        }

        public Builder pointsInTieBreakFirstTo(int val) {
            pointsInTieBreakFirstTo = val;
            return this;
        }

        public Builder pointsInRaceSetFirstTo(int val) {
            pointsInRaceSetFirstTo = val;
            return this;
        }


        public PpbTennisMatchFormat build() {
            return new PpbTennisMatchFormat(this);
        }

    }

    private PpbTennisMatchFormat(Builder builder) {
        matchType = builder.matchType;
        surface = builder.surface;
        matchTeamsGender = builder.matchTeamsGender;
        advantagePlayedNormalGame = builder.advantagePlayedNormalGame;
        advantagePlayedTieBreakGame = builder.advantagePlayedTieBreakGame;
        numSets = builder.numSets;
        lastSetFormat = builder.lastSetFormat;
        gamesInSetFirstTo = builder.gamesInSetFirstTo;
        tieBreakPlayedAfterXGames = builder.tieBreakPlayedAfterXGames;
        pointsInTieBreakFirstTo = builder.pointsInTieBreakFirstTo;
        pointsInRaceSetFirstTo = builder.pointsInRaceSetFirstTo;
        tournamentType = builder.tournamentType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (advantagePlayedNormalGame ? 1231 : 1237);
        result = prime * result + (advantagePlayedTieBreakGame ? 1231 : 1237);
        result = prime * result + gamesInSetFirstTo;
        result = prime * result + ((lastSetFormat == null) ? 0 : lastSetFormat.hashCode());
        result = prime * result + ((matchTeamsGender == null) ? 0 : matchTeamsGender.hashCode());
        result = prime * result + ((matchType == null) ? 0 : matchType.hashCode());
        result = prime * result + numSets;
        result = prime * result + pointsInRaceSetFirstTo;
        result = prime * result + pointsInTieBreakFirstTo;
        result = prime * result + ((surface == null) ? 0 : surface.hashCode());
        result = prime * result + tieBreakPlayedAfterXGames;
        result = prime * result + ((tournamentType == null) ? 0 : tournamentType.hashCode());
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
        PpbTennisMatchFormat other = (PpbTennisMatchFormat) obj;
        if (advantagePlayedNormalGame != other.advantagePlayedNormalGame)
            return false;
        if (advantagePlayedTieBreakGame != other.advantagePlayedTieBreakGame)
            return false;
        if (gamesInSetFirstTo != other.gamesInSetFirstTo)
            return false;
        if (lastSetFormat == null) {
            if (other.lastSetFormat != null)
                return false;
        } else if (!lastSetFormat.equals(other.lastSetFormat))
            return false;
        if (matchTeamsGender == null) {
            if (other.matchTeamsGender != null)
                return false;
        } else if (!matchTeamsGender.equals(other.matchTeamsGender))
            return false;
        if (matchType == null) {
            if (other.matchType != null)
                return false;
        } else if (!matchType.equals(other.matchType))
            return false;
        if (numSets != other.numSets)
            return false;
        if (pointsInRaceSetFirstTo != other.pointsInRaceSetFirstTo)
            return false;
        if (pointsInTieBreakFirstTo != other.pointsInTieBreakFirstTo)
            return false;
        if (surface == null) {
            if (other.surface != null)
                return false;
        } else if (!surface.equals(other.surface))
            return false;
        if (tieBreakPlayedAfterXGames != other.tieBreakPlayedAfterXGames)
            return false;
        if (tournamentType == null) {
            if (other.tournamentType != null)
                return false;
        } else if (!tournamentType.equals(other.tournamentType))
            return false;
        return true;
    }



}
