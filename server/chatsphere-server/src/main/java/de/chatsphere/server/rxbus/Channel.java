package de.chatsphere.server.rxbus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.observables.ConnectableObservable;

/**
 * The channel is built upon thread safe RxJava publish relays and is used to send events to
 * subscribers of the channel. (@see <a href="https://github.com/JakeWharton/RxRelay">RxRelay</a>)
 */
public class Channel {

  private final Relay<Object> relay;
  private final SubscriptionManager subscriptionManager;

  /**
   * Initializes a new Channel.
   */
  Channel() {
    this.relay = PublishRelay.create().toSerialized();
    this.subscriptionManager = new SubscriptionManager();
  }

  public SubscriptionManager getSubscriptionManager() {
    return subscriptionManager;
  }

  /**
   * Sends an event through the channel.
   *
   * @param event the event
   */
  void post(Event event) {
    relay.accept(event);
  }

  /**
   * Gets a RxJava 2 Flowable using buffer as backpressure strategy. (@see <a
   * href="https://www.baeldung.com/rxjava-2-flowable">RxJava 2 Flowable</a>)
   *
   * @return the flowable.
   */
  public synchronized <T> Flowable<T> asFlowable(Class<T> clazz) {
    ConnectableObservable<T> connectableObservable = relay
      .ofType(clazz)
      .share()
      .publish();
    connectableObservable.connect();

    return connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
  }

  /**
   * Checks if the channel has subscribers.
   *
   * @return true/false
   */
  boolean hasListeners() {
    return relay.hasObservers();
  }
}
