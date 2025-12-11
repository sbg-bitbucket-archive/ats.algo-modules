package ats.algo.sport.ufc;

import java.io.Serializable;

import ats.algo.genericsupportfunctions.JsonSerializer;

public class UfcSavedState implements Serializable {

    private static final long serialVersionUID = 1L;



    public UfcSavedState() {}

    /*
     * convert to Json
     */
    /**
     * convert this object to Json
     * 
     * @return
     */
    public String toJson() {
        return JsonSerializer.serialize(this, false);
    }

    /**
     * create a new instance of this class from the supplied json string
     * 
     * @param savedState
     * @return
     */
    public static UfcSavedState fromJson(String json) {

        return JsonSerializer.deserialize(json, UfcSavedState.class);
    }
}
