package me.dmmax.akka.spawn.protocol.guice;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import me.dmmax.akka.spawn.protocol.SpawnCommandConverter;
import me.dmmax.akka.spawn.protocol.SpawnProtocol;

public class RegisterSpawnProtocolModule<P> extends AbstractModule {

  private final Class<P> type;
  private final SpawnCommandConverter<P> spawnCommandConverter;

  public RegisterSpawnProtocolModule(Class<P> type, SpawnCommandConverter<P> spawnCommandConverter) {
    this.type = type;
    this.spawnCommandConverter = spawnCommandConverter;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void configure() {
    // Register spawn command converter
    TypeLiteral<SpawnCommandConverter<P>> spawnCommandConverterKey = (TypeLiteral<SpawnCommandConverter<P>>) TypeLiteral.get(
        Types.newParameterizedType(SpawnCommandConverter.class, type));
    bind(spawnCommandConverterKey).toInstance(this.spawnCommandConverter);
    // Register spawn protocol
    TypeLiteral<SpawnProtocol<P>> spawnProtocolKey = (TypeLiteral<SpawnProtocol<P>>) TypeLiteral.get(
        Types.newParameterizedType(SpawnProtocol.class, type));
    TypeLiteral<SpawnProtocolProvider<P>> spawnProtocolProviderKey = (TypeLiteral<SpawnProtocolProvider<P>>) TypeLiteral.get(
        Types.newParameterizedType(SpawnProtocolProvider.class, type));
    bind(spawnProtocolKey).toProvider(spawnProtocolProviderKey);
  }
}
