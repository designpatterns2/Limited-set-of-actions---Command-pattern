package net.sf.bloodball.model;

import net.sf.bloodball.resources.ResourceKeys;

public class GameComponents {

	private CompositeComponent components;

	public GameComponents(Game game) {
		components = new CompositeComponent();
		components.add(ResourceKeys.FIELD_KEY, new Field());
		components.add(ResourceKeys.TEAMS_KEY, new Teams(game));
		components.add(ResourceKeys.BALL_KEY, new Ball((Field) components.getChild(ResourceKeys.FIELD_KEY)));
	}

	public void setBall(Ball ball) {
		components.setChild(ResourceKeys.BALL_KEY, ball);
	}

	public Ball getBall() {
		return (Ball) components.getChild(ResourceKeys.BALL_KEY);
	}

	public Field getField() {
		return (Field) components.getChild(ResourceKeys.FIELD_KEY);
	}

	public Teams getTeams() {
		return (Teams) components.getChild(ResourceKeys.TEAMS_KEY);
	}
}