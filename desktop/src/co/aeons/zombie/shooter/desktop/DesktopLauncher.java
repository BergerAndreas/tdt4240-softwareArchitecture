package co.aeons.zombie.shooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;

import co.aeons.zombie.shooter.ZombieShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		new LwjglApplication(new ZombieShooter(null, new DesktopGoogleServices(), getFilesDir()), config);
	}

	public static File getFilesDir() {
		return new File("");
	}
}
