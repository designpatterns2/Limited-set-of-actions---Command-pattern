package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import java.awt.Point;

import net.sf.bloodball.model.Ball;
import net.sf.bloodball.model.Field;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.Teams;
import net.sf.bloodball.model.player.Player;
import net.sf.bloodball.util.Dices;

public class Move implements ActionCommand {

  private Field field;
  private Ball ball;
  private Teams teams;
  private Point actorPosition;
  private Point actionPosition;
  private MoveActionMethods moveActionMethods;

  public Move(Game game) {
    field = game.getField();
    ball = game.getBall();
    teams = game.getTeams();
    moveActionMethods = new MoveActionMethods(game);
  }

  public Move(Game game, Point actorPosition, Point actionPosition) {
    field = game.getField();
    ball = game.getBall();
    teams = game.getTeams();
    this.actorPosition = actorPosition;
    this.actionPosition = actionPosition;
    moveActionMethods = new MoveActionMethods(game);
  }

  @Override
  public void execute() {
    Ensuring.state(isLegal(), "No legal movement position: " + actionPosition);
    Player actor = moveActionMethods.getPlayerAt(actorPosition);
    movePlayer(actor, actionPosition);
    performTackle(actor, actorPosition, actionPosition);
  }

  @Override
  public boolean isLegal() {
    Player actor = moveActionMethods.getPlayerAt(actorPosition);
    Ensuring.state(actorPosition != null, "Cannot move player to no position.");
    return !sprintsIntoTackleZone(actor, actionPosition) && !squareOccupied(actionPosition) && !actor.hasToStopMoving() && moveActionMethods.areNeighborSquares(actorPosition, actionPosition);
  }

  public boolean endsTeamTurn() {
    return false;
  }

  public boolean isMoveablePlayer(Point position) {
    return moveActionMethods.activeTeamPlayerAt(position) && field.getPlayer(position).isAtCall();
  }

  private void movePlayer(Player actor, Point position) {
    field.removePlayer(moveActionMethods.getPosition(actor));
    field.setPlayer(position, actor);
    if (actor.inBallPossession()) {
      ball.setPosition(position);
    }
    actor.move();
    if (actor.inBallPossession()) {
      ball.setPosition(actionPosition);
    }
  }

  private void performTackle(Player player, Point actorPosition, Point actionPosition) {
    boolean movesBetweenTackleZones = moveActionMethods.isOpponentTackleZone(player, actorPosition) && moveActionMethods.isOpponentTackleZone(player, actionPosition);
    if (movesBetweenTackleZones && Dices.D6.roll() > 4) {
      moveActionMethods.knockOver(actionPosition);
    }
  }

  private boolean squareOccupied(Point position) {
    return field.getPlayer(position) != Player.NO_PLAYER;
  }
  
  private boolean sprintsIntoTackleZone(Player player, Point movePosition) {
  	return !player.inRegularMoveMode()  && field.inTackleZone(movePosition, teams.getOpponentTeam(player.getTeam()));
  }

}