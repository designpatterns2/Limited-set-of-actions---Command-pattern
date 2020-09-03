package net.sf.bloodball;

import net.sf.bloodball.resources.ResourceHandler;
import net.sf.bloodball.resources.ResourceKeys;
import net.sf.bloodball.view.SplashScreen;

import java.util.Locale;

public class BloodBall {

  public static final String VERSION = "1.5";

  private BloodBall() {
  }

  public static void main(String[] args) {
    if (args.length > 1) {
      System.out.println(ResourceHandler.getString(ResourceKeys.ERROR_MESSAGE_ARGUMENT_COUNT));
      System.exit(1);
    }
    SplashScreen splashScreen = new SplashScreen();
    splashScreen.show();
    if (args.length == 1) {
      Locale.setDefault(new Locale(args[0]));
    }
    GameController controller = new GameController();
    splashScreen.dispose();
    controller.showFrame();
  }
}