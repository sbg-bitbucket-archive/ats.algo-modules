package ats.algo.core.recordplayback;

public class DisplayString {

    public enum LineCategory {
        HEADER,
        INPUT_LINE,
        SAME,
        DIFFERENT,
        RECORDING_ROW,
        CURRENT_ROW

    }

    private LineCategory displayStyle;
    private String string;

    public DisplayString(LineCategory displayStyle, String string) {
        this.displayStyle = displayStyle;
        this.string = string;
    }

    public LineCategory getDisplayStyle() {
        return displayStyle;
    }

    public String getString() {
        return string;
    }



}

