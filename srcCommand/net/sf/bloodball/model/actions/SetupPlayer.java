package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import net.sf.bloodball.model.Field;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.Teams;
import net.sf.bloodball.model.player.Player;

import java.awt.Point;

public class SetupPlayer implements SetupCommand {

    private Point position;
    private Field field;
    private Player setupPlayer;
    private Teams teams;
    private SetupActionMethods setupActionMethods;

    public SetupPlayer(Game game, Point position) {
        this.position = position;
        this.field = game.getField();
        this.teams = game.getTeams();
        setupActionMethods = new SetupActionMethods(game);
    }

    public SetupPlayer(Game game, Point position, Player setupPlayer) {
        this.position = position;
        this.field = game.getField();
        this.setupPlayer = setupPlayer;
        this.teams = game.getTeams();
        setupActionMethods = new SetupActionMethods(game);
    }

    @Override
    public void execute() {
        Ensuring.parameter(isLegal(), position + " isn't a legal setup square.");
        field.setPlayer(position, setupPlayer);
    }

    @Override
    public boolean isLegal() {
        return (field.inSetupZone(position, teams.getActiveTeam())
                        && setupActionMethods.isNotOccupied(position)
                        && setupActionMethods.maySetup(teams.getActiveTeam()));
    }
}
