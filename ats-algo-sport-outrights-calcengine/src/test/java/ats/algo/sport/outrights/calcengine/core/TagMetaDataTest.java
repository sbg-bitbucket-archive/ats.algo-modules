package ats.algo.sport.outrights.calcengine.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TagMetaDataTest {

    @Test
    public void tagMetaDataTest() {
        TagMetaData m = new TagMetaData(FixtureType.LEAGUE_PLAYOFF_LEG1, "3v5");
        assertEquals(3, m.getTeamARegularLeagueFinishPosition());
        assertEquals(5, m.getTeamBRegularLeagueFinishPosition());
        m = new TagMetaData(FixtureType.LEAGUE_PLAY_OFF_FINAL, "F17vF18");
        assertEquals("F17", m.getTeamAFromFixtureID());
        assertEquals("F18", m.getTeamBFromFixtureID());
    }

}
