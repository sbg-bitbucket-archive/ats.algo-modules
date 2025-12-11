package ats.algo.sport.outrights.server.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostReply {
    boolean updatedSuccessfully;
    String furtherInformation;

    public PostReply() {}


    /**
     * 
     * @param id
     * @param updatedSuccessfully
     * @param furtherInformation
     */
    public PostReply(boolean updatedSuccessfully, String furtherInformation) {
        this.updatedSuccessfully = updatedSuccessfully;
        this.furtherInformation = furtherInformation;
    }

    /**
     * 
     * @param alert
     */
    public PostReply(Alert alert) {
        this(alert.getAlertType() == AlertType.INFO, alert.getDescription());
    }


    public boolean isUpdatedSuccessfully() {
        return updatedSuccessfully;
    }

    public String getFurtherInformation() {
        return furtherInformation;
    }

    public void setUpdatedSuccessfully(boolean updatedSuccessfully) {
        this.updatedSuccessfully = updatedSuccessfully;
    }

    public void setFurtherInformation(String furtherInformation) {
        this.furtherInformation = furtherInformation;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((furtherInformation == null) ? 0 : furtherInformation.hashCode());
        result = prime * result + (updatedSuccessfully ? 1231 : 1237);
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
        PostReply other = (PostReply) obj;
        if (furtherInformation == null) {
            if (other.furtherInformation != null)
                return false;
        } else if (!furtherInformation.equals(other.furtherInformation))
            return false;
        if (updatedSuccessfully != other.updatedSuccessfully)
            return false;
        return true;
    }


}
