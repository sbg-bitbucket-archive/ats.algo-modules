package ats.algo.core.request;

import java.io.Serializable;

import ats.core.util.json.JsonUtil;

public class AbstractAlgoResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String uniqueRequestId; // uniqueId used by the JMS transport
                                      // layer
    protected String versionId;
    protected String serverId;
    protected boolean fatalError;
    protected String fatalErrorCause;


    public AbstractAlgoResponse() {
        super();
        versionId = VersionId.ID;
        fatalError = false;
    }

    public AbstractAlgoResponse(String uniqueRequestId) {
        this();
        this.uniqueRequestId = uniqueRequestId;
    }

    public boolean isFatalError() {
        return fatalError;
    }

    public void setFatalError(boolean fatalError) {
        this.fatalError = fatalError;
    }

    public String getFatalErrorCause() {
        return fatalErrorCause;
    }

    public void setFatalErrorCause(String fatalErrorCause) {
        this.fatalErrorCause = fatalErrorCause;
    }

    public String getUniqueRequestId() {
        return uniqueRequestId;
    }

    public void setUniqueRequestId(String uniqueRequestId) {
        this.uniqueRequestId = uniqueRequestId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    /**
     * 
     * @param uniqueRequestId
     * @param cause
     * @return
     */
    public static AbstractAlgoResponse generateFatalErrorResponse(String uniqueRequestId, String cause) {
        AbstractAlgoResponse response = new AbstractAlgoResponse(uniqueRequestId);
        response.fatalError = true;
        response.fatalErrorCause = cause;
        return response;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (fatalError ? 1231 : 1237);
        result = prime * result + ((fatalErrorCause == null) ? 0 : fatalErrorCause.hashCode());
        result = prime * result + ((serverId == null) ? 0 : serverId.hashCode());
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
        AbstractAlgoResponse other = (AbstractAlgoResponse) obj;
        if (fatalError != other.fatalError)
            return false;
        if (fatalErrorCause == null) {
            if (other.fatalErrorCause != null)
                return false;
        } else if (!fatalErrorCause.equals(other.fatalErrorCause))
            return false;
        if (serverId == null) {
            if (other.serverId != null)
                return false;
        } else if (!serverId.equals(other.serverId))
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
