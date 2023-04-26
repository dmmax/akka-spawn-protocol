package me.dmmax.akka.spawn.protocol.guice;

import javax.inject.Singleton;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;
import me.dmmax.akka.spawn.protocol.utils.AkkaTestKitExtension;

public class AkkaActorSystemModule extends AbstractModule {

  private final AkkaTestKitExtension _extension;

  public AkkaActorSystemModule(AkkaTestKitExtension extension) {
    _extension = extension;
  }

  @Provides
  @Singleton
  ActorSystem<Void> actorSystem() {
    return _extension.actorSystem();
  }

  @Provides
  @Singleton
  Scheduler scheduler() {
    return _extension.scheduler();
  }
}
