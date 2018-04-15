package co.aeons.zombie.shooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import co.aeons.zombie.shooter.managers.GameInputProcessor;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.managers.Jukebox;
import co.aeons.zombie.shooter.service.ISettingsService;
import co.aeons.zombie.shooter.service.ServiceLocator;
import co.aeons.zombie.shooter.service.network.INetworkService;

public class ZombieShooter extends Game implements INetworkService.IGameListener {

	public static int WIDTH;
	public static int HEIGHT;

	public static OrthographicCamera cam;
	public static Viewport gamePort;

	private GameStateManager gsm;
	private static final String TAG = ZombieShooter.class.getSimpleName();

	//Google play services stuff
	public INetworkService networkService;
	public ISettingsService settingsService;

	//FIXME: Uncomment this to unfuck android
	/*
	public ZombieShooter(INetworkService networkService, ISettingsService settingsService) {
		this.networkService = networkService;
		this.settingsService = settingsService;
	}
	*/

	public void create() {

		WIDTH = 640;
		HEIGHT = 360;

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

		Gdx.input.setInputProcessor(
				new GameInputProcessor()
		);

		Jukebox.load("sounds/explode.ogg", "explode");
		Jukebox.load("sounds/extralife.ogg", "extralife");
		Jukebox.load("sounds/largesaucer.ogg", "largesaucer");
		Jukebox.load("sounds/music.mp3", "despacito");
		Jukebox.load("sounds/Gunshot.mp3", "gunshot");
		Jukebox.load("sounds/ZombieHitSound.mp3", "zombieHit");
		Jukebox.load("sounds/Powerup.mp3", "powerup");
		Jukebox.load("sounds/saucershoot.ogg", "saucershoot");
		Jukebox.load("sounds/shoot.ogg", "shoot");
		Jukebox.load("sounds/smallsaucer.ogg", "smallsaucer");
		Jukebox.load("sounds/thruster.ogg", "thruster");

		//Initialize network and settings service
		//FIXME: To get android working
		/*
		ServiceLocator.initializeAppComponent(networkService, settingsService);
		ServiceLocator.getAppComponent().getNetworkService().setGameListener(this);
		*/
		//		Initialize background music
		Jukebox.playMusic();

		gsm = new GameStateManager();

	}

	public void render() {

		// clear screen to black
		Gdx.gl.glClearColor(0, 255, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.draw();

		GameKeys.update();

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

	@Override
	public void onMultiplayerGameStarting() {
		Gdx.app.debug(TAG, "onMultiplayerGameStarting: ");
		//TODO: Implement multiplayer here
		//setScreen(new MpGamePresenter(this, new MainMenuPresenter(this)));
	}

}
