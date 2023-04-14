package me.dmmax.akka.spawn.protocol;

import java.util.Objects;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import me.dmmax.akka.spawn.protocol.PingActor.Ping;
import me.dmmax.akka.spawn.protocol.PongActor.Pong;

public class PingActor extends AbstractBehavior<Ping> {

  public PingActor(ActorContext<Ping> context) {
    super(context);
  }

  @Override
  public Receive<Ping> createReceive() {
    return newReceiveBuilder()
        .onMessage(Ping.class, this::onPing)
        .build();
  }

  private Behavior<Ping> onPing(Ping ping) {
    System.out.println("Ping");
    ping.replyTo().tell(new Pong(getContext().getSelf()));
    return this;
  }

  public static Behavior<Ping> create() {
    return Behaviors.setup(PingActor::new);
  }

  public static class Ping {

    private final ActorRef<Pong> replyTo;

    public Ping(ActorRef<Pong> replyTo) {
      this.replyTo = replyTo;
    }

    public ActorRef<Pong> replyTo() {
      return replyTo;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Ping ping = (Ping) o;
      return Objects.equals(replyTo, ping.replyTo);
    }

    @Override
    public int hashCode() {
      return Objects.hash(replyTo);
    }
  }
}
