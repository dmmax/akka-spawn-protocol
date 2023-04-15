package me.dmmax.akka.spawn.protocol;

import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import me.dmmax.akka.spawn.protocol.PingActor.Ping;
import me.dmmax.akka.spawn.protocol.PongActor.Pong;
import me.dmmax.akka.spawn.protocol.SpawnerActor.Command;
import me.dmmax.akka.spawn.protocol.SpawnerActor.SpawnActorCommandWrapper;
import me.dmmax.akka.spawn.protocol.utils.AkkaTestKitExtension;

class SpawnProtocolIntegrationTest {

  @RegisterExtension
  static AkkaTestKitExtension akkaTestKitExtension = new AkkaTestKitExtension();

  @Test
  void should_create_nested_actor_under_root() {
    // given
    SpawnProtocol<Command> rootActorSpawner = createAndWrapActorIntoSpawnProtocol();
    SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
    // when
    ActorRef<PingActor.Ping> pingActor = rootActorSpawner.createActor(pingActorInfo);
    // then
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    // It is possible to access nested actor directly
    pingActor.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor));
  }

  @Test
  void should_spawn_two_child_actor_with_same_name() {
    // given
    SpawnProtocol<Command> rootActorSpawner = createAndWrapActorIntoSpawnProtocol();
    SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
    // when
    ActorRef<PingActor.Ping> pingActor1 = rootActorSpawner.createActor(pingActorInfo);
    ActorRef<PingActor.Ping> pingActor2 = rootActorSpawner.createActor(pingActorInfo);
    // then
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    // It is possible to access nested actor directly
    pingActor1.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor1));
    pingActor2.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor2));
  }

  private SpawnProtocol<Command> createAndWrapActorIntoSpawnProtocol() {
    ActorSystem<Void> actorSystem = akkaTestKitExtension.actorSystem();
    ActorRef<Command> rootActor = actorSystem.systemActorOf(SpawnerActor.create(), "root-".concat(UUID.randomUUID().toString()),
        Props.empty());
    return new SpawnProtocol<>(akkaTestKitExtension.scheduler(), Duration.ofSeconds(1), rootActor, SpawnActorCommandWrapper::new);
  }
}
