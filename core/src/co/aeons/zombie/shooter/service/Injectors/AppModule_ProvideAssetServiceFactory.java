package no.ntnu.tdt4240.asteroids.service;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvideAssetServiceFactory implements Factory<AssetService> {
  private final AppModule module;

  public AppModule_ProvideAssetServiceFactory(AppModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public AssetService get() {
    return Preconditions.checkNotNull(
        module.provideAssetService(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<AssetService> create(AppModule module) {
    return new AppModule_ProvideAssetServiceFactory(module);
  }

  /** Proxies {@link AppModule#provideAssetService()}. */
  public static AssetService proxyProvideAssetService(AppModule instance) {
    return instance.provideAssetService();
  }
}
