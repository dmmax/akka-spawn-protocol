package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;

/**
 * Utility class for spawning actors.
 */
public class SpawnProtocols {

  /**
   * Spawns a child actor and sends a response to the replyTo actor.
   *
   * @param actorContext - actor context
   * @param spawnActorCommand - command to spawn an actor
   * @param <P> - parent actor message type
   * @param <C> - child actor message type
   * @return spawned child actor reference
   */
  public static <P, C> ActorRef<C> spawnChildActor(ActorContext<P> actorContext, SpawnActorCommand<C> spawnActorCommand) {
    SpawnActorInfo<C> actorInfo = spawnActorCommand.actorInfo();
    ActorRef<C> childActor = actorInfo.creationStrategy().createActor(actorContext, actorInfo.behavior(), actorInfo.props());
    AskSpawnResponse<C> askSpawnResponse = new AskSpawnResponse<>(childActor);
    spawnActorCommand.replyTo().tell(askSpawnResponse);
    return childActor;
  }
}
