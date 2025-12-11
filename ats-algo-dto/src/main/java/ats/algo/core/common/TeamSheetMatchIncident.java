package ats.algo.core.common;

import ats.algo.core.baseclasses.MatchIncident;
import ats.core.util.json.JsonUtil;

public class TeamSheetMatchIncident extends MatchIncident {
    private static final long serialVersionUID = 1L;

    public enum TeamSheetMatchIncidentType {
        INITIAL_TEAM_SHEET,
        UPDATED_TEAM_SHEET
    }

    private TeamSheetMatchIncidentType incidentSubType;
    private TeamSheet teamSheet;


    public TeamSheet getTeamSheet() {
        return teamSheet;
    }

    public void setTeamSheet(TeamSheet teamSheet) {
        this.teamSheet = teamSheet;
    }

    // public String getOldName() {
    // return oldName;
    // }
    //
    // public void setOldName(String oldName) {
    // this.oldName = oldName;
    // }
    //
    // public String getNewName() {
    // return newName;
    // }
    //
    // public void setNewName(String newName) {
    // this.newName = newName;
    // }

    /**
     * generates a match incident for specifying the team sheet - i.e. the names of the 11 player starting plus the six
     * on the bench. Should
     * 
     * @param teamSheet The list of 11 players + n substitutes (usually n = 6 but may be different for some
     *        competitions). For each player specifies whether they starting on the pitch or not. The player names and
     *        ids are taken from this sheet.
     * @param elapsedTime the match clock
     * @return
     */
    public static TeamSheetMatchIncident generateMatchIncidentForInitialTeamSheet(TeamSheet teamSheet) {
        TeamSheetMatchIncident incident = new TeamSheetMatchIncident();
        incident.setIncidentSubType(TeamSheetMatchIncidentType.INITIAL_TEAM_SHEET);
        incident.setTeamSheet(teamSheet);
        incident.setElapsedTime(-1);
        return incident;
    }



    /**
     * Updates the status of players within a previously supplied team sheet. The map should only contain some or all of
     * the players who were listed on the previously supplied team sheet. Any players listed who were not on the
     * previously supplied team sheet will be ignored.
     * 
     * @param teamSheet the map listing those players where the status needs to be updated
     * @param teamId
     * @return
     */
    public static TeamSheetMatchIncident generateMatchIncidentForUpdateTeamSheet(TeamSheet teamSheet) {
        TeamSheetMatchIncident incident = new TeamSheetMatchIncident();
        incident.setIncidentSubType(TeamSheetMatchIncidentType.UPDATED_TEAM_SHEET);
        incident.setTeamSheet(teamSheet);
        incident.setElapsedTime(-1);
        return incident;
    }


    // /**
    // * Changes the name of one of the players on the team sheet, leaving the status unchanged. Typically called when
    // the
    // * trader is manually entering the names
    // *
    // * @param oldName
    // * @param newName
    // * @return
    // */
    // public static TeamSheetMatchIncident generateMatchIncidentForNameChange(String oldName, String newName) {
    // TeamSheetMatchIncident incident = new TeamSheetMatchIncident();
    // incident.setIncidentType(TeamSheetMatchIncidentType.NAME_CHANGE);
    // incident.oldName = oldName;
    // incident.newName = newName;
    // incident.setElapsedTime(-1);
    // return incident;
    // }


    @Override
    public TeamSheetMatchIncidentType getIncidentSubType() {
        return incidentSubType;
    }

    public void setIncidentSubType(TeamSheetMatchIncidentType incidentSubType) {
        this.incidentSubType = incidentSubType;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
