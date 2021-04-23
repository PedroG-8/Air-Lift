package entities;

import genclass.GenericIO;
import sharedRegions.*;

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
   private final Plane plane;

  /**
   *   Instantiation of a customer thread.
   *
   *     @param name thread name
   *     @param customerId customer id
   *     @param bShop reference to the barber shop
   *     @param nIter number of iterations of the customer life cycle
   */

   public Passenger (String name, int passengerId, DepartureAirport departAirport, Plane plane)
   {
      super (name);
      this.passengerId = passengerId;
      passengerState = PassengerStates.GOINGTOAIRPORT;
      this.departAirport = departAirport;
      this.plane = plane;
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
//	   while (true) {
//		   boolean shown = departAirport.waitInQueue();
//		   if (shown) break;
//	   }
	   departAirport.waitInQueue();
	   departAirport.showDocuments();
	   plane.boardThePlane();
	   plane.leaveThePlane();
//	   travelToAirport();
//
//	   while(!departAirport.waitInQueue()) 
//		   showDocuments();
		   
   }

  /**
   *  Living normal life.
   *
   *  Internal operation.
   */

   private void travelToAirport()
   {
      try
      { 
    	  sleep ((long) (1 + 500 * Math.random ()));
      }
      catch (InterruptedException e) {}
      GenericIO.writelnString("Passenger " + passengerId + " is in the airport!");
   }

  /**
   *  Showing documents.
   *
   *  Internal operation.
   */

//   private void showDocuments()
//   {
//      try
//      { 
//    	  GenericIO.writelnString("Showing documents");
//    	  sleep ((long) (1 + 20 * Math.random ()));
//      }
//      catch (InterruptedException e) {}
//   }
}
