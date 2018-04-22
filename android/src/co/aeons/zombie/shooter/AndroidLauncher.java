package co.aeons.zombie.shooter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.WindowManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.example.games.basegameutils.GameHelper;
import co.aeons.zombie.shooter.IGoogleServices;
import co.aeons.zombie.shooter.PlatformAndroid;
import co.aeons.zombie.shooter.ZombieShooter;
import co.aeons.zombie.shooter.R;
import co.aeons.zombie.shooter.multiplayerListeners.MessageReceived;
import co.aeons.zombie.shooter.multiplayerListeners.RoomStatusUpdate;
import co.aeons.zombie.shooter.multiplayerListeners.RoomUpdate;
import co.aeons.zombie.shooter.utils.MultiplayerMessage;
import co.aeons.zombie.shooter.utils.enums.MultiplayerState;

import java.util.ArrayList;
import java.util.Collections;

public class AndroidLauncher extends AndroidApplication implements IGoogleServices {

	private final static int REQUEST_CODE_UNUSED = 9002;
	// Código que usa google internamente para saber que es una ventana de espera
	private final static int REQUEST_CODE_WAITING_ROOM = 10002;
	private final static int REQUEST_CODE_SELECT_PLAYERS = 10000;
	private final static int REQUEST_CODE_INVITATION_INBOX = 10001;

	private final static int NUMBER_OF_OPPONENTS = 1;

	// Variable que guarderemos el ID de la habitación para futuras consultas
	public String roomId;

	// Variable que guardaremos nuestro ID dentro de la habitación para futuras consultas
	public String myId;

	// Los participantes que existen en la habitación actual
	public ArrayList<Participant> participants;

	// Mensaje recibido desde el Listener MessageReceived que se pasará a la Screen
	// Este mensaje va por UDP y corresponde a la inforamción del juego
	public MultiplayerMessage gameMessage;

	// Comprobaremos si puede iniciarse el multijugador o no
	// Basicamente esto se hace comprobando si ambos jugadores están en la habitación creada
	public MultiplayerState multiplayerState;

	private GameHelper _gameHelper;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Create the GameHelper.
		_gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
		_gameHelper.enableDebugLog(false);

		resetMultiplayerProperties();

		GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
		{
			@Override
			public void onSignInSucceeded(){
			}

			@Override
			public void onSignInFailed(){
			}
		};

		_gameHelper.setup(gameHelperListener);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		PlatformAndroid platform = new PlatformAndroid();
		platform.setActivity(this);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize(new ZombieShooter(platform, this, getFilesDir()), config);
	}

	@Override
	protected void onStart() {
		super.onStart();
		_gameHelper.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		_gameHelper.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Gdx.app.log("multi","RequestCode: "+requestCode+"  - ResultCode: "+resultCode);
		switch (requestCode){
			case REQUEST_CODE_WAITING_ROOM:
				// Obtenemos el resultado que coincide con el de la petición
				if (resultCode == Activity.RESULT_OK) {
					// Empezamos el juego multijugador
					multiplayerState = MultiplayerState.STARTMULTIPLAYER;
				} else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
					// Un jugador se va
					leaveRoom();
					multiplayerState = MultiplayerState.CANCEL;
				} else if (resultCode == Activity.RESULT_CANCELED) {
					// El jugador cancela la partida, esto para nosotros se transforma en una
					// solicitud de abandono de habitación
					leaveRoom();
					multiplayerState = MultiplayerState.CANCEL;
				}
				break;
			case REQUEST_CODE_SELECT_PLAYERS:
				if (resultCode == Activity.RESULT_OK) {
					multiplayerState = MultiplayerState.WAITING;
					// Obtenemos la lista de usuarios a los que hemos invitado
					final ArrayList<String> invitees = data.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS);

					// Iniciamos nuestro criterio de auto de emparejamiento
					Bundle autoMatchCriteria = null;
					// Información obtenida desde la ventana de invitación
					int minAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
					int maxAutoMatchPlayers = data.getIntExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);

					if (minAutoMatchPlayers > 0 || maxAutoMatchPlayers > 0) {
						autoMatchCriteria = RoomConfig.createAutoMatchCriteria(
								minAutoMatchPlayers, maxAutoMatchPlayers, 0);
					}

					// Creamos la configuración de la habitación
					RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(new RoomUpdate(this));
					rtmConfigBuilder.addPlayersToInvite(invitees);
					rtmConfigBuilder.setMessageReceivedListener(new MessageReceived(this));
					rtmConfigBuilder.setRoomStatusUpdateListener(new RoomStatusUpdate(this));

					if (autoMatchCriteria != null) {
						rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
					}
					// Reseteamos las propiedades del multijugador por si acaso
					resetMultiplayerProperties();

					// Creamos la sala multijugador con esta configuración de la habitación
					Games.RealTimeMultiplayer.create(_gameHelper.getApiClient(), rtmConfigBuilder.build());
				}else{
					multiplayerState = MultiplayerState.CANCEL;
				}
				break;
			case REQUEST_CODE_INVITATION_INBOX:
				if (resultCode == Activity.RESULT_OK) {
					multiplayerState = MultiplayerState.WAITING;
					// Obtenemos la invitación desde la ventana correspondiente
					Invitation inv = data.getExtras().getParcelable(Multiplayer.EXTRA_INVITATION);

					// Iniciamos el proceso de aceptación de la invitación

					// Creamos la configuración de la habitación
					RoomConfig.Builder roomConfigBuilder = RoomConfig.builder(new RoomUpdate(this));
					roomConfigBuilder.setInvitationIdToAccept(inv.getInvitationId())
							.setMessageReceivedListener(new MessageReceived(this))
							.setRoomStatusUpdateListener(new RoomStatusUpdate(this));

					// Nos unimos a la habitación correspondiente con nuesta ID de invitación
					Games.RealTimeMultiplayer.join(_gameHelper.getApiClient(), roomConfigBuilder.build());
				}else{
					multiplayerState = MultiplayerState.CANCEL;
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
		_gameHelper.onActivityResult(requestCode, resultCode, data);
	}




	@Override
	public void submitScore(long score) {
		if (isSignedIn() == true) {
			Games.Leaderboards.submitScore(_gameHelper.getApiClient(),  getString(R.string.leaderboard_id), score);
			startActivityForResult(Games.Leaderboards.getLeaderboardIntent(_gameHelper.getApiClient(), getString(R.string.leaderboard_id)), REQUEST_CODE_UNUSED);
		} else {
			// Maybe sign in here then redirect to submitting score?
		}
	}

	@Override
	public boolean isSignedIn()
	{
		return _gameHelper.isSignedIn();
	}

	@Override
	public void unlockAchievement(String achievementId) {
		if (isSignedIn()) {
			Games.Achievements.unlock(_gameHelper.getApiClient(), achievementId);
		} else {

		}
	}

	@Override
	public void showAchievements() {
		if (isSignedIn()) {
			startActivityForResult(Games.Achievements.getAchievementsIntent(_gameHelper.getApiClient()),
					REQUEST_CODE_UNUSED);
		} else {

		}
	}

	@Override
	public void startQuickGame() {
		// Reseteamos la variable de control
		multiplayerState = MultiplayerState.WAITING;
		// Creamos la configuración de nuestra habitación
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(NUMBER_OF_OPPONENTS,
				NUMBER_OF_OPPONENTS, 0);
		// Ubicamos los listeners
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(new RoomUpdate(this));
		rtmConfigBuilder.setMessageReceivedListener(new MessageReceived(this));
		rtmConfigBuilder.setRoomStatusUpdateListener(new RoomStatusUpdate(this));
		rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);
		// Creamos la partida
		Games.RealTimeMultiplayer.create(_gameHelper.getApiClient(), rtmConfigBuilder.build());
	}

	@Override
	public void invitePlayer() {
		Intent i = Games.RealTimeMultiplayer.getSelectOpponentsIntent(_gameHelper.getApiClient(),1,1);
		startActivityForResult(i,REQUEST_CODE_SELECT_PLAYERS);
	}

	@Override
	public void seeMyInvitations() {
		Intent i = Games.Invitations.getInvitationInboxIntent(_gameHelper.getApiClient());
		startActivityForResult(i, REQUEST_CODE_INVITATION_INBOX);
	}

	// Cada vez que en algún listener se modifique en algo la habitación activará
	// este método que se usa para actualizar la lista de participantes
	public void updateRoomProperties(Room room){
		if (room != null) {
			participants = room.getParticipants();
		}
	}

	@Override
	// Método usado para que la Screen pregunte si se puede empezar la partida
	public MultiplayerState getMultiplayerState() {
		return multiplayerState;
	}

	@Override
	// Método que envia un mensaje por UDP a todos los participantes (menos a uno mismo)
	// Antes de que toni pregunte: Aunque ponga que te lo envies a ti mismo no funciona
	// leído en StackOverflow y comprobado empiricamente
	public void sendGameMessage(String message) {
		for(Participant p : participants){
			if(!p.getParticipantId().equals(myId))
				Games.RealTimeMultiplayer.sendUnreliableMessage(_gameHelper.getApiClient(),message.getBytes(),roomId,p.getParticipantId());
		}
	}

	@Override
	// Método usado por la Screen para obtener el mensaje que usa el juego
	public MultiplayerMessage receiveGameMessage() {
		return gameMessage;
	}

	// Obtiene de una Room el ID nuestro
	public void setMyId(Room room){
		myId = room.getParticipantId(Games.Players.getCurrentPlayerId(_gameHelper.getApiClient()));
	}

	// Muestra la ventana de espera
	public void showWaitingRoom(Room room) {
		// minimum number of players required for our game
		// For simplicity, we require everyone to join the game before we start it
		// (this is signaled by Integer.MAX_VALUE).
		final int MIN_PLAYERS = Integer.MAX_VALUE;
		Intent i = Games.RealTimeMultiplayer.getWaitingRoomIntent(_gameHelper.getApiClient(), room, MIN_PLAYERS);

		// show waiting room UI
		startActivityForResult(i, REQUEST_CODE_WAITING_ROOM);
	}

	// Abandonar la habitación
	@Override
	public void leaveRoom() {
		Gdx.app.log("multi","Leave room");

		if (roomId != "") {
			Games.RealTimeMultiplayer.leave(_gameHelper.getApiClient(), new RoomUpdate(this), roomId);
			roomId = "";
		}

		resetMultiplayerProperties();
	}

	@Override
	public String getHostId(){
		ArrayList<String> players = new ArrayList<>();
		for (Participant p: participants){
			players.add(p.getParticipantId());
		}
		//Sort the list to get matching host
        Collections.sort(players);
		return players.get(0);
	}

	@Override
    public String getMyId(){
	    return this.myId;
    }

	// Método privado usado para resetear las propiedades del multijugador
	private void resetMultiplayerProperties(){
		multiplayerState		= MultiplayerState.NONE;
		gameMessage = null;
		roomId 					= "";
		participants 			= new ArrayList<Participant>();
		gameMessage 			= new MultiplayerMessage();
	}

	@Override
	protected void onPause(){
		//do whatever you need to on pause
		sendGameMessage(1+"§");
		super.onPause();
	}
}
