package ats.algo.sport.tennis;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.MatchStateFromFeed;
import ats.algo.core.common.TeamId;
import ats.core.util.json.JsonUtil;

/**
 * immutable class to hold score as per datafeed
 * 
 * @author Geoff
 *
 */
public class TennisMatchStateFromFeed extends MatchStateFromFeed implements Serializable {

    private static final long serialVersionUID = 1L;
    private int setsA;
    private int setsB;
    private int gamesA;
    private int gamesB;
    private int pointsA;
    private int pointsB;
    private PlayerId playerServingNow; // Currently Unavailable from feed
    private TeamId teamServingNow;



    /**
     * holds the match state as specified by an external feed
     * 
     * @param setsA
     * @param setsB
     * @param gamesA
     * @param gamesB
     * @param pointsA 0, 1(=15), 2(=30), 3(=40), 4(=Adv)
     * @param pointsB 0, 1(=15), 2(=30), 3(=40), 4(=Adv)
     * @param teamServingNow
     */
    public TennisMatchStateFromFeed(@JsonProperty("setsA") int setsA, @JsonProperty("setsB") int setsB,
                    @JsonProperty("gamesA") int gamesA, @JsonProperty("gamesB") int gamesB,
                    @JsonProperty("pointsA") int pointsA, @JsonProperty("pointsB") int pointsB,
                    @JsonProperty("teamServingNow") TeamId teamServingNow) {
        super();
        this.setsA = setsA;
        this.setsB = setsB;
        this.gamesA = gamesA;
        this.gamesB = gamesB;
        this.pointsA = pointsA;
        this.pointsB = pointsB;
        this.playerServingNow = teamIdToPlayerId(teamServingNow);// playerServingNow; provisional solution
        this.teamServingNow = teamServingNow;
    }

    public TennisMatchStateFromFeed copy() {
        TennisMatchStateFromFeed copied = new TennisMatchStateFromFeed(this.setsA, this.setsB, this.gamesA, this.gamesB,
                        this.pointsA, this.pointsB, this.teamServingNow);
        return copied;
    }



    private PlayerId teamIdToPlayerId(TeamId teamServingNow2) {
        if (teamServingNow2 == TeamId.A)
            return PlayerId.A1;
        else
            return PlayerId.B1;
    }

    public int getSetsA() {
        return setsA;
    }


    public int getSetsB() {
        return setsB;
    }



    public int getGamesA() {
        return gamesA;
    }


    public int getGamesB() {
        return gamesB;
    }


    /**
     * 
     * @return 0, 1(=15), 2(=30), 3(=40), 4(=Adv)
     */
    public int getPointsA() {
        return pointsA;
    }



    /**
     * 
     * @return 0, 1(=15), 2(=30), 3(=40), 4(=Adv)
     */
    public int getPointsB() {
        return pointsB;
    }



    public PlayerId getPlayerServingNow() {
        return playerServingNow;
    }

    public void setPlayerServingNow(PlayerId playerServingNow) {
        this.playerServingNow = playerServingNow;
    }

    public TeamId getTeamServingNow() {
        return teamServingNow;
    }

    public void setTeamServingNow(TeamId teamServingNow) {
        this.teamServingNow = teamServingNow;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + gamesA;
        result = prime * result + gamesB;
        result = prime * result + ((playerServingNow == null) ? 0 : playerServingNow.hashCode());
        result = prime * result + pointsA;
        result = prime * result + pointsB;
        result = prime * result + setsA;
        result = prime * result + setsB;
        result = prime * result + ((teamServingNow == null) ? 0 : teamServingNow.hashCode());
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
        TennisMatchStateFromFeed other = (TennisMatchStateFromFeed) obj;
        if (gamesA != other.gamesA)
            return false;
        if (gamesB != other.gamesB)
            return false;
        if (playerServingNow != other.playerServingNow)
            return false;
        if (pointsA != other.pointsA)
            return false;
        if (pointsB != other.pointsB)
            return false;
        if (setsA != other.setsA)
            return false;
        if (setsB != other.setsB)
            return false;
        if (teamServingNow != other.teamServingNow)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }

    @Override
    public String toShortString() {
        return "sets=" + setsA + "-" + setsB + ", games=" + gamesA + "-" + gamesB + ", points=" + pointsA + "-"
                        + pointsB + "-" + playerServingNow + "-" + teamServingNow + "";
    }



}
