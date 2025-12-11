package ats.algo.core.baseclasses;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ats.algo.genericsupportfunctions.CopySerializableObject;

/**
 * Container to hold any data that xxxMatchEngine wants saved between calls to the calculate method. A derived version
 * of this base classe should be created for any pricing model that needs it. Any derived classes must be serializable.
 * 
 * We highly recommend that any derived class implements an override "equals" method that can be used to verify that the
 * class is in fact correctly serializing and deserializing. This check is performed by MAtchRunner. If not overriden
 * then the base version of the method will be used, which just checks that the class names are the same
 * 
 * @author Geoff
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "subClass")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchEngineSavedState implements Serializable {

    private static final long serialVersionUID = 1L;
    private String savedState;

    public String getSavedState() {
        return savedState;
    }

    public void setSavedState(String savedState) {
        this.savedState = savedState;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((savedState == null) ? 0 : savedState.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MatchEngineSavedState other = (MatchEngineSavedState) obj;
        if (savedState == null) {
            if (other.savedState != null)
                return false;
        } else if (!savedState.equals(other.savedState))
            return false;
        return true;
    }

    /**
     * makes a copy of itself. Since the contents can be pretty much anything, use the fact that the class is
     * serializable
     * 
     * @return
     */
    public MatchEngineSavedState copy() {
        return CopySerializableObject.copy(this);
    }

}
