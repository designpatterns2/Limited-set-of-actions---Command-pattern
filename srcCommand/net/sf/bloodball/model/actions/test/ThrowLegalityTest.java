package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.actions.Throw;
import net.sf.bloodball.model.player.Player;

public class ThrowLegalityTest extends ActionTest {

  public ThrowLegalityTest(String name) {
    super(name);
  }

  public void testLegalWithEmptyField() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    assertTrue(!new Throw(getGame(), squareZeroOne, squareTwoOne).isLegal());
  }

  public void testLegalWithInReachGuestTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getGuestTeamPlayer(), squareTwoOne);
    assertTrue(!new Throw(getGame(), squareZeroOne, squareTwoOne).isLegal());
  }

  public void testLegalWithInReachHomeTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareTwoOne);
    assertTrue(new Throw(getGame(), squareZeroOne, squareTwoOne).isLegal());
  }

  public void testLegalWithInReachProneHomeTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    Player player = setPlayerTo(getHomeTeamPlayer(), squareTwoOne);
    player.knockOver();
    assertTrue(!new Throw(getGame(), squareZeroOne, squareTwoOne).isLegal());
  }

  public void testLegalWithMaximalReach() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareSixteenOne);
    assertTrue(new Throw(getGame(), squareZeroOne, squareSixteenOne).isLegal());
  }

  public void testLegalWithNeighborSquare() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareOneOne);
    assertTrue(!new Throw(getGame(), squareZeroOne, squareOneOne).isLegal());
  }

  public void testLegalWithoutBall() {
    setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
    setPlayerTo(getHomeTeamPlayer(), squareTwoOne);
    assertTrue(!new Throw(getGame(), squareZeroOne, squareTwoOne).isLegal());
  }

  public void testLegalWithOutOfReachHomeTeamPlayer() {
    setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    setPlayerTo(getHomeTeamPlayer(), squareEighteenOne);
    assertTrue(!new Throw(getGame(), squareOneOne, squareEighteenOne).isLegal());
  }
  
  public void testLegalityAfterMove() throws Exception {
    Player player = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneOne);
    movePlayerToSquare(player, squareOneTwo);
    setPlayerTo(getHomeTeamPlayer(), squareOneFour);
    assertTrue(!new Throw(getGame(), squareOneTwo, squareOneFour).isLegal());
  }
}