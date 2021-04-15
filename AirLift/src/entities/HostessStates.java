package entities;

/**
 *    Definition of the internal states of the barber during his life cycle.
 */

public final class HostessStates
{
  /**
   *   The barber is resting while waiting for a customer.
   */

   public static final int WAITFORFLIGHT = 0;

  /**
   *   The barber is cutting some customer hair.
   */

   public static final int WAITFORPASSENGER = 1;
   
   /**
    *   The barber is cutting some customer hair.
    */

   public static final int CHECKPASSENGER = 2;
   
   /**
    *   The barber is cutting some customer hair.
    */

   public static final int READYTOFLY = 3;
    
  /**
   *   It can not be instantiated.
   */

   private HostessStates ()
   { }
}
