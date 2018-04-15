package no.ntnu.tdt4240.asteroids.service;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import no.ntnu.tdt4240.asteroids.service.network.INetworkService;

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

  /** Proxies {@link AppModule#networkService()}. */
  public static INetworkService proxyNetworkService(AppModule instance) {
    return instance.networkService();
  }
}
