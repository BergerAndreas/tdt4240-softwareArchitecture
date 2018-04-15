package co.aeons.zombie.shooter.service;

import javax.inject.Singleton;

import co.aeons.zombie.shooter.service.network.INetworkService;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private INetworkService networkService;
    private ISettingsService settingsService;

    public AppModule(INetworkService networkService, ISettingsService settingsService) {
        this.networkService = networkService;
        this.settingsService = settingsService;
    }

    @Provides
    @Singleton
    public INetworkService networkService() {
        return networkService;
    }

    @Provides
    @Singleton
    public ISettingsService provideSettingsService() {
        return settingsService;
    }
}
