package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ats.core.util.json.JsonUtil;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetPointsEntry {
    private String teamID;
    private double targetPoints;


    public TargetPointsEntry() {}


    public TargetPointsEntry(String teamID, double targetPoints) {
        super();
        this.teamID = teamID;
        this.targetPoints = targetPoints;
    }


    public String getTeamID() {
        return teamID;
    }


    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }


    public double getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(double targetPoints) {
        this.targetPoints = targetPoints;
    }

    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
