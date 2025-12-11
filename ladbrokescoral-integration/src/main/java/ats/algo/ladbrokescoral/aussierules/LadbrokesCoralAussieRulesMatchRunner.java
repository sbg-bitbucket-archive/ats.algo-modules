package ats.algo.ladbrokescoral.aussierules;

import ats.algo.ladbrokescoral.LadbrokesCoralSportInitialisation;
import ats.algo.sport.afl.AflMatchRunner;

/**
 * Matchrunner which uses the LadbrokesCoral aussie rules model
 */
public class LadbrokesCoralAussieRulesMatchRunner extends AflMatchRunner {

    public static void main(String[] args) {
        LadbrokesCoralSportInitialisation.initAussieRules();
        init(args);
    }
}
