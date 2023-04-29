package me.dmmax.akka.spawn.protocol.guice;

import com.google.inject.Key;
import com.google.inject.util.Types;
import akka.actor.typed.ActorRef;
import me.dmmax.akka.spawn.protocol.SpawnCommandConverter;
import me.dmmax.akka.spawn.protocol.SpawnProtocol;

/**
 * Utility class for creating Guice keys for {@link SpawnProtocol} and related classes
 */
@SuppressWarnings("unchecked")
public class SpawnProtocolTypes {

  public static <T> Key<SpawnProtocol<T>> spawnProtocolKey(Class<T> actorType) {
    return (Key<SpawnProtocol<T>>) Key.get(Types.newParameterizedType(SpawnProtocol.class, actorType));
  }

  public static <T> Key<SpawnProtocolProvider<T>> spawnProtocolProviderKey(Class<T> actorType) {
    return (Key<SpawnProtocolProvider<T>>) Key.get(Types.newParameterizedType(SpawnProtocolProvider.class, actorType));
  }

  public static <T> Key<SpawnCommandConverter<T>> spawnCommandConverterKey(Class<T> actorType) {
    return (Key<SpawnCommandConverter<T>>) Key.get(Types.newParameterizedType(SpawnCommandConverter.class, actorType));
  }

  public static <T> Key<ActorRef<T>> actorRefKey(Class<T> actorType) {
    return (Key<ActorRef<T>>) Key.get(Types.newParameterizedType(ActorRef.class, actorType));
  }
}
