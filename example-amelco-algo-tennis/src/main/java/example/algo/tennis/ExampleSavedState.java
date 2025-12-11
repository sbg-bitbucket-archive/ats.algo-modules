package example.algo.tennis;

import java.io.Serializable;

import ats.algo.core.common.TeamId;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;

/**
 * Example of how to use this class. In this example we just record the number of points won by each player in the match
 * so far
 * 
 * @author Geoff
 *
 */
public class ExampleSavedState implements Serializable {

    private static final long serialVersionUID = 1L;

    private int nPointsWonByA;
    private int nPointsWonByB;

    ExampleSavedState() {
        nPointsWonByA = 0;
        nPointsWonByB = 0;
    }

    public int getnPointsWonByA() {
        return nPointsWonByA;
    }

    public void setnPointsWonByA(int nPointsWonByA) {
        this.nPointsWonByA = nPointsWonByA;
    }

    public int getnPointsWonByB() {
        return nPointsWonByB;
    }

    public void setnPointsWonByB(int nPointsWonByB) {
        this.nPointsWonByB = nPointsWonByB;
    }

    /**
     * If matchIncident is of type pointwon then increment the count for the player who won the point
     */
    public void updateForIncident(TennisMatchIncident incident) {
        if (incident.getIncidentSubType() == TennisMatchIncidentType.POINT_WON) {
            if (incident.getPointWinner() == TeamId.A)
                nPointsWonByA++;
            else
                nPointsWonByB++;
        }
    }

    /*
     * convert to Json
     */
    /**
     * convert this object to Json
     * 
     * @return
     */
    public String toJson() {
        return JsonSerializer.serialize(this, false);
    }

    /**
     * create a new instance of this class from the supplied json string
     * 
     * @param savedState
     * @return
     */
    public static ExampleSavedState fromJson(String json) {

        return JsonSerializer.deserialize(json, ExampleSavedState.class);
    }

}
