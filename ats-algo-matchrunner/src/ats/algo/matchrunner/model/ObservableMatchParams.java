package ats.algo.matchrunner.model;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import ats.algo.core.MarketGroup;
import ats.algo.core.baseclasses.MatchParam;
import ats.algo.core.baseclasses.MatchParams;
import ats.algo.core.enumMatchParam.EnumMatchParam;
import ats.algo.genericsupportfunctions.SplitCamelCase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * wrapper for the MatchParams class to convert the data to/from ObservableList format so it can be displayed in the GUI
 * 
 * @author Geoff
 * 
 */
public class ObservableMatchParams {



    private MatchParams matchParams;
    private ObservableList<ObservableMatchParam> observableMatchParams = FXCollections.observableArrayList();
    private ObservableList<ObservableMatchParam> observableMatchParamsPlayers = FXCollections.observableArrayList();
    private ObservableList<ObservableMatchParam> observableMatchParamsPenalties = FXCollections.observableArrayList();


    public ObservableMatchParams() {
        /*
         * do nothing
         */
    }

    public MatchParams getMatchParams() {
        return matchParams;
    }

    public ObservableList<ObservableMatchParam> getData() {
        return observableMatchParams;
    }

    public ObservableList<ObservableMatchParam> getData1() {
        return observableMatchParamsPlayers;
    }

    public ObservableList<ObservableMatchParam> getData2() {
        return observableMatchParamsPenalties;
    }

    /**
     * update the displayed data
     * 
     * @param matchParams
     */
    public void updateDisplayedMatchParams(MatchParams matchParams) {
        this.matchParams = matchParams;
        updateDisplayedData();
    }

    private void updateDisplayedData() {
        observableMatchParams.clear();
        observableMatchParamsPlayers.clear();
        observableMatchParamsPenalties.clear();
        for (Entry<String, MatchParam> entry : matchParams.getParamMap().entrySet()) {
            MatchParam matchParam = entry.getValue();
            String key = entry.getKey();
            if (matchParam.getMarketGroup() == MarketGroup.INDIVIDUAL)
                observableMatchParamsPlayers.add(new ObservableMatchParam(key, matchParam));
            else if (matchParam.getMarketGroup() == MarketGroup.PENALTY)
                observableMatchParamsPenalties.add(new ObservableMatchParam(key, matchParam));
            else
                observableMatchParams.add(new ObservableMatchParam(key, matchParam));
        }
    }



    /**
     * converts an instance of the MatchParam class to the format needed to be displayed in the GUI
     * 
     * @author Geoff
     *
     */
    public class ObservableMatchParam {
        private final String key;
        private final StringProperty name;
        private final StringProperty type;
        private final StringProperty group;
        private final StringProperty mean;
        private final StringProperty stdDevn;
        private final StringProperty bias;
        private final boolean displayAsPercentage;
        private final String numberDisplayFormat;
        private final MatchParam matchParam;

        public ObservableMatchParam(String key, MatchParam param) {
            this.key = key;
            String description = param.getDescription();
            String nameProperty = null;
            if (description == null)
                nameProperty = SplitCamelCase.split(key);
            else
                nameProperty = description;
            this.name = new SimpleStringProperty(nameProperty);
            this.type = new SimpleStringProperty(param.getMatchParameterType().toString());
            this.group = new SimpleStringProperty(param.getMarketGroup().toString());
            this.matchParam = param;
            switch (param.getMatchParameterType()) {
                case TRADER_CONTROL:
                    numberDisplayFormat = "%d";
                    displayAsPercentage = false;
                    int iVal = (int) param.getGaussian().getMean();
                    this.mean = new SimpleStringProperty(String.format(numberDisplayFormat, iVal));
                    this.stdDevn = new SimpleStringProperty("");
                    this.bias = new SimpleStringProperty("");
                    break;
                case ENUM:
                    numberDisplayFormat = "%d";
                    displayAsPercentage = false;
                    this.mean = new SimpleStringProperty(((EnumMatchParam) param).getValue());
                    this.stdDevn = new SimpleStringProperty("");
                    this.bias = new SimpleStringProperty("");
                    break;
                default:
                    this.displayAsPercentage = param.isDisplayAsPercentage();
                    double multiplier = 1;
                    if (displayAsPercentage) {
                        numberDisplayFormat = "%.1f%%";
                        multiplier = 100;
                    } else
                        numberDisplayFormat = "%.3f";

                    this.mean = new SimpleStringProperty(
                                    String.format(numberDisplayFormat, param.getGaussian().getMean() * multiplier));
                    this.stdDevn = new SimpleStringProperty(
                                    String.format(numberDisplayFormat, param.getGaussian().getStdDevn() * multiplier));
                    this.bias = new SimpleStringProperty(
                                    String.format(numberDisplayFormat, param.getGaussian().getBias() * multiplier));
                    break;
            }


        }

        public StringProperty nameProperty() {
            return name;
        }

        public StringProperty typeProperty() {
            return type;
        }

        public StringProperty groupProperty() {
            return group;
        }

        public StringProperty valueProperty() {
            return mean;
        }

        public StringProperty stdDevnProperty() {
            return stdDevn;
        }

        public StringProperty biasProperty() {
            return bias;
        }



        /**
         * called when user has manually changed the Mean via the gui
         * 
         * @param newValueStr
         */
        public String updateMean(String oldValueStr, String newValueStr) {
            String errMsg = null;
            switch (matchParam.getMatchParameterType()) {
                case TRADER_CONTROL:
                    int newIntValue = 0;
                    try {
                        newIntValue = Integer.parseInt(newValueStr);
                    } catch (NumberFormatException e) {
                        errMsg = String.format("%s is not a valid integer", newValueStr);
                    }
                    if (errMsg == null) {
                        int lBound = (int) matchParam.getMinAllowedParamValue();
                        int uBound = (int) matchParam.getMaxAllowedParamValue();
                        if (newIntValue < lBound || newIntValue > uBound) {
                            errMsg = String.format("Value %d is not within allowed bounds %d to %d", newIntValue,
                                            lBound, uBound);
                        } else {
                            matchParam.getGaussian().setMean((double) newIntValue);
                        }
                    }
                    break;
                case ENUM:
                    EnumMatchParam enumMatchParam = (EnumMatchParam) matchParam;
                    if (enumMatchParam.validEnum(newValueStr))
                        enumMatchParam.setValue(newValueStr);
                    else
                        errMsg = String.format("%s is not a valid option.  Possible options are: %s", newValueStr,
                                        enumMatchParam.validSelectionsStr());
                    break;
                default:
                    double newValue = 0;
                    try {
                        if (this.displayAsPercentage)
                            newValueStr = this.stripPctSign(newValueStr);
                        newValue = Double.parseDouble(newValueStr);
                    } catch (NumberFormatException e) {
                        errMsg = String.format("%s is not a valid number", newValueStr);
                    }
                    if (errMsg == null) {
                        double multiplier;
                        if (matchParam.isDisplayAsPercentage())
                            multiplier = 100;
                        else
                            multiplier = 1;
                        double adjNewValue = newValue / multiplier;
                        if (matchParam.valueIsValid(adjNewValue, matchParam.getGaussian().getBias()))
                            matchParam.updateGaussian(adjNewValue, matchParam.getGaussian().getStdDevn());
                        else {
                            errMsg = String.format(
                                            "Value %.3f or value + bias %.3f is not within allowed bounds %.3f to %.3f",
                                            adjNewValue, matchParam.getGaussian().getBias(),
                                            matchParam.getMinAllowedParamValue(), matchParam.getMaxAllowedParamValue());
                        }
                    }
                    break;
            }

            updateDisplayedData();
            return errMsg;
        }

        private String stripPctSign(String s) {
            String s2 = s.substring(s.length() - 1);
            if (s2.equals("%"))
                return s.substring(0, s.length() - 1);
            else
                return s;
        }

        /**
         * called when user has manually changed the StdDevn via the gui
         * 
         * @param newStdDevnStr
         */
        public String updateStdDevn(String oldValueStr, String newStdDevnStr) {
            String errMsg = null;
            double newStdDevn = 0;
            try {
                if (this.displayAsPercentage)
                    newStdDevnStr = this.stripPctSign(newStdDevnStr);
                newStdDevn = Double.parseDouble(newStdDevnStr);
            } catch (NumberFormatException e) {
                errMsg = "Not a valid number";
            }
            if (errMsg == null) {
                double multiplier;
                if (matchParam.isDisplayAsPercentage())
                    multiplier = 100;
                else
                    multiplier = 1;
                double adjNewStdDevn = newStdDevn / multiplier;
                matchParam.updateGaussian(matchParam.getGaussian().getMean(), adjNewStdDevn);
            }
            updateDisplayedData();
            return errMsg;
        }

        /**
         * called when user has manually changed the bias via the gui
         * 
         * @param newBiasStr
         */
        public String updateBias(String oldValueStr, String newBiasStr) {
            String errMsg = null;
            double newBias = 0;
            try {
                if (this.displayAsPercentage)
                    newBiasStr = this.stripPctSign(newBiasStr);
                newBias = Double.parseDouble(newBiasStr);
            } catch (NumberFormatException e) {
                errMsg = "Not a valid number";
            }
            if (errMsg == null) {
                LinkedHashMap<String, MatchParam> map = matchParams.getParamMap();
                MatchParam matchParam = map.get(key);
                double multiplier;
                if (matchParam.isDisplayAsPercentage())
                    multiplier = 100;
                else
                    multiplier = 1;
                double adjNewBias = newBias / multiplier;
                if (matchParam.valueIsValid(matchParam.getGaussian().getMean(), adjNewBias))
                    matchParam.getGaussian().setBias(adjNewBias);
                else
                    errMsg = String.format("Value %.3f or value + bias %.3f is not within allowed bounds %.3f to %.3f",
                                    matchParam.getGaussian().getMean(), adjNewBias,
                                    matchParam.getMinAllowedParamValue(), matchParam.getMaxAllowedParamValue());
            }
            updateDisplayedData();
            return errMsg;
        }
    }
}
