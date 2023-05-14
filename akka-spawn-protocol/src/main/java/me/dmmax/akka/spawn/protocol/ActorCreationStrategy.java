package me.dmmax.akka.spawn.protocol;

import java.util.UUID;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.Props;
import akka.actor.typed.javadsl.ActorContext;

/**
 * Strategy for creating an actor.
 */
public interface ActorCreationStrategy {

  /**
   * Creates an actor.
   *
   * @param actorContext - parent actor context
   * @param behavior - behavior of the child actor
   * @param props - props of the child actor
   * @param <C> - child actor message type
   * @param <P> - parent actor message type
   * @return - child actor
   */
  <C, P> ActorRef<C> createActor(ActorContext<P> actorContext, Behavior<C> behavior, Props props);

  /**
   * Creates an actor with an anonymous name.
   *
   * @return - anonymous actor creation strategy
   */
  static ActorCreationStrategy anonymous() {
    return new AnnonymousActorCreationStrategy();
  }

  /**
   * Creates an actor with a unique suffix using {@link UUID} and a name prefix.
   *
   * @param namePrefix - name prefix which will be used for the actor name
   * @return - unique actor creation strategy
   */
  static ActorCreationStrategy unique(String namePrefix) {
    return new UniqueNameActorCreationStrategy(namePrefix);
  }

  /**
   * Creates an actor with a sequential suffix and defined prefix.
   *
   * @param namePrefix - name prefix which will be used for the actor name
   * @return - sequential actor creation strategy
   */
  static ActorCreationStrategy sequence(String namePrefix) {
    return new SequenceNameActorCreationStrategy(namePrefix);
  }

  /**
   * Creates an actor with an anonymous name.
   */
  class AnnonymousActorCreationStrategy implements ActorCreationStrategy {

    @Override
    public <C, P> ActorRef<C> createActor(ActorContext<P> actorContext, Behavior<C> behavior, Props props) {
      return actorContext.spawnAnonymous(behavior, props);
    }
  }

  /**
   * Creates an actor with a unique suffix using {@link UUID}.
   */
  class UniqueNameActorCreationStrategy implements ActorCreationStrategy {

    private final String namePrefix;

    private UniqueNameActorCreationStrategy(String namePrefix) {
      this.namePrefix = namePrefix;
    }

    @Override
    public <C, P> ActorRef<C> createActor(ActorContext<P> actorContext, Behavior<C> behavior, Props props) {
      return actorContext.spawn(behavior, uniqueActorName(), props);
    }

    private String uniqueActorName() {
      return namePrefix.concat("-").concat(UUID.randomUUID().toString());
    }
  }

  /**
   * Creates an actor with a sequential suffix.
   */
  class SequenceNameActorCreationStrategy implements ActorCreationStrategy {

    private final String namePrefix;

    private SequenceNameActorCreationStrategy(String namePrefix) {
      this.namePrefix = namePrefix;
    }

    @Override
    public <C, P> ActorRef<C> createActor(ActorContext<P> actorContext, Behavior<C> behavior, Props props) {
      return actorContext.spawn(behavior, sequenceActorName(actorContext), props);
    }

    private String sequenceActorName(ActorContext<?> actorContext) {
      int counter = 0;
      while (actorContext.getChild(actorName(counter)).isPresent()) {
        counter++;
      }
      return actorName(counter);
    }

    private String actorName(int counter) {
      return counter == 0 ? namePrefix : namePrefix + "-" + counter;
    }
  }
}
