package ats.algo.sport.football.gui;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchIncident;
import ats.algo.core.baseclasses.SimpleMatchState;
import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.common.MatchReferralIncident;
import ats.algo.core.common.MatchReferralIncident.MatchReferralIncidentType;
import ats.algo.core.common.PlayerInfo;
import ats.algo.core.common.PlayerMatchIncident;
import ats.algo.core.common.TeamId;
import ats.algo.core.common.TeamSheet;
import ats.algo.core.common.TeamSheetMatchIncident;
import ats.algo.core.common.TeamSheetMatchIncident.TeamSheetMatchIncidentType;
import ats.algo.core.incidentgenerator.GuiData;
import ats.algo.core.incidentgenerator.GuiDataComponent;
import ats.algo.core.incidentgenerator.GuiIncidentResponse;
import ats.algo.core.incidentgenerator.MatchIncidentGenerator;
import ats.algo.sport.football.ExampleTeamSheets;
import ats.algo.sport.football.FootballMatchIncident;
import ats.algo.sport.football.FootballSimpleMatchState;
import ats.algo.sport.football.FootballMatchIncident.FootballMatchIncidentType;

public class FootballMatchIncidentGenerator extends MatchIncidentGenerator {

    Map<String, Object> standardOptions;
    Map<String, Object> teamSheetOptions;
    TeamSheet teamSheet;

    public FootballMatchIncidentGenerator() {
        standardOptions = new LinkedHashMap<>(20);
        standardOptions.put("Goal", FootballMatchIncidentType.GOAL);
        standardOptions.put("Corner", FootballMatchIncidentType.CORNER);
        standardOptions.put("Yellow card", FootballMatchIncidentType.YELLOW_CARD);
        standardOptions.put("Red card", FootballMatchIncidentType.RED_CARD);
        standardOptions.put("Penalty", FootballMatchIncidentType.PENALTY);
        standardOptions.put("Free Kick", FootballMatchIncidentType.FREE_KICK);
        standardOptions.put("Throw In", FootballMatchIncidentType.THROWIN);
        standardOptions.put("Possible goal", FootballMatchIncidentType.POSSIBLE_GOAL);
        standardOptions.put("Goal not confirmed", FootballMatchIncidentType.GOAL_NOT_CONFIRMED);
        standardOptions.put("Possible corner", FootballMatchIncidentType.POSSIBLE_CORNER);
        standardOptions.put("Corner not confirmed", FootballMatchIncidentType.CORNER_NOT_CONFIRMED);
        standardOptions.put("Possible yellow card", FootballMatchIncidentType.POSSIBLE_YELLOW_CARD);
        standardOptions.put("Yellow card not confirmed", FootballMatchIncidentType.YELLOW_CARD_NOT_CONFIRMED);
        standardOptions.put("Possible red card", FootballMatchIncidentType.POSSIBLE_RED_CARD);
        standardOptions.put("Red card not confirmed", FootballMatchIncidentType.RED_CARD_NOT_CONFIRMED);
        standardOptions.put("Possible penalty", FootballMatchIncidentType.POSSIBLE_PENALTY);
        standardOptions.put("Penalty not confirmed", FootballMatchIncidentType.PENALTY_NOT_CONFIRMED);
        standardOptions.put("Penalty confirmed", FootballMatchIncidentType.PENALTY_CONFIRMED);
        standardOptions.put("Shootout start", FootballMatchIncidentType.SHOOTOUT_START);
        standardOptions.put("Shootout miss", FootballMatchIncidentType.SHOOTOUT_MISS);
        standardOptions.put("Possible referral", MatchReferralIncidentType.VAR_POSSIBLE_REFERRAL);
        standardOptions.put("Referral complited", MatchReferralIncidentType.VAR_REFERRAL_COMPLETED);
        standardOptions.put("Referral confirmed", MatchReferralIncidentType.VAR_REFERRAL_CONFIRMED);
        teamSheetOptions = new LinkedHashMap<>(1);
        teamSheetOptions.put("Initial team sheet", TeamSheetMatchIncidentType.INITIAL_TEAM_SHEET);
    }

    @Override
    public boolean usesTimer() {
        return true;
    }

    @Override
    public GuiData generateGuiData(SimpleMatchState simpleMatchState) {
        FootballSimpleMatchState fs = (FootballSimpleMatchState) simpleMatchState;
        GuiData guiData = new GuiData();
        GuiDataComponent standardGuiDataComponent = new GuiDataComponent("Standard Incident Types", "Incident", "Team");
        guiData.addGuiDataComponents(standardGuiDataComponent);

        String elapsedTimeMatchIncidentTypeDescription = null;
        ElapsedTimeMatchIncidentType expectedNextElapsedTimeMatchIncidentType = null;
        switch (fs.getMatchPeriod()) {
            case PREMATCH:
                elapsedTimeMatchIncidentTypeDescription = "Start match";
                expectedNextElapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_START;
                addStandardGuiOptions(standardGuiDataComponent, true);
                break;
            case AT_HALF_TIME:
            case AT_FULL_TIME:
            case IN_EXTRA_TIME_HALF_TIME:
                elapsedTimeMatchIncidentTypeDescription = "Period start";
                expectedNextElapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_START;
                addStandardGuiOptions(standardGuiDataComponent, true);
                break;
            case IN_FIRST_HALF:
            case IN_SECOND_HALF:
            case IN_EXTRA_TIME_FIRST_HALF:
            case IN_EXTRA_TIME_SECOND_HALF:
                elapsedTimeMatchIncidentTypeDescription = "Period end";
                expectedNextElapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                addStandardGuiOptions(standardGuiDataComponent, false);
                break;
            case IN_SHOOTOUT:
                elapsedTimeMatchIncidentTypeDescription = "In shootout";
                expectedNextElapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                addStandardGuiOptions(standardGuiDataComponent, false);
                break;
            case MATCH_COMPLETED:
                elapsedTimeMatchIncidentTypeDescription = "Match over";
                expectedNextElapsedTimeMatchIncidentType = ElapsedTimeMatchIncidentType.SET_PERIOD_END;
                addStandardGuiOptions(standardGuiDataComponent, true);
                break;
        }
        teamSheet = fs.getTeamSheet();
        if (teamSheet != null) {
            GuiDataComponent teamSheetGuiDataComponent = new GuiDataComponent("TeamSheet Incidents", "Incident", "");
            addTeamSheetGuiOptions(teamSheetGuiDataComponent);
            guiData.addGuiDataComponents(teamSheetGuiDataComponent);
            GuiDataComponent playersGuiDataComponent = new GuiDataComponent("Player Incidents", "Player", "Goal no");
            addPlayerGuiOptions(playersGuiDataComponent, fs);
            guiData.addGuiDataComponents(playersGuiDataComponent);
        }
        guiData.setElapsedTimeMatchIncidentTypeDescription(elapsedTimeMatchIncidentTypeDescription);
        guiData.setExpectedNextElapsedTimeMatchIncidentType(expectedNextElapsedTimeMatchIncidentType);
        return guiData;
    }

    /**
     * add the list of players to the guiDataComponent and list of goals for which goal scorer not specified to list 2
     * 
     * @param playersGuiDataComponent
     * @param fs
     */
    private void addPlayerGuiOptions(GuiDataComponent guiDataComponent, FootballSimpleMatchState fs) {

        for (String playerKey : teamSheet.getTeamSheetMap().keySet())
            guiDataComponent.addToList1(playerKey);
        int nGoals = fs.getGoalsA() + fs.getGoalsB();
        if (nGoals == 0)
            guiDataComponent.addToList2("No goals scored yet");
        else
            for (int goalNo = 1; goalNo <= nGoals; goalNo++)
                guiDataComponent.addToList2(Integer.toString(goalNo));
        guiDataComponent.setOnlyPeriodChangeSelectable(nGoals == 0);
    }

    private void addStandardGuiOptions(GuiDataComponent guiDataComponent, boolean onlyPeriodChangeSelectable) {
        for (String optionName : standardOptions.keySet())
            guiDataComponent.addToList1(optionName);
        guiDataComponent.addToList2("Team A");
        guiDataComponent.addToList2("Team B");
        guiDataComponent.setOnlyPeriodChangeSelectable(onlyPeriodChangeSelectable);
    }

    private void addTeamSheetGuiOptions(GuiDataComponent guiDataComponent) {
        for (String optionName : teamSheetOptions.keySet())
            guiDataComponent.addToList1(optionName);
        guiDataComponent.addToList2("");
        guiDataComponent.setOnlyPeriodChangeSelectable(false);
    }

    @Override
    public MatchIncident generateMatchIncident(GuiIncidentResponse guiResponse) {
        MatchIncident matchIncident = null;
        switch (guiResponse.getGuiDataComponentIndex()) {
            case 0:
            default:
                matchIncident = processStandardOptionsResponse(guiResponse);
                break;
            case 1:
                matchIncident = processTeamSheetResponse(guiResponse);
                break;
            case 2:
                matchIncident = processPlayerResponse(guiResponse);
                break;
        }
        return matchIncident;
    }

    private MatchIncident processStandardOptionsResponse(GuiIncidentResponse guiResponse) {
        String optionName = guiResponse.getList1SelectedOption();
        Object incidentType = standardOptions.get(optionName);
        if (incidentType == null)
            return null;

        TeamId teamId = convertTeamNameToId(guiResponse.getList2SelectedOption());
        MatchIncident incident = null;
        if (incidentType instanceof FootballMatchIncidentType)
            incident = new FootballMatchIncident((FootballMatchIncidentType) incidentType, guiResponse.getElapsedTime(),
                            teamId);
        else if (incidentType instanceof MatchReferralIncidentType)
            incident = new MatchReferralIncident((MatchReferralIncidentType) incidentType,
                            guiResponse.getElapsedTime());

        return incident;
    }

    private MatchIncident processPlayerResponse(GuiIncidentResponse guiResponse) {
        String playerKey = guiResponse.getList1SelectedOption();
        PlayerInfo playerInfo = teamSheet.getTeamSheetMap().get(playerKey);
        if (playerInfo == null)
            return null;
        else {
            int goalNo = Integer.parseInt(guiResponse.getList2SelectedOption());
            return PlayerMatchIncident.generateMatchIncidentForGoalScorer(goalNo, playerInfo.getTeamId(),
                            playerInfo.getPlayerName());
        }
    }

    private MatchIncident processTeamSheetResponse(GuiIncidentResponse guiResponse) {
        String optionName = guiResponse.getList1SelectedOption();
        Object incidentType = teamSheetOptions.get(optionName);
        MatchIncident incident = null;
        if (incidentType == TeamSheetMatchIncidentType.INITIAL_TEAM_SHEET)
            incident = TeamSheetMatchIncident
                            .generateMatchIncidentForInitialTeamSheet(ExampleTeamSheets.getExampleTeamSheet());
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
