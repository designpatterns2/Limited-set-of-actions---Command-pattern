package net.sf.bloodball.model.actions;

import de.vestrial.util.awt.Points;
import java.awt.Point;
import java.util.*;

import de.vestrial.util.error.Ensuring;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.player.*;
import net.sf.bloodball.util.Dices;

public class Throw implements ActionCommand {

  private double INTERCEPTION_DISTANCE = 0.5;

  private Game game;
  private Field field;
  private Ball ball;
  private Teams teams;
  private Point actorPosition;
  private Point actionPosition;

  private MoveActionMethods moveActionMethods;

  public Throw(Game game) {
    this.game = game;
    field = game.getField();
    ball = game.getBall();
    teams = game.getTeams();
    moveActionMethods = new MoveActionMethods(game);
  }

  public Throw(Game game, Point actorPosition, Point actionPosition) {
    this.game = game;
    field = game.getField();
    ball = game.getBall();
    teams = game.getTeams();
    this.actorPosition = actorPosition;
    this.actionPosition = actionPosition;
    moveActionMethods = new MoveActionMethods(game);
  }


  @Override
  public void execute() {
    Ensuring.state(isLegal(), "Ball cannot be thrown to " + actionPosition);
    executeThrowingRoll(actorPosition, actionPosition);
    game.startNewTurn();
  }

  @Override
  public boolean isLegal() {
    Player thrower = moveActionMethods.getPlayerAt(actorPosition);
    Player catcher = moveActionMethods.getPlayerAt(actionPosition);
    return !thrower.hasMoved() && thrower.inBallPossession() && moveActionMethods.areCollegues(thrower, catcher) && !catcher.isProne() && isInReach(actorPosition, actionPosition);
  }

  @Override
  public boolean endsTeamTurn() {
    return true;
  }

  private void executeThrowingRoll(Point throwerPosition, Point catcherPosition) {
    int catchLimit = 6 + (int) (Points.getDistance(throwerPosition, catcherPosition) / 4);
    int missLimit = 3 + (int) (Points.getDistance(throwerPosition, catcherPosition) / 4);
    rollThrowTable(throwerPosition, catcherPosition, missLimit, catchLimit);
  }

  public boolean isInReach(Point first, Point second) {
    return !moveActionMethods.areNeighborSquares(first, second) && Points.getDistance(first, second) <= 16;
  }


  private void rollThrowTable(Point throwerPosition, Point catcherPosition, int missLimit, int catchLimit) {
    Player thrower = moveActionMethods.getPlayerAt(throwerPosition);
    Player catcher = moveActionMethods.getPlayerAt(catcherPosition);
    int throwerSurrounders = field.playersInTackleZoneCount(throwerPosition, teams.getOpponentTeam(thrower.getTeam()));
    int catcherSurrounders = field.playersInTackleZoneCount(catcherPosition, teams.getOpponentTeam(catcher.getTeam()));
    int result = Dices.D6.roll(2) + thrower.getType().getThrowingSkill() + catcher.getType().getCoolness();
    result -= throwerSurrounders + catcherSurrounders;
    field.getPlayer(ball.getPosition()).dropBall();
    if (result < missLimit) {
      interceptBall(throwerPosition, catcherPosition);
    } else if (result < catchLimit) {
      missBall(catcherPosition);
    } else {
      catchBall(catcherPosition);
    }
  }

  private void catchBall(Point catcherPosition) {
    ball.setPosition(catcherPosition);
    field.getPlayer(catcherPosition).catchBall();
  }

  private void interceptBall(Point throwerPosition, Point catcherPosition) {
    Player thrower = moveActionMethods.getPlayerAt(throwerPosition);
    Point interceptionPosition = getInterceptionPosition(throwerPosition, catcherPosition);
    if (interceptionPosition != null) {
      ball.setPosition(interceptionPosition);
      field.getPlayer(interceptionPosition).catchBall();
    } else if (moveActionMethods.isOpponentTackleZone(thrower, throwerPosition)) {
      ball.scatter();
    } else {
      missBall(catcherPosition);
    }
  }

  private void missBall(Point catcherPosition) {
    ball.setPosition(catcherPosition);
    ball.scatter();
  }

  private Point getInterceptionPosition(Point throwerPosition, Point catcherPosition) {
    Iterator interceptionPositions = getPossibleInterceptionPositions(throwerPosition, catcherPosition).iterator();
    if (!interceptionPositions.hasNext()) {
      return null;
    }
    Point interceptorPosition = (Point) interceptionPositions.next();
    double interceptorDistance = Points.getDistance(throwerPosition, interceptorPosition);
    while (interceptionPositions.hasNext()) {
      Point position = (Point) interceptionPositions.next();
      double distance = Points.getDistance(throwerPosition, position);
      if (distance < interceptorDistance) {
        interceptorPosition = position;
        interceptorDistance = distance;
      }
    }
    return interceptorPosition;
  }

  private List getPossibleInterceptionPositions(Point throwerPosition, Point catcherPosition) {
    List interceptionPositions = new ArrayList();
    for (int x = 0; x < FieldExtents.SIZE.width; x++) {
      for (int y = 0; y < FieldExtents.SIZE.height; y++) {
        Point position = new Point(x, y);
        Player player = field.getPlayer(position);
        Team opponentTeam = teams.getInactiveTeam();
        if (!player.isProne() && opponentTeam.isMember(player) && isInterceptableAt(throwerPosition, position, catcherPosition)) {
          interceptionPositions.add(position);
        }
      }
    }
    return interceptionPositions;
  }

  private boolean isInterceptableAt(Point throwerPosition, Point interceptorPosition, Point catcherPosition) {
    double numerator = (throwerPosition.x - catcherPosition.x) * (interceptorPosition.y - catcherPosition.y);
    numerator -= (throwerPosition.y - catcherPosition.y) * (interceptorPosition.x - catcherPosition.x);
    numerator = Math.abs(numerator);
    double denominator = (catcherPosition.x - throwerPosition.x) * (catcherPosition.x - throwerPosition.x);
    denominator += (catcherPosition.y - throwerPosition.y) * (catcherPosition.y - throwerPosition.y);
    denominator = Math.sqrt(denominator);
    return numerator / denominator < INTERCEPTION_DISTANCE && inLineOfThrow(throwerPosition, interceptorPosition, catcherPosition);
  }

  private boolean inLineOfThrow(Point throwerPosition, Point interceptorPosition, Point catcherPosition) {
    double distance = Points.getDistance(throwerPosition, catcherPosition);
    double distanceToThrower = Points.getDistance(interceptorPosition, throwerPosition);
    double distanceToCatcher = Points.getDistance(interceptorPosition, catcherPosition);
    return distanceToThrower < distance && distanceToCatcher < distance;
  }
}