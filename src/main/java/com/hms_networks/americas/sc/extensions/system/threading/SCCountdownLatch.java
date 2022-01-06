package com.hms_networks.americas.sc.extensions.system.threading;

/**
 * Class implementation of a CountDownLatch for Java 1.4.
 *
 * @since 1.4.0
 * @author HMS Networks, MU Americas Solution Center
 */
public class SCCountdownLatch {

  /** The count of the latch. */
  private int count;

  /**
   * Constructs a new {@code SCCountdownLatch} initialized with the given count.
   *
   * @param count the number of times {@link #countDown} must be invoked before threads can pass
   *     through {@link #await}.
   * @throws IllegalArgumentException if {@code count} is negative.
   */
  public SCCountdownLatch(int count) {
    this.count = count;
  }

  /**
   * Waits for the latch to count down to zero, unless the thread is interrupted.
   *
   * @throws InterruptedException if the current thread is interrupted
   */
  public synchronized void await() throws InterruptedException {
    if (count > 0) {
      wait();
    }
  }

  /** Decrements the count of the latch, releasing all waiting threads if the count reaches zero. */
  public synchronized void countDown() {
    count--;
    if (count == 0) {
      this.notifyAll();
    }
  }
}
