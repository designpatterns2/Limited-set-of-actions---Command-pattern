package net.sf.bloodball.model.actions.test;

import java.awt.Point;
import net.sf.bloodball.model.FieldExtents;
import net.sf.bloodball.model.actions.HandOff;
import net.sf.bloodball.model.player.Player;

public class HandOffTest extends ActionTest {

  public HandOffTest(String name) {
    super(name);
  }

  private void performLegalHandOffToSquareZeroTwo() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    new HandOff(getGame(), squareZeroOne, squareZeroTwo).execute();
  }

  public void testHandOffChancesBallPosition() {
    performLegalHandOffToSquareZeroTwo();
    assertEquals(squareZeroTwo, getBallPosition());
  }

  public void testHandOffEndsTurn() {
    performLegalHandOffToSquareZeroTwo();
    assertTrue(getGame().getTeams().isActiveTeam(getGuestTeam()));
  }

  public void testIllegalHandOff() {
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    try {
      new HandOff(getGame(), squareOneOne, squareOneTwo).execute();
      fail("IllegalArgumentException expected");
    } catch (IllegalStateException expected) {
    }
  }

  public void testIsLegalToEmptySquare() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(!new HandOff(getGame(), squareZeroOne, squareZeroTwo).isLegal());
  }

  public void testIsLegalToNeighborFriend() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(new HandOff(getGame(), squareZeroTwo, squareZeroOne).isLegal());
  }

  public void testIsLegalToNeighborOpponent() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
    assertTrue(!new HandOff(getGame(), squareZeroTwo, squareZeroOne).isLegal());
  }

  public void testIsLegalToProneFriend() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    getLegalPlayerAt(squareZeroOne).knockOver();
    assertTrue(!new HandOff(getGame(), squareZeroTwo, squareZeroOne).isLegal());
  }

  public void testIsLegalToUnreachableFriend() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroThree);
    assertTrue(!new HandOff(getGame(), squareZeroOne, squareZeroThree).isLegal());
  }

  public void testIsLegalWithoutBall() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareZeroTwo);
    assertTrue(!new HandOff(getGame(), squareZeroOne, squareZeroTwo).isLegal());
  }

  public void testLegalityAfterMove() throws Exception {
    Player player = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    movePlayerToSquare(player, squareOneTwo);
    setPlayerTo(getHomeTeamPlayer(), squareOneThree);
    assertTrue(new HandOff(getGame(), squareOneTwo, squareOneThree).isLegal());
  }
}