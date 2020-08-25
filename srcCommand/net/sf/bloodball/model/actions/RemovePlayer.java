package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import net.sf.bloodball.model.Field;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Team;

import java.awt.Point;

public class RemovePlayer implements SetupCommand {

    private Field field;
    private Team activeTeam;
    private Point position;

    public RemovePlayer(Game game, Point position) {
        field = game.getField();
        activeTeam = game.getTeams().getActiveTeam();
        this.position = position;
    }

    @Override
    public void execute() {
        Ensuring.parameter(activeTeam.isMember(field.getPlayer(position)), "No removable TeamPlayer at " + position);
        field.removePlayer(position);
    }

    @Override
    public boolean isLegal() {
        return activeTeam.isMember(field.getPlayer(position));
    }

}
