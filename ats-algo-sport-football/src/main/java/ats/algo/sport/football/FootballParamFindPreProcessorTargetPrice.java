package ats.algo.sport.football;

class FootballParamFindPreProcessorTargetPrice {
    String lineId;
    double dLineId;
    double targetProb;
    boolean isFavourite;

    public FootballParamFindPreProcessorTargetPrice(String lineId, double targetProb, boolean isFavourite) {
        this.lineId = lineId;
        dLineId = -9999.0;
        if (lineId != null) {
            try {
                this.dLineId = Double.parseDouble(lineId);
            } catch (NumberFormatException e) {
                /*
                 * do nothing
                 */
            }
        }
        this.targetProb = targetProb;
        this.isFavourite = isFavourite;
    }


    @Override
    public String toString() {
        return "FootballParamFindPreProcessorTargetPrice [lineId=" + lineId + ", targetProb=" + targetProb
                        + ", isFavourite=" + isFavourite + "]";
    }

}
