package me.dmmax.akka.spawn.protocol;

import java.time.Duration;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Scheduler;
import akka.actor.typed.javadsl.AskPattern;
import akka.japi.function.Function;

/**
 * Protocol for spawning actors under the parent actor using the ask pattern {@link AskPattern}
 *
 * @param <P> – parent message
 */
public class SpawnProtocol<P> {

  private final Scheduler scheduler;
  private final Duration askTimeout;
  private final ActorRef<P> parentActor;
  private final SpawnCommandConverter<P> spawnCommandConverter;

  public SpawnProtocol(Scheduler scheduler, Duration askTimeout, ActorRef<P> parentActor, SpawnCommandConverter<P> spawnCommandConverter) {
    this.scheduler = scheduler;
    this.askTimeout = askTimeout;
    this.parentActor = parentActor;
    this.spawnCommandConverter = spawnCommandConverter;
  }

  /**
   * Creates a child actor under the parent actor using the ask pattern. The default ask timeout will be used {@link #askTimeout}
   *
   * @param childActorInfo - child actor info
   * @param <C> - child actor message type
   * @return child actor ref
   */
  public <C> ActorRef<C> createActor(SpawnActorInfo<C> childActorInfo) {
    return createActor(childActorInfo, askTimeout);
  }

  /**
   * Creates a child actor under the parent actor using the ask pattern
   *
   * @param childActorInfo - child actor info
   * @param askTimeout - ask timeout
   * @param <C> - child actor message type
   * @return child actor ref
   */
  public <C> ActorRef<C> createActor(SpawnActorInfo<C> childActorInfo, Duration askTimeout) {
    Function<ActorRef<AskSpawnResponse<C>>, P> messageFactory = actorRef -> {
      SpawnActor<C> spawnChildActorCommand = new SpawnActor<>(actorRef, childActorInfo);
      return spawnCommandConverter.toActorSpawnMessage(spawnChildActorCommand);
    };
    return AskPattern.ask(
            parentActor,
            messageFactory,
            askTimeout,
            scheduler)
        .toCompletableFuture().join().actorRef();
  }
}
