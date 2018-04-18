package co.aeons.zombie.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import co.aeons.zombie.shooter.DesktopGoogleServices;
import co.aeons.zombie.shooter.SpaceGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 480;
		config.title = "Space Game";
		new LwjglApplication(new SpaceGame(null, new DesktopGoogleServices()), config);
	}
}
