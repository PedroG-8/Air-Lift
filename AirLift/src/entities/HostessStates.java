package entities;

/**
 *    Definition of the internal states of the hostess during her life cycle.
 */

public final class HostessStates {
	
   /**
    *   The hostess initial/final state. She is waiting for the pilot to inform her that plane has parked at the departure gate
    */

   public static final int WAITFORFLIGHT = 0;

   /**
    *   The hostess is waiting for a passenger in the Queue
    */

   public static final int WAITFORPASSENGER = 1;
   
   /**
    *   The hostess checks passenger documents
    */

   public static final int CHECKPASSENGER = 2;
   
   /**
    *   Transition state where the hostess is now ready to fly
    */

   public static final int READYTOFLY = 3;
    
   /**
    *   It can not be instantiated.
    */

   private HostessStates ()
   { }
   
}
