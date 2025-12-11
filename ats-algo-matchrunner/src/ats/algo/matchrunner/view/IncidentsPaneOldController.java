package ats.algo.matchrunner.view;

import ats.algo.core.baseclasses.MatchIncidentPrompt;
import ats.algo.matchrunner.MatchRunner;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class IncidentsPaneOldController {

    private MatchRunner matchRunner;

    @FXML
    private TextField eventEntryBox;
    @FXML
    private TextFlow matchEventPrompt;
    @FXML
    private Label matchEventErrorMsg;

    private Text textMatchEventText;


    @FXML
    private void initialize() {
        textMatchEventText = new Text("event prompt");
        ObservableList<Node> x = matchEventPrompt.getChildren();
        x.add(textMatchEventText);
        matchEventPrompt.setStyle("-fx-alignment: RIGHT;");
    }

    public void setParent(MatchRunner matchRunner) {
        this.matchRunner = matchRunner;
    }

    @FXML
    private void onEventEntry() {
        matchRunner.processOldIncidentEntry(eventEntryBox.getText());
    }

    @FXML
    void onUndoLastButtonPressed() {
        matchRunner.undoLastEvent();
    }

    public void notifyInputError() {
        matchEventErrorMsg.setText("Error in input. Please re-enter");
    }

    public void setNextPrompt(MatchIncidentPrompt prompt) {
        textMatchEventText.setText(prompt.getPrompt());
        String defaultValue = prompt.getDefaultValue();
        if (defaultValue == null)
            defaultValue = "";
        eventEntryBox.setText(defaultValue);
        eventEntryBox.positionCaret(defaultValue.length());
        eventEntryBox.setStyle("-fx-alignment: CENTER;");
        matchEventErrorMsg.setText("");
    }


}
