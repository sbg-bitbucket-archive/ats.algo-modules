package ats.algo.montecarloframework;

import java.util.LinkedHashMap;
import java.util.Map;

import ats.algo.core.baseclasses.MatchFormat;
import ats.algo.core.baseclasses.MatchFormatOptions;

class TestMatchFormat extends MatchFormat {

    TestMatchFormat() {
        super(null);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    int nSets;

    /*
     * can be empty for this test
     */
    @Override
    public LinkedHashMap<String, String> getAsMap() {
        return null;
    }

    @Override
    public String setFromMap(Map<String, String> map) {
        return null;
    }

    @Override
    public void applyFormat(Map<String, Object> format) {
        // TODO Auto-generated method stub

    }

    @Override
    public MatchFormatOptions matchFormatOptions() {
        throw new IllegalArgumentException("Not yet implemented for TestMatch");
    }

    @Override
    public MatchFormat generateDefaultMatchFormat() {
        return null;
    }
}

