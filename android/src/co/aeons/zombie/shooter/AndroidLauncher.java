package co.aeons.zombie.shooter;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	private static final String TAG = AndroidLauncher.class.getSimpleName();
	private static AndroidLauncher instance;
	private AndroidNetworkService playService;

	public AndroidLauncher() {
		instance = this;
	}

	public static AndroidLauncher getInstance() {
		return instance;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		playService = new AndroidNetworkService(this);
		co.aeons.zombie.shooter.service.ISettingsService settingsService = new AndroidSettingsService(this.getApplicationContext());
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useAccelerometer = false;
		config.useImmersiveMode = true;
		config.useWakelock = true;
		initialize(new ZombieShooter(playService, settingsService), config);
		playService.setup();
	}

	@Override
	protected void onStart() {
		super.onStart();
		playService.onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		playService.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		playService.onActivityResult(requestCode, resultCode, data);

	}
}
