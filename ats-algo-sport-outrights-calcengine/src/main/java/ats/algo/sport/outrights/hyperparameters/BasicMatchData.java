package ats.algo.sport.outrights.hyperparameters;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ats.algo.genericsupportfunctions.CsvAble;

public class BasicMatchData implements CsvAble {

    private LocalDateTime date;
    private String teamA;
    private String teamB;
    private double offenseRatingA;
    private double defenseRatingA;
    private double offenseRatingB;
    private double defenseRatingB;
    private int goalsA;
    private int goalsB;
    private double homeWinProb365;
    private double drawProb365;
    private double awayWinProb365;

    private static final int N_PARAMS = 12;

    public BasicMatchData() {}

    public static BasicMatchData generate(HistoryMatchInfos h) {
        BasicMatchData b = new BasicMatchData();
        b.date = h.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        b.teamA = h.getHomeTeamName();
        b.teamB = h.getAwayTeamName();
        b.offenseRatingA = h.getORateHome();
        b.defenseRatingA = h.getDRateHome();
        b.offenseRatingB = h.getORateAway();
        b.defenseRatingB = h.getDRateAway();
        b.goalsA = h.getFtHomeGoal();
        b.goalsB = h.getFtAwayGoal();
        b.homeWinProb365 = h.getHomeWinProb365();
        b.drawProb365 = h.getDrawProb365();
        b.awayWinProb365 = h.getAwayWinProb365();
        return b;
    }

    @Override
    public List<String> hdrs() {
        List<String> hdrs = new ArrayList<>(N_PARAMS);
        hdrs.add("date");
        hdrs.add("teamA");
        hdrs.add("teamB");
        hdrs.add("offenseRatingA");
        hdrs.add("defenseRatingA");
        hdrs.add("offenseRatingB");
        hdrs.add("defenseRatingB");
        hdrs.add("goalsA");
        hdrs.add("goalsB");
        hdrs.add("homeWinProb365");
        hdrs.add("drawProb365");
        hdrs.add("awayWinProb365");
        return hdrs;
    }

    @Override
    public List<String> rowContent() {
        List<String> row = new ArrayList<>();
        row.add(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        row.add(teamA);
        row.add(teamB);
        row.add(String.format("%.3f", offenseRatingA));
        row.add(String.format("%.3f", defenseRatingA));
        row.add(String.format("%.3f", offenseRatingB));
        row.add(String.format("%.3f", defenseRatingB));
        row.add(String.format("%d", goalsA));
        row.add(String.format("%d", goalsB));
        row.add(String.format("%.3f", homeWinProb365));
        row.add(String.format("%.3f", drawProb365));
        row.add(String.format("%.3f", awayWinProb365));
        return row;
    }

    @Override
    public void initialiseFromRowContent(List<String> row) {
        /*
         * not needed for this class - only ever write to CSv
         */
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public double getOffenseRatingA() {
        return offenseRatingA;
    }

    public void setOffenseRatingA(double o_rate_home) {
        this.offenseRatingA = o_rate_home;
    }

    public double getDefenseRatingA() {
        return defenseRatingA;
    }

    public void setDefenseRatingA(double d_rate_home) {
        this.defenseRatingA = d_rate_home;
    }

    public double getOffenseRatingB() {
        return offenseRatingB;
    }

    public void setOffenseRatingB(double o_rate_away) {
        this.offenseRatingB = o_rate_away;
    }

    public double getDefenseRatingB() {
        return defenseRatingB;
    }

    public void setDefenseRatingB(double d_rate_away) {
        this.defenseRatingB = d_rate_away;
    }

    public int getGoalsA() {
        return goalsA;
    }

    public void setGoalsA(int ftHomeGoal) {
        this.goalsA = ftHomeGoal;
    }

    public int getGoalsB() {
        return goalsB;
    }

    public void setGoalsB(int ftAwayGoal) {
        this.goalsB = ftAwayGoal;
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

}
