package ats.algo.sport.outrights;


import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.incidentgenerator.GuiData;
import ats.algo.core.incidentgenerator.GuiDataComponent;
import ats.algo.core.incidentgenerator.GuiIncidentResponse;
import ats.algo.core.incidentgenerator.MatchIncidentGenerator;

public class OutrightsMatchIncidentGenerator extends MatchIncidentGenerator {

    public OutrightsMatchIncidentGenerator() {}

    @Override
    public boolean usesTimer() {
        return false;
    }

    @Override
    public GuiData generateGuiData(SimpleMatchState simpleMatchState) {
        GuiData guiData = new GuiData();
        guiData.setStopClock(true);
        GuiDataComponent guiDataComponent = new GuiDataComponent("Standard Incident Types", "Incident", "Not used");
        guiData.addGuiDataComponents(guiDataComponent);
        addStandardGuiOptions(guiDataComponent);
        String elapsedTimeMatchIncidentTypeDescription = null;
        ElapsedTimeMatchIncidentType expectedNextElapsedTimeMatchIncidentType = null;
        guiData.setElapsedTimeMatchIncidentTypeDescription(elapsedTimeMatchIncidentTypeDescription);
        guiData.setExpectedNextElapsedTimeMatchIncidentType(expectedNextElapsedTimeMatchIncidentType);
        return guiData;
    }

    private void addStandardGuiOptions(GuiDataComponent guiDataComponent) {
        guiDataComponent.addToList1("ALL_INCIDENTS");
        guiDataComponent.addToList2("ALL");
    }

    @Override
    public MatchIncident generateMatchIncident(GuiIncidentResponse guiResponse) {
        OutrightsMatchIncident incident = new OutrightsMatchIncident();
        return incident;
    }

}
