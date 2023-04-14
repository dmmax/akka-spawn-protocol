package me.dmmax.akka.spawn.protocol;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import me.dmmax.akka.spawn.protocol.PingActor.Ping;
import me.dmmax.akka.spawn.protocol.PongActor.Pong;
import me.dmmax.akka.spawn.protocol.SpawnerActor.Command;

public class SpawnerActor extends AbstractBehavior<Command> {

  private final ActorRef<Ping> pingWrapper;
  private final ActorRef<Pong> pongWrapper;

  private SpawnerActor(ActorContext<Command> context) {
    super(context);
    pingWrapper = context.messageAdapter(Ping.class, PingWrapper::new);
    pongWrapper = context.messageAdapter(Pong.class, PongWrapper::new);
  }

  @Override
  public Receive<Command> createReceive() {
    return newReceiveBuilder()
        .onMessage(PongWrapper.class, this::onPong)
        .onMessage(PingWrapper.class, this::onPing)
        .onMessage(SpawnActorCommandWrapper.class, this::onSpawnActor)
        .build();
  }

  private <CHILD> Behavior<Command> onSpawnActor(SpawnActorCommandWrapper<CHILD> wrapper) {
    SpawnProtocols.spawnChildActor(getContext(), wrapper.spawnActorCommand());
    return this;
  }

  private Behavior<Command> onPing(PingWrapper wrapper) {
    System.out.println("Ping");
    Ping ping = wrapper.ping();
    ping.replyTo().tell(new Pong(pingWrapper));
    return this;
  }

  private Behavior<Command> onPong(PongWrapper wrapper) {
    System.out.println("Pong");
    Pong pong = wrapper.pong();
    pong.replyTo().tell(new Ping(pongWrapper));
    return this;
  }

  public static Behavior<Command> create() {
    return Behaviors.setup(SpawnerActor::new);
  }

  interface Command {

  }

  static class SpawnActorCommandWrapper<CHILD> implements Command {

    private final SpawnActor<CHILD> askSpawnResponse;

    SpawnActorCommandWrapper(SpawnActor<CHILD> askSpawnResponse) {
      this.askSpawnResponse = askSpawnResponse;
    }

    private SpawnActor<CHILD> spawnActorCommand() {
      return askSpawnResponse;
    }
  }

  static class PingWrapper implements Command {

    private final Ping ping;

    PingWrapper(Ping ping) {
      this.ping = ping;
    }

    Ping ping() {
      return ping;
    }
  }

  static class PongWrapper implements Command {

    private final Pong pong;

    PongWrapper(Pong pong) {
      this.pong = pong;
    }

    Pong pong() {
      return pong;
    }
  }
}
