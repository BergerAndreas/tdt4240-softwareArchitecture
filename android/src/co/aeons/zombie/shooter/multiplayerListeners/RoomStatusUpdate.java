package co.aeons.zombie.shooter.multiplayerListeners;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import co.aeons.zombie.shooter.AndroidLauncher;

import java.util.List;


public class RoomStatusUpdate implements RoomStatusUpdateListener{

    private AndroidLauncher callback;

    public RoomStatusUpdate(AndroidLauncher androidLauncher){
        callback = androidLauncher;
    }

    @Override
    public void onRoomConnecting(Room room) {
        Gdx.app.log("multi","onRoomConnecting   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        Gdx.app.log("multi","onRoomAutoMatching   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, List<String> list) {
        Gdx.app.log("multi","onPeerInvitedToRoom   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onPeerDeclined(Room room, List<String> list) {
        callback.updateRoomProperties(room);
    }

    @Override
    public void onPeerJoined(Room room, List<String> list) {
        Gdx.app.log("multi","onPeerJoined   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onPeerLeft(Room room, List<String> list) {
        Gdx.app.log("multi","onPeerLeft   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onConnectedToRoom(Room room) {
        Gdx.app.log("multi","onConnectedToRoom   -   RoomID:"+room.getRoomId());
        callback.roomId = room.getRoomId();
        callback.setMyId(room);
        callback.updateRoomProperties(room);
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        Gdx.app.log("multi","onDisconnectedFromRoom   -  RoomID:"+room.getRoomId());
        callback.roomId = "";
    }

    @Override
    public void onPeersConnected(Room room, List<String> list) {
        Gdx.app.log("multi","onPeersConnected   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onPeersDisconnected(Room room, List<String> list) {
        Gdx.app.log("multi","onPeersDisconnected   -  RoomID:"+room.getRoomId());
        callback.updateRoomProperties(room);
    }

    @Override
    public void onP2PConnected(String s) {

    }

    @Override
    public void onP2PDisconnected(String s) {

    }
}
