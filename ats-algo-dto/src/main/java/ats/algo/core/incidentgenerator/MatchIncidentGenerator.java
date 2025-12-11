package ats.algo.core.incidentgenerator;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident;

public abstract class MatchIncidentGenerator {

    /**
     * returns true if this mode requires the timer controls
     * 
     * @return
     */
    public abstract boolean usesTimer();

    /**
     * generates the data required to populate a GUI
     * 
     * @param simpleMatchState
     * @return
     */
    public abstract GuiData generateGuiData(SimpleMatchState simpleMatchState);

    /**
     * generates a matchIncident based on the supplied response
     * 
     * @param guiResponse
     * @return
     */
    public abstract MatchIncident generateMatchIncident(GuiIncidentResponse guiResponse);

    public MatchIncident generateElapsedTimeIncident(GuiTimerResponse guiResponse) {
        ElapsedTimeMatchIncident incident = new ElapsedTimeMatchIncident(guiResponse.getElapsedTimeMatchIncidentType(),
                        guiResponse.getElapsedTime());
        incident.setSourceSystem(guiResponse.getSourceSystem());
        incident.setIncidentId("T" + System.currentTimeMillis());
        return incident;
    }

}
