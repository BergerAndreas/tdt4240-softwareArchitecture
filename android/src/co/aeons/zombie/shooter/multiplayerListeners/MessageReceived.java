package co.aeons.zombie.shooter.multiplayerListeners;


import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import co.aeons.zombie.shooter.AndroidLauncher;
import co.aeons.zombie.shooter.utils.MultiplayerMessage;

public class MessageReceived implements RealTimeMessageReceivedListener{

    private AndroidLauncher callback;
    private MultiplayerMessage income;

    public MessageReceived(AndroidLauncher androidLauncher){
        callback = androidLauncher;
        income = new MultiplayerMessage();
    }

    @Override
    public void onRealTimeMessageReceived(RealTimeMessage realTimeMessage) {
        income.setPropertiesFromMessage(new String(realTimeMessage.getMessageData()));
        callback.gameMessage = income;
    }
}
