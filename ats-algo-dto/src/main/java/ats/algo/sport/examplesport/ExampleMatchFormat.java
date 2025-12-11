package ats.algo.sport.examplesport;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;

public class ExampleMatchFormat extends MatchFormat {

    private static final long serialVersionUID = 1L;
    private int noLegsInMatch;

    public ExampleMatchFormat() {
        super(null);
        noLegsInMatch = 3;
    }

    public int getNoLegsInMatch() {
        return noLegsInMatch;
    }

    public void setNoLegsInMatch(int n) {
        noLegsInMatch = n;
    }

    private final String nLegsKeyName = "No of legs in match (1-99)";

    @Override
    public void applyFormat(Map<String, Object> format) {}

    @Override
    public LinkedHashMap<String, String> getAsMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        map.put(nLegsKeyName, String.format("%d", noLegsInMatch));
        return map;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        String errorMessage = null;
        String newValue = map.get(nLegsKeyName);
        try {
            noLegsInMatch = Integer.parseInt(newValue);
            if ((noLegsInMatch) < 1 || (noLegsInMatch > 99))
                throw new Exception();
        } catch (Exception e) {
            errorMessage = String.format("Invalid input: %s.  Not an integer or out of range", newValue);
        }
        return errorMessage;
    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        throw new IllegalArgumentException("Not implemented for ExampleMatchFormat");
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return new ExampleMatchFormat();
    }

}
