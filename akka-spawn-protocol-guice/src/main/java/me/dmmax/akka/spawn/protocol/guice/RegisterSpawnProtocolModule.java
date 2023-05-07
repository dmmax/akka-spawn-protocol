package me.dmmax.akka.spawn.protocol.guice;

import static me.dmmax.akka.spawn.protocol.guice.SpawnProtocolTypes.*;

import com.google.inject.AbstractModule;
import me.dmmax.akka.spawn.protocol.SpawnActorCommand;
import me.dmmax.akka.spawn.protocol.SpawnCommandConverter;

/**
 * Guice module to register spawn protocol for a specific type of actor.
 *
 * @param <T> - actor type
 */
public class RegisterSpawnProtocolModule<T> extends AbstractModule {

  private final Class<T> actorType;
  private final SpawnCommandConverter<T> spawnCommandConverter;

  /**
   * @param actorType – actor type
   * @param spawnCommandConverter – command converter which wraps {@link SpawnActorCommand} to actor message type
   */
  public RegisterSpawnProtocolModule(Class<T> actorType, SpawnCommandConverter<T> spawnCommandConverter) {
    this.actorType = actorType;
    this.spawnCommandConverter = spawnCommandConverter;
  }

  @Override
  protected void configure() {
    bind(spawnCommandConverterKey(actorType)).toInstance(this.spawnCommandConverter);
    bind(spawnProtocolKey(actorType)).toProvider(spawnProtocolProviderKey(actorType));
  }
}
