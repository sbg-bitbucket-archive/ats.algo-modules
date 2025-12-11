package ats.algo.sport.buildabet;



@FunctionalInterface
public interface BuildBetSelectionHandler {
    public boolean handleCorrectScore(int scoreA, int scoreB, double lineId);
}
