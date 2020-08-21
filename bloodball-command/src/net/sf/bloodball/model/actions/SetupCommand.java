package net.sf.bloodball.model.actions;

public interface SetupCommand {
    void execute();
    boolean isLegal();
}
