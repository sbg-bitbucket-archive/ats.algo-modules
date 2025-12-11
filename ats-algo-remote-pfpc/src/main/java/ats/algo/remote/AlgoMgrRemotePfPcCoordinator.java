package ats.algo.remote;

import java.io.Serializable;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.jms.core.JmsTemplate;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

import ats.algo.algomanager.AlgoCalculator;
import ats.algo.algomanager.AlgoParamFinder;
import ats.algo.core.baseclasses.AlgoMatchParams;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.markets.Markets;
import ats.algo.core.request.AbstractAlgoResponse;
import ats.algo.core.request.ClassifiedParamFindRequest;
import ats.algo.core.request.ClassifiedPriceCalcRequest;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.core.AtsBean;

public class AlgoMgrRemotePfPcCoordinator extends AtsBean implements MessageListener {

    private JmsTemplate paramFindingResponseTemplate;
    private JmsTemplate priceCalcResponseTemplate;
    private int maxParamFindQueues = 0;
    private int maxPriceCalcQueues = 0;
    private int maxClassifiedParamFindThreads = 0;
    private int maxClassifiedPriceCalcThreads = 0;
    private Semaphore classifiedParamFindThrottle;
    private Semaphore classifiedPriceCalcThrottle;
    private Map<Integer, ParamFindContext> paramFindExecutors = Maps.newConcurrentMap();
    private Map<String, Destination> replyToMap = Maps.newConcurrentMap();
    private Map<Integer, PriceCalculationContext> priceCalcExecutors = Maps.newHashMap();
    private AtomicInteger queuedParamFinds = new AtomicInteger();
    private AtomicInteger queuedPriceCalcs = new AtomicInteger();
    private ExecutorService paramFindingExecutorService;
    private ExecutorService priceCalcExecutorService;
    private ExecutorService classifiedParamFindingExecutorService;
    private ExecutorService classifiedPriceCalcExecutorService;
    private ScheduledExecutorService paramFindingExecutorMonitor;
    private long maxToleratedParamFindInMins = 5;
    private long warningParamFindDurationInSecs = 150;
    private String serverId;

    public void setMaxClassifiedParamFindThreads(int maxClassifiedParamFindThreads) {
        this.maxClassifiedParamFindThreads = maxClassifiedParamFindThreads;
    }

    public void setMaxClassifiedPriceCalcThreads(int maxClassifiedPriceCalcThreads) {
        this.maxClassifiedPriceCalcThreads = maxClassifiedPriceCalcThreads;
    }

    public void setMaxToleratedParamFindInMins(long maxToleratedParamFindInMins) {
        this.maxToleratedParamFindInMins = maxToleratedParamFindInMins;
    }

    public void setWarningParamFindDurationInSecs(long warningParamFindDurationInSecs) {
        this.warningParamFindDurationInSecs = warningParamFindDurationInSecs;
    }

    public void setParamFindingResponseTemplate(JmsTemplate paramFindingTemplate) {
        this.paramFindingResponseTemplate = paramFindingTemplate;
    }

    public void setPriceCalcResponseTemplate(JmsTemplate priceCalcTemplate) {
        this.priceCalcResponseTemplate = priceCalcTemplate;
    }

    public void setMaxParamFindQueues(int maxParamFindQueues) {
        this.maxParamFindQueues = maxParamFindQueues;
    }

    public void setMaxPriceCalcQueues(int maxPriceCalcQueues) {
        this.maxPriceCalcQueues = maxPriceCalcQueues;
    }

    public void init() throws Exception {
        paramFindingExecutorMonitor = Executors.newSingleThreadScheduledExecutor();
        paramFindingExecutorMonitor.scheduleAtFixedRate(new ParamFindingMonitoringTask(), 180, 20, TimeUnit.SECONDS);
        serverId = System.getProperty("cluster.processNodeId");
        if (serverId == null)
            serverId = "Unknown";
    }

    @Override
    public void onMessage(Message jmsMessage) {
        try {
            ObjectMessage om = (ObjectMessage) jmsMessage;
            Serializable pojo = om.getObject();
            Destination jmsReplyTo = om.getJMSReplyTo();
            if (pojo instanceof PriceCalcRequest) {
                PriceCalcRequest pcr = (PriceCalcRequest) pojo;
                if (jmsReplyTo != null) {
                    replyToMap.put(pcr.getUniqueRequestId(), jmsReplyTo);
                }
                processRegularPriceCalcRequest(pcr);
            } else if (pojo instanceof ParamFindRequest) {
                ParamFindRequest pfr = (ParamFindRequest) pojo;
                if (jmsReplyTo != null) {
                    replyToMap.put(pfr.getUniqueRequestId(), jmsReplyTo);
                }
                processRegularParamFindRequest(pfr);
            } else if (pojo instanceof ClassifiedPriceCalcRequest) {
                ClassifiedPriceCalcRequest enhancedReq = (ClassifiedPriceCalcRequest) pojo;
                if (jmsReplyTo != null) {
                    replyToMap.put(enhancedReq.getUniqueRequestId(), jmsReplyTo);
                }
                processClassifiedPriceCalcRequest(enhancedReq);
            } else if (pojo instanceof ClassifiedParamFindRequest) {
                ClassifiedParamFindRequest enhancedReq = (ClassifiedParamFindRequest) pojo;
                if (jmsReplyTo != null) {
                    replyToMap.put(enhancedReq.getUniqueRequestId(), jmsReplyTo);
                }
                processClassifiedParamFindRequest(enhancedReq);
            } else {
                warn("Unrecognised request type %s", pojo);
            }
        } catch (Throwable e) {
            error("Problem handling request message %s", e, jmsMessage);
        }
    }

    private void processRegularParamFindRequest(ParamFindRequest pfr) {
        Integer executorIndex =
                        maxParamFindQueues > 0 ? (int) pfr.getMatchParams().getEventId() % maxParamFindQueues : 0;
        ParamFindContext paramFindContext;
        synchronized (paramFindExecutors) {
            paramFindContext = paramFindExecutors.get(executorIndex);
            if (paramFindContext == null) {
                if (paramFindingExecutorService == null && maxParamFindQueues > 0) {
                    paramFindingExecutorService = Executors.newWorkStealingPool(maxParamFindQueues);
                }
                paramFindExecutors.put(executorIndex, paramFindContext = new ParamFindContext());
            }
        }
        paramFindContext.submit(pfr);
    }

    private void processRegularPriceCalcRequest(PriceCalcRequest pcr) {
        Integer executorIndex =
                        maxPriceCalcQueues > 0 ? (int) pcr.getMatchParams().getEventId() % maxPriceCalcQueues : 0;
        PriceCalculationContext priceCalculationContext;
        synchronized (priceCalcExecutors) {
            priceCalculationContext = priceCalcExecutors.get(executorIndex);
            if (priceCalculationContext == null) {
                if (priceCalcExecutorService == null && maxPriceCalcQueues > 0) {
                    priceCalcExecutorService = Executors.newWorkStealingPool(maxPriceCalcQueues);
                }
                priceCalcExecutors.put(executorIndex, priceCalculationContext = new PriceCalculationContext());
            }
        }
        priceCalculationContext.submit(pcr);
    }

    private void processClassifiedParamFindRequest(ClassifiedParamFindRequest enhancedReq) {
        if (maxClassifiedParamFindThreads > 1) {
            if (null == classifiedParamFindingExecutorService) {
                classifiedParamFindingExecutorService = Executors.newFixedThreadPool(maxClassifiedParamFindThreads);
                classifiedParamFindThrottle = new Semaphore(maxClassifiedParamFindThreads);
            }
            try {
                classifiedParamFindThrottle.acquire();
            } catch (InterruptedException e) {
                error(e);
                Thread.currentThread().interrupt();
            }
            classifiedParamFindingExecutorService.execute(() -> executeClassifiedParamFind(enhancedReq));
            return;
        }
        executeClassifiedParamFind(enhancedReq);
    }

    private void executeClassifiedParamFind(ClassifiedParamFindRequest enhancedReq) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String uniqueRequestId = enhancedReq.getUniqueRequestId();
        try {
            ParamFindResponse response = enhancedReq.executeCalculation();
            sendReply(paramFindingResponseTemplate, uniqueRequestId, response);
        } catch (Exception ex) {
            sendReply(paramFindingResponseTemplate, uniqueRequestId,
                            ParamFindResponse.generateFatalErrorResponse(uniqueRequestId, ex.getMessage()));
        } finally {
            if (classifiedParamFindThrottle != null) {
                classifiedParamFindThrottle.release();
            }
        }
        debug("Took %s seconds to handle Classified PFR %s for event %s", stopwatch.elapsed(TimeUnit.SECONDS),
                        uniqueRequestId, enhancedReq.getEventId());
    }

    private void processClassifiedPriceCalcRequest(ClassifiedPriceCalcRequest enhancedReq) {
        if (maxClassifiedPriceCalcThreads > 1) {
            if (null == classifiedPriceCalcExecutorService) {
                classifiedPriceCalcExecutorService = Executors.newFixedThreadPool(maxClassifiedPriceCalcThreads);
                classifiedPriceCalcThrottle = new Semaphore(maxClassifiedPriceCalcThreads);
            }
            try {
                classifiedPriceCalcThrottle.acquire();
            } catch (InterruptedException e) {
                error(e);
                Thread.currentThread().interrupt();
            }
            classifiedPriceCalcExecutorService.execute(() -> executeClassifiedPriceCalc(enhancedReq));
            return;
        }
        executeClassifiedPriceCalc(enhancedReq);
    }

    private void sendReply(JmsTemplate template, String reqId, AbstractAlgoResponse reply) {
        Destination replyTo = replyToMap.remove(reqId);
        reply.setServerId(serverId);
        if (replyTo != null) {
            debug("Sending %s for req %s to %s", reply.getClass().getSimpleName(), reqId, replyTo);
            template.convertAndSend(replyTo, reply, message -> {
                message.setJMSCorrelationID(reqId);
                return message;
            });
        } else {
            template.convertAndSend(reply);
        }
    }

    private void executeClassifiedPriceCalc(ClassifiedPriceCalcRequest enhancedReq) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        String uniqueRequestId = enhancedReq.getUniqueRequestId();
        try {
            PriceCalcResponse response = enhancedReq.executeCalculation();
            sendReply(priceCalcResponseTemplate, uniqueRequestId, response);
        } catch (Exception ex) {
            sendReply(priceCalcResponseTemplate, uniqueRequestId,
                            PriceCalcResponse.generateFatalErrorResponse(uniqueRequestId, ex.getMessage()));
        } finally {
            if (classifiedPriceCalcThrottle != null) {
                classifiedPriceCalcThrottle.release();
            }
        }
        debug("Took %s seconds to handle Classified PCR %s for event %s", stopwatch.elapsed(TimeUnit.SECONDS),
                        uniqueRequestId, enhancedReq.getEventId());
    }

    private final class ParamFindingMonitoringTask implements Runnable {

        @Override
        public void run() {
            try {
                if (paramFindExecutors.isEmpty()) {
                    return;
                }
                for (ParamFindContext paramFindContext : paramFindExecutors.values()) {
                    final long elapsedSeconds = paramFindContext.getCurrentCalcElapsedTime(TimeUnit.SECONDS);
                    final long elapsedMinutes = elapsedSeconds / 60;
                    ParamFindRequest currentRequest = paramFindContext.getCurrentRequest();
                    if (elapsedMinutes >= maxToleratedParamFindInMins) {
                        error(stackTraces());
                        for (ParamFindContext aContext : paramFindExecutors.values()) {
                            if (aContext.getCurrentCalcElapsedTime(TimeUnit.MILLISECONDS) > 0) {
                                sendErrorInParamFind(aContext.getCurrentRequest(), new TimeoutException());
                            }
                        }
                        error("%s has been going on for %s minutes - shutting down", currentRequest, elapsedMinutes);
                        Thread.sleep(1000);
                        Runtime.getRuntime().halt(-1);
                    } else if (elapsedSeconds >= warningParamFindDurationInSecs) {
                        warn("%s ParamFind has beeing going on for %s seconds", currentRequest, elapsedSeconds);
                    }
                }
            } catch (Exception ex) {
                error(ex);
            }
        }
    }

    private String stackTraces() {
        StringBuilder sb = new StringBuilder();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 100);
        for (ThreadInfo threadInfo : threadInfos) {
            sb.append('"').append(threadInfo.getThreadName()).append("\" ");
            State threadState = threadInfo.getThreadState();
            sb.append("\n java.lang.Thread.State: ");
            sb.append(threadState);
            StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                sb.append("\n        at ").append(stackTraceElement);
            }
            sb.append("\n\n");
        }
        return sb.toString();
    }

    private void sendErrorInParamFind(ParamFindRequest request, Throwable e) {
        if (request != null) {
            sendReply(paramFindingResponseTemplate, request.getUniqueRequestId(),
                            ParamFindResponse.generateFatalErrorResponse(request.getUniqueRequestId(), e.getMessage()));
        }
    }

    private final class ParamFindContext {
        AlgoParamFinder algoParamFinder = createParamFinder();
        volatile Stopwatch stopwatch;
        ParamFindRequest currentRequest;

        public void submit(ParamFindRequest req) {
            debug("%s pending param finds", queuedParamFinds.incrementAndGet());
            if (maxParamFindQueues > 0) {
                paramFindingExecutorService.submit(() -> handleParamFindRequest(req));
            } else {
                handleParamFindRequest(req);
            }
        }

        public long getCurrentCalcElapsedTime(TimeUnit tu) {
            if (stopwatch != null && stopwatch.isRunning()) {
                return stopwatch.elapsed(tu);
            }
            return 0;
        }

        public ParamFindRequest getCurrentRequest() {
            return currentRequest;
        }

        public void handleParamFindRequest(ParamFindRequest request) {
            int pending = queuedParamFinds.decrementAndGet();

            debug("PFR's still queued = %s. About to calc event %s PF", pending, request.getMatchParams().getEventId());

            stopwatch = Stopwatch.createStarted();

            ParamFindResults paramFindResults;
            AlgoMatchParams matchParams;

            synchronized (algoParamFinder) {
                try {
                    currentRequest = request;
                    algoParamFinder.setParamFindParams(request);
                    algoParamFinder.call();
                    stopwatch.stop();
                    currentRequest = null;
                } catch (Throwable e) {
                    stopwatch.stop();
                    currentRequest = null;
                    error(e);
                    closeParamFinder();
                    algoParamFinder = createParamFinder();
                    sendErrorInParamFind(request, e);
                    return;
                }

                paramFindResults = algoParamFinder.getParamFindResults();
                matchParams = algoParamFinder.getCalculatedMatchParams();
            }

            String uniqueRequestId = request.getUniqueRequestId();
            ParamFindResponse pfr = new ParamFindResponse(uniqueRequestId, paramFindResults,
                            matchParams.generateGenericMatchParams());
            pfr.setFatalError(algoParamFinder.isFatalError());
            pfr.setFatalErrorCause(algoParamFinder.getFatalErrorCause());
            sendReply(paramFindingResponseTemplate, uniqueRequestId, pfr);

            debug("Took %s seconds to handle PFR %s for event %s, %s remaining PFR tasks",
                            stopwatch.elapsed(TimeUnit.SECONDS), uniqueRequestId, request.getMatchParams().getEventId(),
                            queuedParamFinds.get());
        }

        private void closeParamFinder() {
            try {
                algoParamFinder.close();
            } catch (Exception ce) {
                error("Problem closing param finder after excaption caught", ce);
            }
        }

        private AlgoParamFinder createParamFinder() {
            return new AlgoParamFinder();
        }
    }

    private final class PriceCalculationContext {
        AlgoCalculator algoCalculator = createPriceCalculator();

        public void submit(PriceCalcRequest pcr) {
            long pending = queuedPriceCalcs.incrementAndGet();
            debug("%s pending price calcs", pending);
            if (maxPriceCalcQueues > 0) {
                priceCalcExecutorService.submit(() -> handlePriceCalcRequest(pcr));
            } else {
                handlePriceCalcRequest(pcr);
            }
        }

        public void handlePriceCalcRequest(PriceCalcRequest request) {
            long startTime = System.currentTimeMillis();
            queuedPriceCalcs.decrementAndGet();

            debug("Processing %s", request);

            Stopwatch stopwatch = Stopwatch.createStarted();

            PriceCalcResponse response;

            String uniqueRequestId = request.getUniqueRequestId();
            synchronized (algoCalculator) {

                algoCalculator.setCalculationParams(request);
                try {
                    algoCalculator.call();
                } catch (Throwable e) {
                    error(e);
                    closePriceCalculator();
                    algoCalculator = createPriceCalculator();
                    response = PriceCalcResponse.generateFatalErrorResponse(uniqueRequestId, e.getMessage());
                    sendReply(priceCalcResponseTemplate, uniqueRequestId, response);
                    return;
                }
                long endTime = System.currentTimeMillis();
                Markets markets = algoCalculator.getCalculatedMarkets();

                if (markets != null && markets.size() > 0) {
                    debug("%s markets generated for match %s", markets.size(), request.getMatchParams().getEventId());
                } else {
                    warn("No markets generated for match %s", request.getMatchParams().getEventId());
                }

                response = new PriceCalcResponse(uniqueRequestId, markets,
                                algoCalculator.getUpdatedMatchParams().generateGenericMatchParams(),
                                algoCalculator.getUpdatedMatchEngineSavedState(), null);
                response.setFatalError(algoCalculator.isFatalError());
                response.setFatalErrorCause(algoCalculator.getFatalErrorCause());
                response.setTimePriceCalcRequestReceived(startTime);
                response.setTimePriceCalcResponseIssued(endTime);
            }

            sendReply(priceCalcResponseTemplate, uniqueRequestId, response);

            debug("Took %s to handle PCR %s for event %s, %s remaining PCR tasks", stopwatch.stop(),
                            request.getUniqueRequestId(), request.getMatchParams().getEventId(),
                            queuedPriceCalcs.get());
        }

        private AlgoCalculator createPriceCalculator() {
            return new AlgoCalculator();
        }

        private void closePriceCalculator() {
            try {
                algoCalculator.close();
            } catch (Exception ce) {
                error("Problem closing price calculator after excaption caught", ce);
            }
        }
    }
}
