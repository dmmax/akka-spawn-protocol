# Akka Spawn Protocol

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dmmax_akka-spawn-protocol&metric=coverage)](https://sonarcloud.io/summary/new_code?id=dmmax_akka-spawn-protocol)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=dmmax_akka-spawn-protocol&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=dmmax_akka-spawn-protocol)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=dmmax_akka-spawn-protocol&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=dmmax_akka-spawn-protocol)

Uses Spawn Protocol to create [Akka](http://akka.io/)'s Actors from outside. It can be applied at any level of the actors' hierarchy.

I was inspired by [Akka's spawn protocol](https://doc.akka.io/japi/akka/current/akka/actor/typed/SpawnProtocol$.html), but I think that any
actor could be a spawner if the actor implements a method to spawn the child actors that is why I decided to extend the initial idea.

## Setup

[![Maven Central](https://img.shields.io/maven-central/v/me.dmmax.akka/akka-spawn-protocol.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/me.dmmax.akka/akka-spawn-protocol)

```groovy
dependencies {
    implementation(
            'me.dmmax.akka:akka-spawn-protocol:1.1'
    )
    // ... other dependencies
}
```

### BOM

As an alternative approach you can use [BOM](https://docs.gradle.org/current/userguide/platforms.html) to manage the spawn protocol versions
in one place

```groovy
dependencies {
    implementation platform('me.dmmax.akka:akka-spawn-protocol-bom:1.1')
    // Don't need to specify the version for root and extensions
    implementation 'me.dmmax.akka:akka-spawn-protocol'
    implementation 'me.dmmax.akka:akka-spawn-protocol-guice'
    // ... other dependencies
}
```

BOM includes:

BOM           | Artifact
--------------|-------------------------
Akka spawn protocol modules | `me.dmmax.akka:akka-spawn-protocol-[module]`
Akka BOM | `com.typesafe.akka:akka-bom_2.12`

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
SpawnProtocol<SpawnerActor.Command> spawnProtocol = ...;
// Create a child actor
SpawnActorInfo<Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.unique("pinger"));
ActorRef<Ping> pingActor = spawnPtocol.create(pingActorInfo);
```

## Available extensions

* [Guice integration](akka-spawn-protocol-guice/README.md)

## Todo

I have a list of [issues](https://github.com/dmmax/akka-spawn-protocol/issues) which I am planning to work in the nearest future. Feel free
to add your own ideas there.
