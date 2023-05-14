# Extension of Spawn Protocol for Akka using Guice

## Setup

[![Maven Central](https://img.shields.io/maven-central/v/me.dmmax.akka/akka-spawn-protocol-guice.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/me.dmmax.akka/akka-spawn-protocol-guice)

```groovy
dependencies {
    implementation(
            'me.dmmax.akka:akka-spawn-protocol:1.0'
    )
    // ... other dependencies
}
```

## Examples

First, you need to register the module of SpawnProtocol 
```java
class MyModule extends AbstractModule {
  
    @Override
    protected void configure() {
      // Be sure that ActorRef<MyActor.Command> is already available in the Guice context
      install(new RegisterSpawnProtocolModule<>(MyActor.Command.class, MyActor.SpawnActorCommandWrapper::new));
    }
}
```

after that, you can inject the `SpawnProtocol` and use it to spawn other actors
```java
class AnotherModule extends AbstractModule {
  
    @Singleton
    @Provides
    ActorRef<PingActor.Ping> pingActor(SpawnProtocol<MyActor.Command> spawner) {
      SpawnActorInfo<PingActor.Ping> pingActorInfo = new SpawnActorInfo<>(PingActor.create(), ActorCreationStrategy.sequence("pinger"));
      return spawner.createActor(pingActorInfo);
    }
}
```