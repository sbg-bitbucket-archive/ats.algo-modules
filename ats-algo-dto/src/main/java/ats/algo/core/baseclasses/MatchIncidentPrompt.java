package ats.algo.core.baseclasses;

/**
 * For framework use only. Defines the data returned by the MatchState.getNextPrompt method
 * 
 * @author Geoff
 *
 */
public final class MatchIncidentPrompt {
    private boolean matchOver;
    private String prompt;
    private String defaultValue;

    /**
     * Constructor when match in progress
     * 
     * @param prompt
     * @param defaultValue
     */
    public MatchIncidentPrompt(String prompt, String defaultValue) {
        super();
        this.matchOver = false;
        this.prompt = prompt;
        this.defaultValue = defaultValue;
    }

    /**
     * Constructor when match completed
     * 
     * @param string
     */
    public MatchIncidentPrompt(String string) {
        super();
        this.matchOver = true;
        this.prompt = string;
        this.defaultValue = "N/A";
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPromt(String prompt) {
        this.prompt = prompt;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isMatchOver() {
        return matchOver;
    }

}
