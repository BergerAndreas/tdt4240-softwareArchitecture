package co.aeons.zombie.shooter.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Jukebox {

	private static Jukebox ourInstance = new Jukebox();
	private static HashMap<String, Sound> sounds;
	private static Music ingameMusic;
	private static Music gameoverMusic;

	private static boolean isMuted;
	
	static {
		sounds = new HashMap<String, Sound>();
	}
	
	public static void load(String path, String name) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		sounds.put(name, sound);
		ingameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
		ingameMusic.setLooping(true);
		gameoverMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/gameover.mp3"));
		gameoverMusic.setLooping(true);
		isMuted = false;
	}
	
	public static void play(String name) {
		sounds.get(name).play();
	}

	public static void playIngameMusic() {
		if (!ingameMusic.isPlaying()){
			ingameMusic.play();
		}
	}

	public static void playGameoverMusic() {
		if (!gameoverMusic.isPlaying()){
			gameoverMusic.play();
		}
	}

	public static void muteMusic(String name){
		sounds.get(name).play(0);
	}

	public static void unmuteMusic(String name){
		sounds.get(name).play(1);
	}

	public static void loop(String name) {
		sounds.get(name).loop();
	}
	
	public static void stop(String name) {
		sounds.get(name).stop();
	}
	
	public static void stopAll() {
		for(Sound s : sounds.values()) {
			s.stop();
		}
	}

	public static Jukebox getOurInstance() {
		return ourInstance;
	}

	public static HashMap<String, Sound> getSounds() {
		return sounds;
	}

	public static Music getIngameMusic() {
		return ingameMusic;
	}

	public static Music getGameoverMusic() {
		return gameoverMusic;
	}

	public static boolean isMuted() {
		return isMuted;
	}

	public static void setIsMuted(boolean muted){
		isMuted = muted;
	}

	public static void toggleMuteMusic() {
		if (!isMuted) {
//			Mute
			ingameMusic.setVolume(0);
			setIsMuted(true);
		} else {
//			Unmute
			ingameMusic.setVolume(100);
			setIsMuted(false);
		}
	}

}













