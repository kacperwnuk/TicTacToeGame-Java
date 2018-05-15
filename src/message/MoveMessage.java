package message;

import javafx.util.Pair;
import model.Player;

/*
 * Message with move made by user.
 */

public class MoveMessage {

	private Pair<Integer, Integer> coords;
	private Player player;

	public MoveMessage(Pair<Integer, Integer> coords, Player player) {
		setCoords(coords);
		setPlayer(player);
	}

	public Pair<Integer, Integer> getCoords() {
		return coords;
	}

	public void setCoords(Pair<Integer, Integer> coords) {
		this.coords = coords;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		if (coords != null) {
			return player.getUsername() + " " + player.getFigure() + " "
					+ coords.getKey() + " " + coords.getValue();
		} else {
			return player.getUsername() + " " + player.getFigure();
		}
	}

}
