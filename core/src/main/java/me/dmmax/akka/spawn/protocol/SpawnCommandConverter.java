package me.dmmax.akka.spawn.protocol;

/**
 * Converts a {@link SpawnActorCommand} child command to a message that can be sent to the parent actor
 *
 * @param <P> - parent actor message type
 */
public interface SpawnCommandConverter<P> {

  /**
   * Converts a {@link SpawnActorCommand} child command to a message that can be sent to the parent actor
   *
   * @param spawnActorCommand – child spawn command
   * @param <C> - child actor message type
   * @return message that can be sent to the parent actor
   */
  <C> P toActorSpawnMessage(SpawnActorCommand<C> spawnActorCommand);
}
