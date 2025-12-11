package ats.algo.core.eventsettings;

import java.io.Serializable;

public abstract class AbstractEventSetting implements Serializable {


    private static final long serialVersionUID = 1L;
    protected boolean modifiable;

    public boolean isModifiable() {
        return modifiable;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (modifiable ? 1231 : 1237);
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
        AbstractEventSetting other = (AbstractEventSetting) obj;
        if (modifiable != other.modifiable)
            return false;
        return true;
    }



}
