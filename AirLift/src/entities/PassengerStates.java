package entities;

/**
 *    Definition of the internal states of the passenger during his life cycle.
 */

public final class PassengerStates
{
  /**
   *   The passenger takes the bus to go to the departure airport.
   */

   public static final int GOINGTOAIRPORT = 0;

  /**
   *   The passenger queue at the boarding gate waiting for the flight to be announced.
   */

   public static final int INQUEUE = 1;

  /**
   *   The passenger flies to the destination airport.
   */

   public static final int INFLIGHT = 2;

  /**
   *   The passenger arrives at the destination airport, disembarks and leaves the airport.
   */

   public static final int ATDESTINATION = 3;

  /**
   *   It can not be instantiated.
   */

   private PassengerStates ()
   { }
   
}
