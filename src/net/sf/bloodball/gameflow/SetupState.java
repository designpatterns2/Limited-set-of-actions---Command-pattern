package net.sf.bloodball.gameflow;

import java.awt.Point;

import net.sf.bloodball.model.actions.RemovePlayer;
import net.sf.bloodball.model.actions.SetupActionMethods;
import net.sf.bloodball.model.actions.SetupPlayer;
import net.sf.bloodball.model.player.Player;

public abstract class SetupState extends State {
  private SetupActionMethods setupActionMethods;

  public SetupState(GameFlowController context) {
    super(context);
    setupActionMethods = new SetupActionMethods(context.getGame());
  }

  protected void putPlayer(Point position, Player player) {
    new SetupPlayer(getGame(), position, player).execute();
  }

  protected void removePlayer(Point position) {
    new RemovePlayer(getGame(), position).execute();
    setMayEndTurn(false);
  }

  public SetupActionMethods getSetup() {
    return setupActionMethods;
  }

  protected void setSetup(SetupActionMethods setupActionMethods) {
    this.setupActionMethods = setupActionMethods;
  }

}