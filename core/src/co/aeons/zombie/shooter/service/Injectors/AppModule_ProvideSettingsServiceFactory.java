package co.aeons.zombie.shooter.service.Injectors;

import javax.annotation.Generated;

import co.aeons.zombie.shooter.service.AppModule;
import co.aeons.zombie.shooter.service.ISettingsService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

@Generated(
        value = "dagger.internal.codegen.ComponentProcessor",
        comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvideSettingsServiceFactory implements Factory<ISettingsService> {
    private final AppModule module;

    public AppModule_ProvideSettingsServiceFactory(AppModule module) {
        assert module != null;
        this.module = module;
    }

    @Override
    public ISettingsService get() {
        return Preconditions.checkNotNull(
                module.provideSettingsService(),
                "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ISettingsService> create(AppModule module) {
        return new AppModule_ProvideSettingsServiceFactory(module);
    }

    /**
     * Proxies {@link AppModule#provideSettingsService()}.
     */
    public static ISettingsService proxyProvideSettingsService(AppModule instance) {
        return instance.provideSettingsService();
    }
}
