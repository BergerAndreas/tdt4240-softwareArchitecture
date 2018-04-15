package co.aeons.zombie.shooter.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.ZombieShooter;

public class MenuState extends GameState {

	// Cameras and viewport
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	private Stage stage;

	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private BitmapFont titleFont;
	private Skin skin;
	
	private final String title = "Ugly Z";
	
	private int currentItem;
	private String[] menuItems;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {

		this.gameCam = new OrthographicCamera();

		// Initializes a new viewport
		this.gamePort = new FitViewport(
				ZombieShooter.WIDTH,
				ZombieShooter.HEIGHT,
				gameCam
		);
		gamePort.apply();

		//sets up camera
		gameCam.position.set(this.gameCam.viewportWidth / 2, this.gameCam.viewportHeight / 2, 0);
		gameCam.update();
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		stage = new Stage(gamePort, sb);

		titleFont = new BitmapFont();
		titleFont.setColor(Color.WHITE);
		titleFont.getData().setScale(2);
		menuItems = new String[] {
			"Play",
			"Highscores",
			"Quit"
		};

		skin = new Skin(Gdx.files.internal("skins/neutralizer-ui.json"));
		Gdx.input.setInputProcessor(this.stage);
		

	}
	
	public void update(float dt) {
		
		handleInput();

	}
	
	public void draw() {
		
		sb.setProjectionMatrix(ZombieShooter.cam.combined);
		sr.setProjectionMatrix(ZombieShooter.cam.combined);

		sb.begin();

		float width = titleFont.getSpaceWidth();
		titleFont.draw(
				sb,
				title,
				(ZombieShooter.WIDTH-width)/2,
				350
		);
		sb.end();

		Table mainTable = new Table();
		//Set table to fill stage
		mainTable.setFillParent(true);
		//Set alignment of contents in the table.
		mainTable.center();
		//Create buttons

		TextButton singleplayerButton = new TextButton("Singleplayer", skin);
		TextButton multiplayerButton = new TextButton("Multiplayer", skin);
		TextButton optionsButton = new TextButton("Options", skin);
		TextButton exitButton = new TextButton("Exit", skin);

//Add listeners to buttons
		singleplayerButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gsm.setState(GameStateManager.PLAY);
			}
		});
		//TODO: Add multiplayer listener
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});


		//Add buttons to table
		mainTable.add(singleplayerButton);
		mainTable.row();
		mainTable.add(multiplayerButton);
		mainTable.row();
		mainTable.add(optionsButton);
		mainTable.row();
		mainTable.add(exitButton);
		stage.addActor(mainTable);
		//Make stage show stuff
		this.stage.act();
		this.stage.draw();
	}

	public void handleInput() {

		if(GameKeys.isPressed(GameKeys.UP)) {
			if(currentItem > 0) {
				currentItem--;
			}
		}
		if(GameKeys.isPressed(GameKeys.DOWN)) {
			if(currentItem < menuItems.length - 1) {
				currentItem++;
			}
		}
		if(GameKeys.isPressed(GameKeys.ENTER)) {
			select();
		}

	}

	private void select() {
		// play
		if(currentItem == 0) {
			gsm.setState(GameStateManager.PLAY);
		}
		// high scores
		else if(currentItem == 1) {
			gsm.setState(GameStateManager.HIGHSCORE);
		}
		else if(currentItem == 2) {
			Gdx.app.exit();
		}
	}
	
	public void dispose() {
		sb.dispose();
		sr.dispose();
		titleFont.dispose();
		stage.dispose();
	}

}










