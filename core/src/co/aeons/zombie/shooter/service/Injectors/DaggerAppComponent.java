package no.ntnu.tdt4240.asteroids.service;

import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;
import no.ntnu.tdt4240.asteroids.game.AnimationFactory;
import no.ntnu.tdt4240.asteroids.service.audio.AudioService;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerAppComponent implements AppComponent {
  private Provider<INetworkService> networkServiceProvider;

  private Provider<AssetService> provideAssetServiceProvider;

  private Provider<ISettingsService> provideSettingsServiceProvider;

  private Provider<AudioService> provideAudioManagerProvider;

  private Provider<AnimationFactory> provideAnimationFactoryProvider;

  private DaggerAppComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.networkServiceProvider =
        DoubleCheck.provider(AppModule_NetworkServiceFactory.create(builder.appModule));

    this.provideAssetServiceProvider =
        DoubleCheck.provider(AppModule_ProvideAssetServiceFactory.create(builder.appModule));

    this.provideSettingsServiceProvider =
        DoubleCheck.provider(AppModule_ProvideSettingsServiceFactory.create(builder.appModule));

    this.provideAudioManagerProvider =
        DoubleCheck.provider(
            AppModule_ProvideAudioManagerFactory.create(
                builder.appModule, provideAssetServiceProvider, provideSettingsServiceProvider));

    this.provideAnimationFactoryProvider =
        DoubleCheck.provider(
            AppModule_ProvideAnimationFactoryFactory.create(
                builder.appModule, provideAssetServiceProvider));
  }

  @Override
  public INetworkService getNetworkService() {
    return networkServiceProvider.get();
  }

  @Override
  public AudioService getAudioService() {
    return provideAudioManagerProvider.get();
  }

  @Override
  public ISettingsService getSettingsService() {
    return provideSettingsServiceProvider.get();
  }

  @Override
  public AssetService getAssetService() {
    return provideAssetServiceProvider.get();
  }

  @Override
  public AnimationFactory getAnimationFactory() {
    return provideAnimationFactoryProvider.get();
  }

  public static final class Builder {
    private AppModule appModule;

    private Builder() {}

    public AppComponent build() {
      if (appModule == null) {
        throw new IllegalStateException(AppModule.class.getCanonicalName() + " must be set");
      }
      return new DaggerAppComponent(this);
    }

    public Builder appModule(AppModule appModule) {
      this.appModule = Preconditions.checkNotNull(appModule);
      return this;
    }
  }
}
