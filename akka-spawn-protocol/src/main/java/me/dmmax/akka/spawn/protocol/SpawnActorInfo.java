package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.Behavior;
import akka.actor.typed.Props;

/**
 * Information about the actor to spawn.
 *
 * @param <T> – type of the actor to spawn
 */
public class SpawnActorInfo<T> {

  private final Behavior<T> behavior;
  private final ActorCreationStrategy creationStrategy;
  private final Props props;

  /**
   * The {@link Props#empty()} will be used for spawning the actor.
   *
   * @param behavior – behavior of the actor to spawn
   * @param creationStrategy – creation strategy of the actor to spawn
   */
  public SpawnActorInfo(Behavior<T> behavior, ActorCreationStrategy creationStrategy) {
    this(behavior, creationStrategy, Props.empty());
  }

  /**
   * @param behavior – behavior of the actor to spawn
   * @param creationStrategy – creation strategy of the actor to spawn
   * @param props – props of the actor to spawn
   */
  public SpawnActorInfo(Behavior<T> behavior, ActorCreationStrategy creationStrategy, Props props) {
    this.behavior = behavior;
    this.creationStrategy = creationStrategy;
    this.props = props;
  }

  /**
   * Returns – behavior of the actor to spawn.
   *
   * @return – behavior of the actor to spawn
   */
  public Behavior<T> behavior() {
    return behavior;
  }

  /**
   * Returns – creation strategy of the actor to spawn.
   *
   * @return – creation strategy of the actor to spawn
   */
  public ActorCreationStrategy creationStrategy() {
    return creationStrategy;
  }

  /**
   * Returns – props of the actor to spawn.
   *
   * @return – props of the actor to spawn
   */
  public Props props() {
    return props;
  }
}
