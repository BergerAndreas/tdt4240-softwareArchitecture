package co.aeons.zombie.shooter.gamestates;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.managers.GameKeys;
import co.aeons.zombie.shooter.managers.GameStateManager;


public class GameOverState extends GameState {
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private boolean newHighScore;
	private char[] newName;
	private int currentChar;
	
	private BitmapFont gameOverFont;
	private BitmapFont font;
	
	public GameOverState(GameStateManager gsm) {
		super(gsm);
	}
	
	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();

		
	}
	
	public void update(float dt) {
		handleInput();
	}
	
	public void draw() {
		
		sb.setProjectionMatrix(ZombieShooter.cam.combined);
		
		sb.begin();
		
		String s;
		float w;
		
		s = "Game Over";

		if(!newHighScore) {
			sb.end();
			return;
		}
		

		for(int i = 0; i < newName.length; i++) {
			font.draw(
				sb,
				Character.toString(newName[i]),
				230 + 14 * i,
				120
			);
		}
		
		sb.end();
		
		sr.begin(ShapeType.Line);
		sr.line(
			230 + 14 * currentChar,
			100,
			244 + 14 * currentChar,
			100
		);
		sr.end();
		
	}
	
	public void handleInput() {
		
		if(GameKeys.isPressed(GameKeys.ENTER)) {

			gsm.setState(GameStateManager.MENU);
		}
		
		if(GameKeys.isPressed(GameKeys.UP)) {
			if(newName[currentChar] == ' ') {
				newName[currentChar] = 'Z';
			}
			else {
				newName[currentChar]--;
				if(newName[currentChar] < 'A') {
					newName[currentChar] = ' ';
				}
			}
		}
		
		if(GameKeys.isPressed(GameKeys.DOWN)) {
			if(newName[currentChar] == ' ') {
				newName[currentChar] = 'A';
			}
			else {
				newName[currentChar]++;
				if(newName[currentChar] > 'Z') {
					newName[currentChar] = ' ';
				}
			}
		}
		
		if(GameKeys.isPressed(GameKeys.RIGHT)) {
			if(currentChar < newName.length - 1) {
				currentChar++;
			}
		}
		
		if(GameKeys.isPressed(GameKeys.LEFT)) {
			if(currentChar > 0) {
				currentChar--;
			}
		}
		
	}
	
	public void dispose() {
		sb.dispose();
		sr.dispose();
		gameOverFont.dispose();
		font.dispose();
	}
	
}









