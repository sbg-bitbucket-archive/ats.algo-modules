package ats.algo.core.eventsettings;

import ats.core.util.json.JsonUtil;

public class GeneralEventSetting extends AbstractEventSetting {

    private static final long serialVersionUID = 1L;

    private String value;

    public GeneralEventSetting() {
        super();
    }

    public GeneralEventSetting(boolean modifiable, String value) {
        this();
        super.setModifiable(modifiable);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GeneralEventSetting other = (GeneralEventSetting) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
