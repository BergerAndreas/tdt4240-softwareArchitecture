package co.aeons.zombie.shooter.multiplayerListeners;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import co.aeons.zombie.shooter.AndroidLauncher;

import java.util.Calendar;


public class RoomUpdate implements RoomUpdateListener{

    private AndroidLauncher callback;

    public RoomUpdate(AndroidLauncher androidLauncher){
        callback = androidLauncher;
    }

    @Override
    public void onRoomCreated(int i, Room room) {
        Gdx.app.log("multi","onRoomCreated   -  Code:"+i+"   RoomID:"+room.getRoomId());
        callback.roomId = room.getRoomId();
        callback.showWaitingRoom(room);
    }

    @Override
    public void onJoinedRoom(int i, Room room) {
        Gdx.app.log("multi","onJoinedRoom   -  Code:"+i+"   RoomID:"+room.getRoomId());
        callback.roomId = room.getRoomId();
        callback.showWaitingRoom(room);
    }

    @Override
    public void onLeftRoom(int i, String s) {
        Gdx.app.log("multi","onLeftRoom   -  Code:"+i+"   User:"+s);
    }

    @Override
    public void onRoomConnected(int i, Room room) {
        Gdx.app.log("multi","onRoomConnected   -  Code:"+i+"   RoomID:"+room.getRoomId());
    }
}
