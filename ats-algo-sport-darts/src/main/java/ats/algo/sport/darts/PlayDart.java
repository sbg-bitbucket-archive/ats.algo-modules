package ats.algo.sport.darts;

import ats.algo.genericsupportfunctions.RandomNoGenerator;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.LegState;

public class PlayDart {

    /**
     * loads the Target type into the checkout table
     * 
     * @throws Exception
     */
    private static DartTarget setTarget(int ipN) {
        int opMultiplier = (int) (ipN / 100);
        int opN = ipN - opMultiplier * 100;
        if (opMultiplier == 0)
            opMultiplier = 1;
        if (opN == 0)
            opN = 1;
        if (opMultiplier < 1 || opMultiplier > 3 || opN < 1 || (opN > 20 && opN != 25))
            throw new IllegalArgumentException();
        DartTarget dartTarget = new DartTarget(opMultiplier, opN);
        return dartTarget;
    }

    //
    // holds one row of data in the checkout table
    //
    private class CheckoutEntry {
        @SuppressWarnings("unused")
        public int score;
        public int nDartsToFinish;
        public boolean fewerDartsViaBull;
        public int nextNoIfStd;
        public int nextNoIfViaBull;
        public int multiplierIfStd;
        public int multiplierIfBull;

        /// <summary>
        /// one row in the checkout table
        /// </summary>
        /// <param name="score">the current score for the player about to throw</param>
        /// <param name="nDartsToFinish">min no of darts required to finish from current
        /// score</param>
        /// <param name="fewerDartsViaBull">true if can finish in fewer darts by hitting
        /// inner bull</param>
        /// <param name="nextNoStd">next no to aim at in format mnn, where m =2,3 is
        /// multiplier;</param>
        /// <param name="nextNoBull">next no to aim at in format mnn, where m =2,3 is
        /// multiplier;</param>
        public CheckoutEntry(int score, int nDartsToFinish, boolean fewerDartsViaBull, int nextNoStd, int nextNoBull) {
            this.score = score;
            this.nDartsToFinish = nDartsToFinish;
            this.fewerDartsViaBull = fewerDartsViaBull;
            this.nextNoIfStd = 0;
            this.multiplierIfStd = 0;
            this.nextNoIfViaBull = 0;
            this.multiplierIfBull = 0;
            this.nextNoIfViaBull = nextNoBull;
            DartTarget dartTarget;
            dartTarget = setTarget(nextNoStd);
            this.nextNoIfStd = dartTarget.getNo();
            this.multiplierIfStd = dartTarget.getMultiplier();
            dartTarget = setTarget(nextNoBull);
            this.nextNoIfViaBull = dartTarget.getNo();
            this.multiplierIfBull = dartTarget.getMultiplier();
        }
    }

    /// <summary>
    /// contains the data required to determine the next dart target for any score
    /// up to 170
    /// </summary>
    private CheckoutEntry[] checkoutTable = {new CheckoutEntry(0, -1, false, 0, 0),
            new CheckoutEntry(1, -1, false, 0, 0), new CheckoutEntry(2, 1, false, 201, 0),
            new CheckoutEntry(3, 2, false, 1, 0), new CheckoutEntry(4, 1, false, 202, 0),
            new CheckoutEntry(5, 2, false, 1, 0), new CheckoutEntry(6, 1, false, 203, 0),
            new CheckoutEntry(7, 2, false, 3, 0), new CheckoutEntry(8, 1, false, 204, 0),
            new CheckoutEntry(9, 2, false, 1, 0), new CheckoutEntry(10, 1, false, 205, 0),
            new CheckoutEntry(11, 2, false, 3, 0), new CheckoutEntry(12, 1, false, 206, 0),
            new CheckoutEntry(13, 2, false, 5, 0), new CheckoutEntry(14, 1, false, 207, 0),
            new CheckoutEntry(15, 2, false, 7, 0), new CheckoutEntry(16, 1, false, 208, 0),
            new CheckoutEntry(17, 2, false, 1, 0), new CheckoutEntry(18, 1, false, 209, 0),
            new CheckoutEntry(19, 2, false, 3, 0), new CheckoutEntry(20, 1, false, 210, 0),
            new CheckoutEntry(21, 2, false, 5, 0), new CheckoutEntry(22, 1, false, 211, 0),
            new CheckoutEntry(23, 2, false, 7, 0), new CheckoutEntry(24, 1, false, 212, 0),
            new CheckoutEntry(25, 2, false, 9, 0), new CheckoutEntry(26, 1, false, 213, 0),
            new CheckoutEntry(27, 2, false, 11, 0), new CheckoutEntry(28, 1, false, 214, 0),
            new CheckoutEntry(29, 2, false, 13, 0), new CheckoutEntry(30, 1, false, 215, 0),
            new CheckoutEntry(31, 2, false, 15, 0), new CheckoutEntry(32, 1, false, 216, 0),
            new CheckoutEntry(33, 2, false, 1, 0), new CheckoutEntry(34, 1, false, 217, 0),
            new CheckoutEntry(35, 2, false, 3, 0), new CheckoutEntry(36, 1, false, 218, 0),
            new CheckoutEntry(37, 2, false, 5, 0), new CheckoutEntry(38, 1, false, 219, 0),
            new CheckoutEntry(39, 2, false, 7, 0), new CheckoutEntry(40, 1, false, 220, 0),
            new CheckoutEntry(41, 2, false, 9, 0), new CheckoutEntry(42, 2, false, 10, 0),
            new CheckoutEntry(43, 2, false, 11, 0), new CheckoutEntry(44, 2, false, 12, 0),
            new CheckoutEntry(45, 2, false, 13, 0), new CheckoutEntry(46, 2, false, 14, 0),
            new CheckoutEntry(47, 2, false, 15, 0), new CheckoutEntry(48, 2, false, 16, 0),
            new CheckoutEntry(49, 2, false, 17, 0), new CheckoutEntry(50, 2, true, 18, 225),
            new CheckoutEntry(51, 2, false, 19, 0), new CheckoutEntry(52, 2, false, 20, 0),
            new CheckoutEntry(53, 2, false, 13, 0), new CheckoutEntry(54, 2, false, 14, 0),
            new CheckoutEntry(55, 2, false, 15, 0), new CheckoutEntry(56, 2, false, 16, 0),
            new CheckoutEntry(57, 2, false, 17, 0), new CheckoutEntry(58, 2, false, 18, 0),
            new CheckoutEntry(59, 2, false, 19, 0), new CheckoutEntry(60, 2, false, 20, 0),
            new CheckoutEntry(61, 2, false, 315, 0), new CheckoutEntry(62, 2, false, 310, 0),
            new CheckoutEntry(63, 2, false, 313, 0), new CheckoutEntry(64, 2, false, 216, 0),
            new CheckoutEntry(65, 2, false, 315, 0), new CheckoutEntry(66, 2, false, 310, 0),
            new CheckoutEntry(67, 2, false, 317, 0), new CheckoutEntry(68, 2, false, 316, 0),
            new CheckoutEntry(69, 2, false, 319, 0), new CheckoutEntry(70, 2, false, 318, 0),
            new CheckoutEntry(71, 2, false, 313, 0), new CheckoutEntry(72, 2, false, 316, 0),
            new CheckoutEntry(73, 2, false, 319, 0), new CheckoutEntry(74, 2, false, 314, 0),
            new CheckoutEntry(75, 2, false, 315, 0), new CheckoutEntry(76, 2, false, 320, 0),
            new CheckoutEntry(77, 2, false, 315, 0), new CheckoutEntry(78, 2, false, 314, 0),
            new CheckoutEntry(79, 2, false, 313, 0), new CheckoutEntry(80, 2, false, 316, 0),
            new CheckoutEntry(81, 2, false, 315, 0), new CheckoutEntry(82, 2, false, 314, 0),
            new CheckoutEntry(83, 2, false, 317, 0), new CheckoutEntry(84, 2, false, 320, 0),
            new CheckoutEntry(85, 2, false, 315, 0), new CheckoutEntry(86, 2, false, 318, 0),
            new CheckoutEntry(87, 2, false, 317, 0), new CheckoutEntry(88, 2, false, 316, 0),
            new CheckoutEntry(89, 2, false, 319, 0), new CheckoutEntry(90, 2, false, 318, 0),
            new CheckoutEntry(91, 2, false, 317, 0), new CheckoutEntry(92, 2, false, 320, 0),
            new CheckoutEntry(93, 2, false, 319, 0), new CheckoutEntry(94, 2, false, 318, 0),
            new CheckoutEntry(95, 3, true, 319, 315), new CheckoutEntry(96, 2, false, 320, 0),
            new CheckoutEntry(97, 2, false, 319, 0), new CheckoutEntry(98, 2, true, 320, 316),
            new CheckoutEntry(99, 3, false, 319, 0), new CheckoutEntry(100, 2, false, 320, 0),
            new CheckoutEntry(101, 3, true, 317, 317), new CheckoutEntry(102, 3, false, 320, 0),
            new CheckoutEntry(103, 3, false, 320, 0), new CheckoutEntry(104, 3, true, 318, 318),
            new CheckoutEntry(105, 3, false, 320, 0), new CheckoutEntry(106, 3, false, 320, 0),
            new CheckoutEntry(107, 3, true, 319, 319), new CheckoutEntry(108, 3, false, 320, 0),
            new CheckoutEntry(109, 3, false, 320, 0), new CheckoutEntry(110, 3, true, 320, 320),
            new CheckoutEntry(111, 3, false, 320, 0), new CheckoutEntry(112, 3, false, 320, 0),
            new CheckoutEntry(113, 3, false, 320, 0), new CheckoutEntry(114, 3, false, 320, 0),
            new CheckoutEntry(115, 3, false, 320, 0), new CheckoutEntry(116, 3, false, 320, 0),
            new CheckoutEntry(117, 3, false, 320, 0), new CheckoutEntry(118, 3, false, 320, 0),
            new CheckoutEntry(119, 3, false, 319, 0), new CheckoutEntry(120, 3, false, 320, 0),
            new CheckoutEntry(121, 3, false, 320, 0), new CheckoutEntry(122, 3, false, 318, 0),
            new CheckoutEntry(123, 3, false, 319, 0), new CheckoutEntry(124, 3, false, 320, 0),
            new CheckoutEntry(125, 3, false, 320, 0), new CheckoutEntry(126, 3, false, 319, 0),
            new CheckoutEntry(127, 3, false, 319, 0), new CheckoutEntry(128, 3, false, 318, 0),
            new CheckoutEntry(129, 3, false, 319, 0), new CheckoutEntry(130, 3, false, 320, 0),
            new CheckoutEntry(131, 3, false, 320, 0), new CheckoutEntry(132, 3, false, 320, 0),
            new CheckoutEntry(133, 3, false, 320, 0), new CheckoutEntry(134, 3, false, 320, 0),
            new CheckoutEntry(135, 3, false, 320, 0), new CheckoutEntry(136, 3, false, 320, 0),
            new CheckoutEntry(137, 3, false, 317, 0), new CheckoutEntry(138, 3, false, 320, 0),
            new CheckoutEntry(139, 3, false, 320, 0), new CheckoutEntry(140, 3, false, 320, 0),
            new CheckoutEntry(141, 3, false, 320, 0), new CheckoutEntry(142, 3, false, 320, 0),
            new CheckoutEntry(143, 3, false, 320, 0), new CheckoutEntry(144, 3, false, 320, 0),
            new CheckoutEntry(145, 3, false, 320, 0), new CheckoutEntry(146, 3, false, 320, 0),
            new CheckoutEntry(147, 3, false, 320, 0), new CheckoutEntry(148, 3, false, 320, 0),
            new CheckoutEntry(149, 3, false, 320, 0), new CheckoutEntry(150, 3, false, 320, 0),
            new CheckoutEntry(151, 3, false, 320, 0), new CheckoutEntry(152, 3, false, 320, 0),
            new CheckoutEntry(153, 3, false, 320, 0), new CheckoutEntry(154, 3, false, 320, 0),
            new CheckoutEntry(155, 3, false, 320, 0), new CheckoutEntry(156, 3, false, 320, 0),
            new CheckoutEntry(157, 3, false, 320, 0), new CheckoutEntry(158, 3, false, 320, 0),
            new CheckoutEntry(159, 4, false, 320, 0), new CheckoutEntry(160, 3, false, 320, 0),
            new CheckoutEntry(161, 3, false, 320, 0), new CheckoutEntry(162, 4, false, 320, 0),
            new CheckoutEntry(163, 4, false, 320, 0), new CheckoutEntry(164, 3, false, 320, 0),
            new CheckoutEntry(165, 4, false, 320, 0), new CheckoutEntry(166, 4, false, 320, 0),
            new CheckoutEntry(167, 3, false, 320, 0), new CheckoutEntry(168, 4, false, 320, 0),
            new CheckoutEntry(169, 4, false, 320, 0), new CheckoutEntry(170, 3, false, 320, 0)};

    /**
     * adjusts the target to aim at to avoid the numbers from which 3 dart finish is not possible
     * 
     * @param score the current score
     * @param n the default target - 20 or 19
     * @return true if 3 dart finish not possible from this score
     */
    public int adjustTargetToAvoidDeadNumbers(int score, int n) {
        if (!notoAvoid(score - 3 * n))
            return n;
        if (!notoAvoid(score - 3 * (n - 1)))
            return n - 1;
        if (!notoAvoid(score - 3 * (n - 2)))
            return n - 2;
        if (!notoAvoid(score - 3 * (n - 3)))
            return n - 3;
        return n - 4;
    }

    /**
     * returns true if n is a number from which 3 dart finish not possible
     * 
     * @param n
     * @return
     */
    private boolean notoAvoid(int n) {
        return ((n == 169) || (n == 168) || (n == 166) || (n == 165) || (n == 163) || (n == 162) || (n == 159));
    }

    /**
     * decides what to aim dart at, based upon the current score and emulates results of throwing the dart
     * 
     * @param playerAtOche
     * @param score
     * @param target
     * @return
     */
    public DartTarget throwDart(DartProbs playerAtOche, LegState score, DartTarget target) {
        DartTarget actual;
        if (score.doubleReqdToStart && score.scoreAtOche() == 501) {
            target.setMultiplier(2);
            target.setNo(20);
        } else {
            if (score.scoreAtOche() > 170) {
                boolean twoTripleTwentiesAlready = (score.getThreeDartSet().noDartsThrown == 2)
                                && score.getThreeDartSet().hitT20(1) && score.getThreeDartSet().hitT20(2);
                boolean aimforT20 = true;
                target.setMultiplier(3); // triple
                if (twoTripleTwentiesAlready)
                    aimforT20 = !aimForT19();
                if (aimforT20)
                    target.setNo(adjustTargetToAvoidDeadNumbers(score.scoreAtOche(), 20));
                else
                    target.setNo(adjustTargetToAvoidDeadNumbers(score.scoreAtOche(), 19));
            } else {
                CheckoutEntry checkoutEntry = checkoutTable[score.scoreAtOche()];
                if (restPlayerCanCheckoutInTwoDarts(score.scoreAtRest()) && checkoutEntry.fewerDartsViaBull) {
                    // go for bull
                    target.setMultiplier(checkoutEntry.multiplierIfBull);
                    target.setNo(checkoutEntry.nextNoIfViaBull);
                } else {
                    // standard checkout
                    target.setMultiplier(checkoutEntry.multiplierIfStd);
                    target.setNo(checkoutEntry.nextNoIfStd);
                }
            }
        }
        //
        // worked out what to aim for. Execute and save both target and actual
        //
        if (target.getMultiplier() == 0)
            throw new IllegalArgumentException();

        actual = executeThrow(playerAtOche, target);
        return actual;
    }

    /**
     * returns true if should aim for T19 rather than T20
     * 
     * @return
     */
    private boolean aimForT19() {
        double cProbAimFor19 = 0.05;
        return (RandomNoGenerator.nextDouble() < cProbAimFor19);
    }

    /**
     * determines whether the player not currently throwing can checkout in two or fewer darts
     * 
     * @param score rest player's score
     * @return
     */
    private boolean restPlayerCanCheckoutInTwoDarts(int score) {
        if (score > 110)
            return false;
        CheckoutEntry checkoutRow = checkoutTable[score];
        return checkoutRow.nDartsToFinish <= 2;
    }

    /**
     * Emulates the throw of the dart
     * 
     * @param probs object which holds the probability tables
     * @param target
     * @return
     */
    private DartTarget executeThrow(DartProbs probs, DartTarget target) {
        int aimedAt;
        if (target.getNo() == 25)
            aimedAt = DartProbs.aimedAtBull;
        else
            aimedAt = target.getMultiplier();
        DartTarget actual = probs.getActual(aimedAt, target.getNo());
        return actual;
    }
}
