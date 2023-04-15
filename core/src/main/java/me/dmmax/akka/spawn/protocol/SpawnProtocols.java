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
  public static <P, C> ActorRef<C> spawnChildActor(ActorContext<P> actorContext, SpawnActor<C> spawnActorCommand) {
    SpawnActorInfo<C> actorInfo = spawnActorCommand.actorInfo();
    String uniqueActorName = uniqueActorName(actorContext, actorInfo.actorName());
    ActorRef<C> childActor = actorContext.spawn(actorInfo.behavior(), uniqueActorName, actorInfo.props());
    AskSpawnResponse<C> askSpawnResponse = new AskSpawnResponse<>(childActor);
    spawnActorCommand.replyTo().tell(askSpawnResponse);
    return childActor;
  }

  private static <P> String uniqueActorName(ActorContext<P> actorContext, String actorName) {
    int counter = 0;
    while (actorContext.getChild(actorName(actorName, counter)).isPresent()) {
      counter++;
    }
    return actorName(actorName, counter);
  }

  private static String actorName(String actorName, int counter) {
    return counter == 0 ? actorName : actorName + "-" + counter;
  }
}
