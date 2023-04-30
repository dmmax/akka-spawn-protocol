# Akka Spawn Protocol

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

Uses Spawn Protocol to create [Akka](https://akka.io/)'s Actors from outside. It can be applied at any level of actors' hierarchy

## Example of usage

Create a spawn protocol

```java
// Preconditions
akka.actor.ActorSystem classicActorSystem=akka.actor.ActorSystem.create();
ActorSystem<Void> actorSystem=Adapter.toTyped(classicActorSystem);
Scheduler scheduler=actorSystem.scheduler();
// Actor-specific configuration
Duration askTimeout=Duration.ofSeconds(1);
ActorRef<SpawnerActor.Command>spawnerActor=actorSystem.systemActorOf(SpawnerActor.create(),"rootActor",Props.empty());
// Create the spawn actor
SpawnProtocol<SpawnerActor.Command>spawnProtocol=new SpawnProtocol<>(scheduler,askTimeout,spawnerActor,SpawnActorCommandWrapper::new);
```

Create actors using SpawnProtocol

```java
SpawnProtocol<SpawnerActor.Command>spawnProtocol=...;
// Create a child actor
SpawnActorInfo<Ping> pingActorInfo=new SpawnActorInfo<>(PingActor.create(),ActorCreationStrategy.unique("pinger"));
ActorRef<Ping> pingActor=spawnPtocol.create(pingActorInfo);
```

## Available extensions

* [Guice integration](extensions/guice/README.md)

## Todo

I have a list of [issues](https://github.com/dmmax/akka-spawn-protocol/issues) which I am planning to work in the nearest future. Feel free
to add your own ideas there.