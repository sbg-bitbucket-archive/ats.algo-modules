package ats.algo.matchrunner.view;

import java.util.ArrayList;
import java.util.List;

import ats.algo.core.incidentgenerator.GuiData;
import ats.algo.core.incidentgenerator.GuiDataComponent;
import ats.algo.core.incidentgenerator.GuiIncidentResponse;
import ats.algo.matchrunner.MatchRunner;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.paint.Color;

public class IncidentsPaneV2Controller {

    private MatchRunner matchRunner;
    private int elapsedTime;
    private GuiData guiData;
    private int guiDataComponentIndex;
    private int nComponents;

    private List<String> lastList1Selections;
    private List<String> lastList2Selections;

    @FXML
    private ListView<String> observableList1;
    @FXML
    private ListView<String> observableList2;
    @FXML
    private ComboBox<String> observableSources;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelList1;
    @FXML
    private Label labelList2;
    @FXML
    private Button buttonMore;
    @FXML
    private Button buttonSendIncident;
    @FXML
    private Button buttonUndo;

    @FXML
    private void initialize() {
        observableSources.getItems().add("TRADER");
        observableSources.getItems().add("BETRADAR");
        observableSources.getItems().add("ENETPULSE");
        observableSources.getItems().add("IMG");
        observableSources.getItems().add("ISD");
        observableSources.getItems().add("RUNNINGBALL");
        observableSources.getItems().add("TIPEX");

        lastList1Selections = new ArrayList<>(5);// big enough for up to 5
                                                 // GuiDataComponents in
                                                 // GuiData
        lastList2Selections = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            lastList1Selections.add("Empty");
            lastList2Selections.add("Empty");
        }
        observableSources.getSelectionModel().select(0);
    }

    public void setParent(MatchRunner matchRunner) {
        this.matchRunner = matchRunner;
    }

    @FXML
    public void onUndoLastButtonPressed() {
        matchRunner.undoLastEvent();
    }

    @FXML
    public void onSendIncidentButtonPressed() {
        if (!guiData.getGuiDataComponents().get(guiDataComponentIndex).isOnlyPeriodChangeSelectable()) {
            GuiIncidentResponse guiResponse = new GuiIncidentResponse();
            guiResponse.setGuiDataComponentIndex(guiDataComponentIndex);
            int sourceIndex = observableSources.getSelectionModel().getSelectedIndex();
            String sourceName = observableSources.getItems().get(sourceIndex);
            guiResponse.setSourceSystem(sourceName);
            int list1Index = observableList1.getSelectionModel().getSelectedIndex();
            String list1Name = observableList1.getItems().get(list1Index);
            guiResponse.setList1SelectedOption(list1Name);
            int list2Index = observableList2.getSelectionModel().getSelectedIndex();
            String list2Name = observableList2.getItems().get(list2Index);
            guiResponse.setList2SelectedOption(list2Name);
            guiResponse.setElapsedTime(elapsedTime);
            lastList1Selections.set(guiDataComponentIndex, list1Name);
            lastList2Selections.set(guiDataComponentIndex, list2Name);
            matchRunner.processV2MatchIncidentEntry(guiResponse);
        }
    }

    @FXML
    void onMoreButtonPressed() {
        if (nComponents > 1) {
            /*
             * cycle through the available pages
             */
            guiDataComponentIndex++;
            if (guiDataComponentIndex >= nComponents)
                guiDataComponentIndex = 0;
            displayGuiDataComponent(guiDataComponentIndex);
        }
    }

    public void updateGui(GuiData guiData, int elapsedTime) {
        this.guiData = guiData;
        this.elapsedTime = elapsedTime;
        /*
         * initialise the GUI with the data from the first gui component
         */
        nComponents = guiData.getGuiDataComponents().size();
        displayGuiDataComponent(0);
        guiDataComponentIndex = 0;
        if (nComponents == 1) {
            greyOutButton(buttonMore);
        } else
            unGreyOutButton(buttonMore);
        if (!guiData.isStopClock()) {
            greyOutButton(buttonSendIncident);
            greyOutButton(buttonUndo);
        }

    }

    private void greyOutButton(Button button) {
        button.setTextFill(Color.GRAY);
    }

    private void unGreyOutButton(Button button) {
        button.setTextFill(Color.BLACK);
    }

    private void displayGuiDataComponent(int index) {
        int list1SelectionIndex = 0;
        int list2SelectionIndex = 0;
        String lastList1Selection = lastList1Selections.get(index);
        String lastList2Selection = lastList2Selections.get(index);
        GuiDataComponent guiDataComponent = guiData.getGuiDataComponents().get(index);
        labelTitle.setText(guiDataComponent.getTitle());
        labelList1.setText(guiDataComponent.getList1Name());
        labelList2.setText(guiDataComponent.getList2Name());
        observableList1.getItems().clear();
        int i1 = 0;
        for (String s : guiDataComponent.getList1()) {
            observableList1.getItems().add(s);
            if (s.equals(lastList1Selection))
                list1SelectionIndex = i1;
            i1++;
        }
        observableList2.getItems().clear();
        int i2 = 0;
        for (String s : guiDataComponent.getList2()) {
            observableList2.getItems().add(s);
            if (s.equals(lastList2Selection))
                list2SelectionIndex = i2;
            i2++;
        }
        list2SelectionIndex = 0;
        observableList1.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        observableList2.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        observableList1.getSelectionModel().select(list1SelectionIndex);
        observableList2.getSelectionModel().select(list2SelectionIndex);
        if (guiDataComponent.isOnlyPeriodChangeSelectable()) {
            greyOutButton(buttonSendIncident);
            greyOutButton(buttonUndo);
        } else {
            unGreyOutButton(buttonSendIncident);
            unGreyOutButton(buttonUndo);
        }
        buttonMore.setText(String.format("More incident types (%d of %d)", index + 1, this.nComponents));

    }

    public void stopButtonClick() {
        buttonMore.setTextFill(Color.GRAY);
        buttonSendIncident.setTextFill(Color.GRAY);
        buttonUndo.setTextFill(Color.GRAY);
        buttonMore.setMouseTransparent(true);
        buttonSendIncident.setMouseTransparent(true);
        buttonUndo.setMouseTransparent(true);
    }

    public void startButtonClick() {
        buttonMore.setTextFill(Color.BLACK);
        buttonSendIncident.setTextFill(Color.BLACK);
        buttonUndo.setTextFill(Color.BLACK);
        buttonMore.setMouseTransparent(false);
        buttonSendIncident.setMouseTransparent(false);
        buttonUndo.setMouseTransparent(false);
    }

}
