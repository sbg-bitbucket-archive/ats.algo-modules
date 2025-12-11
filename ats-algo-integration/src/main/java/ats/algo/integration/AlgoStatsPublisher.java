package ats.algo.integration;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.influxdb.dto.Point;
import org.influxdb.dto.Point.Builder;

import com.google.common.collect.Lists;

import ats.algo.algomanager.AlgoManager;
import ats.algo.algomanager.AlgoManagerStatistics;
import ats.algo.algomanager.AlgoStats;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.timestamp.TimeStamp;
import ats.algo.genericsupportfunctions.GCMath;
import ats.core.AtsBean;
import ats.influxdb.TimeSeriesPublisher;

public class AlgoStatsPublisher extends AtsBean {
    private String influxUrl;
    private TimeSeriesPublisher influxPublisher;
    private boolean enabled = true;
    private String influxUser;
    private String influxPassword;
    private String influxDbName = "atsMetrics";
    private AlgoManagerStatistics lastStats;
    private boolean failedProcessLoad;
    private List<TimeStamp> timestampsPending = Lists.newArrayList();

    public void setInfluxPublisher(TimeSeriesPublisher influxPublisher) {
        this.influxPublisher = influxPublisher;
    }

    public void setInfluxDbName(String influxDbName) {
        this.influxDbName = influxDbName;
    }

    public void setInfluxPassword(String influxPassword) {
        this.influxPassword = influxPassword;
    }

    public void setInfluxUser(String influxUser) {
        this.influxUser = influxUser;
    }

    public void setInfluxUrl(String influxUrl) {
        this.influxUrl = influxUrl;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void init() {
        if (enabled) {
            influxPublisher = new TimeSeriesPublisher();
            influxPublisher.setDbName(influxDbName);
            influxPublisher.setUrl(influxUrl);
            influxPublisher.setUsername(influxUser);
            influxPublisher.setPassword(influxPassword);
            influxPublisher.setEnabled(true);
            influxPublisher.init();
        }
    }

    public void pendPublishing(TimeStamp timeStamp) {
        if (!enabled || null == timeStamp) {
            return;
        }
        synchronized (influxPublisher) {
            timestampsPending.add(timeStamp);
            debug("%s TimeStamps pending publishing", timestampsPending.size());
        }
    }

    public void publishTimings() {
        if (!enabled) {
            return;
        }

        List<TimeStamp> timestampsToPublish;

        synchronized (influxPublisher) {
            if (timestampsPending.isEmpty()) {
                return;
            }
            timestampsToPublish = Lists.newArrayList(timestampsPending);
            timestampsPending.clear();
        }
        debug("Publishing %s TimeStamps ", timestampsToPublish.size());
        timestampsToPublish.forEach(ts -> publishTimestamp(ts));
    }

    public void addPoint(Point point) {
        if (enabled) {
            influxPublisher.addPoint(point);
        } else {
            debug("Not enabled to publish %s", point);
        }
    }

    public String getProcessNodeId() {
        return System.getProperty("cluster.processNodeId", "Blah");
    }

    public void publishStats(AlgoManager algoManager, AlgoManagerStatistics stats) {
        if (enabled) {
            if (lastStats == null || !lastStats.equals(stats)) {
                lastStats = stats;
                Builder builder = Point.measurement("algoMgrStats").tag("srv", getProcessNodeId());

                builder.addField("pendingTimerQueueTasks", algoManager.getAlgoMgrQueueSize());
                builder.addField("activeEvents", stats.getnActiveEvents());
                builder.addField("eventsCreated", stats.getnEventsCreated());

                AlgoStats summaryStats = lastStats.getSummaryStats();
                builder.addField("avgParamFinds", summaryStats.getAverageParamFinds());
                builder.addField("avgPriceCalcs", summaryStats.getAveragePriceCalcs());
                builder.addField("avgPriceCalcs", summaryStats.getCountFatalPriceCalcErrors());
                builder.addField("fatalParamFinds", summaryStats.getCountFatalParamFindErrors());
                builder.addField("fatalPriceCalcs", summaryStats.getCountFatalPriceCalcErrors());
                builder.addField("freeMemMb", Runtime.getRuntime().freeMemory() / 1048576);
                builder.addField("systemLoad", ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());

                if (!failedProcessLoad) {
                    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
                    try {
                        Method method = operatingSystemMXBean.getClass().getMethod("getProcessCpuLoad");
                        method.setAccessible(true);
                        Number value = (Number) method.invoke(operatingSystemMXBean);
                        builder.addField("processLoad", GCMath.round(value.doubleValue(), 4));
                    } catch (Exception e) {
                        failedProcessLoad = true;
                    }
                }

                int runnableThreadCount = 0;
                int blockedThreadCount = 0;
                ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
                for (Thread t : Thread.getAllStackTraces().keySet()) {
                    if (t.getThreadGroup() == threadGroup) {
                        if (t.getState() == Thread.State.RUNNABLE) {
                            ++runnableThreadCount;
                        } else if (t.getState() == Thread.State.BLOCKED) {
                            ++blockedThreadCount;
                        }
                    }
                }
                builder.addField("threadCount", runnableThreadCount);
                builder.addField("blockedThreads", blockedThreadCount);

                influxPublisher.addPoint(builder.build());

                Map<SupportedSportType, AlgoStats> summaryStatsBySport = stats.getSummaryStatsBySport();
                for (Entry<SupportedSportType, AlgoStats> entry : summaryStatsBySport.entrySet()) {
                    publishSportStats(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void publishParamFindStatus(SupportedSportType sport, Long eventId, ParamFindResults paramFindResults,
                    long elapsedTimeMs) {
        if (enabled) {
            String sportName = makeSportNameNicer(sport);
            Builder builder = Point.measurement("algoMgr" + sportName + "ParamFinds").tag("srv", getProcessNodeId());
            builder.tag("eventId", String.valueOf(eventId));
            builder.addField("durationSecs", elapsedTimeMs / 1000);
            builder.addField("success", paramFindResults.isSuccess());
            builder.addField("iterations", paramFindResults.getnIterations());
            influxPublisher.addPoint(builder.build());
        }
    }

    protected void publishSportStats(SupportedSportType key, AlgoStats stats) {
        String sportName = makeSportNameNicer(key);
        Builder builder = Point.measurement("algoMgr" + sportName + "Stats").tag("srv", getProcessNodeId());

        builder.addField("averageParamFinds", stats.getAverageParamFinds());
        builder.addField("averagePriceCalcs", stats.getAveragePriceCalcs());
        builder.addField("countParamFinds", stats.getCountParamFinds());
        builder.addField("countPriceCalcs", stats.getCountPriceCalcs());
        builder.addField("maxParamFinds", stats.getMaxParamFinds());
        builder.addField("maxPriceCalcs", stats.getMaxPriceCalcs());

        builder.addField("fatalParamFinds", stats.getCountFatalParamFindErrors());
        builder.addField("fatalPriceCalcs", stats.getCountFatalPriceCalcErrors());

        influxPublisher.addPoint(builder.build());
    }

    protected String makeSportNameNicer(SupportedSportType key) {
        String sportName = key.name();
        return sportName.charAt(0) + sportName.substring(1).toLowerCase().replace("_", "");
    }

    protected void publishTimestamp(TimeStamp timeStamp) {
        try {
            Builder builder = Point.measurement("algoMgrTimeStamp").tag("srv", getProcessNodeId());

            builder.addField("eventId", timeStamp.getEventId());
            builder.addField("incidentReqId", timeStamp.getRequestId());

            builder.addField("incidentCreated", timeStamp.getTimeMatchIncidentCreated());
            // builder.addField("incidentReqId",
            // timeStamp.getTimeMarketServerReceivedUpdatedMarkets());
            builder.addField("incidentReceivedByAlgoManager", timeStamp.getTimeMatchIncidentReceivedByAlgoManager());
            builder.addField("priceCalcRequestIssued", timeStamp.getTimePriceCalcRequestIssued());
            builder.addField("receivedByCalculationServer",
                            timeStamp.getTimePriceCalcRequestReceivedByCalculationServer());
            builder.addField("issuedByCalculationServer", timeStamp.getTimePriceCalcResponseIssedByCalculationServer());
            builder.addField("priceCalcResponseInAlgoManager", timeStamp.getTimePriceCalcResponseReceived());
            builder.addField("marketsPublishedByAlgoManager", timeStamp.getTimeUpdatedMarketsPublishedByAlgoManager());

            long duration = timeStamp.getTimeUpdatedMarketsPublishedByAlgoManager()
                            - timeStamp.getTimeMatchIncidentCreated();
            Point point = builder.build();
            debug("%s seconds to process event %s incident %s %s", duration / 1000l, timeStamp.getEventId(),
                            timeStamp.getRequestId(), point);

            // influxPublisher.addPoint(point);
        } catch (Exception ex) {
            error("Problem publishing %s", ex, timeStamp);
        }
    }
}
