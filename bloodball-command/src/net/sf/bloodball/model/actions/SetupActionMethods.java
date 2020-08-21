package net.sf.bloodball.model.actions;

import java.awt.Point;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Team;
import net.sf.bloodball.model.player.Player;

public class SetupActionMethods {
	
	private Game game;

	public SetupActionMethods(Game game) {
		this.game = game;
	}
	
	protected Game getGame() {
		return game;
	} 	

	public boolean isLegalSetupPlayer(Team team, int playerNumber) {
		Player player = team.getPlayerByNumber(playerNumber);
		return game.getTeams().isActiveTeam(team) && maySetup(team) && player.isReserve();
	}

  	protected boolean isNotOccupied(Point position) {
    return game.getField().getPlayer(position) == Player.NO_PLAYER;
  }

	protected boolean maySetup(Team team) {
		return team.getPlayersToSetupCount() > 0;
	}

}
