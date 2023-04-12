package me.dmmax.akka.spawn.protocol;

/**
 * @param <P> - parent actor message type
 */
public interface SpawnCommandConverter<P> {

  /**
   * Converts a {@link SpawnActor} child command to a message that can be sent to the parent actor
   *
   * @param spawnActorCommand – child spawn command
   * @param <C> - child actor message type
   * @return message that can be sent to the parent actor
   */
  <C> P toActorSpawnMessage(SpawnActor<C> spawnActorCommand);
}
