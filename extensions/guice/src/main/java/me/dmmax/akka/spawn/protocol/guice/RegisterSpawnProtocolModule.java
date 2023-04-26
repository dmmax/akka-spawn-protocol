package me.dmmax.akka.spawn.protocol.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import me.dmmax.akka.spawn.protocol.SpawnActor;
import me.dmmax.akka.spawn.protocol.SpawnCommandConverter;
import me.dmmax.akka.spawn.protocol.SpawnProtocol;

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
   * @param spawnCommandConverter – command converter which wraps {@link SpawnActor} to actor message type
   */
  public RegisterSpawnProtocolModule(Class<T> actorType, SpawnCommandConverter<T> spawnCommandConverter) {
    this.actorType = actorType;
    this.spawnCommandConverter = spawnCommandConverter;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void configure() {
    // Register spawn command converter
    TypeLiteral<SpawnCommandConverter<T>> spawnCommandConverterKey = (TypeLiteral<SpawnCommandConverter<T>>) TypeLiteral.get(
        Types.newParameterizedType(SpawnCommandConverter.class, actorType));
    bind(spawnCommandConverterKey).toInstance(this.spawnCommandConverter);
    // Register spawn protocol
    TypeLiteral<SpawnProtocol<T>> spawnProtocolKey = (TypeLiteral<SpawnProtocol<T>>) TypeLiteral.get(
        Types.newParameterizedType(SpawnProtocol.class, actorType));
    TypeLiteral<SpawnProtocolProvider<T>> spawnProtocolProviderKey = (TypeLiteral<SpawnProtocolProvider<T>>) TypeLiteral.get(
        Types.newParameterizedType(SpawnProtocolProvider.class, actorType));
    bind(spawnProtocolKey).toProvider(spawnProtocolProviderKey);
  }
}
