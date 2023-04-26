package me.dmmax.akka.spawn.protocol.guice;

import java.time.Duration;
import com.google.inject.Inject;
import com.google.inject.Provider;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import me.dmmax.akka.spawn.protocol.SpawnCommandConverter;
import me.dmmax.akka.spawn.protocol.SpawnProtocol;

class SpawnProtocolProvider<T> implements Provider<SpawnProtocol<T>> {

  private final Scheduler scheduler;
  private final Provider<ActorRef<T>> actorRefProvider;
  private final SpawnCommandConverter<T> spawnCommandConverter;
  private final Duration askTimeout = Duration.ofSeconds(5);

  @Inject
  SpawnProtocolProvider(Scheduler scheduler, Provider<ActorRef<T>> actorRefProvider, SpawnCommandConverter<T> spawnCommandConverter) {
    this.scheduler = scheduler;
    this.actorRefProvider = actorRefProvider;
    this.spawnCommandConverter = spawnCommandConverter;
  }

  @Override
  public SpawnProtocol<T> get() {
    return new SpawnProtocol<>(scheduler, askTimeout, actorRefProvider.get(), spawnCommandConverter);
  }
}