package ats.algo.inplayGS;

public class PlayerData {
    boolean inStartingLineUp;
    double probScoresGivenTeamScoresAndOnPitch;

    public PlayerData(boolean inStartingLineUp, double probPreMatchScoresGivenTeamScores) {
        this.inStartingLineUp = inStartingLineUp;
        this.probScoresGivenTeamScoresAndOnPitch = probPreMatchScoresGivenTeamScores;
    }

    @Override
    public String toString() {
        return String.format("PlayerData [inStartingLineUp: %b, probScoresGivenTeamScoresAndOnPitch= %.3f]",
                        inStartingLineUp, probScoresGivenTeamScoresAndOnPitch);
    }



}
