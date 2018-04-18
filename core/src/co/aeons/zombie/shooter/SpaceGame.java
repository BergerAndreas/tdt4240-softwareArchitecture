package co.aeons.zombie.shooter;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import co.aeons.zombie.shooter.screens.MainMenuScreen;
import co.aeons.zombie.shooter.screens.MultiplayerScreen;
import co.aeons.zombie.shooter.utils.*;

public class SpaceGame extends Game {

	// Objeto encargado de la renderización del juego
	public static SpriteBatch batch;
	// Con esto vamos a crear un entorno ortonormal 2d y añadirlo al spritebatch
	public static OrthographicCamera camera;
	// Sirve para permitir cambiar la orientación de la pantalla
	public static Platform platform;

	// Ancho y alto de la pantalla para la camara ortonormal
	public static int width;
	public static int height;

	//Orientación del dispositivo
	public static String orientation;

	// Objeto encargado de obtener las shapes
	private static ShapeLoader shapeLoader;

	public static IGoogleServices googleServices;

	public SpaceGame(Platform platform, IGoogleServices googleServices) {
		this.platform = platform;
		SpaceGame.googleServices = googleServices;
	}

	@Override
	public void create () {
		width = 800;
		height = 480;
		orientation = "sensorLandscape";

		AssetsManager.load();
		AudioManager.load();
		BackgroundManager.load();
		TouchManager.initialize();
		ShapeRendererManager.initialize();
		FontManager.initialize(width);
		shapeLoader = ShapeLoader.initialize("shapeEntities");

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SpaceGame.width, SpaceGame.height);

		BasicScreen.activeInitialAttenuation();
		ScreenManager.changeScreen(this, MainMenuScreen.class);
		ShapeRendererManager.initialize();
	}

	public void render() {
		super.render();
	}

	//Convierte la pantalla en modo portrait
	public static void changeToPortrait() {
		if (!orientation.equals("portrait")) {
			orientation = "portrait";
			if (platform != null)
				platform.setOrientation("portrait");

			if (width > height)
				exchangeWidthHeight();
		}
	}

	//Convierte la pantalla en modo landscape
	public static void changeToLandscape() {
		if (!orientation.equals("sensorLandscape")) {
			orientation = "sensorLandscape";
			if (platform != null)
				platform.setOrientation("sensorLandscape");

			if (height > width)
				exchangeWidthHeight();
		}
	}

	//Intercambia el valor de width con height y viceversa
	private static void exchangeWidthHeight() {
		int newWidth = height;
		int newHeight = width;

		width = newWidth;
		height = newHeight;

		Gdx.graphics.setWindowedMode(newWidth, newHeight);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
	}

	/**
	 * Método que se encarga de obtener los vertices que forman una shape concreta
	 */
	public static float[] loadShape(String entity){
		return shapeLoader.getVertices(entity);
	}

	public static float[] loadDesiredSize(String entity) {return shapeLoader.getDesiredSize(entity);}

	public void dispose() {
		batch.dispose();
	}
}