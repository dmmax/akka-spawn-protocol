package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;

/**
 * Command to spawn an actor.
 *
 * @param <T> – type of the actor to spawn
 */
public class SpawnActor<T> {

  private final ActorRef<AskSpawnResponse<T>> replyTo;
  private final SpawnActorInfo<T> actorInfo;

  /**
   * @param replyTo – actor to send response to
   * @param actorInfo – information about actor to spawn
   */
  public SpawnActor(ActorRef<AskSpawnResponse<T>> replyTo, SpawnActorInfo<T> actorInfo) {
    this.replyTo = replyTo;
    this.actorInfo = actorInfo;
  }

  /**
   * Returns – actor to send response to.
   *
   * @return – actor to send response to
   */
  public ActorRef<AskSpawnResponse<T>> replyTo() {
    return replyTo;
  }

  /**
   * Returns – information about actor to spawn.
   *
   * @return – information about actor to spawn
   */
  public SpawnActorInfo<T> actorInfo() {
    return actorInfo;
  }
}
