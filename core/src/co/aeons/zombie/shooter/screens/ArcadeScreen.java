package co.aeons.zombie.shooter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.GameScreen;
import co.aeons.zombie.shooter.gameObjects.Button;
import co.aeons.zombie.shooter.gameObjects.arcadeMode.ArcadeShip;
import co.aeons.zombie.shooter.utils.*;
import co.aeons.zombie.shooter.utils.enums.DialogBoxState;
import co.aeons.zombie.shooter.utils.enums.GameState;

public class ArcadeScreen extends GameScreen {

	private final SpaceGame game;

	//Tiempo que bloquearemos el cambio de capa cada vez que se realice un cambio
	private static final float MAX_TIME_TO_BLOCK = 1f;
	//Tiempo máximo hasta que actualicemos lastMeasureY
	private static final float TIME_TO_MEASURE_Y = 0.2f;
	//Mínimo alpha para las transparencias de los obstacles
	private static final float MIN_ALPHA = 0.3f;
	//Indica el escalado que tendrán los objetos en la capa baja
	private static final float BOTTOM_SCALE = 0.5f;

	//Objetos interactuables de la pantalla
	private ArcadeShip ship;

	//Guarda la última medición del acelerómetro Y, en intervalos de MAX_TIME_TO_MEASURE_Y
	private float lastMeasureY;

	//Contador para que indica cada cuánto actualizaremos lastMeasureY
	private float timeToMeasureY;

	//Contador que indica si estamos en el cambio de capa, y por tanto hay que bloquear otra posibilidad de cambio
	private float timeToBlock;

	//Indica la capa en la que se está, indicando '-1' la capa de abajo y '1' la capa de arriba
	public static int layer;

	//Acumula el tiempo que está vivo el jugador en la partida
	private float timeAlive;

	//Botón que nos permitirá salir del juego
	private Button exit;

	//Guardará el record del jugador para mostrarlo en pantalla
	private int record;

	//Indicará si en la partida ha habido nuevo record
	private boolean newRecord;

	//Cuadro de diálogo que se preguntará confirmación para salir del modo
	private DialogBox menuExitDialog;

	//Cuadro de diálogo que se preguntará confirmación para salir del modo
	private DialogBox menuSubmitRecord;

	public ArcadeScreen(final SpaceGame game) {
		this.game = game;

		//Convertimos la pantalla en modo portrait
		SpaceGame.changeToPortrait();

		//Avisamos al background manager de qué fondo queremos
		BackgroundManager.changeCurrentBackgrounds(BackgroundManager.BackgroundType.ARCADE);

		CameraManager.loadShakeEffect(1f,CameraManager.NORMAL_SHAKE);
		exit = new Button("buttonExit", SpaceGame.width - 50, SpaceGame.height - 50, null, true);
		menuExitDialog = new DialogBox("exitModeQuestion");
		menuSubmitRecord = new DialogBox("submitRecordQuestion");

		this.initialize();
	}

	//Inicializa el estado del juego cada vez que se comienza una nueva partida
	private void initialize() {
		ship = new ArcadeShip();
		state = GameState.READY;
		ObstacleManager.load();
		timeToMeasureY = 0;
		lastMeasureY = Gdx.input.getAccelerometerY();
		timeToBlock = 0;
		layer = 1;
		timeAlive = 0;
		record = ArcadeScreen.obtainRecord();
	}

	//Recoge el record, y si no hay ninguno coge un 0 por defecto
	public static int obtainRecord() {
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		return prefs.getInteger("record", 0);
	}

	//Guarda el record conseguido si supera el timeAlive, y devuelve true en caso de ser así
	private boolean updateRecord() {
		boolean res = false;

		if (timeAlive > record) {
			saveRecord((int) timeAlive);
			res = true;
		}

		return res;
	}

	//Resetea el record a 0
	public static void resetRecord() {
		saveRecord(0);
	}

	//Hace persistente el value del parámetro dentro del record del juego
	private static void saveRecord(int value) {
		Preferences prefs = Gdx.app.getPreferences("My Preferences");
		prefs.putInteger("record", value);
		prefs.flush();
	}

	public float getTimeAlive() {
		return timeAlive;
	}

	@Override
	public void renderEveryState(float delta) {
	}

	@Override
	public void updateEveryState(float delta) {
		//Si el estado no está en pausa, actualizamos la vibración de la pantalla
		if (!state.equals(GameState.PAUSE)) {
			CameraManager.update(delta);
		}
	}

	@Override
	public void renderReady(float delta) {
		FontManager.drawText("tapToStart", 100, 100);
	}

	@Override
	public void updateReady(float delta) {
		//Si se toca la pantalla, pasamos a START
		if (Gdx.input.justTouched()) {
			state = GameState.START;
		}
	}

	@Override
	public void renderStart(float delta) {
		//Primero realizamos el cálculo del alpha según el escalado actual de la nave
		float alpha = (ship.getLogicShape().getScaleX() - BOTTOM_SCALE) / (1 - BOTTOM_SCALE);
		alpha *= (1 - MIN_ALPHA);
		alpha += MIN_ALPHA;

		//Pintamos naves y obstáculos según orden y el alpha correspondiente para cada uno
		BackgroundManager.render(0);
		BackgroundManager.render(1);
		ObstacleManager.renderBottom((1 + MIN_ALPHA) - alpha);
		if (ship.getLogicShape().getScaleX() > 0.75) {
			BackgroundManager.render(2);
			ObstacleManager.renderTop(alpha);
			ship.render();
		} else {
			ship.render();
			BackgroundManager.render(2);
			ObstacleManager.renderTop(alpha);
		}


		this.renderTime();
	}

	@Override
	public void updateStart(float delta) {
		//Actualizamos nave, obstáculos y las capas
		ship.update(delta);
		ObstacleManager.update(delta);

		//Hasta que no pase un segundo, no podemos pasar de capa
		if (timeAlive > 1) {
			this.updateLayers(delta);
		}

		//Actualizamos el tiempo
		timeAlive += delta;

		//Por último, comprobamos si hay colisión con la nave
		//Si no hay colisión, comprobamos si se ha tocado la pantalla para entrar en pause
		if (ObstacleManager.existsCollision(ship, layer)) {
			//Aplicamos el estado de haber terminado la partida
			ship.defeat();
			newRecord = this.updateRecord();

			//Realizamos los efectos visuales y de sonido
			Gdx.input.vibrate(300);
			CameraManager.startShake();

			//Si hemos conseguido nuevo record se considerará victoria, en caso contrario, derrota
			if (newRecord) {
				state = GameState.WIN;
				AudioManager.playSound("new_record");
			} else {
				state = GameState.LOSE;
				AudioManager.playSound("arcade_shock_effect");
			}
		} else if (Gdx.input.justTouched()) {
			state = GameState.PAUSE;
			AudioManager.pauseMusic();
			AudioManager.playSound("pause");
		}
	}

	@Override
	public void renderPause(float delta) {
		//Mostramos lo que queremos
		FontManager.drawText("pause", SpaceGame.height / 2);
		FontManager.drawText("record", ": " + record, 10, 760);
		ship.render();
		this.renderTime();

		//Se mostrará el menuExitDialog si no está HIDDEN, y si lo está, mostramos el botón exit
		if (menuExitDialog.getState().equals(DialogBoxState.HIDDEN))
			exit.render();
		else
			menuExitDialog.render();
	}

	@Override
	public void updatePause(float delta) {
		//Haremos una cosa u otra dependiendo del estado de menuExitDialog
		if (menuExitDialog.getState().equals(DialogBoxState.HIDDEN)) {
			exit.update();

			//Si se ha tocado la pantalla actuamos según si se ha tocado el botón exit u otro lado
			if (Gdx.input.justTouched()) {
				if (exit.isPressed()) {
					menuExitDialog.setStateToWaiting();
				} else {
					state = GameState.START;
					AudioManager.playSound("pause");
				}
			}
		} else if (menuExitDialog.getState().equals(DialogBoxState.CONFIRMED)) {
			ScreenManager.changeScreen(game, MainMenuScreen.class);
			this.disposeScreen();
		} else if (menuExitDialog.getState().equals(DialogBoxState.CANCELLED)) {
			menuExitDialog.setStateToHidden();
			exit.setPressed(false);
		} else if (menuExitDialog.getState().equals(DialogBoxState.WAITING)) {
			menuExitDialog.update();
		}
	}

	@Override
	public void renderWin(float delta) {
		renderStart(delta);
		if (menuSubmitRecord.getState().equals(DialogBoxState.HIDDEN))
			FontManager.drawText("newRecord", 400);
		else
			menuSubmitRecord.render();
	}

	@Override
	public void updateWin(float delta) {
		ship.update(delta);

		//Haremos una cosa u otra dependiendo del estado de menuExitDialog
		if (menuSubmitRecord.getState().equals(DialogBoxState.HIDDEN)) {

			//Si tocamos la pantalla, mostramos el cuadro de diálogo
			if (Gdx.input.justTouched()) {
				menuSubmitRecord.setStateToWaiting();
			}
		} else if (menuSubmitRecord.getState().equals(DialogBoxState.CONFIRMED)) {
			//Si confirmamos escondemos la ventana, subimos el record, lo mostramos y volvemos a READY
			menuSubmitRecord.setStateToHidden();
			SpaceGame.googleServices.submitScore((long) timeAlive);
			//SpaceGame.googleServices.showScores();
			this.initialize();
		} else if (menuSubmitRecord.getState().equals(DialogBoxState.CANCELLED)) {
			menuSubmitRecord.setStateToHidden();
			this.initialize();
		} else if (menuSubmitRecord.getState().equals(DialogBoxState.WAITING)) {
			menuSubmitRecord.update();
		}
	}

	@Override
	public void renderLose(float delta) {
		renderStart(delta);
		FontManager.drawText("gameOver", 400);
	}

	@Override
	public void updateLose(float delta) {
		ship.update(delta);

		//Deberemos volver a tocar la pantalla para reiniciar la partida
		if (Gdx.input.justTouched()) {
			this.initialize();
		}
	}

	public void updateLayers(float delta) {
		//Comprobamos si ha pasado el tiempo de bloqueo para poder cambiar de capa
		//Si no ha pasado el tiempo, significará que estamos en la transición de un cambio de capa
		if (timeToBlock <= 0) {

			//Si la distancia entre la medición actual y la última del acelerómetro Y es mayor a 3
			//significa que por tanto bloqueamos el cambio de capa
			if (Math.abs(Gdx.input.getAccelerometerY() - lastMeasureY) > 3)
				timeToBlock = MAX_TIME_TO_BLOCK;

			//Actualizamos el tiempo para poder volver a medir el acelerómetro Y
			timeToMeasureY += delta;

			//Si ya se ha cumplido el tiempo de la última medición, volvemos a medir y reseteamos el contador
			if (timeToMeasureY > TIME_TO_MEASURE_Y) {
				lastMeasureY = Gdx.input.getAccelerometerY();
				timeToMeasureY = 0;
			}
		} else {
			//Entraremos por aquí si estamos realizando el cambio de capa

			//Actualizamos el tiempo de bloqueo
			timeToBlock -= delta;

			//Comprobamos si el tiempo de bloqueo ha terminado, y si es así, significa que ha terminado la transición de cambio
			if (timeToBlock <= 0) {

				//Recolocamos la nave en su posición final dependiendo de la capa destino
				if (layer == 1)
					ship.setScale(BOTTOM_SCALE, BOTTOM_SCALE);
				else
					ship.setScale(1, 1);

				//Cambiamos finalmente la capa en la que estamos y volvemos a medir el acelerómetro Y
				layer *= -1;
				lastMeasureY = Gdx.input.getAccelerometerY();
			} else {
				//Si no ha terminado el tiempo de bloqueo, transicionamos la posición de la nave a la nueva capa
				ship.setScale(ship.getLogicShape().getScaleX() + (BOTTOM_SCALE * delta * layer * -1),
							  ship.getLogicShape().getScaleY() + (BOTTOM_SCALE * delta * layer * -1));
			}
		}
	}

	private void renderTime() {
		FontManager.drawText("time", ": " + ((int) timeAlive), 10, 790);
	}

	@Override
	public void disposeScreen() {
		super.dispose();
		ship.dispose();
	}
}