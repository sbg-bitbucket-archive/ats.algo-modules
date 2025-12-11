package ats.algo.sport.esports;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ats.algo.genericsupportfunctions.PairOfIntegers;

/**
 * Tests for {@link EsportsSimpleMatchState}
 *
 */
public class EsportsSimpleMatchStateTest {


    /**
     * Test the {@link EsportsSimpleMatchState#EsportsSimpleMatchState(boolean, boolean, int, int, Map)} constructor
     */
    @Test
    public void testConstructor() {

        final boolean isPrematch = false, isCompleted = false;
        final int homeScore = 0, awayScore = 0;
        final Map<String, PairOfIntegers> roundScores = null;
        final EsportsSimpleMatchState esportsSimpleMatchState =
                        new EsportsSimpleMatchState(isPrematch, isCompleted, homeScore, awayScore, roundScores);
        
        Assert.assertFalse(esportsSimpleMatchState.isPreMatch());
        Assert.assertFalse(esportsSimpleMatchState.isMatchCompleted());
        Assert.assertEquals(homeScore, esportsSimpleMatchState.getHomeTotalScore());
        Assert.assertEquals(awayScore, esportsSimpleMatchState.getAwayTotalScore());
        Assert.assertNotNull(esportsSimpleMatchState.getRoundScores());
    }
    
    
    /**
     * Test {@link EsportsSimpleMatchState} getters and setters
     */
    @Test
    public void testGettersSetters() {
           
        final EsportsSimpleMatchState esportsSimpleMatchState =
                        new EsportsSimpleMatchState(true, false, 0, 0, null);

        esportsSimpleMatchState.setPreMatch(false);
        Assert.assertFalse(esportsSimpleMatchState.isPreMatch());
        
        esportsSimpleMatchState.setMatchCompleted(true);
        Assert.assertTrue(esportsSimpleMatchState.isMatchCompleted());
        
        Assert.assertEquals(1, esportsSimpleMatchState.setHomeTotalScore(1).getHomeTotalScore());
        Assert.assertEquals(1, esportsSimpleMatchState.setAwayTotalScore(1).getAwayTotalScore());
        
        Assert.assertNotNull(esportsSimpleMatchState.setRoundScores(null).getRoundScores());
    }
    
    
    /**
     * Test {@link EsportsSimpleMatchState#equals(Object)} and {@link EsportsSimpleMatchState#hashCode()}
     */
    @Test
    public void testEqualsAndHasCode() {
               
        EsportsSimpleMatchState esportsSimpleMatchState =
                        new EsportsSimpleMatchState(true, false, 0, 0, null);
        
        EsportsSimpleMatchState esportsSimpleMatchState2 =
                        new EsportsSimpleMatchState(true, false, 0, 0, null);
        
        Assert.assertTrue(esportsSimpleMatchState.equals(esportsSimpleMatchState2));
        Assert.assertTrue(esportsSimpleMatchState2.equals(esportsSimpleMatchState));
        Assert.assertTrue(esportsSimpleMatchState.equals(esportsSimpleMatchState));
        Assert.assertEquals(esportsSimpleMatchState.hashCode(), esportsSimpleMatchState2.hashCode());
        
        Assert.assertFalse(esportsSimpleMatchState.equals(esportsSimpleMatchState2.setHomeTotalScore(12)));
        Assert.assertFalse(esportsSimpleMatchState.equals(esportsSimpleMatchState2.setAwayTotalScore(12)));
        Assert.assertFalse(esportsSimpleMatchState.equals(esportsSimpleMatchState2.setRoundScores(new HashMap<>())));
        
        Assert.assertFalse(esportsSimpleMatchState.equals(esportsSimpleMatchState2.setRoundScores(new HashMap<>())));
        
        esportsSimpleMatchState =
                        new EsportsSimpleMatchState(true, false, 0, 0, new HashMap<>());
        esportsSimpleMatchState2 =
                        new EsportsSimpleMatchState(true, false, 0, 0, new HashMap<>());
        Assert.assertTrue(esportsSimpleMatchState.equals(esportsSimpleMatchState2));

    }
    
}
