package me.dmmax.akka.spawn.protocol.utils;

import java.util.function.Supplier;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Scheduler;

public class AkkaTestKitExtension implements BeforeAllCallback, AfterAllCallback {

  private final Supplier<ActorTestKit> testKitSupplier;
  private ActorTestKit _testKit;

  public AkkaTestKitExtension() {
    this(ActorTestKit::create);
  }

  public AkkaTestKitExtension(Supplier<ActorTestKit> testKitSupplier) {
    this.testKitSupplier = testKitSupplier;
  }

  @Override
  public void beforeAll(ExtensionContext context) {
    _testKit = testKitSupplier.get();
  }

  @Override
  public void afterAll(ExtensionContext context) {
    if (_testKit != null) {
      _testKit.shutdownTestKit();
    }
  }

  public ActorSystem<Void> actorSystem() {
    return testKit().system();
  }

  public Scheduler scheduler() {
    return testKit().scheduler();
  }

  public ActorTestKit testKit() {
    return _testKit;
  }
}
