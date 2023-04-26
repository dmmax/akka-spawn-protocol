package me.dmmax.akka.spawn.protocol.guice;

import java.util.UUID;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.util.Types;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Props;
import me.dmmax.akka.spawn.protocol.ActorCreationStrategy;
import me.dmmax.akka.spawn.protocol.PingActor;
import me.dmmax.akka.spawn.protocol.PingActor.Ping;
import me.dmmax.akka.spawn.protocol.PongActor.Pong;
import me.dmmax.akka.spawn.protocol.SpawnActorInfo;
import me.dmmax.akka.spawn.protocol.SpawnProtocol;
import me.dmmax.akka.spawn.protocol.SpawnerActor;
import me.dmmax.akka.spawn.protocol.SpawnerActor.Command;
import me.dmmax.akka.spawn.protocol.SpawnerActor.SpawnActorCommandWrapper;
import me.dmmax.akka.spawn.protocol.utils.AkkaTestKitExtension;

@SuppressWarnings("unchecked")
class RegisterSpawnProtocolModuleTest {

  @RegisterExtension
  static AkkaTestKitExtension akkaTestKitExtension = new AkkaTestKitExtension();

  @Test
  void should_register_spawn_protocol() {
    Injector injector = createInjectorWithRegisterModule();
    SpawnProtocol<Command> spawner = (SpawnProtocol<Command>) injector.getInstance(
        Key.get(Types.newParameterizedType(SpawnProtocol.class, Command.class)));
    SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
    ActorRef<PingActor.Ping> pingActor = spawner.createActor(pingActorInfo);
    // then
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    pingActor.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor));
  }


  private Injector createInjectorWithRegisterModule() {
    return Guice.createInjector(
        new RegisterSpawnProtocolModule<>(Command.class, SpawnActorCommandWrapper::new),
        new AkkaActorSystemModule(akkaTestKitExtension),
        new RegisterSpawnerActorModule());
  }

  static class RegisterSpawnerActorModule extends AbstractModule {

    @Provides
    @Singleton
    ActorRef<Command> spawnerActor(ActorSystem<Void> actorSystem) {
      // A root actor anyway should be created by the actor system to be accessible in SpawnProtocol
      return actorSystem.systemActorOf(SpawnerActor.create(), "spawner-" + UUID.randomUUID(), Props.empty());
    }
  }
}