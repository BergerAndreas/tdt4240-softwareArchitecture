package co.aeons.zombie.shooter.service.Injectors;

import javax.annotation.Generated;
import javax.inject.Provider;

import co.aeons.zombie.shooter.service.AppComponent;
import co.aeons.zombie.shooter.service.AppModule;
import co.aeons.zombie.shooter.service.ISettingsService;
import co.aeons.zombie.shooter.service.network.INetworkService;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerAppComponent implements AppComponent {
  private Provider<INetworkService> networkServiceProvider;

  private Provider<ISettingsService> provideSettingsServiceProvider;


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

    this.provideSettingsServiceProvider =
        DoubleCheck.provider(AppModule_ProvideSettingsServiceFactory.create(builder.appModule));

  }

  @Override
  public INetworkService getNetworkService() {
    return networkServiceProvider.get();
  }

  @Override
  public ISettingsService getSettingsService() {
    return provideSettingsServiceProvider.get();
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
