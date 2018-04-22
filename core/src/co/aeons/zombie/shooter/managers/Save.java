package co.aeons.zombie.shooter.managers;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import co.aeons.zombie.shooter.ZombieShooter;

/**
 * Created by Erikkvo on 15-Apr-18.
 */

public class Save {

    public static GameData gd;

//    TODO: Find out how/when to save file, so it does not overwrite file each time
    public static void save() {
        try {
//            Append = false will overwrite file
            FileOutputStream fos = new FileOutputStream(ZombieShooter.filesDir+"highscores.sav");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(gd);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    public static void load() {
        try {
            if (!saveFileExists()) {
                init();
                return;
            }

            FileInputStream fos = new FileInputStream(ZombieShooter.filesDir+"highscores.sav");
            ObjectInputStream in = new ObjectInputStream(fos);
            gd = (GameData) in.readObject();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Gdx.app.exit();
        }
    }

    public static boolean saveFileExists() {
        File f = new File(ZombieShooter.filesDir+"highscores.sav");
        return f.exists();
    }

    public static void init() {
        gd = GameData.GameData();
        gd.init();
        save();
    }

}
