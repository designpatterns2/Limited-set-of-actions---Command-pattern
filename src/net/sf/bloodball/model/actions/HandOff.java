package net.sf.bloodball.model.actions;

import java.awt.Point;

import de.vestrial.util.error.Ensuring;
import net.sf.bloodball.model.Ball;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Player;

public class HandOff implements ActionCommand {

  private Game game;
  private Ball ball;
  private Point actorPosition;
  private Point actionPosition;
  private MoveActionMethods moveActionMethods;

  public HandOff(Game game) {
    this.game = game;
    ball = game.getBall();
    moveActionMethods = new MoveActionMethods(game);
  }

  public HandOff(Game game, Point actorPosition, Point actionPosition) {
    this.game = game;
    ball = game.getBall();
    this.actorPosition = actorPosition;
    this.actionPosition = actionPosition;
    moveActionMethods = new MoveActionMethods(game);
  }

  @Override
  public void execute() {
    Player actor = moveActionMethods.getPlayerAt(actorPosition);
    Player reactor = moveActionMethods.getPlayerAt(actionPosition);
    Ensuring.state(isLegal(), "Can not hand off ball to " + actionPosition);
    actor.dropBall();
    ball.setPosition(actionPosition);
    reactor.catchBall();
    game.startNewTurn();
  }

  @Override
  public boolean isLegal() {
    Player actor = moveActionMethods.getPlayerAt(actorPosition);
    Player reactor = moveActionMethods.getPlayerAt(actionPosition);
    return actor.inBallPossession()
            && !actorPosition.equals(actionPosition)
            && moveActionMethods.areNeighbors(actor, reactor)
            && moveActionMethods.areCollegues(actor, reactor)
            && !reactor.isProne();
  }

  @Override
  public boolean endsTeamTurn() {
    return true;
  }
}