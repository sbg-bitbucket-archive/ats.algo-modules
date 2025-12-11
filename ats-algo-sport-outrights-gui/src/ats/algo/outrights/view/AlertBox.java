package ats.algo.outrights.view;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;

@SuppressWarnings("restriction")
public class AlertBox {

    public static void displayComingSoon() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Coming soon!");
        alert.setHeaderText("not yet implemented");
        alert.showAndWait();
    }

    public static void displayMsg(String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Match Runner");
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    /**
     * 
     * @param title
     * @param msg
     * @param defaultOption
     * @param otherOption
     * @return true if default option is selected
     */
    public static boolean confirmDefaultOption(String title, String msg, String defaultOption, String otherOption) {
        ButtonType yes = new ButtonType(defaultOption, ButtonData.YES);
        ButtonType no = new ButtonType(otherOption, ButtonData.NO);
        Alert alert = new Alert(AlertType.CONFIRMATION, msg, no, yes);
        alert.setTitle(title);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonData bData = result.get().getButtonData();
        boolean b = bData == ButtonData.YES;
        return b;
    }

    public static void displayError(String msg) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Outright Match Runner Fatal Error");
        alert.setHeaderText(msg);
        alert.showAndWait();

    }



}
