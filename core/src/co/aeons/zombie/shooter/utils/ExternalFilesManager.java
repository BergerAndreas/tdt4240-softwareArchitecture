package co.aeons.zombie.shooter.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class ExternalFilesManager {


    public static String loadMultiplayerSettings(){
        String result = "";

        FileHandle settings = Gdx.files.external("multiplayerSettings");

        if(settings.exists()){
            result = settings.readString();
        }
        return result;
    }

    public static void saveMultiplayerSettings(String data){
        FileHandle settings = Gdx.files.external("multiplayerSettings");

        settings.writeString(data, false);
    }
}
