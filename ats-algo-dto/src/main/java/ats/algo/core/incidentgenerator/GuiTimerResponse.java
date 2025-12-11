package ats.algo.core.incidentgenerator;

import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;

public class GuiTimerResponse {
    private String sourceSystem;


    private ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType;
    private int elapsedTime;

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public ElapsedTimeMatchIncidentType getElapsedTimeMatchIncidentType() {
        return elapsedTimeMatchIncidentType;
    }

    public void setElapsedTimeMatchIncidentType(ElapsedTimeMatchIncidentType elapsedTimeMatchIncidentType) {
        this.elapsedTimeMatchIncidentType = elapsedTimeMatchIncidentType;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }



}
