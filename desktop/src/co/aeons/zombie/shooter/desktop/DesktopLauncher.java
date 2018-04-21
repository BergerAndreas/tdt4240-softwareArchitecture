package co.aeons.zombie.shooter.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.io.File;

import co.aeons.zombie.shooter.ZombieShooter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		new LwjglApplication(new ZombieShooter(null, new DesktopGoogleServices(), getFilesDir()), config);
		config.addIcon("icons/uglyZ-16.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-32.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-48.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-64.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-72.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-96.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-128.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-144.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-192.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-256.png", Files.FileType.Internal);
		config.addIcon("icons/uglyZ-512.png", Files.FileType.Internal);
	}

	public static File getFilesDir() {
		return new File("");
	}
}
