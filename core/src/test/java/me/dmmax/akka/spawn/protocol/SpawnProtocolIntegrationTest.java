package me.dmmax.akka.spawn.protocol;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import akka.actor.typed.Scheduler;
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
    ActorSystem<Void> actorSystem = akkaTestKitExtension.actorSystem();
    Scheduler scheduler = akkaTestKitExtension.scheduler();
    ActorRef<Command> rootActor = actorSystem.systemActorOf(SpawnerActor.create(), "root", Props.empty());
    SpawnProtocol<Command> rootActorSpawner = new SpawnProtocol<>(
        scheduler, Duration.ofSeconds(1), rootActor, SpawnActorCommandWrapper::new);
    SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), "pinger");
    // when
    ActorRef<PingActor.Ping> pingActor = rootActorSpawner.createActor(pingActorInfo);
    // then
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    // It is possible to access nested actor directly
    pingActor.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor));
  }
}
