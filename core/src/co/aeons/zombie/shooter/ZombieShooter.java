package co.aeons.zombie.shooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;

import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.managers.Save;

public class ZombieShooter extends Game {

	public static int WIDTH;
	public static int HEIGHT;
	public static final String TITLE = "Ugly Z";

	public static OrthographicCamera cam;
	public static Viewport gamePort;
	public static File filesDir;

	private GameStateManager gsm;

	//Google play
	public static Platform platform;
	public static IGoogleServices googleServices;


	public ZombieShooter(Platform platform, IGoogleServices googleServices, File filesDir) {
		this.platform = platform;
		ZombieShooter.googleServices = googleServices;
		this.filesDir = filesDir;
	}


	public void create() {

		WIDTH = 640;
		HEIGHT = 360;
		Gdx.graphics.setTitle(TITLE);

		cam = new OrthographicCamera(WIDTH, HEIGHT);
		cam.translate(WIDTH / 2, HEIGHT / 2);
		cam.update();

		// Initializes a new viewport
		gamePort = new FitViewport(
				WIDTH,
				HEIGHT,
				cam
		);
		gamePort.apply();

//		In-game audio
		Jukebox.load("sounds/music.mp3", "despacito");
		Jukebox.load("sounds/gameover.mp3", "gameover");
		Jukebox.load("sounds/Gunshot.mp3", "gunshot");
		Jukebox.load("sounds/ZombieHitSound.mp3", "zombieHit");
		Jukebox.load("sounds/Powerup.mp3", "powerup");
		Jukebox.load("sounds/elite.mp3", "elite");
		Jukebox.load("sounds/blyat.mp3", "blyat");
		Jukebox.load("sounds/drrr.mp3", "drrr");
		Jukebox.load("sounds/pom.mp3", "pom");
		Jukebox.load("sounds/quickMafs.mp3", "quickMafs");
		Jukebox.load("sounds/rawSauce.mp3", "rawSauce");
		Jukebox.load("sounds/skiddipipopop.mp3", "skiddipipopop");
		Jukebox.load("sounds/skrra.mp3", "skrra");
		Jukebox.load("sounds/skya.mp3", "skya");
		Jukebox.load("sounds/pistolReload.wav", "pistolReload");
		Jukebox.load("sounds/brReload.wav", "brReload");
		Jukebox.load("sounds/shotgunReload.wav", "shotgunReload");
		Jukebox.load("sounds/china.mp3", "china");

		//		Initialize background music
		Jukebox.playIngameMusic();

//		Load save file
		Save.load();

		gsm = new GameStateManager();
	}

	public void render() {

		// clear screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();
	}

	public void resize(int width, int height) {
		// Ensures resizing works properly
		gamePort.update(width, height);
		cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
		cam.update();
	}
	public void pause() {}
	public void resume() {}
	public void dispose() {}


}
