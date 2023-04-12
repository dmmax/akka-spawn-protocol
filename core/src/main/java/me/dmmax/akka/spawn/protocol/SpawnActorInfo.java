package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.Behavior;
import akka.actor.typed.Props;

public class SpawnActorInfo<T> {

  private final Behavior<T> behavior;
  private final String actorName;
  private final Props props;

  public SpawnActorInfo(Behavior<T> behavior, String actorName) {
    this(behavior, actorName, Props.empty());
  }

  public SpawnActorInfo(Behavior<T> behavior, String actorName, Props props) {
    this.behavior = behavior;
    this.actorName = actorName;
    this.props = props;
  }

  public Behavior<T> behavior() {
    return behavior;
  }

  public String actorName() {
    return actorName;
  }

  public Props props() {
    return props;
  }
}
