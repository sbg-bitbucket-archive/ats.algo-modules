package ats.algo.sport.outrights.hyperparameters;

import java.util.Arrays;
import java.util.Date;

public class HistoryMatchInfos {
    private double oRateHome = 0.0; // those two need to be filled from the history data
    private double dRateHome = 0.0;
    private double oRateAway = 0.0; // those two need to be filled from the history data
    private double dRateAway = 0.0;

    private int homeRestDays;
    private int awayRestDays;

    private Date rateDate;

    private String homeTeamName;
    private String awayTeamName;
    private Date date;

    private int ftHomeGoal;
    private int ftAwayGoal;

    private int homeCorners;
    private int awayCorners;

    private int homeYellows;
    private int awayYellows;

    private int homeReds;
    private int awayReds;

    private double[] bbavMR = new double[3]; // home:AU, draw:AW, away:AY
    // private int[] matchOddsPinnacle
    private double bbavO25; // over, Col:BB
    private double bbavU25; // average, Col:BD

    private double bbavLine; // betbrain handical for home:BF
    private double bbavAHCPHome; // BH
    private double bbavAHCPAway; // BI

    private double homeWinProb365;
    private double drawProb365;
    private double awayWinProb365;

    public HistoryMatchInfos(String homeTeamName, String awayTeamName, Date date, int ftHomeGoal, int ftAwayGoal,
                    int homeCorners, int awayCorners, int homeYellows, int awayYellows, int homeReds, int awayReds,
                    double[] bbavMR, double bbavO25, double bbavU25, double bbavLine, double bbavAHCPHome,
                    double bbavAHCPAway, double homeWinProb365, double drawProb365, double awayWinProb365, int homeRest,
                    int awayRest) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.date = date;
        this.ftHomeGoal = ftHomeGoal;
        this.ftAwayGoal = ftAwayGoal;
        this.homeCorners = homeCorners;
        this.awayCorners = awayCorners;
        this.homeYellows = homeYellows;
        this.awayYellows = awayYellows;
        this.homeReds = homeReds;
        this.awayReds = awayReds;
        this.bbavMR = bbavMR; // home:AU, draw:AW, away:AY
        this.bbavO25 = bbavO25; // over, Col:BB
        this.bbavU25 = bbavU25; // average, Col:BD
        this.bbavLine = bbavLine; // betbrain handical for home:BF
        this.bbavAHCPHome = bbavAHCPHome;
        this.bbavAHCPAway = bbavAHCPAway;

        this.homeWinProb365 = homeWinProb365;
        this.drawProb365 = drawProb365;
        this.awayWinProb365 = awayWinProb365;
        this.homeRestDays = homeRest;
        this.awayRestDays = awayRest;
    }



    public int getHomeRestDays() {
        return homeRestDays;
    }



    public void setHomeRestDays(int homeRestDays) {
        this.homeRestDays = homeRestDays;
    }



    public int getAwayRestDays() {
        return awayRestDays;
    }



    public void setAwayRestDays(int awayRestDays) {
        this.awayRestDays = awayRestDays;
    }



    public double getHomeWinProb365() {
        return homeWinProb365;
    }

    public void setHomeWinProb365(double homeWinProb365) {
        this.homeWinProb365 = homeWinProb365;
    }

    public double getDrawProb365() {
        return drawProb365;
    }

    public void setDrawProb365(double drawProb365) {
        this.drawProb365 = drawProb365;
    }



    public double getAwayWinProb365() {
        return awayWinProb365;
    }



    public void setAwayWinProb365(double awayWinProb365) {
        this.awayWinProb365 = awayWinProb365;
    }



    public Date getRateDate() {
        return rateDate;
    }



    public void setRateDate(Date rate_date) {
        this.rateDate = rate_date;
    }


    public double getORateHome() {
        return oRateHome;
    }


    public void setORateHome(double o_rate_home) {
        this.oRateHome = o_rate_home;
    }


    public double getDRateHome() {
        return dRateHome;
    }

    public void setDRateHome(double d_rate_home) {
        this.dRateHome = d_rate_home;
    }

    public double getORateAway() {
        return oRateAway;
    }



    public void setORateAway(double o_rate_away) {
        this.oRateAway = o_rate_away;
    }



    public double getDRateAway() {
        return dRateAway;
    }



    public void setDRateAway(double d_rate_away) {
        this.dRateAway = d_rate_away;
    }



    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public void setAwayTeamName(String awayTeamName) {
        this.awayTeamName = awayTeamName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFtHomeGoal() {
        return ftHomeGoal;
    }

    public void setFtHomeGoal(int ftHomeGoal) {
        this.ftHomeGoal = ftHomeGoal;
    }

    public int getFtAwayGoal() {
        return ftAwayGoal;
    }

    public void setFtAwayGoal(int ftAwayGoal) {
        this.ftAwayGoal = ftAwayGoal;
    }

    public int getHomeCorners() {
        return homeCorners;
    }

    public void setHomeCorners(int homeCorners) {
        this.homeCorners = homeCorners;
    }

    public int getAwayCorners() {
        return awayCorners;
    }

    public void setAwayCorners(int awayCorners) {
        this.awayCorners = awayCorners;
    }

    public int getHomeYellows() {
        return homeYellows;
    }

    public void setHomeYellows(int homeYellows) {
        this.homeYellows = homeYellows;
    }

    public int getAwayYellows() {
        return awayYellows;
    }

    public void setAwayYellows(int awayYellows) {
        this.awayYellows = awayYellows;
    }

    public int getHomeReds() {
        return homeReds;
    }

    public void setHomeReds(int homeReds) {
        this.homeReds = homeReds;
    }

    public int getAwayReds() {
        return awayReds;
    }

    public void setAwayReds(int awayReds) {
        this.awayReds = awayReds;
    }

    public double[] getBbavMR() {
        return bbavMR;
    }

    public void setBbavMR(double[] bbavMR) {
        this.bbavMR = bbavMR;
    }

    public double getBbavO25() {
        return bbavO25;
    }

    public void setBbavO25(double bbavO25) {
        this.bbavO25 = bbavO25;
    }

    public double getBbavU25() {
        return bbavU25;
    }

    public void setBbavU25(double bbavU25) {
        this.bbavU25 = bbavU25;
    }

    public double getBbavLine() {
        return bbavLine;
    }

    public void setBbavLine(double bbavLine) {
        this.bbavLine = bbavLine;
    }

    public double getBbavAHCPHome() {
        return bbavAHCPHome;
    }

    public void setBbavAHCPHome(double bbavAHCPHome) {
        this.bbavAHCPHome = bbavAHCPHome;
    }

    public double getBbavAHCPAway() {
        return bbavAHCPAway;
    }

    public void setBbavAHCPAway(double bbavAHCPAway) {
        this.bbavAHCPAway = bbavAHCPAway;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + awayCorners;
        result = prime * result + awayReds;
        result = prime * result + ((awayTeamName == null) ? 0 : awayTeamName.hashCode());
        result = prime * result + awayYellows;
        long temp;
        temp = Double.doubleToLongBits(bbavAHCPAway);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bbavAHCPHome);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(bbavMR);
        temp = Double.doubleToLongBits(bbavO25);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bbavU25);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        result = prime * result + ftAwayGoal;
        result = prime * result + ftHomeGoal;
        result = prime * result + homeCorners;
        result = prime * result + homeReds;
        result = prime * result + ((homeTeamName == null) ? 0 : homeTeamName.hashCode());
        result = prime * result + homeYellows;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HistoryMatchInfos other = (HistoryMatchInfos) obj;
        if (awayCorners != other.awayCorners)
            return false;
        if (awayReds != other.awayReds)
            return false;
        if (awayTeamName == null) {
            if (other.awayTeamName != null)
                return false;
        } else if (!awayTeamName.equals(other.awayTeamName))
            return false;
        if (awayYellows != other.awayYellows)
            return false;
        if (Double.doubleToLongBits(bbavAHCPAway) != Double.doubleToLongBits(other.bbavAHCPAway))
            return false;
        if (Double.doubleToLongBits(bbavAHCPHome) != Double.doubleToLongBits(other.bbavAHCPHome))
            return false;
        if (bbavLine != other.bbavLine)
            return false;
        if (!Arrays.equals(bbavMR, other.bbavMR))
            return false;
        if (Double.doubleToLongBits(bbavO25) != Double.doubleToLongBits(other.bbavO25))
            return false;
        if (Double.doubleToLongBits(bbavU25) != Double.doubleToLongBits(other.bbavU25))
            return false;
        if (date == null) {
            if (other.date != null)
                return false;
        } else if (!date.equals(other.date))
            return false;
        if (ftAwayGoal != other.ftAwayGoal)
            return false;
        if (ftHomeGoal != other.ftHomeGoal)
            return false;
        if (homeCorners != other.homeCorners)
            return false;
        if (homeReds != other.homeReds)
            return false;
        if (homeTeamName == null) {
            if (other.homeTeamName != null)
                return false;
        } else if (!homeTeamName.equals(other.homeTeamName))
            return false;
        if (homeYellows != other.homeYellows)
            return false;
        return true;
    }

    public String toString() {
        return "Home Win " + this.homeWinProb365 + " Draw " + this.drawProb365 + " Away Win " + this.awayWinProb365
                        + "Home: " + this.homeTeamName + ", Away: " + this.awayTeamName + ", Date: '"
                        + this.date.toString() + ", Home Goal: " + this.ftHomeGoal + ", Away Goal: " + this.ftAwayGoal
                        + ", Home Corners: " + this.homeCorners + ", Away Corners: " + this.awayCorners
                        + ", Home Yellows: " + this.homeYellows + ", Away Yellows: " + this.awayYellows
                        + ", Home Reds: " + this.homeReds + ", Away Reds: " + this.awayReds + ", bbavMR: " + this.bbavMR
                        + ", bbavO25: " + this.bbavO25 + ", bbavU25: " + this.bbavU25 + ", bbavLine: " + this.bbavLine
                        + ", bbavAHCPHome: " + this.bbavAHCPHome + ", bbavAHCPAway: " + this.bbavAHCPAway
                        + ", o_rate_home: " + this.oRateHome + ", d_rate_home: " + this.dRateHome + ", o_rate_away: "
                        + this.oRateAway + ", d_rate_away: " + this.dRateAway;
    }

}
