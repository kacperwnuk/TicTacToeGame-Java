import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Topic;

import controller.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	private GameController gameController;
	private ConnectionFactory connectionFactory;
	private JMSContext jmsContext;
	private JMSConsumer jmsConsumer;

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {

			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
					"GameWindow.fxml"));

			Parent root = fxmlLoader.load();
			Scene scene = new Scene(root, 600, 350);

			gameController = fxmlLoader.getController();

			primaryStage.setScene(scene);
			primaryStage.setTitle("TicTacToe Game");
			primaryStage.show();
			receiveQueueMessagesAsynch();
			gameController.getUsername();
			gameController.sendReadyMessage();
			gameController.getWindowAlert().waitForOpponent();
		} catch (Exception e) {
			e.printStackTrace();
		}

		primaryStage.setOnCloseRequest(e -> {
			close();
			System.exit(0);
		});
	}

	public static void main(String[] args) {
		launch(args);

	}

	public void receiveQueueMessagesAsynch() {

		connectionFactory = new com.sun.messaging.ConnectionFactory();
		jmsContext = connectionFactory.createContext();
		try {
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(
							com.sun.messaging.ConnectionConfiguration.imqAddressList,
							"localhost:7676/jms");
			Topic topic = new com.sun.messaging.Topic("TicTacToeTopic");
			jmsConsumer = jmsContext.createConsumer(topic);
			jmsConsumer.setMessageListener(gameController);

		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	private void close() {
		jmsConsumer.close();
		jmsContext.close();
	}

}
