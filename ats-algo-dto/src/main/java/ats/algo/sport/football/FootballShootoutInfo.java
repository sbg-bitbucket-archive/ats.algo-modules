package ats.algo.sport.football;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ats.algo.core.common.TeamId;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;
/*
 * Penalty ABBA orders
 */

public class FootballShootoutInfo implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    int penaltyScoreA;
    int penaltyScoreB;
    TeamId startedPenalty;
    int shootingCounter;
    boolean newOrder;
    TeamId shootoutNext = TeamId.UNKNOWN;

    List<FootballMatchIncidentType> shootoutListStatusA = new ArrayList<FootballMatchIncidentType>();
    List<FootballMatchIncidentType> shootoutListStatusB = new ArrayList<FootballMatchIncidentType>();


    public FootballShootoutInfo() {}


    public FootballShootoutInfo(int a, int b, TeamId startedBy, int counter, boolean newOrder) {
        this.penaltyScoreA = a;
        this.penaltyScoreB = b;
        this.startedPenalty = startedBy;
        this.shootingCounter = counter;
        this.newOrder = newOrder;
        this.shootoutNext = this.getShootingNext();
    }

    public FootballShootoutInfo(int a, int b, TeamId startedBy, int counter, boolean newOrder,
                    List<FootballMatchIncidentType> shootoutListStatusA,
                    List<FootballMatchIncidentType> shootoutListStatusB) {
        this.penaltyScoreA = a;
        this.penaltyScoreB = b;
        this.startedPenalty = startedBy;
        this.shootingCounter = counter;
        this.newOrder = newOrder;
        this.shootoutNext = this.getShootingNext();
        this.shootoutListStatusA = cloneEnumArrayList(shootoutListStatusA);
        this.shootoutListStatusB = cloneEnumArrayList(shootoutListStatusB);
    }

    public void setShootoutInfo(int a, int b, TeamId startedBy, int counter,
                    List<FootballMatchIncidentType> shootoutListStatusA,
                    List<FootballMatchIncidentType> shootoutListStatusB) {
        this.penaltyScoreA = a;
        this.penaltyScoreB = b;
        this.startedPenalty = startedBy;
        this.shootingCounter = counter;
        this.shootoutNext = this.getShootingNext();
        this.shootoutListStatusA = cloneEnumArrayList(shootoutListStatusA);
        this.shootoutListStatusB = cloneEnumArrayList(shootoutListStatusB);

    }

    public void updateShootoutListStatusA(FootballMatchIncidentType outcome) {
        shootoutListStatusA.add(outcome);
    }

    public void updateShootoutListStatusB(FootballMatchIncidentType outcome) {
        shootoutListStatusB.add(outcome);
    }


    public List<FootballMatchIncidentType> getShootoutListStatusA() {
        return shootoutListStatusA;
    }


    public void setShootoutListStatusA(List<FootballMatchIncidentType> shootoutListStatusA) {
        this.shootoutListStatusA = shootoutListStatusA;
    }


    public List<FootballMatchIncidentType> getShootoutListStatusB() {
        return shootoutListStatusB;
    }


    public void setShootoutListStatusB(List<FootballMatchIncidentType> shootoutListStatusB) {
        this.shootoutListStatusB = shootoutListStatusB;
    }


    public TeamId getShootoutNext() {
        return shootoutNext;
    }

    public void setShootoutNext(TeamId shootoutNext) {
        this.shootoutNext = shootoutNext;
    }

    public boolean isNewOrder() {
        return newOrder;
    }

    public void setNewOrder(boolean newOrder) {
        this.newOrder = newOrder;
    }

    public int getPenaltyScoreA() {
        return penaltyScoreA;
    }

    public void setPenaltyScoreA(int penaltyScoreA) {
        this.penaltyScoreA = penaltyScoreA;
    }

    public int getPenaltyScoreB() {
        return penaltyScoreB;
    }

    public void setPenaltyScoreB(int penaltyScoreB) {
        this.penaltyScoreB = penaltyScoreB;
    }

    public TeamId getStartedPenalty() {
        return startedPenalty;
    }

    public void setStartedPenalty(TeamId startedPenalty) {
        this.startedPenalty = startedPenalty;
    }

    public int getShootingCounter() {
        return shootingCounter;
    }

    public void setShootingCounter(int shootingCounter) {
        this.shootingCounter = shootingCounter;
    }

    public void updateShootingNext() {
        this.shootoutNext = this.getShootingNext();
    }


    @JsonIgnore
    public TeamId getShootingNext() {
        if (startedPenalty == TeamId.UNKNOWN || startedPenalty == null)
            return TeamId.UNKNOWN;
        if (shootingCounter == 0)
            return startedPenalty;

        if (newOrder) {
            if (shootingCounter >= 1) {
                if (((int) (shootingCounter - 1) / 2) % 2 == 0) // if is 1,2,5,6, whic is !startedPenalty
                {
                    return revert(startedPenalty);
                } else
                    return startedPenalty;

            } else
                return startedPenalty; // first shooting
        } else { // old ABAB order
            if (((int) (shootingCounter) % 2) == 0) // if is 1,2,5,6, whic is !startedPenalty
            {
                return (startedPenalty);
            } else
                return revert(startedPenalty);

        }

    }

    private TeamId revert(TeamId startedPenalty2) {
        if (startedPenalty2 == TeamId.A)
            return TeamId.B;
        else if (startedPenalty2 == TeamId.B)
            return TeamId.A;

        return null;
    }


    public FootballShootoutInfo clone() {
        FootballShootoutInfo coPenaltyInfo = new FootballShootoutInfo(this.penaltyScoreA, this.penaltyScoreB,
                        this.startedPenalty, this.shootingCounter, this.newOrder, this.shootoutListStatusA,
                        this.shootoutListStatusB);
        return coPenaltyInfo;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (newOrder ? 1231 : 1237);
        result = prime * result + penaltyScoreA;
        result = prime * result + penaltyScoreB;
        result = prime * result + shootingCounter;
        result = prime * result + ((shootoutListStatusA == null) ? 0 : shootoutListStatusA.hashCode());
        result = prime * result + ((shootoutListStatusB == null) ? 0 : shootoutListStatusB.hashCode());
        result = prime * result + ((shootoutNext == null) ? 0 : shootoutNext.hashCode());
        result = prime * result + ((startedPenalty == null) ? 0 : startedPenalty.hashCode());
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
        FootballShootoutInfo other = (FootballShootoutInfo) obj;
        if (newOrder != other.newOrder)
            return false;
        if (penaltyScoreA != other.penaltyScoreA)
            return false;
        if (penaltyScoreB != other.penaltyScoreB)
            return false;
        if (shootingCounter != other.shootingCounter)
            return false;
        if (shootoutListStatusA == null) {
            if (other.shootoutListStatusA != null)
                return false;
        } else if (!shootoutListStatusA.equals(other.shootoutListStatusA))
            return false;
        if (shootoutListStatusB == null) {
            if (other.shootoutListStatusB != null)
                return false;
        } else if (!shootoutListStatusB.equals(other.shootoutListStatusB))
            return false;
        if (shootoutNext != other.shootoutNext)
            return false;
        if (startedPenalty != other.startedPenalty)
            return false;
        return true;
    }


    /*
     * T must be enum in this case
     */
    public static <T> List<T> cloneEnumArrayList(List<T> list) {
        List<T> clone = new ArrayList<T>(list.size());
        for (T item : list)
            clone.add(item);
        return clone;
    }



}
