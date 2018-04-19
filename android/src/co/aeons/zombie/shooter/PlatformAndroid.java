package co.aeons.zombie.shooter;

import android.app.Activity;
import android.content.pm.ActivityInfo;

import com.badlogic.gdx.backends.android.AndroidApplication;

import co.aeons.zombie.shooter.Platform;

public class PlatformAndroid extends AndroidApplication implements Platform {

    private Activity activity;

    @Override
    public void setOrientation(String string) {
        if (string.equals("landscape")){
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (string.equals("portrait")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (string.equals("sensorLandscape")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        } else if (string.equals("sensorPortrait")) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

}