package controller;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

public class WindowAlert {

	public void waitForOpponent() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(null);
		alert.setContentText("Poczekaj aż przeciwnik dołączy.");
		alert.showAndWait();
	}

	public void waitForOpponentMove() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText(
				"Aby wykonac nastepny ruch musi go pierwiej wykonac przeciwnik!");
		alert.setContentText("Poczekaj aż przeciwnik zrobi ruch.");
		alert.showAndWait();
	}

	public void foundOpponent() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Info");
		alert.setHeaderText("Znaleziono przeciwnika, wykonaj ruch.");
		alert.setContentText(null);
		alert.showAndWait();
	}

	public String askForUsername() {

		TextInputDialog dialog = new TextInputDialog("gracz");
		dialog.setTitle("Logowanie");
		dialog.setHeaderText("Powiedz innym jak sie nazywasz");
		dialog.setContentText("Wprowadz swoj nick: ");
		Optional<String> result = dialog.showAndWait();
		return result.get();
	}

}
