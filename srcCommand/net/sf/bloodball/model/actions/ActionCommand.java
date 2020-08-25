package net.sf.bloodball.model.actions;

import java.awt.*;

public interface ActionCommand {
    void execute();
    boolean isLegal();
    boolean endsTeamTurn();
}
