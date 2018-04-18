package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import co.aeons.zombie.shooter.screens.ArcadeScreen;
import co.aeons.zombie.shooter.screens.CampaignScreen;
import co.aeons.zombie.shooter.screens.MultiplayerScreen;
import co.aeons.zombie.shooter.utils.enums.GameState;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    // Volumen que tendrán por defecto la música y los efectos, que serán editables
    private static float volumeMusic;
    private static float volumeEffect;

    // Almacenará la música y los sonidos
    private static Music music;
    private static Map<String, Sound> sounds;

    // Almacenará el nombre de la última canción que se activó
    private static String currentMusic;

    //Carga los sonidos en el mapa y pone el manager a su estado inicial
    public static void load() {
        sounds = new HashMap<String, Sound>();

        sounds.put("arcade_shock_effect", AssetsManager.loadSound("arcade_shock_effect"));
        sounds.put("button_backward", AssetsManager.loadSound("button_backward"));
        sounds.put("button_forward", AssetsManager.loadSound("button_forward"));
        sounds.put("inventary", AssetsManager.loadSound("inventary"));
        sounds.put("new_record", AssetsManager.loadSound("new_record"));
        sounds.put("pause", AssetsManager.loadSound("pause"));

        sounds.put("shoot_basic_FX", AssetsManager.loadSound("shoot_basic_FX"));
        sounds.put("shoot_red_FX", AssetsManager.loadSound("shoot_red_FX"));
        sounds.put("shoot_rocket_FX", AssetsManager.loadSound("shoot_rocket_FX"));
        sounds.put("shoot_orange_FX", AssetsManager.loadSound("shoot_orange_FX"));
        sounds.put("shoot_purple_FX", AssetsManager.loadSound("shoot_purple_FX"));
        sounds.put("shoot_bigBasic_FX", AssetsManager.loadSound("shoot_bigBasic_FX"));

        currentMusic = "no_music";
        volumeMusic = 0.3f;
        volumeEffect = 0.7f;
    }

    //Se encarga de manejar la música de forma automática, según la screen y su estado
    public static void update() {
        String newMusic = "no_music";

        //Primero damos un valor a newMusic según el caso
        if (ScreenManager.isCurrentScreenEqualsTo(ArcadeScreen.class)) {
            if (ScreenManager.isCurrentStateEqualsTo(GameState.READY)) {
                newMusic = "arcade";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.START)) {
                newMusic = "arcade";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.PAUSE)) {
                newMusic = "pause";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.WIN)) {

            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.LOSE)) {

            }
        } else if (ScreenManager.isCurrentScreenEqualsTo(CampaignScreen.class)) {
            if (ScreenManager.isCurrentStateEqualsTo(GameState.READY)) {
                newMusic = "campaign";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.START)) {
                newMusic = "campaign";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.PAUSE)) {
                newMusic = "pause";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.WIN)) {
                newMusic = "campaign_win";
            } else if (ScreenManager.isCurrentStateEqualsTo(GameState.LOSE)) {

            }
        } else if (ScreenManager.isCurrentScreenEqualsTo(MultiplayerScreen.class)) {

        } else {
            newMusic = "menu";
        }

        // Según el valor de newMusic, actuamos de una forma u otra
        if (newMusic.equals("pause")) {
            pauseMusic();
        } else if (newMusic.equals("no_music")) {
            stopMusic();
        } else if (newMusic.equals(currentMusic)) {
            if (music == null)
                AudioManager.playMusic(newMusic, true);
            else
                playMusic();
        } else if (!newMusic.equals(currentMusic)) {
            AudioManager.playMusic(newMusic, true);
            currentMusic = newMusic;
        }
    }

    public static float getVolumeMusic() {
        return music.getVolume();
    }

    public static float getVolumeEffect() {
        return volumeEffect;
    }

    // Modifica el volumen de la música si se ha cargado alguna
    public static void setVolumeMusic(float volumeMusic) {
        AudioManager.volumeMusic = volumeMusic;
        if (music != null)
            music.setVolume(volumeMusic);
    }

    // Modifica el volumen de los efectos
    public static void setVolumeEffect(float volumeEffect) {
        AudioManager.volumeEffect = volumeEffect;
    }

    // Ejectua el sonido cuyo nombre coincide con el pasado por parámetro
    public static void playSound(String name) {
        Sound sound = sounds.get(name);
        long id = sound.play();

        sound.setVolume(id, volumeEffect);
    }

    // Carga una canción según el nombre e indica si debe repetirse
    private static void playMusic(String name, boolean isLooping) {
        if (music != null)
            stopMusic();

        music = AssetsManager.loadMusic(name);

        music.setVolume(volumeMusic);
        music.play();

        if (!name.equals("campaign_win")) {
            music.setLooping(isLooping);
        } else {
            music.setLooping(false);
        }
    }

    // Activa una canción ya cargada si estaba parada o en pausa
    private static void playMusic() {
        if (music != null && !isPlaying())
            music.play();
    }

    // Indica si hay una canción cargada que está sonando
    private static boolean isPlaying() {
        boolean res = false;
        if (music != null)
            res = music.isPlaying();
        return res;
    }

    // Pausa una canción si está sonando
    public static void pauseMusic() {
        if (music.isPlaying())
            music.pause();
    }

    // Para una canción si está sonando
    private static void stopMusic() {
        if (isPlaying())
            music.stop();
    }

    public static void dispose() {
        music.dispose();
        sounds.clear();
    }

}
