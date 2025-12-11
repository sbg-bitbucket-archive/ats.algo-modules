package ats.algo.core.incidentgenerator;

import java.util.ArrayList;
import java.util.List;

import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;

public class GuiData {



    private ElapsedTimeMatchIncidentType expectedNextElapsedTimeMatchIncidentType;
    private String ElapsedTimeMatchIncidentTypeDescription;
    private boolean stopClock;

    private List<GuiDataComponent> guiDataComponents;

    public GuiData() {
        guiDataComponents = new ArrayList<>();
    }


    public List<GuiDataComponent> getGuiDataComponents() {
        return guiDataComponents;
    }


    public ElapsedTimeMatchIncidentType getExpectedNextElapsedTimeMatchIncidentType() {
        return expectedNextElapsedTimeMatchIncidentType;
    }

    public void setExpectedNextElapsedTimeMatchIncidentType(
                    ElapsedTimeMatchIncidentType expectedNextElapsedTimeMatchIncidentType) {
        this.expectedNextElapsedTimeMatchIncidentType = expectedNextElapsedTimeMatchIncidentType;
    }

    public String getElapsedTimeMatchIncidentTypeDescription() {
        return ElapsedTimeMatchIncidentTypeDescription;
    }

    public void setElapsedTimeMatchIncidentTypeDescription(String elapsedTimeMatchIncidentTypeDescription) {
        ElapsedTimeMatchIncidentTypeDescription = elapsedTimeMatchIncidentTypeDescription;
    }

    public void addGuiDataComponents(GuiDataComponent guiDataComponent) {
        guiDataComponents.add(guiDataComponent);
    }

    public boolean isStopClock() {
        return stopClock;
    }

    public void setStopClock(boolean stopClock) {
        this.stopClock = stopClock;
    }

}
