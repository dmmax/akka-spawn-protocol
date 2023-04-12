package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;

class AskSpawnResponse<T> {

  private final ActorRef<T> actorRef;

  public AskSpawnResponse(ActorRef<T> actorRef) {
    this.actorRef = actorRef;
  }

  ActorRef<T> actorRef() {
    return actorRef;
  }

}
