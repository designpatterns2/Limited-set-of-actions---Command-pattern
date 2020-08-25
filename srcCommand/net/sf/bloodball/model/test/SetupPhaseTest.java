package net.sf.bloodball.model.test;

import java.awt.Point;
import net.sf.bloodball.model.*;
import net.sf.bloodball.model.actions.*;
import net.sf.bloodball.test.ModelTest;

public class SetupPhaseTest extends ModelTest {

  private Point homeSetupZoneSquare;
  private Point guestSetupZoneSquare;

  public SetupPhaseTest(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    guestSetupZoneSquare = new Point(FieldExtents.GUEST_SETUP_ZONE.getLowerBound(), 0);
    homeSetupZoneSquare = new Point(FieldExtents.HOME_SETUP_ZONE.getLowerBound(), 0);
  }

  private void setupHomePlayerToHomeSetupZoneSquare() {
    setPlayerTo(getHomeTeamPlayer(), homeSetupZoneSquare);
  }

  public void testBallPositionAfterSetup() {
    setPlayerTo(getHomeTeamPlayer(), homeSetupZoneSquare);
    new SetupBall(getGame(), homeSetupZoneSquare).execute();
    assertEquals(homeSetupZoneSquare, getBallPosition());
  }

  public void testIllegalBallSetup() {
    try {
      new SetupBall(getGame(), guestSetupZoneSquare).execute();
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  public void testLegalityOfZones() {
    assertTrue(new SetupPlayer(getGame(), homeSetupZoneSquare).isLegal());
    assertTrue(!new SetupPlayer(getGame(), guestSetupZoneSquare).isLegal());
    beginGameWithGuestTeam();
    assertTrue(new SetupPlayer(getGame(), guestSetupZoneSquare).isLegal());
    assertTrue(!new SetupPlayer(getGame(), homeSetupZoneSquare).isLegal());
  }

  public void testLegalityOfNonemptySquare() {
    setupHomePlayerToHomeSetupZoneSquare();
    assertTrue(!new SetupPlayer(getGame(), homeSetupZoneSquare).isLegal());
  }

  public void testIsLegalBallSetupToBeginningGuestPlayerSquare() {
    beginGameWithGuestTeam();
    setPlayerTo(getGuestTeamPlayer(), guestSetupZoneSquare);
    assertTrue(new SetupBall(getGame(), guestSetupZoneSquare).isLegal());
  }

  public void testIsLegalBallSetupToBeginningHomePlayerSquare() {
    setupHomePlayerToHomeSetupZoneSquare();
    assertTrue(new SetupBall(getGame(), homeSetupZoneSquare).isLegal());
  }

  public void testIsLegalBallSetupToEmptySquare() {
    assertTrue(!(new SetupBall(getGame(), homeSetupZoneSquare).isLegal()));
  }

  public void testIsLegalBallSetupToNonBeginningGuestPlayerSquare() {
    setPlayerTo(getGuestTeamPlayer(), guestSetupZoneSquare);
    assertTrue(!(new SetupBall(getGame(), guestSetupZoneSquare).isLegal()));
  }

  public void testIsLegalBallSetupToNonBeginningHomePlayerSquare() {
    beginGameWithGuestTeam();
    setPlayerTo(getHomeTeamPlayer(), homeSetupZoneSquare);
    assertTrue(!(new SetupBall(getGame(), homeSetupZoneSquare)).isLegal());
  }

  public void testIsLegalGuestPlayerSetup() {
    beginGameWithGuestTeam();
    assertTrue(new SetupBall(getGame(), guestSetupZoneSquare).isLegal());
  }

  public void testIsLegalHomePlayerSetup() {
    assertTrue(new SetupBall(getGame(), homeSetupZoneSquare).isLegal());
  }

  public void testRemovePlayer() {
    assertTrue(!new RemovePlayer(getGame(), homeSetupZoneSquare).isLegal());
    setupHomePlayerToHomeSetupZoneSquare();
    assertTrue(new RemovePlayer(getGame(), homeSetupZoneSquare).isLegal());
    new RemovePlayer(getGame(), homeSetupZoneSquare).execute();
    setPlayerTo(getGuestTeamPlayer(), homeSetupZoneSquare);
    assertTrue(!new RemovePlayer(getGame(), homeSetupZoneSquare).isLegal());
  }

}