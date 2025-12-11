package ats.algo.inplayGS;

import java.io.Serializable;

/**
 * Holds information about a player
 * 
 * @author Geoff
 *
 */
public class PlayerLineupInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private boolean playing;
    private boolean substituted;
    private boolean sentOff;


    String getPlayerName() {
        return playerName;
    }


    void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    boolean isPlaying() {
        return playing;
    }


    void setPlaying(boolean playing) {
        this.playing = playing;
    }


    boolean isSubstituted() {
        return substituted;
    }


    void setSubstituted(boolean substituted) {
        this.substituted = substituted;
    }


    boolean isSentOff() {
        return sentOff;
    }


    void setSentOff(boolean sentOff) {
        this.sentOff = sentOff;
    }


    @Override
    public String toString() {
        return playerName + ", playing=" + playing + ", substituted=" + substituted + ", sentOff=" + sentOff;
    }
}

