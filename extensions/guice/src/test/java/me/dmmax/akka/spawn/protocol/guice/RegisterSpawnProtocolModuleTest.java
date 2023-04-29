package me.dmmax.akka.spawn.protocol.guice;

import static me.dmmax.akka.spawn.protocol.guice.SpawnProtocolTypes.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
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

class RegisterSpawnProtocolModuleTest {

  @RegisterExtension
  static AkkaTestKitExtension akkaTestKitExtension = new AkkaTestKitExtension();

  @Test
  void should_register_spawn_protocol() {
    // given
    Injector injector = createInjector();
    // when
    SpawnProtocol<Command> spawner = injector.getInstance(spawnProtocolKey(Command.class));
    // then try to spawn ping actor manually
    SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
    ActorRef<PingActor.Ping> pingActor = spawner.createActor(pingActorInfo);
    // then verify ping actor works
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    pingActor.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor));
  }

  @Test
  void should_register_spawn_protocol_for_nested_actor() {
    // given
    Injector injector = createInjector(new RegisterNestedActorModule());
    // when
    ActorRef<Ping> pingActor = injector.getInstance(actorRefKey(Ping.class));
    // then verify ping actor works
    TestProbe<Pong> pongTestProbe = akkaTestKitExtension.testKit().createTestProbe();
    pingActor.tell(new Ping(pongTestProbe.getRef()));
    pongTestProbe.expectMessage(new Pong(pingActor));
  }

  private Injector createInjector(Module... additionalModules) {
    return createInjector(Arrays.asList(additionalModules));
  }

  private Injector createInjector(List<Module> additionalModules) {
    List<Module> modulesToRegister = new ArrayList<>();
    modulesToRegister.addAll(standardModules());
    modulesToRegister.addAll(additionalModules);
    return Guice.createInjector(modulesToRegister);
  }

  private List<Module> standardModules() {
    // Register default guice modules to make available actor system, spawner actor and spawn protocol for the spawner
    return Arrays.asList(
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

  static class RegisterNestedActorModule extends AbstractModule {

    @Provides
    ActorRef<PingActor.Ping> nestedActor(SpawnProtocol<Command> spawnerActor) {
      SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(),
          ActorCreationStrategy.sequence("ping"));
      return spawnerActor.createActor(pingActorInfo);
    }
  }
}