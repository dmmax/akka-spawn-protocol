package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;

/**
 * Wraps the {@link ActorRef} of the spawned actor.
 *
 * @param <T> – type of the actor to spawn
 */
class AskSpawnResponse<T> {

  private final ActorRef<T> actorRef;

  /**
   * @param actorRef – actor ref of the spawned actor
   */
  public AskSpawnResponse(ActorRef<T> actorRef) {
    this.actorRef = actorRef;
  }

  /**
   * Returns – actor ref of the spawned actor.
   *
   * @return – actor ref of the spawned actor
   */
  ActorRef<T> actorRef() {
    return actorRef;
  }

}
