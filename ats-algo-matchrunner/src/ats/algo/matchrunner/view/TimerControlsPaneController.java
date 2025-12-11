package ats.algo.matchrunner.view;

import ats.algo.core.common.ElapsedTimeMatchIncident.ElapsedTimeMatchIncidentType;
import ats.algo.core.incidentgenerator.GuiData;
import ats.algo.core.incidentgenerator.GuiTimerResponse;
import ats.algo.matchrunner.MatchRunner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class TimerControlsPaneController {
    Thread t1;
    int elapsedTime;
    static boolean stopClock = false;
    ElapsedTimeMatchIncidentType typeGeneratedByPeriodChangeButton;
    ElapsedTimeMatchIncidentType typeGeneratedByClockChangeButton;
    boolean onlyPeriodChangeButtonSelectable;

    @FXML
    TextField elapsedTimeTextBox;

    @FXML
    Button periodChange;
    @FXML
    Button clockChange;
    @FXML
    Button button10Sec;
    @FXML
    Button button1Min;
    @FXML
    Button button10Min;
    @FXML
    Label errMsg;

    private MatchRunner matchRunner;
    private final int refreshTime = 14;
    private final int maxTime = 6000;

    public TimerControlsPaneController() {

    }

    public void setParent(MatchRunner matchRunner) {
        this.matchRunner = matchRunner;
    }

    public void updateGui(GuiData guiData, int elapsedTime) {
        this.elapsedTime = elapsedTime;
        String timeStr = convertElapsedTimeToStr(elapsedTime);
        elapsedTimeTextBox.setText(timeStr);
        periodChange.setText(guiData.getElapsedTimeMatchIncidentTypeDescription());
        typeGeneratedByPeriodChangeButton = guiData.getExpectedNextElapsedTimeMatchIncidentType();
        onlyPeriodChangeButtonSelectable = guiData.getGuiDataComponents().get(0).isOnlyPeriodChangeSelectable();
        setButtonColours();

    }

    private void setButtonColours() {
        if (onlyPeriodChangeButtonSelectable) {
            button10Sec.setTextFill(Color.GRAY);
            button1Min.setTextFill(Color.GRAY);
            button10Min.setTextFill(Color.GRAY);
            clockChange.setTextFill(Color.GRAY);
        } else {
            button10Sec.setTextFill(Color.BLACK);
            button1Min.setTextFill(Color.BLACK);
            button10Min.setTextFill(Color.BLACK);
            clockChange.setMouseTransparent(false);
            button10Sec.setMouseTransparent(false);
            button10Min.setMouseTransparent(false);
            button1Min.setMouseTransparent(false);
            clockChange.setTextFill(Color.BLACK);
        }

    }

    @FXML
    private void onPeriodChangeButtonPressed() {
        errMsg.setText("");
        elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
        GuiTimerResponse response = new GuiTimerResponse();
        response.setElapsedTimeMatchIncidentType(typeGeneratedByPeriodChangeButton);
        response.setElapsedTime(elapsedTime);
        matchRunner.processV2TimerIncidentEntry(response);
        matchRunner.stopManualResult();
    }

    @FXML
    private void onClockChangeButtonPressed() {
        errMsg.setText("");
        GuiTimerResponse response = new GuiTimerResponse();
        if (clockChange.getText().toString().contains("start")) {
            typeGeneratedByClockChangeButton = ElapsedTimeMatchIncidentType.SET_START_MATCH_CLOCK;
        } else {
            typeGeneratedByClockChangeButton = ElapsedTimeMatchIncidentType.SET_STOP_MATCH_CLOCK;
        }
        response.setElapsedTimeMatchIncidentType(typeGeneratedByClockChangeButton);
        response.setElapsedTime(elapsedTime);
        matchRunner.processV2TimerIncidentEntry(response);

        if (clockChange.getText().toString().contains("start")) {
            stopClock = false;
            clockChange.setText("Clock stop");
            matchRunner.stopPanelClickAble();
            matchRunner.stopPanel2ClickAble();
            periodChange.setMouseTransparent(true);
            periodChange.setTextFill(Color.GRAY);
            clockStart();
        } else {
            clockChange.setText("Clock start");
            clockStop();
            matchRunner.startPanelClickAble();
            matchRunner.startPanel2ClickAble();
            periodChange.setMouseTransparent(false);
            periodChange.setTextFill(Color.BLACK);
        }

    }


    @FXML
    private void on10SecsButtonPressed() {
        elapsedTime += 10;
        if (!onlyPeriodChangeButtonSelectable)
            updateElapsedTime(elapsedTime);
        elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
    }

    @FXML
    private void on1minButtonPressed() {
        elapsedTime += 60;
        if (!onlyPeriodChangeButtonSelectable)
            updateElapsedTime(elapsedTime);
        elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
    }

    @FXML
    private void on10minButtonPressed() {
        elapsedTime += 600;
        if (!onlyPeriodChangeButtonSelectable)
            updateElapsedTime(elapsedTime);
        elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
    }

    @FXML
    private void onElapsedTimeEntered() {
        if (!onlyPeriodChangeButtonSelectable) {
            String timeStr = elapsedTimeTextBox.getText();
            int secs = convertElapsedTimeStrToInt(timeStr);
            if (secs >= 0) {
                updateElapsedTime(secs);
            } else {
                elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
                errMsg.setText("Invalid time entered. No action taken");
            }
        } else {
            elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
        }

    }

    private String convertElapsedTimeToStr(int elapsedTime) {
        int mins = elapsedTime / 60;
        int secs = elapsedTime - mins * 60;
        if (mins < 100)
            return String.format("%02d:%02d", mins, secs);
        else
            return String.format("%03d:%20d", mins, secs);
    }

    /*
     * expects str of form mm:ss
     */
    private int convertElapsedTimeStrToInt(String timeStr) {
        int iColon = timeStr.indexOf(":");
        if (iColon < 0)
            return -1;
        String minStr = timeStr.substring(0, iColon);
        String secStr = timeStr.substring(iColon + 1);
        int elapsedTime;
        try {
            int mins = Integer.valueOf(minStr);
            int secs = Integer.valueOf(secStr);
            elapsedTime = mins * 60 + secs;
        } catch (NumberFormatException e) {
            elapsedTime = -1;
        }
        return elapsedTime;
    }

    private synchronized void updateElapsedTime(int secs) {
        errMsg.setText("");
        GuiTimerResponse response = new GuiTimerResponse();
        response.setElapsedTimeMatchIncidentType(ElapsedTimeMatchIncidentType.SET_MATCH_CLOCK);
        response.setElapsedTime(secs);
        matchRunner.processV2TimerIncidentEntry(response);
    }

    private void clockStart() {
        t1 = new Thread() {
            public void run() {
                try {
                    while (!stopClock) {
                        elapsedTimeTextBox.setText(convertElapsedTimeToStr(elapsedTime));
                        elapsedTime++;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (elapsedTime % (refreshTime + 1) == refreshTime)
                                    updateElapsedTime(elapsedTime);
                            }
                        });
                        if (elapsedTime > maxTime)
                            stopClock = true;
                        sleep(1000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t1.start();
    }

    private void clockStop() {
        stopClock = true;
    }


    public int getElapsedTime() {
        return elapsedTime;
    }

    public Button getClockChange() {
        return clockChange;
    }

    public void stopPeriodStart() {
        periodChange.setTextFill(Color.GRAY);
        periodChange.setMouseTransparent(true);
    }

}
