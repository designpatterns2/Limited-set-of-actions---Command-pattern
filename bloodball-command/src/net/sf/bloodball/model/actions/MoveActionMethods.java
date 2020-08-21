package net.sf.bloodball.model.actions;

import java.awt.Point;
import net.sf.bloodball.model.Field;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Player;

public class MoveActionMethods {

  protected Game game;

  public MoveActionMethods(Game game) {
    this.game = game;
  }

  protected boolean activeTeamPlayerAt(Point position) {
    return game.getTeams().getActiveTeam().isMember(game.getField().getPlayer(position));
  }

  protected Player getPlayerAt(Point position) {
    return game.getField().getPlayer(position);
  }


  protected void knockOver(Point playerPosition) {
    getPlayerAt(playerPosition).knockOver();
    getPlayerAt(playerPosition).endTurn();
  }

  protected Point getPosition(Player player) {
    return game.getField().getPlayerPosition(player);
  }

  protected boolean isOpponentTackleZone(Player player, Point position) {
    for (int dx = -1; dx <= 1; dx++) {
      for (int dy = -1; dy <= 1; dy++) {
        Point neighborSquare = new Point(position.x + dx, position.y + dy);
        if (Field.isInside(neighborSquare) && isTacklingPlayerAt(player, neighborSquare)) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean isTacklingPlayerAt(Player player, Point tacklerPosition) {
    Player tacklingPlayer = getPlayerAt(tacklerPosition);
    return !tacklingPlayer.isProne() && areOpponents(player, tacklingPlayer);
  }

  protected boolean areOpponents(Player first, Player second) {
    return game.getTeams().getOpponentTeam(first.getTeam()) == second.getTeam();
  }

  protected boolean areCollegues(Player first, Player second) {
    return first.getTeam() == second.getTeam();
  }

  protected boolean areNeighbors(Player first, Player second) {
    return game.getField().neighborPlayers(first, second);
  }

  protected boolean areNeighborSquares(Point first, Point second) {
    return game.getField().neighborSquares(first, second);
  }

}