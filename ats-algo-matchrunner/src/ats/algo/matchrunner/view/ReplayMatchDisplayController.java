package ats.algo.matchrunner.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ats.algo.core.common.CalcRequestCause;
import ats.algo.core.recordplayback.RecordedItem;
import ats.algo.core.recordplayback.RecordedItem.RecordedItemType;
import ats.algo.core.recordplayback.Recording;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.DateFunctions;
import ats.algo.matchrunner.MatchRunnable;
import ats.algo.matchrunner.model.ObservableMap;
import ats.algo.matchrunner.model.ObservableMap.ObservableMapRow;
import ats.core.util.json.JsonUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ReplayMatchDisplayController {

    ObservableMap observableActionsList;
    @FXML
    private TableView<ObservableMapRow> actionsTable;
    @FXML
    private TableColumn<ObservableMapRow, String> actionsTableColumn;
    @FXML
    private TextArea requestDetailText;
    @FXML
    private TextArea responseDetailText;
    @FXML
    private TextField fastFwdActionNoText;
    @FXML
    private Label fastFwdMsg;

    @FXML
    private TitledPane requestDetail;
    @FXML
    private TitledPane responseDetail;

    private Stage dialogStage;
    private MatchRunnable matchRunner;
    private Recording recordingFromFile;

    @SuppressWarnings("unused")
    private Recording recordingNow;
    private int currentActionNo;
    private int selectedActionNo;
    private boolean handledFirstMatchIncident;
    private boolean showFullDetail;

    @FXML
    private void handleFinished() {
        dialogStage.close();
    }

    @FXML
    private void handleNextActionButtonPressed() {
        if (currentActionNo < recordingFromFile.size()) {
            RecordedItem item = recordingFromFile.get(currentActionNo);
            RecordedItemType itemType = item.getRecordedItemType();
            switch (itemType) {
                case PARAM_FIND:
                    break;
                case PRICE_CALC:
                    processPriceCalcRequest(item);
                    break;
                case REVERT_TO_EARLIER_STATE:
                    processRevertToEarlierState(item);
                    break;
                default:
                    throw new IllegalArgumentException("Unhandled itemType in recording: " + itemType.toString());
            }
            displayData();
            currentActionNo++;
            selectedActionNo = currentActionNo;

        } else {
            requestDetailText.setText("");
            responseDetailText.setText("");
            updateActionsList();
        }
    }

    private void processRevertToEarlierState(RecordedItem item) {
        matchRunner.handleRevertToEarlierState(item.getRevertToEarlierStateForRequestId());

    }

    private void processPriceCalcRequest(RecordedItem item) {
        PriceCalcRequest request = item.getPriceCalcRequest();
        switch (request.getCalcRequestCause()) {
            case NEW_MATCH:
                matchRunner.handleNewMatch(request.getMatchFormat());
                matchRunner.setEventTier((int) request.getEventSettings().getEventTier());
                break;
            case MATCH_INCIDENT:
                matchRunner.handleMatchIncident(request.getMatchIncident());
                break;
            case EVENT_TIER_CHANGE:
                matchRunner.setEventTier((int) request.getEventSettings().getEventTier());
                break;
            case PARAMS_CHANGED_BY_TRADER:
            case PARAMS_CHANGED_FOLLOWING_PARAM_FIND:
                matchRunner.handleMatchParamsChanged(request.getMatchParams());
                break;
            case PARAM_FIND:
            case TIMER:
            default:
                break;

        }

    }

    @FXML
    private void onFastFwdEventNoEntry() {

        int fastFwdActionNo;
        try {
            fastFwdActionNo = Integer.parseInt(fastFwdActionNoText.getText());
        } catch (NumberFormatException e) {
            fastFwdMsg.setText("Invalid number");
            return;
        }
        if (fastFwdActionNo < currentActionNo) {
            fastFwdMsg.setText(fastFwdActionNo + " is not forward of current action no.");
            return;
        }
        if (fastFwdActionNo >= recordingFromFile.size())
            fastFwdActionNo = recordingFromFile.size() - 1;
        fastFwdMsg.setText("Please wait...");
        AlertBox.displayMsg(String.format("Fast forward to actionNo: %d.  %d actions to process.  May take a while...",
                        fastFwdActionNo, fastFwdActionNo - currentActionNo + 1));
        while (currentActionNo <= fastFwdActionNo) {
            handleNextActionButtonPressed();
            System.gc();
        }
        fastFwdMsg.setText("");
    }

    @FXML
    private void handleJumpToFirstMatchIncident() {
        if (this.handledFirstMatchIncident) {
            AlertBox.displayError("Already handled the first matchIncident.  Can't go backwards");
            return;
        }
        List<RecordedItem> recordedItems = recordingFromFile.getRecordedItemList();
        int firstMatchIncidentNo = 0;

        for (int i = currentActionNo; i < recordedItems.size(); i++) {
            RecordedItem recordedItem = recordedItems.get(i);
            if (recordedItem.getRecordedItemType() == RecordedItemType.PRICE_CALC) {
                if (recordedItem.getPriceCalcRequest().getCalcRequestCause().equals(CalcRequestCause.MATCH_INCIDENT))
                    firstMatchIncidentNo = i;
                break;
            }
        }
        if (firstMatchIncidentNo == 0) {
            AlertBox.displayError("No MatchIncidents in the recording");
            return;
        }
        /*
         * now go backwards to find the most immediately preceeding MatchParams change
         */
        int preceedingParamChangeNo = 0;
        for (int i = firstMatchIncidentNo - 1; i >= 0; i--) {
            RecordedItem recordedItem = recordedItems.get(i);
            if (recordedItem.getRecordedItemType() == RecordedItemType.PRICE_CALC) {
                CalcRequestCause cause = recordedItem.getPriceCalcRequest().getCalcRequestCause();
                if (cause.equals(CalcRequestCause.PARAMS_CHANGED_BY_TRADER)
                                || cause.equals(CalcRequestCause.PARAMS_CHANGED_FOLLOWING_PARAM_FIND)
                                || cause.equals(CalcRequestCause.EVENT_TIER_CHANGE)) {
                    preceedingParamChangeNo = i;
                    break;
                }
            }
        }
        if (preceedingParamChangeNo == 0) {
            AlertBox.displayError("No param changes before first MatchIncident");
            return;
        }
        AlertBox.displayMsg(String.format(
                        "Jumping to the paramChange immediately preceeding the first MatchIncident, actionNo: %d.",
                        preceedingParamChangeNo));
        currentActionNo = preceedingParamChangeNo;
        this.handleNextActionButtonPressed();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * 
     * @param matchRunner
     * @param recordingFromFile
     */
    public void initialize(MatchRunnable matchRunner, Recording recordingFromFile) {
        this.matchRunner = matchRunner;
        this.recordingFromFile = recordingFromFile;
        currentActionNo = 0;
        handledFirstMatchIncident = false;
        initialiseDefaultFonts();
        requestDetailText.setMaxWidth(Region.USE_COMPUTED_SIZE);
        requestDetailText.setPrefWidth(Region.USE_COMPUTED_SIZE);
        observableActionsList = new ObservableMap(null);
        actionsTable.setItems(observableActionsList.getData());
        actionsTable.setEditable(false);
        actionsTableColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        actionsTable.getSelectionModel().selectedItemProperty()
                        .addListener((observable, oldValue, newValue) -> handleActionSelected(newValue));

        responseDetailText.setMaxWidth(Region.USE_COMPUTED_SIZE);
        responseDetailText.setPrefWidth(Region.USE_COMPUTED_SIZE);
    }

    private Object handleActionSelected(ObservableMapRow observableRow) {
        selectedActionNo = Integer.valueOf(observableRow.getKey());
        this.updateActionDetail(selectedActionNo, showFullDetail);
        return null;
    }

    static Font boldFont;
    static Font normalFont;
    static Color normalColor = Color.BLACK;
    static Color lowlightColor = Color.GRAY;
    static Color recordedRowColor = Color.RED;
    static Color currentRowColor = Color.GREEN;

    private void initialiseDefaultFonts() {
        Text text = new Text("Hello");
        normalFont = text.getFont();
        boldFont = Font.font(normalFont.getStyle(), FontWeight.BOLD, normalFont.getSize());
    }

    private void displayData() {
        updateActionsList();
        updateActionDetail(currentActionNo, showFullDetail);
    }

    private void updateActionDetail(int itemNo, boolean showFullDetail) {
        RecordedItem recordedItem = recordingFromFile.get(itemNo);
        requestDetailText.setText(generateActionRequestDetail(recordedItem, showFullDetail));
        responseDetailText.setText(generateActionResponseDetail(recordedItem, showFullDetail));
        String s = (showFullDetail) ? "Full " : "Summary ";
        requestDetail.setText(s + "request for item no: " + Integer.toString(itemNo));
        responseDetail.setText(s + "response for item no: " + Integer.toString(itemNo));

    }

    @FXML
    public void onFullDetailPressed() {
        showFullDetail = !showFullDetail;
        updateActionDetail(selectedActionNo, showFullDetail);
    }


    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private String generateActionRequestDetail(RecordedItem item, boolean showFullRequestResponse) {
        StringBuilder b = new StringBuilder();
        long startTimeMillis = item.getTimeRequestIssued();
        long endTimeMillis = item.getTimeResponseReceived();
        String timeStamp = "No start time recorded";
        String duration = "No end time recorded";
        if (startTimeMillis != 0) {
            Date date = new Date(startTimeMillis);
            timeStamp = "Action initiated at: " + formatter.format(date);
            if (endTimeMillis >= startTimeMillis)
                duration = String.format("Time taken to complete action : %dms", endTimeMillis - startTimeMillis);
        }
        String timeString = timeStamp + "\n" + duration + "\n";
        b.append(timeString).append("\n");
        PriceCalcRequest p2;
        switch (item.getRecordedItemType()) {
            case PARAM_FIND:
                ParamFindRequest p = item.getParamFindRequest();
                String request;
                if (showFullRequestResponse) {
                    request = JsonUtil.marshalJson(p, true);
                } else {
                    request = JsonUtil.marshalJson(p.getMarketPricesList(), true);
                }
                b.append(request);
                break;
            case PRICE_CALC:
                String request2;
                p2 = item.getPriceCalcRequest();
                if (showFullRequestResponse) {
                    request2 = JsonUtil.marshalJson(p2, true);
                } else {
                    switch (p2.getCalcRequestCause()) {
                        case EVENT_TIER_CHANGE:
                            request2 = JsonUtil.marshalJson(p2.getEventSettings(), true);
                            break;
                        case MATCH_INCIDENT:
                            request2 = JsonUtil.marshalJson(p2.getMatchIncident(), true);
                            break;
                        case NEW_MATCH:
                            request2 = JsonUtil.marshalJson(p2.getMatchFormat(), true);
                            break;
                        case PARAMS_CHANGED_BY_TRADER:
                        case PARAMS_CHANGED_FOLLOWING_PARAM_FIND:
                            request2 = JsonUtil.marshalJson(p2.getMatchParams(), true);
                            break;
                        case TIMER:
                            request2 = JsonUtil.marshalJson(p2.getMatchState().generateSimpleMatchState(), true);
                            break;
                        default:
                            request2 = "No summary information";
                            break;
                    }
                }
                b.append(request2);
                break;
            case REVERT_TO_EARLIER_STATE:
                b.append("Revert to requestId: " + item.getRevertToEarlierStateForRequestId());
                break;
            default:
                break;
        }
        return b.toString();
    }

    private String generateActionResponseDetail(RecordedItem item, boolean showFullRequestResponse) {
        StringBuilder b = new StringBuilder();
        if (item.getFatalErrorExecutingRequest())
            b.append("Fatal error executing request");
        else {
            switch (item.getRecordedItemType()) {
                case PARAM_FIND:
                    ParamFindResponse p = item.getParamFindResponse();
                    String response;
                    if (showFullRequestResponse) {
                        response = JsonUtil.marshalJson(p, true);
                    } else {
                        response = JsonUtil.marshalJson(p.getParamFindResults(), true);
                    }
                    b.append(response);
                    break;
                case PRICE_CALC:
                    PriceCalcResponse p2 = item.getPriceCalcResponse();
                    String response2;
                    if (showFullRequestResponse) {
                        response2 = JsonUtil.marshalJson(p2, true);
                    } else {
                        response2 = JsonUtil.marshalJson(p2.getMarkets(), true);
                    }
                    b.append(response2);
                    break;
                case REVERT_TO_EARLIER_STATE:
                    b.append("No information available");
                    break;
                default:
                    break;
            }
        }
        return b.toString();

    }

    public void setRecordingNow(Recording recordingNow) {
        this.recordingNow = recordingNow;
    }

    private void updateActionsList() {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < recordingFromFile.size(); i++) {
            RecordedItem item = recordingFromFile.get(i);
            StringBuilder b = new StringBuilder();
            if (i < currentActionNo) {
                b.append("x ");
                // textActionSummary.setFont(normalFont);
                // textActionSummary.setFill(lowlightColor);
            } else if (i == currentActionNo) {
                b.append("->");
                // textActionSummary.setFont(boldFont);
                // textActionSummary.setFill(normalColor);
            } else
                b.append("  ");

            b.append(String.format("%2d: %s, %s\n", i,
                            DateFunctions.millisToString(item.getTimeRequestIssued(), "HH:mm:ss"),
                            item.shortDescription()));
            map.put(Integer.toString(i), b.toString());
        }
        observableActionsList.updateDisplayedData(map);
    }


    public void displayInitialData() {
        displayData();
        currentActionNo++;

    }

}
