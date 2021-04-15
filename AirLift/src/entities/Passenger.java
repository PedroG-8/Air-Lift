package entities;

import sharedRegions.DepartureAirport;

/**
 *   Customer thread.
 *
 *   It simulates the customer life cycle.
 *   Static solution.
 */

public class Passenger extends Thread
{
  /**
   *  Customer identification.
   */

   private int passengerId;

  /**
   *  Customer state.
   */

   private int passengerState;

  /**
   *  Reference to the barber shop.
   */

   private final DepartureAirport departAirport;

  /**
   *   Instantiation of a customer thread.
   *
   *     @param name thread name
   *     @param customerId customer id
   *     @param bShop reference to the barber shop
   *     @param nIter number of iterations of the customer life cycle
   */

   public Passenger (String name, int passengerId, DepartureAirport departAirport)
   {
      super (name);
      this.passengerId = passengerId;
      passengerState = PassengerStates.GOINGTOAIRPORT;
      this.departAirport = departAirport;
   }

  /**
   *   Set customer id.
   *
   *     @param id customer id
   */

   public void setPassengerId (int id)
   {
      passengerId = id;
   }

  /**
   *   Get customer id.
   *
   *     @return customer id
   */

   public int getPassengerId ()
   {
      return passengerId;
   }

  /**
   *   Set customer state.
   *
   *     @param state new customer state
   */

   public void setPassengerState (int state)
   {
      passengerState = state;
   }

  /**
   *   Get customer state.
   *
   *     @return customer state
   */

   public int getPassengerState ()
   {
      return passengerState;
   }

  /**
   *   Life cycle of the customer.
   */

   @Override
   public void run ()
   {
	   
	   travelToAirport();

	   while(!departAirport.waitInQueue()) 
		   showDocuments();
		   
   }

  /**
   *  Living normal life.
   *
   *  Internal operation.
   */

   private void travelToAirport()
   {
      try
      { sleep ((long) (1 + 40 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }

  /**
   *  Showing documents.
   *
   *  Internal operation.
   */

   private void showDocuments()
   {
      try
      { sleep ((long) (1 + 10 * Math.random ()));
      }
      catch (InterruptedException e) {}
   }
}
