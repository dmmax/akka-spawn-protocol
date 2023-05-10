# Akka Spawn Protocol

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

Uses Spawn Protocol to create [Akka](http://akka.io/)'s Actors from outside. It can be applied at any level of the actors' hierarchy.

I was inspired by [Akka's spawn protocol](https://doc.akka.io/japi/akka/current/akka/actor/typed/SpawnProtocol$.html) but I think that any actor could be a spawner if the actor implements a method to spawn the child actors that is why I decided to extend the initial idea.

## Example of usage

Create a spawn protocol

```java
// Preconditions
akka.actor.ActorSystem classicActorSystem = akka.actor.ActorSystem.create();
ActorSystem<Void> actorSystem = Adapter.toTyped(classicActorSystem);
Scheduler scheduler = actorSystem.scheduler();
// Actor-specific configuration
Duration askTimeout = Duration.ofSeconds(1);
ActorRef<SpawnerActor.Command> spawnerActor = actorSystem.systemActorOf(SpawnerActor.create(), "rootActor", Props.empty());
// Create the spawn actor
SpawnProtocol<SpawnerActor.Command> spawnProtocol = new SpawnProtocol<>(scheduler, askTimeout, spawnerActor, SpawnActorCommandWrapper::new);
```

In addition, you need to handle the wrapper message inside the spawner actor
```java
private <CHILD> Behavior<Command> onSpawnActor(SpawnActorCommandWrapper<CHILD> wrapper) {
  SpawnProtocols.spawnChildActor(getContext(), wrapper.spawnActorCommand());
  return this;
}
```

Now, it is pssible to create child actors using the SpawnProtocol

```java
SpawnProtocol<SpawnerActor.Command>spawnProtocol = ...;
// Create a child actor
SpawnActorInfo<Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.unique("pinger"));
ActorRef<Ping> pingActor = spawnPtocol.create(pingActorInfo);
```

## Available extensions

* [Guice integration](akka-spawn-protocol-guice/README.md)

## Todo

I have a list of [issues](https://github.com/dmmax/akka-spawn-protocol/issues) which I am planning to work in the nearest future. Feel free
to add your own ideas there.
