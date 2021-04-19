package entities;

/**
 *    Definition of the internal states of the barber during his life cycle.
 */

public final class PilotStates
{
  /**
   *   The barber is resting while waiting for a customer.
   */

   public static final int ATTRANSFERGATE = 0;

  /**
   *   The barber is cutting some customer hair.
   */

   public static final int READYFORBOARDING = 1;
   
   /**
    *   The barber is cutting some customer hair.
    */

   public static final int WAITINGFORBOARDING = 2;
   
   /**
    *   The barber is cutting some customer hair.
    */

   public static final int FLYINGFORWARD = 3;
   
   /**
    *   The barber is cutting some customer hair.
    */

   public static final int DEBOARDING = 4;
   
   /**
    *   The barber is cutting some customer hair.
    */

   public static final int FLYINGBACK = 5;
    
  /**
   *   It can not be instantiated.
   */

   private PilotStates()
   { }
}
