package me.dmmax.akka.spawn.protocol;

import static org.assertj.core.api.Assertions.*;

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

/**
 * This test shows how to create nested actors using the {@link SpawnProtocol}
 */
class SpawnProtocolTest {

  @RegisterExtension
  static AkkaTestKitExtension akkaTestKitExtension = new AkkaTestKitExtension();

  @Test
  void should_create_nested_actor_under_root() {
    // given
    SpawnProtocol<Command> rootActorSpawner = createAndWrapActorIntoSpawnProtocol();
    SpawnActorInfo<Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
    // when
    ActorRef<Ping> pingActor = rootActorSpawner.createActor(pingActorInfo);
    // then
    verifyPingActor(pingActor);
  }

  @Test
  void should_spawn_two_child_actor_with_same_defined_prefix() {
    // given
    SpawnProtocol<Command> rootActorSpawner = createAndWrapActorIntoSpawnProtocol();
    SpawnActorInfo<Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
    // when
    ActorRef<Ping> pingActor1 = rootActorSpawner.createActor(pingActorInfo);
    ActorRef<Ping> pingActor2 = rootActorSpawner.createActor(pingActorInfo);
    // then
    assertThat(pingActor1.path().name()).isEqualTo("pinger");
    assertThat(pingActor2.path().name()).isEqualTo("pinger-1");
    verifyPingActor(pingActor1);
    verifyPingActor(pingActor2);
  }

  @Test
  void should_spawn_actor_using_anonymous_strategy() {
    // given
    SpawnProtocol<Command> andWrapActorIntoSpawnProtocol = createAndWrapActorIntoSpawnProtocol();
    SpawnActorInfo<Pong> pongActorInfo = new SpawnActorInfo<>(PongActor.create(), ActorCreationStrategy.anonymous());
    // when
    ActorRef<Pong> pingActor = andWrapActorIntoSpawnProtocol.createActor(pongActorInfo);
    // then
    verifyPongActor(pingActor);
  }

  @Test
  void should_spawn_actor_using_unique_strategy() {
    // given
    SpawnProtocol<Command> andWrapActorIntoSpawnProtocol = createAndWrapActorIntoSpawnProtocol();
    SpawnActorInfo<Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.unique("my-actor"));
    // when
    ActorRef<Ping> pingActor = andWrapActorIntoSpawnProtocol.createActor(pingActorInfo);
    // then
    verifyPingActor(pingActor);
  }

  private void verifyPingActor(ActorRef<Ping> pingActor) {
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    pingActor.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor));
  }

  private void verifyPongActor(ActorRef<Pong> pongActor) {
    TestProbe<Ping> pingTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    pongActor.tell(new Pong(pingTestProbe.getRef()));
    pingTestProbe.expectMessage(new Ping(pongActor));
  }

  private SpawnProtocol<Command> createAndWrapActorIntoSpawnProtocol() {
    ActorSystem<Void> actorSystem = akkaTestKitExtension.actorSystem();
    ActorRef<Command> rootActor = actorSystem.systemActorOf(SpawnerActor.create(), "root-".concat(UUID.randomUUID().toString()),
        Props.empty());
    return new SpawnProtocol<>(akkaTestKitExtension.scheduler(), Duration.ofSeconds(1), rootActor, SpawnActorCommandWrapper::new);
  }
}
