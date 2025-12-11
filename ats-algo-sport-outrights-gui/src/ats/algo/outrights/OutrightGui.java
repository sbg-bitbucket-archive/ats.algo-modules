package ats.algo.outrights;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Provides the main entry point to run the tennis model within the MatchRunner model testing GUI the main entry point
 * just instantiates the matchFormat object plus MatchRunner JavaFx then executes the "start" method which passes
 * control to MatchRunner. MatchRunner then displays the dialog box which allows the user to review/change the
 * MatchFormat settings When the user Ok's the settings the "initialisetennisObjects" method is executed via the
 * callback provided to the MatchRunner constructor
 * 
 * Above all required because we can't instantiate the various required objects until the match format is set, and we
 * can't set the match format without instantiating the gui
 * 
 * @author Rob
 * 
 */

@SuppressWarnings("restriction")
public class OutrightGui extends Application {
    private static OutrightGuiService outrightGuiService;

    public static void main(String[] args) {
        outrightGuiService = new OutrightGuiService(" Outright sport v1.1");
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        outrightGuiService.start(primaryStage);
    }

}
