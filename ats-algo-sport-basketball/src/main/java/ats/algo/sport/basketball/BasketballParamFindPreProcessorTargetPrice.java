package ats.algo.sport.basketball;

class BasketballParamFindPreProcessorTargetPrice {
    String lineId;
    double dLineId;
    double targetProb;
    boolean isFavourite;

    public BasketballParamFindPreProcessorTargetPrice(String lineId, double targetProb, boolean isFavourite) {
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
        return "BasketballParamFindPreProcessorTargetPrice [lineId=" + lineId + ", targetProb=" + targetProb
                        + ", isFavourite=" + isFavourite + "]";
    }

}
