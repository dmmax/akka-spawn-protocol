package me.dmmax.akka.spawn.protocol.guice;

import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import me.dmmax.akka.spawn.protocol.utils.AkkaTestKitExtension;

/**
 * Guice module to register {@link ActorSystem} and {@link Scheduler} which are provided by {@link AkkaTestKitExtension}
 */
public class AkkaActorSystemModule extends AbstractModule {

  private final AkkaTestKitExtension extension;

  public AkkaActorSystemModule(AkkaTestKitExtension extension) {
    this.extension = extension;
  }

  @Provides
  @Singleton
  ActorSystem<Void> actorSystem() {
    return extension.actorSystem();
  }

  @Provides
  @Singleton
  Scheduler scheduler() {
    return extension.scheduler();
  }
}
