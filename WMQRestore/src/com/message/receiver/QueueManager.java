package com.message.receiver;

import com.ibm.mq.*;

public class QueueManager {

    private final String host;
    private final int port;
    private final String channel;
    private final String manager;
    private final MQQueueManager qmgr;

    public QueueManager(String host, int port, String channel, String manager) throws MQException {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.manager = manager;
        this.qmgr = createQueueManager();
    }

    public MQQueue getMQ(String queueName) throws MQException {
        MQQueue queue = qmgr.accessQueue(queueName, MQC.MQOO_INQUIRE | MQC.MQOO_INPUT_AS_Q_DEF, null, null, null);
      //return queue.getCurrentDepth();
        
        return queue;
    }

    @SuppressWarnings("unchecked")
    private MQQueueManager createQueueManager() throws MQException {
        MQEnvironment.channel = channel;
        MQEnvironment.port = port;
        MQEnvironment.hostname = host;
        MQEnvironment.properties.put(MQC.TRANSPORT_PROPERTY, MQC.TRANSPORT_MQSERIES);
        return new MQQueueManager(manager);
    }
    
}