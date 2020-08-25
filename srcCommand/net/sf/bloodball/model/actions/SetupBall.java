package net.sf.bloodball.model.actions;

import de.vestrial.util.error.Ensuring;
import net.sf.bloodball.model.Ball;
import net.sf.bloodball.model.Field;
import net.sf.bloodball.model.Game;
import net.sf.bloodball.model.player.Team;

import java.awt.Point;

public class SetupBall implements SetupCommand {

    private Ball ball;
    private Field field;
    private Team offensiveTeam;
    private Point position;

    public SetupBall(Game game, Point position) {
        ball = game.getBall();
        field = game.getField();
        offensiveTeam = game.getTeams().getOffensiveTeam();
        this.position = position;
    }

    @Override
    public void execute() {
        Ensuring.parameter(offensiveTeam.isMember(field.getPlayer(position)), "No legal ball position: " + position);
        if (ball.getPosition() != null) {
            field.getPlayer(ball.getPosition()).dropBall();
        }
        ball.setPosition(position);
        field.getPlayer(position).pickUpBall();
    }

    @Override
    public boolean isLegal() {
        return offensiveTeam.isMember(field.getPlayer(position));
    }

}
