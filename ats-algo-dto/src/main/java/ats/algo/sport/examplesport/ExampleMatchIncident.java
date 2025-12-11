package ats.algo.sport.examplesport;

import ats.algo.core.baseclasses.MatchIncident;

public class ExampleMatchIncident extends MatchIncident {

    private static final long serialVersionUID = 1L;

    public enum ExampleMatchIncidentType {
        LEGWONBYA,
        LEGWONBYB
    }

    private ExampleMatchIncidentType incidentType;

    public void setExampleMatchIncidentType(ExampleMatchIncidentType incidentType) {
        super.setElapsedTime(0); // not interested in elapsed time for this example
        this.incidentType = incidentType;
    }

    @Override
    public ExampleMatchIncidentType getIncidentSubType() {
        return incidentType;
    }


}
