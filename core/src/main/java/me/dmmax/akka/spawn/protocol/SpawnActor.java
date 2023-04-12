package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;

public interface SpawnActor<T> {

  ActorRef<AskSpawnResponse<T>> replyTo();

  SpawnActorInfo<T> actorInfo();
}
