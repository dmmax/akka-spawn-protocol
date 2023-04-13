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
  private final String actorName;
  private final Props props;

  /**
   * The {@link Props#empty()} will be used for spawning the actor.
   *
   * @param behavior – behavior of the actor to spawn
   * @param actorName – name of the actor to spawn
   */
  public SpawnActorInfo(Behavior<T> behavior, String actorName) {
    this(behavior, actorName, Props.empty());
  }

  /**
   * @param behavior – behavior of the actor to spawn
   * @param actorName – name of the actor to spawn
   * @param props – props of the actor to spawn
   */
  public SpawnActorInfo(Behavior<T> behavior, String actorName, Props props) {
    this.behavior = behavior;
    this.actorName = actorName;
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
   * Returns – name of the actor to spawn.
   *
   * @return – name of the actor to spawn
   */
  public String actorName() {
    return actorName;
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
