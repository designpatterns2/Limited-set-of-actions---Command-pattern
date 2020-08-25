package net.sf.bloodball.model.actions.test;


import net.sf.bloodball.model.actions.Block;
import net.sf.bloodball.model.player.Player;

public class BlockLegalityTest extends ActionTest {
	
	public BlockLegalityTest(String name) {
		super(name);
	}
	
	public void testIsLegalToBlockEmtpyField() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		assertTrue(!new Block(getGame(), squareZeroOne, squareZeroTwo).isLegal());
	}
	
	public void testFarOpponentPlayerNotBlockable() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroThree);
		assertTrue(!new Block(getGame(), squareZeroOne, squareZeroThree).isLegal());
	}
	
	public void testMayBlockWithOpponentNeighbor() throws Exception {
		Player blocker = setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
		assertTrue(block.mayBlock(blocker));
	}
	
	public void testNeighborOpponentPlayerBlockable() {
		setPlayerTo(getHomeTeamPlayer(), squareZeroOne);
		setPlayerTo(getGuestTeamPlayer(), squareZeroTwo);
		assertTrue(new Block(getGame(), squareZeroOne, squareZeroTwo).isLegal());
	}
	
	public void testSelfNotBlockable() {
		setPlayerTo(getGuestTeamPlayer(), squareZeroOne);
		assertTrue(!new Block(getGame(), squareZeroOne, squareZeroOne).isLegal());
	}

}
