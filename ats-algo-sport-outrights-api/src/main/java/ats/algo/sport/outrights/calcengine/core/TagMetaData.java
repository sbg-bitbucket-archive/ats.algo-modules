package ats.algo.sport.outrights.calcengine.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

/*
 * holds data parsed from a fixture tag string in easily accessible form
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagMetaData {

    private String groupID; // used only if FixtureType = KNOCKOUT_GROUP

    /*
     * used only for fixtures involving winners and runners up of group stages of knockout competitions. Identifies
     * which groups each of the two teams comes from , and what their finishing positions were.
     */
    private String groupIDTeamA;
    private int teamAGroupFinishPosition;
    private String groupIDTeamB;
    private int teamBGroupFinishPosition;

    /*
     * used only for fixtures in knockout stages. Links this fixture, e.g. the final to the two preceding fixtures (the
     * two semifinals) and indicates whether the winning or losing teams in those earlier fixtures are to be chosen.
     */
    private String teamAFromFixtureID;
    private boolean teamAPreviousFixtureWinner;
    private String teamBFromFixtureID;
    private boolean teamBPreviousFixtureWinner;
    /*
     * used only for matches in the first round of League Playoff stages Assumes the teams involved in playoffs are
     * identified by an int (typically 0-3 for a four team play-off)
     */
    private int teamARegularLeagueFinishPosition;
    private int teamBRegularLeagueFinishPosition;


    public TagMetaData(FixtureType fixtureType, String tag) {
        // TODO finish coding for the remaining cases
        try {
            String[] sBits;
            switch (fixtureType) {
                case LEAGUE:
                    // do nothing - no useful in in the tag
                    break;
                case LEAGUE_PLAYOFF_LEG1:
                case LEAGUE_PLAYOFF_LEG2:
                    /*
                     * Tag should be of the form "NvM", where N,M are the ints defining the teams in this match
                     */
                    sBits = tag.split("v");
                    teamARegularLeagueFinishPosition = Integer.parseInt(sBits[0]);
                    teamBRegularLeagueFinishPosition = Integer.parseInt(sBits[1]);
                    break;
                case LEAGUE_PLAY_OFF_FINAL:
                    /*
                     * Tag should be the fixture IDs of the preceding fixture
                     */
                    sBits = tag.split("v");
                    teamAFromFixtureID = sBits[0];
                    teamBFromFixtureID = sBits[1];
                    teamAPreviousFixtureWinner = true;
                    teamBPreviousFixtureWinner = true;
                    break;
                case GROUP_STAGE:
                    break;
                case KNOCKOUT_R128:
                    break;
                case KNOCKOUT_R64:
                    break;
                case KNOCKOUT_R32:
                    break;
                case KNOCKOUT_R16:
                    break;
                case KNOCKOUT_QF:
                    break;
                case KNOCKOUT_SF:
                    break;
                case KNOCKOUT_THIRD_PLACE:
                    break;
                case KNOCKOUT_FINAL:
                    break;
                default:
                    throw new IllegalArgumentException("invalid tag:" + tag);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid fixture type:" + fixtureType);
        }
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupIDTeamA() {
        return groupIDTeamA;
    }

    public int getTeamAGroupFinishPosition() {
        return teamAGroupFinishPosition;
    }

    public String getGroupIDTeamB() {
        return groupIDTeamB;
    }

    public int getTeamBGroupFinishPosition() {
        return teamBGroupFinishPosition;
    }

    public String getTeamAFromFixtureID() {
        return teamAFromFixtureID;
    }

    public boolean isTeamAPreviousFixtureWinner() {
        return teamAPreviousFixtureWinner;
    }

    public String getTeamBFromFixtureID() {
        return teamBFromFixtureID;
    }

    public boolean isTeamBPreviousFixtureWinner() {
        return teamBPreviousFixtureWinner;
    }

    public int getTeamARegularLeagueFinishPosition() {
        return teamARegularLeagueFinishPosition;
    }

    public int getTeamBRegularLeagueFinishPosition() {
        return teamBRegularLeagueFinishPosition;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
        result = prime * result + ((groupIDTeamA == null) ? 0 : groupIDTeamA.hashCode());
        result = prime * result + ((groupIDTeamB == null) ? 0 : groupIDTeamB.hashCode());
        result = prime * result + ((teamAFromFixtureID == null) ? 0 : teamAFromFixtureID.hashCode());
        result = prime * result + teamAGroupFinishPosition;
        result = prime * result + (teamAPreviousFixtureWinner ? 1231 : 1237);
        result = prime * result + teamARegularLeagueFinishPosition;
        result = prime * result + ((teamBFromFixtureID == null) ? 0 : teamBFromFixtureID.hashCode());
        result = prime * result + teamBGroupFinishPosition;
        result = prime * result + (teamBPreviousFixtureWinner ? 1231 : 1237);
        result = prime * result + teamBRegularLeagueFinishPosition;
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
        TagMetaData other = (TagMetaData) obj;
        if (groupID == null) {
            if (other.groupID != null)
                return false;
        } else if (!groupID.equals(other.groupID))
            return false;
        if (groupIDTeamA == null) {
            if (other.groupIDTeamA != null)
                return false;
        } else if (!groupIDTeamA.equals(other.groupIDTeamA))
            return false;
        if (groupIDTeamB == null) {
            if (other.groupIDTeamB != null)
                return false;
        } else if (!groupIDTeamB.equals(other.groupIDTeamB))
            return false;
        if (teamAFromFixtureID == null) {
            if (other.teamAFromFixtureID != null)
                return false;
        } else if (!teamAFromFixtureID.equals(other.teamAFromFixtureID))
            return false;
        if (teamAGroupFinishPosition != other.teamAGroupFinishPosition)
            return false;
        if (teamAPreviousFixtureWinner != other.teamAPreviousFixtureWinner)
            return false;
        if (teamARegularLeagueFinishPosition != other.teamARegularLeagueFinishPosition)
            return false;
        if (teamBFromFixtureID == null) {
            if (other.teamBFromFixtureID != null)
                return false;
        } else if (!teamBFromFixtureID.equals(other.teamBFromFixtureID))
            return false;
        if (teamBGroupFinishPosition != other.teamBGroupFinishPosition)
            return false;
        if (teamBPreviousFixtureWinner != other.teamBPreviousFixtureWinner)
            return false;
        if (teamBRegularLeagueFinishPosition != other.teamBRegularLeagueFinishPosition)
            return false;
        return true;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }

}
