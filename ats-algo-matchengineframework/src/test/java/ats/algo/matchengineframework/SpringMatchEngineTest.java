package ats.algo.matchengineframework;

import static org.junit.Assert.*;

import org.junit.Test;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.markets.Markets;
import ats.algo.core.markets.ResultedMarkets;
import ats.algo.sport.tennis.TennisMatchFormat;

public class SpringMatchEngineTest {

    class TestSpringMatchEngine extends SpringMatchEngine {

        TestSpringMatchEngine() {
            super(SupportedSportType.TENNIS);
        }

        @Override
        public void calculate() {}

        @Override
        public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState,
                        MatchState currentMatchState) {
            return null;
        }

        @Override
        public void setMatchFormat(MatchFormat matchFormat) {

        }
    }

    class TestNonSpringMatchEngine extends MatchEngine {

        TestNonSpringMatchEngine() {
            super(SupportedSportType.TENNIS);
        }


        @Override
        public void calculate() {}

        @Override
        public ResultedMarkets resultMarkets(Markets markets, MatchState previousMatchState,
                        MatchState currentMatchState) {
            return null;
        }
    }

    @Test
    public void test() {
        TestSpringMatchEngine matchEngine = new TestSpringMatchEngine();
        TennisMatchFormat matchFormat = new TennisMatchFormat();
        matchEngine.setMatchFormat(matchFormat);
        matchEngine.calculate();
        boolean isSpringMatchEngine = SpringMatchEngine.class.isAssignableFrom(matchEngine.getClass());
        assertTrue(isSpringMatchEngine);
        TestNonSpringMatchEngine matchEngine2 = new TestNonSpringMatchEngine();
        isSpringMatchEngine = SpringMatchEngine.class.isAssignableFrom(matchEngine2.getClass());
        assertFalse(isSpringMatchEngine);
    }

}
