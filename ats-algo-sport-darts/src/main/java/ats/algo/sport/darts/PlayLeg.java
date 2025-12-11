package ats.algo.sport.darts;

import ats.algo.core.common.TeamId;
import ats.algo.sport.darts.DartTarget;
import ats.algo.sport.darts.LegState;
import ats.algo.sport.darts.LegThrowResult;

/**
 * Executes a single instance of a dart leg
 * 
 * @author Geoff
 * 
 */
public class PlayLeg {
    private DartProbs probsA;
    private DartProbs probsB;
    private PlayDart playDart;

    public PlayLeg() {
        playDart = new PlayDart();
    }

    public void setPlayers(DartProbs probsA, DartProbs probsB) {
        this.probsA = probsA;
        this.probsB = probsB;
    }

    /**
     * play a dart leg, starting from the stated score in ls
     * 
     * @param ls current state of the leg, including the score
     * @return
     */
    public LegResult play(LegState ls) {
        if (ls.playerAtOche == TeamId.UNKNOWN)
            throw new IllegalArgumentException();
        LegState cs = ls.copy(); // holds current score
        DartProbs probsForPlayerAtOche;
        if (ls.playerAtOche == TeamId.A)
            probsForPlayerAtOche = probsA;
        else
            probsForPlayerAtOche = probsB;
        //
        // keep throwing darts until someone wins
        //
        DartTarget target = new DartTarget(0, 1); // not interested here in what
                                                  // we aimed at, so just
                                                  // throw away

        while (true) {
            DartTarget throwResult = playDart.throwDart(probsForPlayerAtOche, cs, target);
            LegThrowResult tr = cs.updateScore(throwResult);
            // var str = String.Format("A:{0}: B:{1} atOcheA: {2}.",
            // cs.PlayerA.Points, cs.PlayerB.Points,
            // cs.PlayerAtOche);
            // str = str + String.Format("Target: {0},{1}, actual: {2},{3}",
            // target.multiplier, target.no,
            // throwResult.multiplier, throwResult.no);
            // Console.WriteLine(str);


            if (tr == LegThrowResult.ISLEGWINNINGDART) {
                LegResult lr = new LegResult();
                if (cs.next180InLeg) {
                    lr.first180InLeg = cs.first180InLeg;
                    cs.first180InLeg = TeamId.UNKNOWN;
                    cs.next180InLeg = false;
                }
                lr.legWinner = cs.playerAtOche;
                lr.checkoutColour = DartBoard.colourOfNumber(throwResult.no);
                lr.checkoutScore = cs.currentThreeDartSetScore();
                lr.NDartsThrownA = cs.playerA.NDartsthrown;
                lr.NDartsThrownB = cs.playerB.NDartsthrown;
                lr.N180sThrownA = cs.playerA.N180sThrown;
                lr.N180sThrownB = cs.playerB.N180sThrown;
                return lr;
            }
            if (tr == LegThrowResult.NEXTDARTFROMOTHERPLAYER) {
                if (cs.playerAtOche == TeamId.A)
                    probsForPlayerAtOche = probsA;
                else
                    probsForPlayerAtOche = probsB;
            }
        }
    }
}
