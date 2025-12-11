package ats.algo.algomanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ats.algo.core.baseclasses.MatchIncident;

/*
 * this class is used only for reverting EventState, where composed with an EventState Reverted to plus a list of
 * undoable incidents to be replayed
 */
public class RevertEventState implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private EventState eventState;
    private List<MatchIncident> nonUndoableIncidents;

    // RevertEventState(){
    // eventState = null;
    // undoableIncidents =new ArrayList<String>();
    // }

    public EventState getEventState() {
        return eventState;
    }

    public void setEventState(EventState eventState) {
        this.eventState = eventState;
    }

    /*
     * Call this method will reverse the undoable incidents list
     */
    public List<MatchIncident> getNonUndoableIncidents() {
        if (nonUndoableIncidents != null)
            Collections.reverse(nonUndoableIncidents);
        return nonUndoableIncidents;
    }

    public void setNonUndoableIncidents(List<MatchIncident> undoableIncidents) {
        this.nonUndoableIncidents = undoableIncidents;
    }

    public void addNonUndoableIncidentsToBeReplayed(MatchIncident matchIncident) {
        if (nonUndoableIncidents == null) // < MatchIncident>();
            nonUndoableIncidents = new ArrayList<MatchIncident>();
        nonUndoableIncidents.add(matchIncident);
    }



}
