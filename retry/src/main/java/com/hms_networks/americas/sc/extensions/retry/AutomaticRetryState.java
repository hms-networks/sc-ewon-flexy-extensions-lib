package com.hms_networks.americas.sc.extensions.retry;

/**
 * Class to represent the state of automatic retry code in a similar fashion to enums in Java 1.5+.
 *
 * @author HMS Networks, MU Americas Solution Center
 * @since 1.0.0
 */
public class AutomaticRetryState {

  /** Integer assigned to represent the pending state. */
  private static final int PENDING_INT = 0;

  /** Integer assigned to represent the running state. */
  private static final int RUNNING_INT = 1;

  /** Integer assigned to represent the error (retry) state. */
  private static final int ERROR_RETRY_INT = 2;

  /** Integer assigned to represent the error (stop) state. */
  private static final int ERROR_STOP_INT = 3;

  /** Integer assigned to represent the pending state. */
  private static final int FINISHED_INT = 4;

  /** Public instance of {@link AutomaticRetryState} representing pending state. */
  public static final AutomaticRetryState PENDING = new AutomaticRetryState(PENDING_INT);

  /** Public instance of {@link AutomaticRetryState} representing running state. */
  public static final AutomaticRetryState RUNNING = new AutomaticRetryState(RUNNING_INT);

  /** Public instance of {@link AutomaticRetryState} representing error (retry) state. */
  public static final AutomaticRetryState ERROR_RETRY = new AutomaticRetryState(ERROR_RETRY_INT);

  /** Public instance of {@link AutomaticRetryState} representing error (stop) state. */
  public static final AutomaticRetryState ERROR_STOP = new AutomaticRetryState(ERROR_STOP_INT);

  /** Public instance of {@link AutomaticRetryState} representing finished state. */
  public static final AutomaticRetryState FINISHED = new AutomaticRetryState(FINISHED_INT);

  /** Instance state integer. */
  private final int stateInt;

  /**
   * Private (internal) constructor for creating an instance of {@link AutomaticRetryState} with a
   * state integer.
   *
   * <p>Note: State integer shall be unique.
   *
   * @param stateInt integer to represent states
   */
  private AutomaticRetryState(int stateInt) {
    this.stateInt = stateInt;
  }
}
