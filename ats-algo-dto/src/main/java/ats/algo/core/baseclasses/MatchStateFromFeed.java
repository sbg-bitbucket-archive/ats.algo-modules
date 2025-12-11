package ats.algo.core.baseclasses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import ats.algo.sport.tennis.TennisMatchStateFromFeed;
import ats.algo.sport.football.FootballMatchStateFromFeed;

/**
 * Container for holding any matchstate information obtained from a feed
 * 
 * @author gicha
 *
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonSubTypes({@Type(name = "TennisMatchStateFromFeed", value = TennisMatchStateFromFeed.class),
        @Type(name = "FootballMatchStateFromFeed", value = FootballMatchStateFromFeed.class)})
public abstract class MatchStateFromFeed implements Serializable {
    private static final long serialVersionUID = 1L;


    public abstract int hashCode();

    public abstract boolean equals(Object obj);

    /**
     * delivers a description of the score as reported by the source feed
     * 
     * @return
     */
    public abstract String toShortString();


}
