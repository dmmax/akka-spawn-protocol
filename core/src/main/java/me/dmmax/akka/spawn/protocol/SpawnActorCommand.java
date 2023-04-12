package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;

public class SpawnActorCommand<T> implements SpawnActor<T> {

  private final ActorRef<AskSpawnResponse<T>> replyTo;
  private final SpawnActorInfo<T> actorInfo;

  public SpawnActorCommand(ActorRef<AskSpawnResponse<T>> replyTo, SpawnActorInfo<T> actorInfo) {
    this.replyTo = replyTo;
    this.actorInfo = actorInfo;
  }

  public ActorRef<AskSpawnResponse<T>> replyTo() {
    return replyTo;
  }

  public SpawnActorInfo<T> actorInfo() {
    return actorInfo;
  }
}
