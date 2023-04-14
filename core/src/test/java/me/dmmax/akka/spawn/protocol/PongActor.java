package me.dmmax.akka.spawn.protocol;

import java.util.Objects;
import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import me.dmmax.akka.spawn.protocol.PingActor.Ping;
import me.dmmax.akka.spawn.protocol.PongActor.Pong;

public class PongActor extends AbstractBehavior<Pong> {

  public PongActor(ActorContext<Pong> context) {
    super(context);
  }

  @Override
  public Receive<Pong> createReceive() {
    return newReceiveBuilder()
        .onMessage(Pong.class, this::onPong)
        .build();
  }

  private Behavior<Pong> onPong(Pong pong) {
    System.out.println("Pong");
    pong.replyTo().tell(new Ping(getContext().getSelf()));
    return this;
  }

  public static class Pong {

    private final ActorRef<Ping> replyTo;

    public Pong(ActorRef<Ping> replyTo) {
      this.replyTo = replyTo;
    }

    public ActorRef<Ping> replyTo() {
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
      Pong pong = (Pong) o;
      return Objects.equals(replyTo, pong.replyTo);
    }

    @Override
    public int hashCode() {
      return Objects.hash(replyTo);
    }
  }
}
