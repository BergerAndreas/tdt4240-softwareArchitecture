package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.screens.ArcadeScreen;
import co.aeons.zombie.shooter.utils.enums.GameState;

import java.util.HashMap;

public class BackgroundManager {

    public enum BackgroundType {
        MENU, CAMPAIGN, ARCADE, MULTIPLAYER
    }

    //Almacena los fondos que se mostrarán
    private static HashMap<BackgroundType, Array<Texture>> backgrounds;

    //Posición concreta que tendrán los fondos
    private static Array<Float> scrollingPositions;

    //Velocidad de scroll de los fondos
    private static Array<Float> scrollingSpeeds;

    //Almacenará el array de los fondos que deberán mostrarse en el momento, servirá para acceder múltiples veces al Map
    private static Array<Texture> currentBackground;

    //Indica si el fondo actual debe tener movimiento o no
    private static boolean hasMovement;

    //Indica si el manager ya ha ejecutado su método load
    private static boolean isLoaded;

    public static void load() {
        backgrounds = new HashMap<BackgroundType, Array<Texture>>();
        scrollingPositions = new Array<Float>();
        scrollingSpeeds = new Array<Float>();

        Array<Texture> menuTextures = new Array<Texture>();
        Array<Texture> campaignTextures = new Array<Texture>();
        Array<Texture> arcadeTextures = new Array<Texture>();
        Array<Texture> multiplayerTextures = new Array<Texture>();

        menuTextures.add(AssetsManager.loadTexture("background1_1"));
        menuTextures.add(AssetsManager.loadTexture("planets2"));
        menuTextures.add(AssetsManager.loadTexture("background1_2"));
        menuTextures.add(AssetsManager.loadTexture("background1_3"));
        campaignTextures.add(AssetsManager.loadTexture("background1_1"));
        campaignTextures.add(AssetsManager.loadTexture("background1_2"));
        campaignTextures.add(AssetsManager.loadTexture("background1_3"));
        arcadeTextures.add(AssetsManager.loadTexture("background2_1"));
        arcadeTextures.add(AssetsManager.loadTexture("background2_2"));
        arcadeTextures.add(AssetsManager.loadTexture("background2_3"));
        multiplayerTextures.add(AssetsManager.loadTexture("background3_1"));
        multiplayerTextures.add(AssetsManager.loadTexture("planets"));
        multiplayerTextures.add(AssetsManager.loadTexture("background3_2"));
        multiplayerTextures.add(AssetsManager.loadTexture("background3_3"));

        backgrounds.put(BackgroundType.MENU, menuTextures);
        backgrounds.put(BackgroundType.CAMPAIGN, campaignTextures);
        backgrounds.put(BackgroundType.ARCADE, arcadeTextures);
        backgrounds.put(BackgroundType.MULTIPLAYER, multiplayerTextures);

        scrollingPositions.add(0f);
        scrollingPositions.add(0f);
        scrollingPositions.add(0f);
        scrollingPositions.add(0f);

        scrollingSpeeds.add(100f);
        scrollingSpeeds.add(150f);
        scrollingSpeeds.add(250f);
        scrollingSpeeds.add(300f);

        changeCurrentBackgrounds(null);

        isLoaded = true;
    }

    public static void update(float delta) {
        //Si no se han cargado los fondos, lo hacemos
        if (!isLoaded)
            BackgroundManager.load();

        //El fondo tendrá un decremento de velocidad si el estado del juego no es START
        int decrease = (!ScreenManager.isCurrentStateEqualsTo(GameState.START)) ? 3 : 1;

        //Recalculamos las posiciones de los fondos siempre y cuando deba tener movimiento
        if (hasMovement) {
            for (int i = 0; i < currentBackground.size; i++) {
                scrollingPositions.set(i, scrollingPositions.get(i) - (delta * (scrollingSpeeds.get(i) / decrease)));
                if (scrollingPositions.get(i) <= -currentBackground.get(i).getWidth())
                    scrollingPositions.set(i, 0f);
            }
        } else {
            scrollingPositions.set(1, 0f);
            for (int i = 2; i < currentBackground.size; i++) {
                scrollingPositions.set(i, scrollingPositions.get(i) - (delta * (scrollingSpeeds.get(i) / (decrease*2))));
                if (scrollingPositions.get(i) <= -currentBackground.get(i).getWidth())
                    scrollingPositions.set(i, 0f);
            }
        }
    }

    public static void render() {
        //Si no se han cargado los fondos, lo hacemos
        if (!isLoaded)
            BackgroundManager.load();

        //Pintamos el fondo únicamente si no estamos en el START del arcade, ya que ahí pinta de forma excepcional
        if (!(ScreenManager.isCurrentScreenEqualsTo(ArcadeScreen.class) &&
                ScreenManager.isCurrentStateEqualsTo(GameState.START))) {
            //Pintamos los fondos
            for (int i = 0; i < currentBackground.size; i++)
                render(i);
        }
    }

    //Pinta el fondo concreto que le indiquemos según su posición en el Array
    public static void render(int pos) {
        Texture b = currentBackground.get(pos);

        //Pintamos el fondo, que necesitará pintarse dos veces para el scrolling
        //Según si es landscape o portrait, se pintará de una forma u otra
        if (SpaceGame.orientation.equals("sensorLandscape")) {
            SpaceGame.batch.draw(b, scrollingPositions.get(pos), 0);
            SpaceGame.batch.draw(b, scrollingPositions.get(pos) + b.getWidth(), 0);
        } else {
            SpaceGame.batch.draw(new TextureRegion(b), 0, scrollingPositions.get(pos),
                    SpaceGame.width / 2, b.getHeight() / 2,
                    b.getWidth(), b.getHeight(), 1, 1, 90);
            SpaceGame.batch.draw(new TextureRegion(b), 0, b.getWidth() + scrollingPositions.get(pos),
                    SpaceGame.width / 2, b.getHeight() / 2,
                    b.getWidth(), b.getHeight(), 1, 1, 90);
        }

    }

    //Cambia el fondo que se mostrará según el tipo dado
    public static void changeCurrentBackgrounds(BackgroundType newType) {
        if (newType == null)
            newType = BackgroundType.MENU;
        currentBackground = backgrounds.get(newType);

        //Indicamos si el fondo debe moverse en base al tipo que se vaya a ejecutar
        if (newType.equals(BackgroundType.MULTIPLAYER) || newType.equals(BackgroundType.MENU)) {
            hasMovement = false;
        } else {
            hasMovement = true;
        }
    }

    public static void dispose() {
        currentBackground = backgrounds.get(BackgroundType.MENU);
        for (int i=0; i<currentBackground.size; i++)
            currentBackground.get(i).dispose();

        currentBackground = backgrounds.get(BackgroundType.CAMPAIGN);
        for (int i=0; i<currentBackground.size; i++)
            currentBackground.get(i).dispose();

        currentBackground = backgrounds.get(BackgroundType.ARCADE);
        for (int i=0; i<currentBackground.size; i++)
            currentBackground.get(i).dispose();

        currentBackground = backgrounds.get(BackgroundType.MULTIPLAYER);
        for (int i=0; i<currentBackground.size; i++)
            currentBackground.get(i).dispose();
    }

}
