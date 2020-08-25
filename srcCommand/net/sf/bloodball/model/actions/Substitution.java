package net.sf.bloodball.model.actions;

import java.awt.Point;

import net.sf.bloodball.model.Field;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.model.player.Team;

public class Substitution extends SetupActionMethods implements SetupCommand {

	private Field field;
	private Team team;
	private Point position;
	private int playerNumber;

	public Substitution(Game game) {
		super(game);
	}

	public Substitution(Game game, Point position, int playerNumber) {
		super(game);
		this.field = game.getField();
		this.team = game.getTeams().getActiveTeam();
		this.position = position;
		this.playerNumber = playerNumber;
	}

	@Override
	public void execute() {
		if(field.getSubstitutArea(team).contains(position) && (field.getPlayer(position) == Player.NO_PLAYER))
			field.setPlayer(position, team.getPlayerByNumber(playerNumber));
	}

	@Override
	public boolean isLegal() {
		return isWithinActiveTeamSubstitionArea(position) && isNotOccupied(position);
	}

	private boolean isWithinActiveTeamSubstitionArea(Point position) {
		return field.getSubstitutArea(getGame().getTeams().getActiveTeam()).contains(position);
	}
}
