package ats.algo.sport.esports;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.genericsupportfunctions.PairOfIntegers;

/**
 * Represents the e-sports simple match state
 *
 */
public class EsportsSimpleMatchState extends SimpleMatchState {


    private static final long serialVersionUID = 1L;
    
    /**
     * Home team current/total score
     */
    private int homeTotalScore;
    
    /**
     * Away team current/total score
     */
    private int awayTotalScore;
    
    /**
     * Contains a {@link Pair} of home/away scores per match round
     */
    private Map<String, PairOfIntegers> roundScores;
    
    
    /**
     * 
     * Constructor
     * 
     * @param preMatch        - the match is on prematch period
     * @param matchCompleted  - the match is completed
     * @param homeTotalScore  - home team score
     * @param awayTotalScore  - away team score
     * @param roundScores     - match scores per round
     */
    public EsportsSimpleMatchState(@JsonProperty("preMatch") boolean preMatch,
                    @JsonProperty("matchCompleted") boolean matchCompleted,
                    @JsonProperty("homeTotalScore") int homeTotalScore,
                    @JsonProperty("awayTotalScore") int awayTotalScore,
                    @JsonProperty("roundScores") Map<String, PairOfIntegers> roundScores) {

        super(preMatch, matchCompleted);
        this.homeTotalScore = homeTotalScore;
        this.awayTotalScore = awayTotalScore;
        this.roundScores = roundScores;
    }

    
    /**
     * Constructor
     */
    public EsportsSimpleMatchState() {
        super();
    }
    

    /**
     * Returns the home total score
     * 
     * @return home team total score
     */
    public int getHomeTotalScore() {
        
        return homeTotalScore;
    }


    /**
     * Sets the home total score
     * 
     * @param homeTotalScore - home total score
     * 
     * @return EsportsSimpleMatchState
     */
    public EsportsSimpleMatchState setHomeTotalScore(int homeTotalScore) {
        
        this.homeTotalScore = homeTotalScore;
        return this;
    }


    /**
     * Returns the away total score
     * 
     * @return away team total score
     */
    public int getAwayTotalScore() {
        
        return awayTotalScore;
    }


    /**
     * Sets the away total score
     * 
     * @param awayTotalScore - away total score
     * 
     * @return EsportsSimpleMatchState
     */
    public EsportsSimpleMatchState setAwayTotalScore(int awayTotalScore) {
        
        this.awayTotalScore = awayTotalScore;
        return this;
    }


    /**
     * Returns the round scores
     * 
     * @return round scores
     */
    public Map<String, PairOfIntegers> getRoundScores() {
        
        if (roundScores == null) {
            return new HashMap<>();
        }
        return roundScores;
    }


    /**
     * Sets the round scores
     * 
     * @param roundScores - round scores
     * 
     * @return EsportsSimpleMatchState
     */
    public EsportsSimpleMatchState setRoundScores(Map<String, PairOfIntegers> roundScores) {
        
        this.roundScores = roundScores;
        return this;
    }


    @Override
    public int hashCode() {
        
        return new HashCodeBuilder()
                        .append(awayTotalScore)
                        .append(homeTotalScore)
                        .append(roundScores)
                        .toHashCode();
    }


    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        EsportsSimpleMatchState other = (EsportsSimpleMatchState) obj;
        return homeTotalScore == other.homeTotalScore &&
                        awayTotalScore == other.awayTotalScore &&
                        Objects.equals(roundScores, other.roundScores);
    }

}
