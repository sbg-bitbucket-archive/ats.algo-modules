package ats.algo.core.eventsettings;

import java.util.HashMap;
import java.util.Map;

import ats.core.util.json.JsonUtil;

public class ChannelEventSetting extends AbstractEventSetting {

    private static final long serialVersionUID = 1L;

    private Map<String, String> values;

    public ChannelEventSetting() {
        super();
        values = new HashMap<String, String>();
    }

    public ChannelEventSetting(boolean modifiable) {
        this();
        super.setModifiable(modifiable);
    }


    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }

    public String getValue(String channel) {
        return values.get(channel);
    }

    public String setValue(String channel, String value) {
        return values.put(channel, value);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((values == null) ? 0 : values.hashCode());
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
        ChannelEventSetting other = (ChannelEventSetting) obj;
        if (values == null) {
            if (other.values != null)
                return false;
        } else if (!values.equals(other.values))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }



}
