# Extension of Spawn Protocol for Akka using Guice

## Examples

First, you need to register the module of SpawnProtocol 
```java
class MyModule extends AbstractModule {
  
    @Override
    protected void configure() {
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