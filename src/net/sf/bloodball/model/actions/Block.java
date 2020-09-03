package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.Health;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.util.*;
import java.awt.Point;

public class Block implements ActionCommand {

  private Field field;
  private Teams teams;
  private Point actorPosition;
  private Point actionPosition;
  private MoveActionMethods moveActionMethods;

  public Block(Game game) {
    field = game.getField();
    teams = game.getTeams();
    moveActionMethods = new MoveActionMethods(game);
  }

  public Block(Game game, Point actorPosition, Point actionPosition) {
    field = game.getField();
    teams = game.getTeams();
    this.actorPosition = actorPosition;
    this.actionPosition = actionPosition;
    moveActionMethods = new MoveActionMethods(game);
  }

  @Override
  public void execute() {
    Player blocker = moveActionMethods.getPlayerAt(actorPosition);
    Player blocked = moveActionMethods.getPlayerAt(actionPosition);
    Ensuring.state(isLegal(), "No blockable player at position: " + actionPosition);
    executeBlockingRoll(blocker, blocked);
    blocker.endTurn();
  }

  @Override
  public boolean isLegal() {
    Player blocker = moveActionMethods.getPlayerAt(actorPosition);
    Player blocked = moveActionMethods.getPlayerAt(actionPosition);
    return moveActionMethods.areNeighbors(blocker, blocked) && moveActionMethods.areOpponents(blocker, blocked) && !blocker.hasActed();
  }

  @Override
  public boolean endsTeamTurn() {
    return false;
  }

  public void setPositions(Point actorPosition, Point actionPosition) {
    this.actorPosition = actorPosition;
    this.actionPosition = actionPosition;
  }

  private void executeBlockingRoll(Player blocker, Player blocked) {
    if (blocked.isProne()) {
      rollProneBlockTable(blocker, blocked);
    } else {
      rollBlockTable(blocker, blocked);
    }
  }

  public boolean mayBlock(Player blocker) {
    if (blocker == Player.NO_PLAYER || !blocker.isOnField() || blocker.hasActed()) {
      return false;
    }
    Point position = field.getPlayerPosition(blocker);
    return field.inTackleZone(position, teams.getOpponentTeam(blocker.getTeam()));
  }

  private void rollBlockTable(Player blocker, Player blocked) {
    executeBlockTable(blocker, blocked, 0);
  }

  private void rollProneBlockTable(Player blocker, Player blocked) {
    executeBlockTable(blocker, blocked, 2);
  }

  private void executeBlockTable(Player blocker, Player blocked, int modifier) {
  	int result = Dices.D6.roll(2) + blocker.getType().getStrength() - blocker.getType().getStrength() + modifier;
    if (result <= 2) {
      blocker.injure(Health.STUNNED);
    } else if (result >= 12) {
      blocked.injure(Health.STUNNED);
    } else {
      switch (result) {
        case 4 :
          blocker.knockOver();
          break;
        case 7 :
          break;
        case 8 :
          blocker.knockOver();
          blocked.knockOver();
          break;
        case 11 :
          blocked.knockOver();
      }
    }
  }
}