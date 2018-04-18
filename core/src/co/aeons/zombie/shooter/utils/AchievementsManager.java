package co.aeons.zombie.shooter.utils;

import com.badlogic.gdx.utils.ArrayMap;
import co.aeons.zombie.shooter.SpaceGame;
import co.aeons.zombie.shooter.screens.ArcadeScreen;
import co.aeons.zombie.shooter.utils.enums.GameState;

public class AchievementsManager {

    //Mapa que tendrá la asociación del nombre representativo de un logro con su id
    private static ArrayMap<String, String> idsReferences;

    //Mapa que indicará cuál logro está desactivado
    private static ArrayMap<String, Boolean> achievementsUnlocked;

    //Indica si el gestor ya ha sido cargado
    private static boolean isLoaded;

    //Almacena la capa actual para comparar si se ha cambiado
    private static int currentLayer;

    //Almacena el timeAlive del último momento en el que se cambió de capa
    private static float lastTimeOfChangeLayer;

    //Guarda la actual Screen y ésta es de tipo Arcade
    private static ArcadeScreen arcadeScreen;

    public static void load() {
        idsReferences = new ArrayMap<String, String>();
        achievementsUnlocked = new ArrayMap<String, Boolean>();

        //Ids de los achievements
        idsReferences.put("achievement_finalized_campaign_id", "CgkIm5z9sJkQEAIQAg");
        idsReferences.put("achievement_denizen_deep_id", "CgkIm5z9sJkQEAIQAw");
        idsReferences.put("achievement_denizen_heights_id", "CgkIm5z9sJkQEAIQBA");
        idsReferences.put("achievement_reflexes_id", "CgkIm5z9sJkQEAIQBQ");
        idsReferences.put("achievement_master_id", "CgkIm5z9sJkQEAIQBg");

        //Inicialmente todos estarán bloqueados
        achievementsUnlocked.put("achievement_finalized_campaign_id", false);
        achievementsUnlocked.put("achievement_denizen_deep_id", false);
        achievementsUnlocked.put("achievement_denizen_heights_id", false);
        achievementsUnlocked.put("achievement_reflexes_id", false);
        achievementsUnlocked.put("achievement_master_id", false);

        isLoaded = true;
    }

    public static void update(float delta) {
        //Si estamos en el modo Arcade y el estado es Start, comprobamos los logros
        if (ScreenManager.isCurrentScreenEqualsTo(ArcadeScreen.class) &&
                ScreenManager.isCurrentStateEqualsTo(GameState.START))  {

            //Si acabamos de entrar, guardamos la screen y reiniciamos el momento de cambiar la capa
            if (arcadeScreen == null) {
                arcadeScreen = (ArcadeScreen) ScreenManager.getCurrentScreen();
                lastTimeOfChangeLayer = arcadeScreen.getTimeAlive();
            }

            //Comprobamos el logro de superar los 500 segundos
            if (arcadeScreen.getTimeAlive() >= 500) {
                unlockAchievement("achievement_reflexes_id");
            }

            //Comprobamos los logros de mantenerse sin cambiar de capa
            if (arcadeScreen.layer == currentLayer) {
                if (arcadeScreen.layer == 1 && arcadeScreen.getTimeAlive() - lastTimeOfChangeLayer >= 100) {
                    unlockAchievement("achievement_denizen_heights_id");
                } else if (arcadeScreen.layer == -1 && arcadeScreen.getTimeAlive() - lastTimeOfChangeLayer >= 125) {
                    unlockAchievement("achievement_denizen_deep_id");
                }
            } else {
                //Si se ha cambiado la capa, reseteamos el estado para el logro
                currentLayer = arcadeScreen.layer;
                lastTimeOfChangeLayer = arcadeScreen.getTimeAlive();
            }
        } else {
            //Si no vamos a hacer nada con el arcadeScreen, anulamos su contenido
            arcadeScreen = null;
        }
    }

    //Devuelve el id de un logro a partir de un nombre
    private static String getAchievementId(String achievementName) {
        //Si el manager no está cargado, lo cargamos
        if (!isLoaded)
            load();
        return idsReferences.get(achievementName);
    }

    //Desbloquea un logro dado un nombre
    private static void unlockAchievement(String achievementName) {
        //Si el manager no está cargado, lo cargamos
        if (!isLoaded)
            load();
        Boolean unlocked = achievementsUnlocked.get(achievementName);

        //Si el logro no está bloqueado, lo desbloqueamos y lo marcamos como tal
        if (unlocked == null || !unlocked) {
            SpaceGame.googleServices.unlockAchievement(getAchievementId(achievementName));
            achievementsUnlocked.put(achievementName, true);
        }
    }

    public void dispose() {
        idsReferences.clear();
    }

}
