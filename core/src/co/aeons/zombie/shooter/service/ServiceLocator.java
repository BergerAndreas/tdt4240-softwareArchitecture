package co.aeons.zombie.shooter.service;


import co.aeons.zombie.shooter.service.Injectors.DaggerAppComponent;
import co.aeons.zombie.shooter.service.network.INetworkService;


public abstract class ServiceLocator {

    //public static EntityComponent entityComponent;
    public static AppComponent appComponent;

    /*
    public static void initializeSinglePlayerEntityComponent(PooledEngine engine) {
        entityComponent = DaggerEntityComponent.builder().entityModule(new EntityModule(engine, false)).appComponent(getAppComponent()).build();
    }

    public static void initializeMultiPlayerEntityComponent(PooledEngine engine) {
        entityComponent = DaggerEntityComponent.builder().entityModule(new EntityModule(engine, true)).appComponent(getAppComponent()).build();
    }

    public static EntityComponent getEntityComponent() {
        return entityComponent;
    }
*/

    public static void initializeAppComponent(INetworkService networkService, ISettingsService settingsService) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(networkService, settingsService)).build();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
