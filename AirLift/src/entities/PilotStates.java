package entities;

/**
 *    Definition of the internal states of the pilot during his life cycle.
 */

public final class PilotStates
{
  /**
   *   The pilot initial/final transition state. He is getting ready to start the boarding process.
   */

   public static final int ATTRANSFERGATE = 0;

  /**
   *   Transition state, the pilot is now ready for boarding.
   */

   public static final int READYFORBOARDING = 1;
   
   /**
    *   The pilot is waiting for all passengers to board the plane.
    */

   public static final int WAITINGFORBOARDING = 2;
   
   /**
    *   The pilot is now flying to the destination airport.
    */

   public static final int FLYINGFORWARD = 3;
   
   /**
    *   The pilot is waiting for all passengers to deboard.
    */

   public static final int DEBOARDING = 4;
   
   /**
    *   The pilot is flying the plane back to the departure airport.
    */

   public static final int FLYINGBACK = 5;
    
  /**
   *   It can not be instantiated.
   */

   private PilotStates()
   { }
}
