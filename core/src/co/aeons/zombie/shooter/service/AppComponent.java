package co.aeons.zombie.shooter.service;

import javax.inject.Singleton;

import co.aeons.zombie.shooter.service.network.INetworkService;
import dagger.Component;

@Component(modules = AppModule.class)
@Singleton
public interface AppComponent {

    INetworkService getNetworkService();


    ISettingsService getSettingsService();


}
