package co.aeons.zombie.shooter.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import co.aeons.zombie.shooter.entities.Asteroid;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;
import co.aeons.zombie.shooter.zombie.shooter.ZombieShooter;

public class MenuState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private BitmapFont titleFont;
	private BitmapFont font;
	
	private final String title = "Asteroids";
	
	private int currentItem;
	private String[] menuItems;
	
	private ArrayList<Asteroid> asteroids;
	
	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		



		menuItems = new String[] {
			"Play",
			"Highscores",
			"Quit"
		};
		
		asteroids = new ArrayList<Asteroid>();
		for(int i = 0; i < 6; i++) {
			asteroids.add(
				new Asteroid(
					MathUtils.random(ZombieShooter.WIDTH),
					MathUtils.random(ZombieShooter.HEIGHT),
					Asteroid.LARGE
				)
			);
		}
		

	}
	
	public void update(float dt) {
		
		handleInput();
		
		for(int i = 0 ; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
		}
		
	}
	
	public void draw() {
		
		sb.setProjectionMatrix(ZombieShooter.cam.combined);
		sr.setProjectionMatrix(ZombieShooter.cam.combined);
		
		// draw asteroids
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
		sb.begin();

		
		sb.end();
		
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
		font.dispose();
	}

}










