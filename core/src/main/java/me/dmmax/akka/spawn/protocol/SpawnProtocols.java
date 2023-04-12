package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;
import akka.actor.typed.javadsl.ActorContext;

public class SpawnProtocols {

  public static <P, C> ActorRef<C> spawnChildActor(ActorContext<P> actorContext, SpawnActor<C> spawnChildActor) {
    SpawnActorInfo<C> actorInfo = spawnChildActor.actorInfo();
    ActorRef<C> childActor = actorContext.spawn(actorInfo.behavior(), actorInfo.actorName(), actorInfo.props());
    AskSpawnResponse<C> askSpawnResponse = new AskSpawnResponse<>(childActor);
    spawnChildActor.replyTo().tell(askSpawnResponse);
    return childActor;
  }
}
