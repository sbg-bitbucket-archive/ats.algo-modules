package ats.algo.algomanager.jms;

import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Topic;

import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;


public interface JMSFactoryMethods {

    CachingConnectionFactory createConnectionFactory(String remoteModelAMQBRokerUrl);

    Queue createQueue(String name);

    JmsTemplate createJmsQueueTemplate(CachingConnectionFactory cachingFac);

    Topic createTopic(String name);

    JmsTemplate createJmsTopicTemplate(CachingConnectionFactory cachingFac);

    void startMessageListener(CachingConnectionFactory cachingFac, String topicNameToListenOn,
                    MessageListener messageListener);
}
