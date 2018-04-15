package co.aeons.zombie.shooter.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class Jukebox {

    private static Jukebox ourInstance = new Jukebox();
    private static HashMap<String, Sound> sounds;
    private static Music music;

    private static boolean isMuted;

    static {
        sounds = new HashMap<String, Sound>();
    }

    public static void load(String path, String name) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(name, sound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        isMuted = false;
    }

    public static void play(String name) {
        sounds.get(name).play();
    }

    public static void playMusic() {
        music.play();
        if (!music.isPlaying()) {
            music.play();
        }
    }

    public static void muteMusic(String name) {
        sounds.get(name).play(0);
    }

    public static void unmuteMusic(String name) {
        sounds.get(name).play(1);
    }

    public static void loop(String name) {
        sounds.get(name).loop();
    }

    public static void stop(String name) {
        sounds.get(name).stop();
    }

    public static void stopAll() {
        for (Sound s : sounds.values()) {
            s.stop();
        }
    }

    public static Jukebox getOurInstance() {
        return ourInstance;
    }

    public static HashMap<String, Sound> getSounds() {
        return sounds;
    }

    public static Music getMusic() {
        return music;
    }

    public static boolean isMuted() {
        return isMuted;
    }

    public static void setIsMuted(boolean muted) {
        isMuted = muted;
    }

    public static void toggleMuteMusic() {
        if (!isMuted) {
            music.setVolume(0);
            setIsMuted(true);
            System.out.println("Mute pressed");
        } else {
            music.setVolume(100);
            setIsMuted(false);
            System.out.println("Unmute pressed");
        }
    }

}













