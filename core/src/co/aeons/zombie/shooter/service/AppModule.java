package co.aeons.zombie.shooter.service;

import javax.inject.Singleton;

import co.aeons.zombie.shooter.service.network.INetworkService;
import dagger.Module;
import dagger.Provides;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

@Module
public class AppModule {

    private INetworkService networkService;
    private no.ntnu.tdt4240.asteroids.service.ISettingsService settingsService;

    public AppModule(INetworkService networkService, no.ntnu.tdt4240.asteroids.service.ISettingsService settingsService) {
        this.networkService = networkService;
        this.settingsService = settingsService;
    }

    @Provides
    @Singleton
    INetworkService networkService() {
        return networkService;
    }

    @Provides
    @Singleton
    no.ntnu.tdt4240.asteroids.service.ISettingsService provideSettingsService() {
        return settingsService;
    }

    @Provides
    @Singleton
    AssetService provideAssetService() {
        return new AssetService();
    }

    @Provides
    @Singleton
    AudioService provideAudioManager(AssetService assetService, no.ntnu.tdt4240.asteroids.service.ISettingsService settingsService) {
        return new AudioService(assetService, settingsService);
    }

    @Provides
    @Singleton
    AnimationFactory provideAnimationFactory(AssetService assetService) {
        return new AnimationFactory(assetService);
    }

}
