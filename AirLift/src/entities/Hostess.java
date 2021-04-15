package entities;

 import sharedRegions.DepartureAirport;

/**
 *   Barber thread.
 *
 *   It simulates the barber life cycle.
 *   Static solution.
 */

public class Hostess extends Thread
{
  /**
   *  Barber identification.
   */

   private int hostessId;

  /**
   *  Barber state.
   */

   private int hostessState;

  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport departAirport;

  /**
   *   Instantiation of a barber thread.
   *
   *     @param name thread name
   *     @param barberId barber id
   *     @param bShop reference to the barber shop
   */

   public Hostess (String name, int hostessId, DepartureAirport departAirport)
   {
      super (name);
      this.hostessId = hostessId;
      hostessState = HostessStates.WAITFORFLIGHT;
      this.departAirport = departAirport;
   }

  /**
   *   Set barber id.
   *
   *     @param id barber id
   */

   public void setHostessId (int id)
   {
      hostessId = id;
   }

  /**
   *   Get barber id.
   *
   *     @return barber id
   */

   public int getHostessId ()
   {
      return hostessId;
   }

  /**
   *   Set barber state.
   *
   *     @param state new barber state
   */

   public void setHostessState (int state)
   {
      hostessState = state;
   }

  /**
   *   Get barber state.
   *
   *     @return barber state
   */

   public int getHostessState ()
   {
      return hostessState;
   }

  /**
   *   Life cycle of the barber.
   */

//   @Override
//   public void run ()
//   {
//      int customerId;                                      // customer id
//      boolean endOp;                                       // flag signaling end of operations
//
//      while (true)
//      { endOp = bShop.goToSleep ();                        // the barber sleeps while waiting for a customer to service
//        if (endOp) break;                                  // check for end of operations
//        customerId = bShop.callACustomer ();               // the barber has waken up and calls next customer
//        cutHair ();                                        // the barber cuts the customer hair
//        bShop.receivePayment (customerId);                 // the barber finishes his service and receives payment for it
//      }
//   }

  /**
   *  Cutting the customer hair.
   *
   *  Internal operation.
   */

   private void checkDocuments()
   {
      try
      { sleep ((long) (1 + 100 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
}
