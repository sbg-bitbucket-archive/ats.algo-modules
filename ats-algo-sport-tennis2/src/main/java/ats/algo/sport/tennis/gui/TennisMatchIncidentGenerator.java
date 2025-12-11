package ats.algo.sport.tennis.gui;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.DatafeedMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.DatafeedMatchIncident.DatafeedMatchIncidentType;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.incidentgenerator.GuiData;
import ats.algo.core.incidentgenerator.GuiDataComponent;
import ats.algo.core.incidentgenerator.GuiIncidentResponse;
import ats.algo.core.incidentgenerator.MatchIncidentGenerator;
import ats.algo.sport.tennis.TennisMatchIncident;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchIncident.TennisPointResult;

public class TennisMatchIncidentGenerator extends MatchIncidentGenerator {

    Map<String, Object> standardOptions;

    public TennisMatchIncidentGenerator() {
        standardOptions = new LinkedHashMap<>(25);

        standardOptions.put("MATCH_STARTING", TennisMatchIncidentType.MATCH_STARTING);
        standardOptions.put("ACE", TennisMatchIncidentType.POINT_WON);
        standardOptions.put("DOUBLE_FAULT", TennisMatchIncidentType.POINT_WON);
        standardOptions.put("RALLY", TennisMatchIncidentType.POINT_WON);
        standardOptions.put("UNKNOWN", TennisMatchIncidentType.POINT_WON);
        standardOptions.put("NEW_SET_STARTING", TennisMatchIncidentType.NEW_SET_STARTING);
        standardOptions.put("INJURY_STATUS_REVERSE", TennisMatchIncidentType.INJURY_STATUS_REVERSE);
        standardOptions.put("FAULT", TennisMatchIncidentType.FAULT);
        standardOptions.put("SERVING_ORDER", TennisMatchIncidentType.SERVING_ORDER);
        standardOptions.put("IPTL_POWERPOINT", TennisMatchIncidentType.IPTL_POWERPOINT);
        standardOptions.put("PENALTY_POINT_WON", TennisMatchIncidentType.PENALTY_POINT_WON);
        standardOptions.put("PENALTY_GAME_WON", TennisMatchIncidentType.PENALTY_GAME_WON);
        standardOptions.put("PENALTY_MATCH_WON", TennisMatchIncidentType.PENALTY_MATCH_WON);
        standardOptions.put("POINT_START", TennisMatchIncidentType.POINT_START);
        standardOptions.put("CHALLENGER_BALLMARK", TennisMatchIncidentType.CHALLENGER_BALLMARK);
        standardOptions.put("MATCH_END", TennisMatchIncidentType.MATCH_END);
        standardOptions.put("RAIN", TennisMatchIncidentType.RAIN);
        standardOptions.put("TOILET_BREAK", TennisMatchIncidentType.TOILET_BREAK);
        standardOptions.put("Penalty confirmed", TennisMatchIncidentType.HEAT_DELAY);
        standardOptions.put("MEDICAL_TIMEOUT", TennisMatchIncidentType.MEDICAL_TIMEOUT);
        standardOptions.put("ON_COURT_COACHING", TennisMatchIncidentType.ON_COURT_COACHING);
        standardOptions.put("CHALLENGE", TennisMatchIncidentType.CHALLENGE);
        standardOptions.put("BET STOP", DatafeedMatchIncidentType.BET_STOP);
        standardOptions.put("BET START", DatafeedMatchIncidentType.BET_START);
    }

    @Override
    public boolean usesTimer() {
        return false;
    }

    @Override
    public GuiData generateGuiData(SimpleMatchState simpleMatchState) {
        GuiData guiData = new GuiData();
        guiData.setStopClock(true);
        GuiDataComponent guiDataComponent = new GuiDataComponent("Standard Incident Types", "Incident", "Team");
        guiData.addGuiDataComponents(guiDataComponent);
        if (simpleMatchState.isPreMatch())
            addStandardGuiOptionsPrematch(guiDataComponent);
        else
            addStandardGuiOptionsInplay(guiDataComponent, simpleMatchState);
        String elapsedTimeMatchIncidentTypeDescription = null;
        ElapsedTimeMatchIncidentType expectedNextElapsedTimeMatchIncidentType = null;
        guiData.setElapsedTimeMatchIncidentTypeDescription(elapsedTimeMatchIncidentTypeDescription);
        guiData.setExpectedNextElapsedTimeMatchIncidentType(expectedNextElapsedTimeMatchIncidentType);
        return guiData;
    }

    private void addStandardGuiOptionsPrematch(GuiDataComponent guiDataComponent) {

        for (String optionName : standardOptions.keySet())
            if (optionName.equals("MATCH_STARTING") || optionName.equals("SERVING_ORDER"))
                guiDataComponent.addToList1(optionName);
        guiDataComponent.addToList2("Team A");
        guiDataComponent.addToList2("Team B");
    }

    private void addStandardGuiOptionsInplay(GuiDataComponent guiDataComponent, SimpleMatchState simpleMatchState) {
        for (String optionName : standardOptions.keySet())
            if (!(optionName.equals("MATCH_STARTING")))
                guiDataComponent.addToList1(optionName);
        guiDataComponent.addToList2("Team A");
        guiDataComponent.addToList2("Team B");
    }

    @Override
    public MatchIncident generateMatchIncident(GuiIncidentResponse guiResponse) {
        String optionName = guiResponse.getList1SelectedOption();
        Object incidentType = standardOptions.get(optionName);
        if (incidentType == null)
            return null;

        if (incidentType instanceof DatafeedMatchIncidentType) {
            DatafeedMatchIncident incident = new DatafeedMatchIncident((DatafeedMatchIncidentType) incidentType,
                            guiResponse.getElapsedTime());

            return incident;
        }

        TennisMatchIncident incident;

        TeamId teamId = convertTeamNameToId(guiResponse.getList2SelectedOption());
        incident = new TennisMatchIncident(guiResponse.getElapsedTime(), (TennisMatchIncidentType) incidentType,
                        teamId);
        if (optionName.contains("ACE")) {
            TennisPointResult pointResult = TennisPointResult.ACE;
            incident.setPointResult(pointResult);
        }
        if (optionName.contains("DOUBLE_FAULT")) {
            TennisPointResult pointResult = TennisPointResult.DOUBLE_FAULT;
            incident.setPointResult(pointResult);
        }
        if (optionName.contains("RALLY")) {
            TennisPointResult pointResult = TennisPointResult.RALLY;
            incident.setPointResult(pointResult);
        }
        if (optionName.contains("UNKNOWM")) {
            TennisPointResult pointResult = TennisPointResult.UNKNOWN;
            incident.setPointResult(pointResult);
        }
        return incident;
    }

    private TeamId convertTeamNameToId(String name) {
        TeamId teamId;
        switch (name) {
            case "Team A":
                teamId = TeamId.A;
                break;
            case "Team B":
                teamId = TeamId.B;
                break;
            default:
                teamId = TeamId.UNKNOWN;
                break;

        }
        return teamId;
    }

}
