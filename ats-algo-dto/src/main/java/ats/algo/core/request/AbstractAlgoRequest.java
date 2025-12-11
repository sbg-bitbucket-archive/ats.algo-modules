package ats.algo.core.request;

import java.io.Serializable;
import java.util.UUID;

import ats.core.util.json.JsonUtil;

public class AbstractAlgoRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String uniqueRequestId; // a derived id which is guaranteed to be unique
    protected long requestTime;
    protected String versionId;

    public AbstractAlgoRequest() {
        super();
        versionId = VersionId.ID;

    }


    /*
     * don't use standard set syntax to avoid problems with josn serialisation
     */
    public void updateUniqueRequestId(String uniqueRequestId) {
        this.uniqueRequestId = uniqueRequestId;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public String getUniqueRequestId() {
        return uniqueRequestId;
    }

    public String getVersionId() {
        return versionId;
    }

    public static String createUniqueRequestId() {
        return "R-" + UUID.randomUUID().toString();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (requestTime ^ (requestTime >>> 32));
        result = prime * result + ((uniqueRequestId == null) ? 0 : uniqueRequestId.hashCode());
        result = prime * result + ((versionId == null) ? 0 : versionId.hashCode());
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
        AbstractAlgoRequest other = (AbstractAlgoRequest) obj;
        if (requestTime != other.requestTime)
            return false;
        if (uniqueRequestId == null) {
            if (other.uniqueRequestId != null)
                return false;
        } else if (!uniqueRequestId.equals(other.uniqueRequestId))
            return false;
        if (versionId == null) {
            if (other.versionId != null)
                return false;
        } else if (!versionId.equals(other.versionId))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return JsonUtil.marshalJson(this);
    }


}
