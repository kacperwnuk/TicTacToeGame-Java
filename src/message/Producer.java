package message;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.messaging.ConnectionFactory;
import com.sun.messaging.Topic;

public class Producer {

	public void sendQueueMessages(MoveMessage moveMessage) {
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(
							com.sun.messaging.ConnectionConfiguration.imqAddressList,
							"localhost:7676/jms");
			JMSContext jmsContext = connectionFactory.createContext();
			JMSProducer jmsProducer = jmsContext.createProducer();
			Topic topic = new com.sun.messaging.Topic("TicTacToeTopic");

			Gson message = new GsonBuilder().create();
			String msg = "Message " + moveMessage.toString();
			jmsProducer.send(topic, message.toJson(moveMessage));
			System.out.printf("Wiadomosc '%s' wyslana \n", msg);

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
