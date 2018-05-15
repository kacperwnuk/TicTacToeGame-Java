package controller;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import message.MoveMessage;
import message.Producer;
import model.Player;
import model.Player.Figure;
import model.TicTacToeModel;

public class GameController implements MessageListener {

	private TicTacToeModel model = new TicTacToeModel();
	private Player player = new Player();
	private boolean readyToPlay = false;
	private WindowAlert windowAlert = new WindowAlert();
	private MoveMessage moveMessage;
	private MoveMessage opponentMoveMessage;
	private Producer producer = new Producer();

	@FXML
	Text turnText;
	@FXML
	GridPane playfield;

	public Node getNodeByRowColumnIndex(final int row, final int column) {
		Node result = null;
		ObservableList<Node> childrens = playfield.getChildren();

		for (Node node : childrens) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(
					node) == column) {
				result = node;
				break;
			}
		}

		return result;
	}

	private void drawX(TextFlow src) {
		Text text = new Text("X");
		text.setFont(Font.font(30));
		src.getChildren().add(text);
	}

	private void drawX(int row, int column) {
		System.out.println("Rysuje na wspolrzednych " + row + column);
		TextFlow src = (TextFlow) getNodeByRowColumnIndex(row, column);
		drawX(src);
	}

	private void drawO(int row, int column) {
		System.out.println("Rysuje na wspolrzednych " + row + column);
		TextFlow src = (TextFlow) getNodeByRowColumnIndex(row, column);
		drawO(src);
	}

	private void drawO(TextFlow src) {
		Text text = new Text("O");
		text.setFont(Font.font(30));
		src.getChildren().add(text);
	}

	@FXML
	private void initialize() {
	}

	@FXML
	private void draw(MouseEvent e) {

		TextFlow src = (TextFlow) e.getSource();
		Node source = (Node) e.getSource();

		if (!isReadyToPlay()) {
			if (player.getFigure() == Figure.Blank) {
				windowAlert.waitForOpponent();
			} else {
				windowAlert.waitForOpponentMove();
			}
			return;
		}
		System.out.println("Gramy");
		if (model.isMarked(GridPane.getRowIndex(source), GridPane
				.getColumnIndex(source)) == TicTacToeModel.State.Blank) {

			if (model.getTurn() == TicTacToeModel.State.X) {
				drawX(src);

				turnText.setText("Turn: O");
				model.setMarked(GridPane.getRowIndex(source), GridPane
						.getColumnIndex(source), TicTacToeModel.State.X);
				model.setTurn(TicTacToeModel.State.O);
				checkResult(model.checkGame(GridPane.getRowIndex(source),
						GridPane.getColumnIndex(source),
						TicTacToeModel.State.X));

			} else {
				drawO(src);

				turnText.setText("Turn: X");
				model.setMarked(GridPane.getRowIndex(source), GridPane
						.getColumnIndex(source), TicTacToeModel.State.O);

				model.setTurn(TicTacToeModel.State.X);
				checkResult(model.checkGame(GridPane.getRowIndex(source),
						GridPane.getColumnIndex(source),
						TicTacToeModel.State.O));

			}

			MoveMessage move = new MoveMessage(new Pair<>(GridPane.getRowIndex(
					source), GridPane.getColumnIndex(source)), player);
			sendMoveMessage(move);

		}

	}

	private void checkResult(Pair<Boolean, TicTacToeModel.State> result) {
		if (result.getKey()) {
			if (result.getValue() != TicTacToeModel.State.Blank) {
				System.out.println(result.getValue());

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wynik");
				alert.setHeaderText("Wygrał " + result.getValue());
				alert.showAndWait();

				restart();

			} else {
				System.out.println("draw");
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wynik");
				alert.setHeaderText("Remis!");
				alert.showAndWait();

				restart();
			}
		}
	}

	public void getUsername() {
		player.setUsername(windowAlert.askForUsername());
	}

	private void waitForOpponentMove() {

		setReadyToPlay(false);
	}

	private void sendMoveMessage(MoveMessage move) {
		producer.sendQueueMessages(move);
		waitForOpponentMove();
	}

	@Override
	public void onMessage(Message message) {
		TextMessage msg = (TextMessage) message;

		Gson gson = new GsonBuilder().create();

		try {
			setOpponentMoveMessage(gson.fromJson(msg.getText(),
					MoveMessage.class));
		} catch (JsonSyntaxException | JMSException e) {
			e.printStackTrace();
		}

		if (getOpponentMoveMessage() != null) {
			System.out.println("Message received" + getOpponentMoveMessage()
					.toString());

			if (getOpponentMoveMessage().getPlayer().equals(player)) {
				System.out.println("wiadomosc od sb samego.");
				return;
			}

			System.out.println("Mozemy grac");

			if (getOpponentMoveMessage().getCoords() != null) {

				if (player.getFigure() == Figure.Blank) {
					player.setFigure(Figure.O);
					Platform.runLater(() -> windowAlert.foundOpponent());
				}

				Platform.runLater(() -> drawOpponentMove());
			} else {
				player.setFigure(Figure.X);
				Platform.runLater(() -> windowAlert.foundOpponent());
			}

			setReadyToPlay(true);

		}

	}

	private void drawOpponentMove() {

		int rowIndex = new Integer(opponentMoveMessage.getCoords().getKey());
		int columnIndex = new Integer(opponentMoveMessage.getCoords()
				.getValue());

		if (model.getTurn() == TicTacToeModel.State.X) {
			drawX(rowIndex, columnIndex);

			turnText.setText("Turn: O");
			model.setMarked(rowIndex, columnIndex, TicTacToeModel.State.X);
			model.setTurn(TicTacToeModel.State.O);
			checkResult(model.checkGame(rowIndex, columnIndex,
					TicTacToeModel.State.X));

		} else {
			drawO(rowIndex, columnIndex);

			turnText.setText("Turn: X");
			model.setMarked(rowIndex, columnIndex, TicTacToeModel.State.O);

			model.setTurn(TicTacToeModel.State.X);
			checkResult(model.checkGame(rowIndex, columnIndex,
					TicTacToeModel.State.O));

		}

		System.out.println("rysuje ruch " + opponentMoveMessage.toString());
	}

	public void sendReadyMessage() {
		MoveMessage moveMessage = new MoveMessage(null, player);
		producer.sendQueueMessages(moveMessage);
	}

	private void restart() {
		turnText.setText("Turn: X");
		playfield.getChildren().forEach(e -> {

			if (e.getClass().toString().equals(
					"class javafx.scene.text.TextFlow")) {
				TextFlow src = (TextFlow) e;
				src.getChildren().clear();
			}

		});

		model = new TicTacToeModel();
	}

	public boolean isReadyToPlay() {
		return readyToPlay;
	}

	public void setReadyToPlay(boolean readyToPlay) {
		this.readyToPlay = readyToPlay;
	}

	public MoveMessage getMoveMessage() {
		return moveMessage;
	}

	public void setMoveMessage(MoveMessage moveMessage) {
		this.moveMessage = moveMessage;
	}

	public MoveMessage getOpponentMoveMessage() {
		return opponentMoveMessage;
	}

	public void setOpponentMoveMessage(MoveMessage opponentMoveMessage) {
		this.opponentMoveMessage = opponentMoveMessage;
	}

	public WindowAlert getWindowAlert() {
		return windowAlert;
	}

}
