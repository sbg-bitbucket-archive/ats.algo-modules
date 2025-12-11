package ats.algo.algomanager;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;
import javax.jms.BytesMessage;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MessageConverter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalCause;
import com.google.common.collect.Maps;

import ats.algo.algomanager.jms.JMSFactoryMethods;
import ats.algo.core.common.SupportedSportType;
import ats.algo.core.comparetomarket.ParamFindResults;
import ats.algo.core.request.AbstractAlgoRequest;
import ats.algo.core.request.AbstractAlgoResponse;
import ats.algo.core.request.ClassifiedParamFindRequest;
import ats.algo.core.request.ClassifiedPriceCalcRequest;
import ats.algo.core.request.ParamFindRequest;
import ats.algo.core.request.ParamFindResponse;
import ats.algo.core.request.PriceCalcRequest;
import ats.algo.core.request.PriceCalcResponse;
import ats.algo.genericsupportfunctions.JsonSerializer;
import ats.algo.genericsupportfunctions.MQHelper;

public class JmsAlgoManagerConfiguration extends AlgoManagerConfiguration
                implements MessageListener, JMSFactoryMethods {

    private JmsTemplate paramFindingTemplate;
    private JmsTemplate priceCalcTemplate;
    private JmsTemplate externalParamFindingTemplate;
    private JmsTemplate externalPriceCalcTemplate;
    private Map<SupportedSportType, ActiveMQQueue> externalPriceCalcQueues = Maps.newConcurrentMap();
    private Map<SupportedSportType, ActiveMQQueue> externalParamFindQueues = Maps.newConcurrentMap();
    private long paramFindRequestsCacheTimeMins = 25;
    private long priceCalcRequestsCacheTimeMins = 25;
    boolean compressExternalRequests;
    private Queue privatePCReplyQueue;
    private Queue privatePFReplyQueue;
    private int maxConcurrentReplyThreads = 5;
    private boolean doNotUseReplyQueuesForExternalRequests;
    private ScheduledExecutorService requestTimeoutScheduler = Executors.newSingleThreadScheduledExecutor();
    private AtomicLong pendingPCRequestCount = new AtomicLong();
    private AtomicLong pendingPFRequestCount = new AtomicLong();
    private double priceCalcTimeoutSecs = 60;
    private double priceCalcTimeoutMultiplier = 1;
    private double paramFindTimeoutSecs = 180;
    private double paramFindTimeoutMultiplier = 3;

    class WatchWrapper {
        AbstractAlgoRequest req;
        ScheduledFuture<?> schedule;
        long eventId;

        public WatchWrapper(long eventId, AbstractAlgoRequest req, ScheduledFuture<?> schedule) {
            super();
            this.eventId = eventId;
            this.req = req;
            this.schedule = schedule;
        }

        public long getEventId() {
            return eventId;
        }

        public String getUniqueRequestId() {
            return req.getUniqueRequestId();
        }

        public void cancel() {
            schedule.cancel(false);
        }

        public boolean isDone() {
            return schedule.isDone();
        }
    }

    class PCWatchWrapper extends WatchWrapper {

        public PCWatchWrapper(PriceCalcRequest pcr, ScheduledFuture<?> schedule) {
            super(pcr.getEventId(), pcr, schedule);
        }

        @Override
        public int hashCode() {
            return getUniqueRequestId().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PCWatchWrapper other = (PCWatchWrapper) obj;
            return getUniqueRequestId().equals(other.getUniqueRequestId());
        }
    }

    Map<String, PCWatchWrapper> inflightPCSchedules = Maps.newConcurrentMap();

    class PFWatchWrapper extends WatchWrapper {

        public PFWatchWrapper(ParamFindRequest pfr, ScheduledFuture<?> schedule) {
            super(pfr.getEventId(), pfr, schedule);
        }

        @Override
        public int hashCode() {
            return getUniqueRequestId().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            PFWatchWrapper other = (PFWatchWrapper) obj;
            return getUniqueRequestId().equals(other.getUniqueRequestId());
        }
    }

    Map<String, PFWatchWrapper> inflightPFSchedules = Maps.newConcurrentMap();

    private Cache<String, ParamFindRequest> paramFindRequestsCache = createParamFindCache();

    public Cache<String, ParamFindRequest> createParamFindCache() {
        return CacheBuilder.newBuilder().expireAfterWrite(paramFindRequestsCacheTimeMins, TimeUnit.MINUTES)
                        .removalListener(notification -> {
                            if (notification.getCause() == RemovalCause.EXPIRED) {
                                ParamFindRequest request = (ParamFindRequest) notification.getValue();
                                warn("PF response timeout %s", request);
                                algoHandleParamFindError.handle(request.getEventId(), request.getUniqueRequestId(),
                                                "Waited too long for response to param find request");
                            }
                        }).build();
    }

    private Cache<String, PriceCalcRequest> priceCalcRequestsCache = createPriceCalcCache();

    public Cache<String, PriceCalcRequest> createPriceCalcCache() {
        return CacheBuilder.newBuilder().expireAfterWrite(priceCalcRequestsCacheTimeMins, TimeUnit.MINUTES)
                        .removalListener(notification -> {
                            if (notification.getCause() == RemovalCause.EXPIRED) {
                                PriceCalcRequest request = (PriceCalcRequest) notification.getValue();
                                warn("PCR response timeout %s", request);
                                algoHandlePriceCalcError.handle(request.getEventId(), request.getUniqueRequestId(),
                                                "Waited too long for response to price calc request");
                            }
                        }).build();
    }

    private LastRequestTimeForReponseTimeCache paramFindLastRequestResponseTimeCache =
                    new LastRequestTimeForReponseTimeCache();
    private LastRequestTimeForReponseTimeCache priceCalcLastRequestResponseTimeCache =
                    new LastRequestTimeForReponseTimeCache();

    private class LastRequestTimeForReponseTimeCache {

        private Cache<Long, Long> requestResponseTimeCache =
                        CacheBuilder.newBuilder().expireAfterWrite(3, TimeUnit.HOURS).build();

        public boolean isResponseInExpectedSequence(Long eventId, AbstractAlgoResponse response, String uniqueRequestId,
                        long requestTimeForResponse) {
            Long requestTimeOfLastResponse = requestResponseTimeCache.getIfPresent(eventId);
            if (requestTimeOfLastResponse != null && requestTimeOfLastResponse > requestTimeForResponse) {
                warn("*Dropping* slow/lagged %s for event %s req %s sent at %s", response.getClass().getSimpleName(),
                                eventId, uniqueRequestId, new Date(requestTimeForResponse));
                return false;
            }
            requestResponseTimeCache.put(eventId, requestTimeForResponse);
            debug("Match %s %s %s took %s secs", eventId, response.getClass().getSimpleName(), uniqueRequestId,
                            (System.currentTimeMillis() - requestTimeForResponse) / 1000);
            return true;
        }
    }

    public JmsAlgoManagerConfiguration() {
        SupportedSportsInitialisation.init();
    }

    /**
     * 
     * @param brokerUrl
     * @param compressExternalRequests
     */
    public JmsAlgoManagerConfiguration(String brokerUrl, boolean compressExternalRequests) {
        this();
        this.compressExternalRequests = compressExternalRequests;
        this.withRemotePcFcTransport(brokerUrl);
        init();
    }

    public double getPriceCalcTimeoutSecs() {
        return priceCalcTimeoutSecs;
    }

    public void setPriceCalcTimeoutSecs(double priceCalcTimeoutSecs) {
        this.priceCalcTimeoutSecs = priceCalcTimeoutSecs;
    }

    public double getPriceCalcTimeoutMultiplier() {
        return priceCalcTimeoutMultiplier;
    }

    public void setPriceCalcTimeoutMultiplier(double priceCalcTimeoutMultiplier) {
        this.priceCalcTimeoutMultiplier = priceCalcTimeoutMultiplier;
    }

    public double getParamFindTimeoutSecs() {
        return paramFindTimeoutSecs;
    }

    public void setParamFindTimeoutSecs(double paramFindTimeoutSecs) {
        this.paramFindTimeoutSecs = paramFindTimeoutSecs;
    }

    public double getParamFindTimeoutMultiplier() {
        return paramFindTimeoutMultiplier;
    }

    public void setParamFindTimeoutMultiplier(double paramFindTimeoutMultiplier) {
        this.paramFindTimeoutMultiplier = paramFindTimeoutMultiplier;
    }

    public void setPriceCalcRequestsCacheTimeMins(long priceCalcRequestsCacheTimeMins) {
        this.priceCalcRequestsCacheTimeMins = priceCalcRequestsCacheTimeMins;
    }

    public void setParamFindRequestsCacheTimeMins(long paramFindRequestsCacheTimeMins) {
        this.paramFindRequestsCacheTimeMins = paramFindRequestsCacheTimeMins;
    }

    public void setMaxConcurrentReplyThreads(int maxConcurrentReplyThreads) {
        this.maxConcurrentReplyThreads = maxConcurrentReplyThreads;
    }

    public void setCompressExternalRequests(boolean compressExternalRequests) {
        this.compressExternalRequests = compressExternalRequests;
    }

    public void init() {
        paramFindRequestsCache = createParamFindCache();
        priceCalcRequestsCache = createPriceCalcCache();
        initPrivateReplyQueues();
        doNotUseReplyQueuesForExternalRequests = false;
        /*
         * temporary work-around for problem BS are having with reply queues
         */
        String str = System.getProperty("algomgr.jms.doNotUseReplyQueuesForExternalRequests");
        if (str != null && str.toLowerCase().equals("true")) {
            doNotUseReplyQueuesForExternalRequests = true;
        }
    }

    protected void initPrivateReplyQueues() {

        String srvId = System.getProperty("cluster.processNodeId");
        if (srvId == null) {
            warn("cluster.processNodeId not defined for this JVM.  Using UUID as queue name instead");
            srvId = UUID.randomUUID().toString();
        }
        prepareServerBasedReplyComponents(srvId);
    }

    private void prepareServerBasedReplyComponents(String srvId) {
        info("Going private !");

        createPrivateReplyToQueues(srvId);

        startMessageListenerOnPrivateReplyQueue(priceCalcTemplate.getConnectionFactory(), privatePCReplyQueue);
        startMessageListenerOnPrivateReplyQueue(paramFindingTemplate.getConnectionFactory(), privatePFReplyQueue);
    }

    protected void createPrivateReplyToQueues(String srvId) {
        if (privatePCReplyQueue == null) {
            String privatePCReplyQueueName = "algoMgr" + srvId + "-PC-replies";
            info("My private PC reply queue name is %s", privatePCReplyQueueName);
            privatePCReplyQueue = createQueue(privatePCReplyQueueName);
        }
        if (privatePFReplyQueue == null) {
            String privatePFReplyQueueName = "algoMgr" + srvId + "-PF-replies";
            privatePFReplyQueue = createQueue(privatePFReplyQueueName);
            info("My private PF reply queue name is %s", privatePFReplyQueueName);
        }
    }

    public JMSFactoryMethods withRemotePcFcTransport(String brokerUrl) {
        CachingConnectionFactory cachingFac = createConnectionFactory(brokerUrl);

        priceCalcTemplate = createJmsQueueTemplate(cachingFac);
        priceCalcTemplate.setDefaultDestination(new ActiveMQQueue("algoMgrPriceCalcReqs"));

        paramFindingTemplate = createJmsQueueTemplate(cachingFac);
        paramFindingTemplate.setDefaultDestination(new ActiveMQQueue("algoMgrParamFindReqs"));

        startMessageListener(cachingFac, "algoMgrPriceCalcResponse", this);

        startMessageListener(cachingFac, "algoMgrParamFindResponse", this);

        return this;
    }

    /**
     * For programatic creation in demo apps that integrate with 'remote' models.
     * 
     * @param remoteModelAMQBRokerUrl
     */
    public JMSFactoryMethods withRemoteModelTransport(String remoteModelAMQBRokerUrl,
                    boolean compressExternalMessages) {
        CachingConnectionFactory externalBrokerConnectionFactory = createConnectionFactory(remoteModelAMQBRokerUrl);

        externalPriceCalcTemplate = createJmsQueueTemplate(externalBrokerConnectionFactory);
        externalParamFindingTemplate = createJmsQueueTemplate(externalBrokerConnectionFactory);
        if (compressExternalMessages) {
            MessageConverter priceCalcMessageConverter = new ExternalMessageCompressor();
            externalPriceCalcTemplate.setMessageConverter(priceCalcMessageConverter);
            MessageConverter paramFindMessageConverter = new ExternalMessageCompressor();
            externalPriceCalcTemplate.setMessageConverter(paramFindMessageConverter);
        } else {
            /*
             * do nothing - the default message converter will do the job
             */
        }

        startMessageListener(externalBrokerConnectionFactory, "algoMgrPriceCalcResponse", this);

        startMessageListener(externalBrokerConnectionFactory, "algoMgrParamFindResponse", this);

        createPrivateReplyToQueues(System.getProperty("cluster.processNodeId", "UnknownSrv"));

        startMessageListenerOnPrivateReplyQueue(externalBrokerConnectionFactory, privatePCReplyQueue);

        startMessageListenerOnPrivateReplyQueue(externalBrokerConnectionFactory, privatePFReplyQueue);

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ats.algo.algomanager.JMSFactoryMethods#createQueue(java.lang.String)
     */
    @Override
    public ActiveMQQueue createQueue(String name) {
        return new ActiveMQQueue(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ats.algo.algomanager.JMSFactoryMethods#createTopic(java.lang.String)
     */
    @Override
    public ActiveMQTopic createTopic(String name) {
        return new ActiveMQTopic(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ats.algo.algomanager.JMSFactoryMethods#createJmsQueueTemplate(org. springframework.jms.connection.
     * CachingConnectionFactory)
     */
    @Override
    public JmsTemplate createJmsQueueTemplate(CachingConnectionFactory cachingFac) {
        return createJmsTemplate(cachingFac, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ats.algo.algomanager.JMSFactoryMethods#createJmsTopicTemplate(org. springframework.jms.connection.
     * CachingConnectionFactory)
     */
    @Override
    public JmsTemplate createJmsTopicTemplate(CachingConnectionFactory cachingFac) {
        return createJmsTemplate(cachingFac, true);
    }

    protected JmsTemplate createJmsTemplate(CachingConnectionFactory cachingFac, boolean pubSub) {
        JmsTemplate template = new JmsTemplate(cachingFac);
        template.setPubSubDomain(pubSub);
        template.setDeliveryPersistent(false);
        template.setExplicitQosEnabled(true);
        return template;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ats.algo.algomanager.JMSFactoryMethods#startMessageListner(org. springframework.jms.connection.
     * CachingConnectionFactory, java.lang.String, javax.jms.MessageListener)
     */
    @Override
    public void startMessageListener(CachingConnectionFactory cachingFac, String topicNameToListenOn,
                    MessageListener messageListener) {
        listenOnDestination(cachingFac, new ActiveMQTopic(topicNameToListenOn), maxConcurrentReplyThreads);
    }

    private void startMessageListenerOnPrivateReplyQueue(ConnectionFactory connectionFactory, Queue privateReplyQueue) {
        listenOnDestination(connectionFactory, privateReplyQueue, maxConcurrentReplyThreads);
    }

    private void listenOnDestination(ConnectionFactory connectionFactory, Destination destination,
                    int maxConcurrentConsumers) {
        DefaultMessageListenerContainer responseListener = new DefaultMessageListenerContainer();
        responseListener.setConnectionFactory(connectionFactory);
        responseListener.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
        responseListener.setDestination(destination);
        responseListener.setMaxConcurrentConsumers(maxConcurrentConsumers);
        responseListener.setReceiveTimeout(-1);
        responseListener.setMessageListener(this);
        responseListener.initialize();
        responseListener.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ats.algo.algomanager.JMSFactoryMethods#createConnectionFactory(java.lang. String)
     */
    @Override
    public CachingConnectionFactory createConnectionFactory(String remoteModelAMQBRokerUrl) {
        ActiveMQConnectionFactory amqfac = new ActiveMQConnectionFactory(remoteModelAMQBRokerUrl);
        amqfac.setTrustAllPackages(true);
        CachingConnectionFactory cachingFac = new CachingConnectionFactory(amqfac);
        cachingFac.setSessionCacheSize(10);
        cachingFac.setCacheConsumers(false);
        return cachingFac;
    }

    public void setExternalParamFindingTemplate(JmsTemplate externalParamFindingTemplate) {
        this.externalParamFindingTemplate = externalParamFindingTemplate;
    }

    public void setExternalPriceCalcTemplate(JmsTemplate externalPriceCalcTemplate) {
        this.externalPriceCalcTemplate = externalPriceCalcTemplate;
    }

    @Resource
    public void setParamFindingTemplate(JmsTemplate paramFindingTemplate) {
        this.paramFindingTemplate = paramFindingTemplate;
    }

    @Resource
    public void setPriceCalcTemplate(JmsTemplate priceCalcTemplate) {
        this.priceCalcTemplate = priceCalcTemplate;
    }

    @Override
    public void abandonPriceCalc(long eventId, String uniqueRequestId) {
        PriceCalcRequest removed = priceCalcRequestsCache.asMap().remove(uniqueRequestId);
        if (removed != null) {
            info("Abandoned response handler for inflight PC req %s on event %s", uniqueRequestId, eventId);
        } else {
            warn("Too late to abandon pending PC req %s for event %s", uniqueRequestId, eventId);
        }
        prunePendingPCWatch(uniqueRequestId);
    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest pcr) {
        try {
            schedulePriceCalcTimeoutWatch(pcr);
            sendPCReq(pcr);
        } catch (Exception ex) {
            priceCalcRequestsCache.invalidate(pcr.getUniqueRequestId());
            error("Problem scheduling price calc %s", ex, pcr);
            algoHandlePriceCalcError.handle(pcr.getMatchParams().getEventId(), pcr.getUniqueRequestId(),
                            ex.getMessage());
        }
    }

    @Override
    public void schedulePriceCalc(PriceCalcRequest request, Class<? extends AbstractPriceCalculator> clazz) {
        ClassifiedPriceCalcRequest enhancedPcr = new ClassifiedPriceCalcRequest(request, clazz);
        try {
            schedulePriceCalcTimeoutWatch(request);
            sendPCReq(enhancedPcr);
        } catch (Exception ex) {
            priceCalcRequestsCache.invalidate(request.getUniqueRequestId());
            error("Problem scheduling enhanced price calc %s", ex, request);
            algoHandlePriceCalcError.handle(request.getMatchParams().getEventId(), request.getUniqueRequestId(),
                            ex.getMessage());
        }
    }

    private void sendPCReq(AbstractAlgoRequest req) {
        if (null == privatePCReplyQueue) {
            priceCalcTemplate.convertAndSend(req);
        } else {
            priceCalcTemplate.convertAndSend(req, message -> {
                message.setJMSReplyTo(privatePCReplyQueue);
                message.setJMSCorrelationID(req.getUniqueRequestId());
                return message;
            });
        }
    }

    @Override
    public void scheduleExternalPriceCalc(PriceCalcRequest pcr) {
        ActiveMQQueue q = resolveExternalPriceCalcQueue(pcr.getMatchFormat().getSportType());
        String json = JsonSerializer.serialize(pcr, false);
        schedulePriceCalcTimeoutWatch(pcr);
        try {
            if (doNotUseReplyQueuesForExternalRequests || null == privatePCReplyQueue) {
                externalPriceCalcTemplate.convertAndSend(q, json);
            } else {
                externalPriceCalcTemplate.convertAndSend(q, json, message -> {
                    message.setJMSReplyTo(privatePCReplyQueue);
                    message.setJMSCorrelationID(pcr.getUniqueRequestId());
                    return message;
                });
            }
        } catch (Exception ex) {
            prunePendingPCWatch(pcr.getUniqueRequestId());
            priceCalcRequestsCache.invalidate(pcr.getUniqueRequestId());
            error("Problem with external price calc %s", ex, pcr);
            algoHandlePriceCalcError.handle(pcr.getEventId(), pcr.getUniqueRequestId(), ex.getMessage());
        }
    }

    @Override
    public boolean establishExternalModelConnection(String url) {
        if (null == externalPriceCalcTemplate) {
            try {
                withRemoteModelTransport(url, compressExternalRequests);
            } catch (Exception ex) {
                error(ex);
                return false;
            }
        }
        return true;
    }

    @Override
    public void abandonParamFind(long eventId, String uniqueRequestId) {
        ParamFindRequest removed = paramFindRequestsCache.asMap().remove(uniqueRequestId);
        if (removed != null) {
            info("Abandoned response handler for inflight PF req %s on event %s", uniqueRequestId, eventId);
        } else {
            warn("Too late to abandon pending PF req %s for event %s", uniqueRequestId, eventId);
        }
        prunePendingPFWatch(uniqueRequestId);
    }

    @Override
    public void scheduleParamFind(ParamFindRequest pfr) {
        scheduleParamFindTimeoutWatch(pfr);
        try {
            sendPFReq(pfr);
        } catch (Exception ex) {
            paramFindRequestsCache.invalidate(pfr.getUniqueRequestId());
            error("Problem param find calc %s", ex, pfr);
        }
    }

    @Override
    public void scheduleParamFind(ParamFindRequest request, Class<? extends AbstractParamFinder> clazz) {
        ClassifiedParamFindRequest enhancedPcr = new ClassifiedParamFindRequest(request, clazz);
        scheduleParamFindTimeoutWatch(request);
        try {
            sendPFReq(enhancedPcr);
        } catch (Exception ex) {
            paramFindRequestsCache.invalidate(request.getUniqueRequestId());
            error("Problem scheduling enhanced param find %s", ex, request);
            algoHandleParamFindError.handle(request.getMatchParams().getEventId(), request.getUniqueRequestId(),
                            ex.getMessage());
        }
    }

    private void sendPFReq(AbstractAlgoRequest req) {
        if (null == privatePFReplyQueue) {
            paramFindingTemplate.convertAndSend(req);
        } else {
            paramFindingTemplate.convertAndSend(req, message -> {
                message.setJMSReplyTo(privatePFReplyQueue);
                message.setJMSCorrelationID(req.getUniqueRequestId());
                return message;
            });
        }
    }

    @Override
    public void scheduleExternalParamFind(ParamFindRequest pfr) {
        String json = JsonSerializer.serialize(pfr, false);
        ActiveMQQueue q = resolveExternalParamFindQueue(pfr.getMatchFormat().getSportType());
        scheduleParamFindTimeoutWatch(pfr);
        try {
            if (doNotUseReplyQueuesForExternalRequests || null == privatePFReplyQueue) {
                externalParamFindingTemplate.convertAndSend(q, json);
            } else {
                externalParamFindingTemplate.convertAndSend(q, json, message -> {
                    message.setJMSReplyTo(privatePFReplyQueue);
                    message.setJMSCorrelationID(pfr.getUniqueRequestId());
                    return message;
                });
            }
        } catch (Exception ex) {
            prunePendingPFWatch(pfr.getUniqueRequestId());
            paramFindRequestsCache.invalidate(pfr.getUniqueRequestId());
            error("Problem with external param find calc %s", ex, pfr);
            algoHandleParamFindError.handle(pfr.getEventId(), pfr.getUniqueRequestId(), ex.getMessage());
        }
    }

    @Override
    public int getNoAlgoCalculators() {
        return 0;
    }

    @Override
    public int getNoAlgoParamFinders() {
        return 0;
    }

    @Override
    public int getPriceCalcQueueSize() {
        return 0;
    }

    @Override
    public int getParamFindQueueSize() {
        return 0;
    }

    @Override
    public void onMessage(Message jmsMessage) {
        try {
            if (jmsMessage instanceof ObjectMessage) {
                ObjectMessage om = (ObjectMessage) jmsMessage;
                Serializable pojo = om.getObject();
                if (pojo instanceof ParamFindResponse) {
                    handleParamFindResponse((ParamFindResponse) pojo);
                    return;
                } else if (pojo instanceof PriceCalcResponse) {
                    handlePriceCalcResponse((PriceCalcResponse) pojo);
                    return;
                }
            } else if (jmsMessage instanceof TextMessage || jmsMessage instanceof BytesMessage) {
                String json = MQHelper.decodeMessage(jmsMessage);
                if (MQHelper.isCompressed(jmsMessage))
                    debug("Decompressed received message.  Size %d -> %d", MQHelper.getMessageLength(jmsMessage),
                                    json.length());
                if (json.indexOf(PriceCalcResponse.class.getSimpleName()) > -1) {
                    PriceCalcResponse pcr = JsonSerializer.deserialize(json, PriceCalcResponse.class);
                    handlePriceCalcResponse(pcr);
                    return;
                } else if (json.indexOf(ParamFindResponse.class.getSimpleName()) > -1) {
                    ParamFindResponse pfr = JsonSerializer.deserialize(json, ParamFindResponse.class);
                    handleParamFindResponse(pfr);
                    return;
                }
                warn("Unrecognised json: %s", json);
            }
            warn("Received unrecognised response %s", jmsMessage);
        } catch (Exception e) {
            error("Problem handling message %s", e, jmsMessage);
        }
    }

    protected void handlePriceCalcResponse(PriceCalcResponse priceCalcResponse) {
        String uuidID = priceCalcResponse.getUniqueRequestId();
        PriceCalcRequest request = priceCalcRequestsCache.asMap().remove(uuidID);
        if (null == request)
            return;

        prunePendingPCWatch(uuidID);

        long eventId = request.getEventId();
        if (priceCalcLastRequestResponseTimeCache.isResponseInExpectedSequence(eventId, priceCalcResponse,
                        request.getUniqueRequestId(), request.getRequestTime())) {
            debug("Got event %d PCR %s response", eventId, uuidID);
            if (priceCalcResponse.isFatalError()) {
                error("Got event %d uniqueRequestId %s PriceCalcResponse error %s", eventId, uuidID,
                                priceCalcResponse.getFatalErrorCause());
                algoHandlePriceCalcError.handle(eventId, uuidID, priceCalcResponse.getFatalErrorCause());
                return;
            }
            algoHandlePriceCalcResponse.handle(request.getEventId(), priceCalcResponse);
        }
    }

    protected void decrementPendingPriceCalcCounter() {
        decrementFloor(pendingPCRequestCount);
    }

    protected void decrementPendingParamFindCalcCounter() {
        decrementFloor(pendingPFRequestCount);
    }

    protected void decrementFloor(AtomicLong count) {
        if (count.decrementAndGet() < 0) {
            count.set(0);
        }
    }

    protected ActiveMQQueue resolveExternalPriceCalcQueue(SupportedSportType ss) {
        ActiveMQQueue q = externalPriceCalcQueues.get(ss);
        if (null == q) {
            q = createQueue("priceCalcQueue." + ss.name());
            externalPriceCalcQueues.put(ss, q);
        }
        return q;
    }

    protected ActiveMQQueue resolveExternalParamFindQueue(SupportedSportType ss) {
        ActiveMQQueue q = externalParamFindQueues.get(ss);
        if (null == q) {
            q = createQueue("paramFindQueue." + ss.name());
            externalParamFindQueues.put(ss, q);
        }
        return q;
    }

    protected void handleParamFindResponse(ParamFindResponse paramFindResponse) {
        String uuidID = paramFindResponse.getUniqueRequestId();
        ParamFindRequest request = paramFindRequestsCache.asMap().remove(uuidID);
        if (null == request)
            return;

        prunePendingPFWatch(uuidID);

        long eventId = request.getEventId();
        if (paramFindLastRequestResponseTimeCache.isResponseInExpectedSequence(eventId, paramFindResponse, uuidID,
                        request.getRequestTime())) {
            debug("Got event %d PFR %s response", eventId, uuidID);
            if (paramFindResponse.isFatalError()) {
                error("Got param find fatal error.  eventId %d, uniqueRequestId: %s, cause: %s", eventId, uuidID,
                                paramFindResponse.getFatalErrorCause());
                algoHandleParamFindError.handle(eventId, uuidID, paramFindResponse.getFatalErrorCause());
                return;
            }
            ParamFindResults paramFindResults = paramFindResponse.getParamFindResults();
            if (paramFindResults == null) {
                String errMsg = String.format("No ParamFindResults object in response from event %d request %s",
                                eventId, uuidID);
                error(errMsg);
                algoHandleParamFindError.handle(eventId, uuidID, errMsg);
                return;
            }
            algoHandleParamFindResponse.handle(eventId, paramFindResponse);
        }
    }

    protected ScheduledFuture<?> scheduleWatch(Runnable watcher, double watchAt) {
        return requestTimeoutScheduler.schedule(watcher, (long) watchAt, TimeUnit.SECONDS);
    }

    protected void prunePendingPCWatch(String uniqueRequestId) {
        PCWatchWrapper removedWrapper = inflightPCSchedules.remove(uniqueRequestId);
        if (removedWrapper != null && !removedWrapper.isDone()) {
            removedWrapper.cancel();
            decrementPendingPriceCalcCounter();
        }
    }

    protected void schedulePriceCalcTimeoutWatch(PriceCalcRequest pcr) {
        priceCalcRequestsCache.put(pcr.getUniqueRequestId(), pcr);
        long pendingCount = pendingPCRequestCount.incrementAndGet();
        if (pendingCount % 10 == 0) {
            debug("%s inflight PCRs", pendingCount);
        }
        double watchAt = priceCalcTimeoutSecs + (pendingCount * priceCalcTimeoutMultiplier);
        ScheduledFuture<?> schedule = scheduleWatch(() -> executePCRTimedOutLogic(pcr), watchAt);
        inflightPCSchedules.put(pcr.getUniqueRequestId(), new PCWatchWrapper(pcr, schedule));
    }

    protected void executePCRTimedOutLogic(PriceCalcRequest pcr) {
        String uniqueRequestId = pcr.getUniqueRequestId();
        PCWatchWrapper removed = inflightPCSchedules.remove(uniqueRequestId);
        if (removed != null) {
            priceCalcRequestsCache.asMap().remove(uniqueRequestId);
            decrementPendingPriceCalcCounter();
            String errMsg = String.format("Watchdog PCR timeout for event %d request %s", removed.getEventId(),
                            uniqueRequestId);
            algoHandlePriceCalcError.handle(pcr.getEventId(), uniqueRequestId, errMsg);
        }
    }

    protected void prunePendingPFWatch(String uniqueRequestId) {
        PFWatchWrapper removedWrapper = inflightPFSchedules.remove(uniqueRequestId);
        if (removedWrapper != null && !removedWrapper.isDone()) {
            removedWrapper.cancel();
            decrementPendingParamFindCalcCounter();
        }
    }

    protected void scheduleParamFindTimeoutWatch(ParamFindRequest pfr) {
        paramFindRequestsCache.put(pfr.getUniqueRequestId(), pfr);
        long pendingCount = pendingPFRequestCount.incrementAndGet();
        if (pendingCount % 10 == 0) {
            debug("%s inflight PFRs", pendingCount);
        }
        double watchAt = paramFindTimeoutSecs + (pendingCount * paramFindTimeoutMultiplier);
        ScheduledFuture<?> schedule = scheduleWatch(() -> executePFTimedOutLogic(pfr), watchAt);
        inflightPFSchedules.put(pfr.getUniqueRequestId(), new PFWatchWrapper(pfr, schedule));
    }

    protected void executePFTimedOutLogic(ParamFindRequest pfr) {
        String uniqueRequestId = pfr.getUniqueRequestId();
        PFWatchWrapper removed = inflightPFSchedules.remove(uniqueRequestId);
        if (removed != null) {
            paramFindRequestsCache.asMap().remove(uniqueRequestId);
            decrementPendingParamFindCalcCounter();
            String errMsg = String.format("Watchdog PFR timeout for event %d request %s", removed.getEventId(),
                            uniqueRequestId);
            algoHandleParamFindError.handle(pfr.getEventId(), uniqueRequestId, errMsg);
        }
    }
}
