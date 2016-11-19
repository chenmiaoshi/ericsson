package com.ibm.mqtt;

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

public class MqttBroker {
	private final static Log logger = LogFactory.getLog(MqttBroker.class);// ��־����  
    // ���Ӳ���  
    private final static String CONNECTION_STRING = "tcp://localhost:9901";  
    private final static boolean CLEAN_START = true;  
    private final static short KEEP_ALIVE = 30;// �ͺ����磬��������Ҫ��ʱ��ȡ���ݣ�����30s  
    private final static String CLIENT_ID = "master";// �ͻ��˱�ʶ  
    private final static int[] QOS_VALUES = { 0, 0, 2, 0 };// ��Ӧ�������Ϣ����  
    private final static String[] TOPICS = { "Test/TestTopics/Topic1",  
            "Test/TestTopics/Topic2", "Test/TestTopics/Topic3",  
            "client/keepalive" };  
    private static MqttBroker instance = new MqttBroker();  
  
    private MqttClient mqttClient;  
  
    /** 
     * ����ʵ������ 
     *  
     * @return 
     */  
    public static MqttBroker getInstance() {  
        return instance;  
    }  
  
    /** 
     * �������ӷ��� 
     */  
    private void connect() throws MqttException {  
        logger.info("connect to mqtt broker.");  
        mqttClient = new MqttClient(CONNECTION_STRING);  
        logger.info("***********register Simple Handler***********");  
        SimpleCallbackHandler simpleCallbackHandler = new SimpleCallbackHandler();  
        mqttClient.registerSimpleHandler(simpleCallbackHandler);// ע�������Ϣ����  
        mqttClient.connect(CLIENT_ID, CLEAN_START, KEEP_ALIVE);  
        logger.info("***********subscribe receiver topics***********");  
        mqttClient.subscribe(TOPICS, QOS_VALUES);// ���Ľ�����  
  
        logger.info("***********CLIENT_ID:" + CLIENT_ID);  
        /** 
         * ��ɶ��ĺ󣬿���������������������ͨ����Ҳ���Է����Լ�����Ϣ 
         */  
        mqttClient.publish("keepalive", "keepalive".getBytes(), QOS_VALUES[0],  
                true);// ������������������ͨ��  
    }  
  
    /** 
     * ������Ϣ 
     *  
     * @param clientId 
     * @param messageId 
     */  
    public void sendMessage(String clientId, String message) {  
        try {  
            if (mqttClient == null || !mqttClient.isConnected()) {  
                connect();  
            }  
  
            //logger.info("send message to " + clientId + ", message is "  + ErrorMapping.doMapping(info.getCode()));  
            // �����Լ�����Ϣ  
            mqttClient.publish("GMCC/client/" + clientId, message.getBytes(),  
                    0, false);  
        } catch (MqttException e) {  
            logger.error(e.getCause());  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * �򵥻ص�����������server���յ���������Ϣ 
     *  
     * @author Join 
     *  
     */  
    class SimpleCallbackHandler implements MqttSimpleCallback {  
  
        /** 
         * ���ͻ�����broker����Ͽ�ʱ���� �����ٴ˴������¶��� 
         */  
        //@Override  
        public void connectionLost() throws Exception {  
            // TODO Auto-generated method stub  
            System.out.println("�ͻ�����broker�Ѿ��Ͽ�");  
        }  
  
        /** 
         * �ͻ��˶�����Ϣ�󣬸÷�������ص����մ�����Ϣ 
         */  
       // @Override  
        public void publishArrived(String topicName, byte[] payload, int Qos,  
                boolean retained) throws Exception {  
            // TODO Auto-generated method stub  
            System.out.println("��������: " + topicName);  
            System.out.println("��Ϣ����: " + new String(payload));  
            System.out.println("��Ϣ����(0,1,2): " + Qos);  
            System.out.println("�Ƿ���ʵʱ���͵���Ϣ(false=ʵʱ��true=�������ϱ����������Ϣ): "  
                    + retained);  
        }  
  
    }  
  
    public static void main(String[] args) {  
        new MqttBroker().sendMessage("client", "message");  
    }  

}
