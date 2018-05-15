package model;

import java.util.UUID;

public class Player {

	public enum Figure {
		Blank, X, O
	};

	private String username;
	private Figure figure;
	private UUID id;

	public Player() {
		setUsername("gracz");
		setFigure(Figure.Blank);
		setId(UUID.randomUUID());
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Figure getFigure() {
		return figure;
	}

	public void setFigure(Figure figure) {
		this.figure = figure;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (!id.equals(other.id))
			return false;
		if (!username.equals(other.getUsername()))
			return false;
		if (username.equals(other.getUsername()) && figure != other.getFigure())
			return false;
		return true;
	}

}
