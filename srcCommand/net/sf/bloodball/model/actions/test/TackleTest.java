package net.sf.bloodball.model.actions.test;

import net.sf.bloodball.model.actions.Move;
import net.sf.bloodball.model.player.Player;

public class TackleTest extends ActionTest {
	
	public static void main(String[] args) {
		new junit.swingui.TestRunner().run(TackleTest.class);
	}

	public TackleTest(String name) {
		super(name);
	}
	
	private void setFailingTackleDice() {
		setD6Result(4);
	}
	
	private void setSucceedingTackleDice() {
		setD6Result(5);
	}
	
	public void testFailedTackle() {
		setFailingTackleDice();
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		new Move(getGame(), squareZeroOne, squareOneOne).execute();
		assertTrue(!actor.isProne());
	}
	
	public void testMoveIntoTackleZone() {
		setSucceedingTackleDice();
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareTwoTwo);
		setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
		new Move(getGame(), squareTwoTwo, squareOneTwo).execute();
		assertTrue(!actor.isProne());
	}
	
	public void testMoveNextToProneOpponent() {
		setSucceedingTackleDice();
		setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroFour);
		getPlayerAt(squareZeroFour).knockOver();
		new Move(getGame(), squareOneTwo, squareOneThree).execute();
		assertTrue(!getPlayerAt(squareOneThree).isProne());
	}
	
	public void testMoveOutOffTackleZone() {
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setSucceedingTackleDice();
		new Move(getGame(), squareOneTwo, squareTwoTwo).execute();
		assertTrue(!getPlayerAt(squareTwoTwo).isProne());
	}
	
	public void testNoTackleWithSameTeam() {
		setSucceedingTackleDice();
		Player actor = setPlayerTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		new Move(getGame(), squareZeroOne, squareOneOne).execute();
		assertTrue(!actor.isProne());
	}
	
	public void testSuccessfulTackle() {
		setSucceedingTackleDice();
		Player actor = setPlayerWithBallTo(getHomeTeamPlayer(), squareOneTwo);
		setPlayerTo(getGuestTeamPlayer(), squareOneOne);
		new Move(getGame(), squareOneTwo, squareTwoTwo).execute();
		assertTrue(actor.isProne());
		assertTrue(getBallPosition() != squareTwoTwo);
	}
	
}
