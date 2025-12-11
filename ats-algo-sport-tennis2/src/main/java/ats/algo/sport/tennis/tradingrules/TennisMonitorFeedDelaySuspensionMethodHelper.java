package ats.algo.sport.tennis.tradingrules;

import ats.algo.core.baseclasses.MatchState;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult;
import ats.algo.core.monitorfeed.tradingrules.MonitorFeedTradingRuleSuspensionMethodResult.MonitorFeedTradingRuleSuspensionMethodResultType;
import ats.algo.sport.tennis.SuspensionStatus;
import ats.algo.sport.tennis.TennisMatchIncident.TennisMatchIncidentType;
import ats.algo.sport.tennis.TennisMatchState;

public class TennisMonitorFeedDelaySuspensionMethodHelper {
    private long medicalTimeoutInitialSuspensionDelay;
    private long medicalTimeoutSuspensionLengthOfTime;
    private boolean medicalTimeoutCeaseReactivationAfterIncident;
    private long challengeInitialSuspensionDelay;
    private long challengeSuspensionLengthOfTime;
    private boolean challengeCeaseReactivationAfterIncident;
    private long rainInitialSuspensionDelay;
    private long rainSuspensionLengthOfTime;
    private boolean rainCeaseReactivationAfterIncident;

    public static class Builder {
        private long medicalTimeoutInitialSuspensionDelay = -1;
        private long medicalTimeoutSuspensionLengthOfTime = -1;
        private boolean medicalTimeoutCeaseReactivationAfterIncident = false;
        private long challengeInitialSuspensionDelay = -1;
        private long challengeSuspensionLengthOfTime = -1;
        private boolean challengeCeaseReactivationAfterIncident = false;
        private long rainInitialSuspensionDelay = -1;
        private long rainSuspensionLengthOfTime = -1;
        private boolean rainCeaseReactivationAfterIncident = false;

        public Builder medicalTimeout(long medicalTimeoutInitialSuspensionDelay,
                        long medicalTimeoutSuspensionLengthOfTime,
                        boolean medicalTimeoutCeaseReactivationAfterIncident) {
            this.medicalTimeoutInitialSuspensionDelay = medicalTimeoutInitialSuspensionDelay;
            this.medicalTimeoutSuspensionLengthOfTime = medicalTimeoutSuspensionLengthOfTime;
            this.medicalTimeoutCeaseReactivationAfterIncident = medicalTimeoutCeaseReactivationAfterIncident;
            return this;
        }

        public Builder challenge(long challengeInitialSuspensionDelay, long challengeSuspensionLengthOfTime,
                        boolean challengeCeaseReactivationAfterIncident) {
            this.challengeInitialSuspensionDelay = challengeInitialSuspensionDelay;
            this.challengeSuspensionLengthOfTime = challengeSuspensionLengthOfTime;
            this.challengeCeaseReactivationAfterIncident = challengeCeaseReactivationAfterIncident;
            return this;
        }

        public Builder rain(long rainInitialSuspensionDelay, long rainSuspensionLengthOfTime,
                        boolean rainCeaseReactivationAfterIncident) {
            this.rainInitialSuspensionDelay = rainInitialSuspensionDelay;
            this.rainSuspensionLengthOfTime = rainSuspensionLengthOfTime;
            this.rainCeaseReactivationAfterIncident = rainCeaseReactivationAfterIncident;
            return this;
        }

        public TennisMonitorFeedDelaySuspensionMethodHelper build() {
            return new TennisMonitorFeedDelaySuspensionMethodHelper(this);
        }

    }

    private TennisMonitorFeedDelaySuspensionMethodHelper(Builder builder) {
        this.medicalTimeoutInitialSuspensionDelay = builder.medicalTimeoutInitialSuspensionDelay;
        this.medicalTimeoutSuspensionLengthOfTime = builder.medicalTimeoutSuspensionLengthOfTime;
        this.medicalTimeoutCeaseReactivationAfterIncident = builder.medicalTimeoutCeaseReactivationAfterIncident;
        this.challengeInitialSuspensionDelay = builder.challengeInitialSuspensionDelay;
        this.challengeSuspensionLengthOfTime = builder.challengeSuspensionLengthOfTime;
        this.challengeCeaseReactivationAfterIncident = builder.challengeCeaseReactivationAfterIncident;
        this.rainInitialSuspensionDelay = builder.rainInitialSuspensionDelay;
        this.rainSuspensionLengthOfTime = builder.rainSuspensionLengthOfTime;
        this.rainCeaseReactivationAfterIncident = builder.rainCeaseReactivationAfterIncident;

    }

    /*
     * 
     * Implementation of the delay suspension method
     * 
     */
    public MonitorFeedTradingRuleSuspensionMethodResult monitorFeedTradingRuleSuspensionMethod(long now,
                    MatchState matchState) {

        Object matchStateType = matchState.getClass();

        if (matchStateType != null && matchStateType == TennisMatchState.class) {

            TennisMatchState tennisMatchState = (TennisMatchState) matchState;

            if (tennisMatchState.getLastIncidentDetails() == null) {
                return null;
            }
            if (tennisMatchState.getLastIncidentDetails().getLastIncident() == null) {
                return null;
            }
            if (tennisMatchState.getLastIncidentDetails().getLastIncident().getIncidentSubType() == null) {
                return null;
            }

            TennisMatchIncidentType incidentType =
                            tennisMatchState.getLastIncidentDetails().getLastIncident().getIncidentSubType();

            if (tennisMatchState.getSuspensionStatus() == null) {
                return null;
            }

            SuspensionStatus suspensionStatus = tennisMatchState.getSuspensionStatus();

            long timeOfSuspensionIncident = 0;

            if (suspensionStatus.isDelaySuspension()) {
                timeOfSuspensionIncident = suspensionStatus.getTimeStampOfIncident();
            }
            /*
             * if last incident is not one of the below incident, suspensionDelay will refresh to -1, which ends the
             * delay
             */
            String reason = null;

            MonitorFeedTradingRuleSuspensionMethodResultType type;
            switch (incidentType) {
                case MEDICAL_TIMEOUT:
                    if (medicalTimeoutInitialSuspensionDelay == 0) { // If event has no wait time between incident and
                                                                     // suspension due to that incident
                        if (now < (timeOfSuspensionIncident + medicalTimeoutInitialSuspensionDelay
                                        + medicalTimeoutSuspensionLengthOfTime)) {
                            reason = "Medical Timeout delay";
                            type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                            return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                        }
                    }
                    if (medicalTimeoutInitialSuspensionDelay > 0) {
                        if ((now - timeOfSuspensionIncident) >= medicalTimeoutInitialSuspensionDelay) { // Checks if we
                                                                                                        // have pass the
                                                                                                        // delayed time
                                                                                                        // between
                                                                                                        // suspension
                                                                                                        // incident and
                                                                                                        // the initial
                                                                                                        // delay time
                                                                                                        // set by
                                                                                                        // trading rules
                            if (now < (timeOfSuspensionIncident + medicalTimeoutInitialSuspensionDelay
                                            + medicalTimeoutSuspensionLengthOfTime)) { // Checks to see if we are within
                                                                                       // the suspension time due to
                                                                                       // suspension incident
                                reason = "Medical Timeout delay";
                                type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                                return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);

                            } else {
                                // Don't suspend due to being after the period of suspension due to this incident
                            }
                        } else {
                            // Don't suspend due to being within the delay of suspension period
                        }
                    }
                    break;
                case CHALLENGE:
                case CHALLENGER_BALLMARK:
                    if (challengeInitialSuspensionDelay == 0) { // If event has no wait time between incident and
                        // suspension due to that incident
                        reason = "Challenge delay";
                        type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                        return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                    }
                    if (challengeInitialSuspensionDelay > 0) {
                        if ((now - timeOfSuspensionIncident) >= challengeInitialSuspensionDelay) { // Checks if we
                            // have pass the
                            // delayed time
                            // between
                            // suspension
                            // incident and
                            // the initial
                            // delay time
                            // set by
                            // trading rules
                            if (now < (timeOfSuspensionIncident + challengeInitialSuspensionDelay
                                            + challengeSuspensionLengthOfTime)) { // Checks to see if we are within
                                                                                  // the suspension time due to
                                                                                  // suspension incident
                                reason = "Challenge delay";
                                type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                                return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                            } else {
                                // Don't suspend due to being after the period of suspension due to this incident
                            }
                        } else {
                            // Don't suspend due to being within the delay of suspension period
                        }
                    }
                    break;
                case RAIN:
                    if (rainInitialSuspensionDelay == 0) { // If event has no wait time between incident and
                        // suspension due to that incident
                        reason = "rain delay";
                        type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                        return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                    }
                    if (rainInitialSuspensionDelay > 0) {
                        if ((now - timeOfSuspensionIncident) >= rainInitialSuspensionDelay) { // Checks if we
                            // have pass the
                            // delayed time
                            // between
                            // suspension
                            // incident and
                            // the initial
                            // delay time
                            // set by
                            // trading rules
                            if (now < (timeOfSuspensionIncident + rainInitialSuspensionDelay
                                            + rainSuspensionLengthOfTime)) { // Checks to see if we are within
                                                                             // the suspension time due to
                                                                             // suspension incident
                                reason = "Rain delay";
                                type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                                return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                            } else {
                                // Don't suspend due to being after the period of suspension due to this incident
                            }
                        } else {
                            // Don't suspend due to being within the delay of suspension period
                        }
                    }
                    break;
                default:
                    break;
            }

            if (suspensionStatus.getReasonOfDelaySuspension() != null) { // null check

                switch (suspensionStatus.getReasonOfDelaySuspension()) {
                    case MEDICAL_TIMEOUT:
                        if (medicalTimeoutCeaseReactivationAfterIncident && (medicalTimeoutInitialSuspensionDelay > -1
                                        || medicalTimeoutSuspensionLengthOfTime > -1)) { // Check we are ignoring
                                                                                         // incidents and we have a
                                                                                         // delay setup.
                            if ((now - timeOfSuspensionIncident) >= medicalTimeoutInitialSuspensionDelay) { // Checks if
                                                                                                            // we have
                                                                                                            // pass the
                                                                                                            // delayed
                                                                                                            // time
                                                                                                            // between
                                                                                                            // suspension
                                                                                                            // incident
                                                                                                            // and the
                                                                                                            // initial
                                                                                                            // delay
                                                                                                            // time set
                                                                                                            // by
                                                                                                            // trading
                                                                                                            // rules
                                if (now < (timeOfSuspensionIncident + medicalTimeoutInitialSuspensionDelay
                                                + medicalTimeoutSuspensionLengthOfTime)) { // Checks to see if we are
                                                                                           // within the suspension time
                                                                                           // due to suspension incident
                                    reason = "Medical Timeout delay";
                                    type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                                    return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                                } else {
                                    // Don't suspend due to being after the suspension period even after excluding other
                                    // incidents
                                }
                            } else {
                                // Don't suspend due to being within the delay of the suspension incidnet even after
                                // excluding incidents

                            }
                        }
                        break;
                    case CHALLENGE:
                    case CHALLENGER_BALLMARK:
                        if (challengeCeaseReactivationAfterIncident && (challengeInitialSuspensionDelay > -1
                                        || challengeSuspensionLengthOfTime > -1)) { // Check we are ignoring
                                                                                    // incidents and we have a
                                                                                    // delay setup.
                            if ((now - timeOfSuspensionIncident) >= challengeInitialSuspensionDelay) { // Checks if
                                                                                                       // we have
                                                                                                       // pass the
                                                                                                       // delayed
                                                                                                       // time
                                                                                                       // between
                                                                                                       // suspension
                                                                                                       // incident
                                                                                                       // and the
                                                                                                       // initial
                                                                                                       // delay
                                                                                                       // time set
                                                                                                       // by
                                                                                                       // trading
                                                                                                       // rules
                                if (now < (timeOfSuspensionIncident + challengeInitialSuspensionDelay
                                                + challengeSuspensionLengthOfTime)) { // Checks to see if we are
                                                                                      // within the suspension time
                                                                                      // due to suspension incident
                                    reason = "Challenge delay";
                                    type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                                    return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                                } else {
                                    // Don't suspend due to being after the suspension period even after excluding other
                                    // incidents
                                }
                            } else {
                                // Don't suspend due to being within the delay of the suspension incidnet even after
                                // excluding incidents

                            }
                        }
                        break;
                    case RAIN:
                        if (rainCeaseReactivationAfterIncident
                                        && (rainInitialSuspensionDelay > -1 || rainSuspensionLengthOfTime > -1)) { // Check
                                                                                                                   // we
                                                                                                                   // are
                                                                                                                   // ignoring
                                                                                                                   // incidents
                                                                                                                   // and
                                                                                                                   // we
                                                                                                                   // have
                                                                                                                   // a
                                                                                                                   // delay
                                                                                                                   // setup.
                            if ((now - timeOfSuspensionIncident) >= rainInitialSuspensionDelay) { // Checks if
                                                                                                  // we have
                                                                                                  // pass the
                                                                                                  // delayed
                                                                                                  // time
                                                                                                  // between
                                                                                                  // suspension
                                                                                                  // incident
                                                                                                  // and the
                                                                                                  // initial
                                                                                                  // delay
                                                                                                  // time set
                                                                                                  // by
                                                                                                  // trading
                                                                                                  // rules
                                if (now < (timeOfSuspensionIncident + rainInitialSuspensionDelay
                                                + rainSuspensionLengthOfTime)) { // Checks to see if we are
                                                                                 // within the suspension time
                                                                                 // due to suspension incident
                                    reason = "Rain delay";
                                    type = MonitorFeedTradingRuleSuspensionMethodResultType.SUSPEND_IMMEDIATELY;
                                    return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);
                                } else {
                                    // Don't suspend due to being after the suspension period even after excluding other
                                    // incidents
                                }
                            } else {
                                // Don't suspend due to being within the delay of the suspension incidnet even after
                                // excluding incidents

                            }
                        }
                        break;
                    default:
                        break;
                }
            }
            type = MonitorFeedTradingRuleSuspensionMethodResultType.APPLY_STANDARD_RULES;
            return new MonitorFeedTradingRuleSuspensionMethodResult(type, reason);

        } else
            return null;
    }

}
