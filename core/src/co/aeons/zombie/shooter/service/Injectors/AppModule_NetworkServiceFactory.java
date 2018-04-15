package co.aeons.zombie.shooter.service.Injectors;

import javax.annotation.Generated;

import co.aeons.zombie.shooter.service.AppModule;
import co.aeons.zombie.shooter.service.network.INetworkService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

@Generated(
        value = "dagger.internal.codegen.ComponentProcessor",
        comments = "https://google.github.io/dagger"
)
public final class AppModule_NetworkServiceFactory implements Factory<INetworkService> {
    private final AppModule module;

    public AppModule_NetworkServiceFactory(AppModule module) {
        assert module != null;
        this.module = module;
    }

    @Override
    public INetworkService get() {
        return Preconditions.checkNotNull(
                module.networkService(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<INetworkService> create(AppModule module) {
        return new AppModule_NetworkServiceFactory(module);
    }

    /**
     * Proxies {@link AppModule#networkService()}.
     */
    public static INetworkService proxyNetworkService(AppModule instance) {
        return instance.networkService();
    }
}
