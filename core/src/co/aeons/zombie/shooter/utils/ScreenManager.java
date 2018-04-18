package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.Screen;
import co.aeons.zombie.shooter.GameScreen;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.utils.enums.GameState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ScreenManager {

    private static Class<?> currentClass;
    private static Screen currentScreen;

    public static void changeScreen(SpaceGame game, Class<?> screenClass, Object ... moreParameters){
        Constructor<?> cons = null;
        Screen newScreen = null;
        currentClass = screenClass;
        try {
            if(moreParameters.length != 0){
                Class<?>[] classParameters = loadClassParameters(moreParameters);
                if(moreParameters.length == 1){
                    cons = screenClass.getConstructor(SpaceGame.class,
                            classParameters[0]);
                    newScreen = (Screen) cons.newInstance(game,
                            classParameters[0].cast(moreParameters[0]));
                }else if(moreParameters.length == 2){
                    cons = screenClass.getConstructor(SpaceGame.class,
                            classParameters[0],
                            classParameters[1]);
                    newScreen = (Screen) cons.newInstance(game,
                            classParameters[0].cast(moreParameters[0]),
                            classParameters[1].cast(moreParameters[1]));
                }else if(moreParameters.length == 3){
                    cons = screenClass.getConstructor(SpaceGame.class,
                            classParameters[0],
                            classParameters[1],
                            classParameters[2]);
                    newScreen = (Screen) cons.newInstance(game,
                            classParameters[0].cast(moreParameters[0]),
                            classParameters[1].cast(moreParameters[1]),
                            classParameters[2].cast(moreParameters[2]));
                }
                assert cons != null;
            }else{
                cons = screenClass.getConstructor(SpaceGame.class);
                assert cons != null;
                newScreen = (Screen) cons.newInstance(game);
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        currentScreen = newScreen;
        game.setScreen(newScreen);
    }

    private static Class<?>[] loadClassParameters(Object ... moreParameters){
        Class<?>[] result = new Class[moreParameters.length];
        for(int i = 0; i <moreParameters.length; i++){
            if(moreParameters[i] instanceof String){
                result[i] = String.class;
            }else if(moreParameters[i] instanceof Boolean){
                result[i] = Boolean.class;
            }
        }
        return result;
    }

    // Devuelve true si el estado actual del juego es el mismo que el pasado por parámetro
    public static boolean isCurrentStateEqualsTo(GameState state){
        boolean result = false;

        if(currentScreen instanceof GameScreen){
            GameScreen screen = (GameScreen) currentScreen;
            if (screen.state != null && screen.state.equals(state))
                result = true;
        }

        return result;
    }

    public static Screen getCurrentScreen() {
        return currentScreen;
    }

    public static boolean isCurrentScreenEqualsTo(Class screenClass){
        return currentClass.getName().equals(screenClass.getName());
    }
}
