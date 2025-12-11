package ats.algo.core.recordplayback;

import java.io.Serializable;
import java.util.Map;

public class ConfiguredProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> generalProperties;
    private Map<String, String> sportSpecificProperties;

    public Map<String, String> getGeneralProperties() {
        return generalProperties;
    }

    public void setGeneralProperties(Map<String, String> generalProperties) {
        this.generalProperties = generalProperties;
    }

    public Map<String, String> getSportSpecificProperties() {
        return sportSpecificProperties;
    }

    public void setSportSpecificProperties(Map<String, String> sportSpecificProperties) {
        this.sportSpecificProperties = sportSpecificProperties;
    }



}
