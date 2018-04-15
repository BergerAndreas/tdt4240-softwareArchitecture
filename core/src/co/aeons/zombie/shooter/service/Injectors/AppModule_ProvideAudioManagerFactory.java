package co.aeons.zombie.shooter.service.Injectors;

import javax.annotation.Generated;
import javax.inject.Provider;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvideAudioManagerFactory implements Factory<AudioService> {
  private final AppModule module;

  private final Provider<AssetService> assetServiceProvider;

  private final Provider<ISettingsService> settingsServiceProvider;

  public AppModule_ProvideAudioManagerFactory(
      AppModule module,
      Provider<AssetService> assetServiceProvider,
      Provider<ISettingsService> settingsServiceProvider) {
    assert module != null;
    this.module = module;
    assert assetServiceProvider != null;
    this.assetServiceProvider = assetServiceProvider;
    assert settingsServiceProvider != null;
    this.settingsServiceProvider = settingsServiceProvider;
  }

  @Override
  public AudioService get() {
    return Preconditions.checkNotNull(
        module.provideAudioManager(assetServiceProvider.get(), settingsServiceProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<AudioService> create(
      AppModule module,
      Provider<AssetService> assetServiceProvider,
      Provider<ISettingsService> settingsServiceProvider) {
    return new AppModule_ProvideAudioManagerFactory(
        module, assetServiceProvider, settingsServiceProvider);
  }

  /** Proxies {@link AppModule#provideAudioManager(AssetService, ISettingsService)}. */
  public static AudioService proxyProvideAudioManager(
      AppModule instance, AssetService assetService, ISettingsService settingsService) {
    return instance.provideAudioManager(assetService, settingsService);
  }
}
