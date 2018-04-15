package co.aeons.zombie.shooter.service.Injectors;

import javax.annotation.Generated;
import javax.inject.Provider;

import co.aeons.zombie.shooter.service.AppModule;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class AppModule_ProvideAnimationFactoryFactory implements Factory<AnimationFactory> {
  private final AppModule module;

  private final Provider<AssetService> assetServiceProvider;

  public AppModule_ProvideAnimationFactoryFactory(
      AppModule module, Provider<AssetService> assetServiceProvider) {
    assert module != null;
    this.module = module;
    assert assetServiceProvider != null;
    this.assetServiceProvider = assetServiceProvider;
  }

  @Override
  public AnimationFactory get() {
    return Preconditions.checkNotNull(
        module.provideAnimationFactory(assetServiceProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<AnimationFactory> create(
      AppModule module, Provider<AssetService> assetServiceProvider) {
    return new AppModule_ProvideAnimationFactoryFactory(module, assetServiceProvider);
  }

  /** Proxies {@link AppModule#provideAnimationFactory(AssetService)}. */
  public static AnimationFactory proxyProvideAnimationFactory(
      AppModule instance, AssetService assetService) {
    return instance.provideAnimationFactory(assetService);
  }
}
