package ats.algo.scraping.scrapers;

public class FiveThirtyEightTeamData {
    private double points;
    private double off;
    private double def;
    private int w;
    private int d;
    private int l;
    private int goalDiff;
    private String team; // optional only

    public FiveThirtyEightTeamData(double off, double def, int w, int d, int l, int goalDiff, String team) {
        this.off = off;
        this.def = def;
        this.w = w;
        this.d = d;
        this.l = l;
        this.goalDiff = goalDiff;
        this.team = team;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getOff() {
        return off;
    }

    public void setOff(double off) {
        this.off = off;
    }

    public double getDef() {
        return def;
    }

    public void setDef(double def) {
        this.def = def;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public double getL() {
        return l;
    }

    public void setL(int l) {
        this.l = l;
    }

    public int getGoalDiff() {
        return goalDiff;
    }

    public void setGoalDiff(int goalDiff) {
        this.goalDiff = goalDiff;
    }

    public String toString() {
        return "Team: " + this.team + ", OFF: " + this.off + ", DEF: " + this.def + ", W: " + this.w + ", D: " + this.d
                        + ", L: " + this.l;
    }

}
