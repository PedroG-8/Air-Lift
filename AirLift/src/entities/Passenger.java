package entities;

import genclass.GenericIO;
import sharedRegions.*;

/**
 *   Passenger thread.
 *
 *   It simulates the passenger life cycle.
 */

public class Passenger extends Thread 
{
  /**
   *  Passenger identification.
   */

   private int passengerId;

  /**
   *  Passenger state.
   */

   private int passengerState;

  /**
   *  Reference to the departure airport.
   */

   private final DepartureAirport departAirport;
   
  /**
   *  Reference to the plane.
   */
   
   private final Plane plane;

  /**
   *   Instantiation of a Passenger thread.
   *
   *     @param name thread name
   *     @param passengerId passenger id
   *     @param departAirport reference to the Departure Airport
   *     @param plane reference to the Plane
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
   *   Set passenger id.
   *
   *     @param id passenger id
   */

   public void setPassengerId (int id)
   {
      passengerId = id;
   }

  /**
   *   Get passenger id.
   *
   *     @return passenger id
   */

   public int getPassengerId ()
   {
      return passengerId;
   }

  /**
   *   Set passenger state.
   *
   *     @param state new passenger state
   */

   public void setPassengerState (int state)
   {
      passengerState = state;
   }

  /**
   *   Get passenger state.
   *
   *     @return passenger state
   */

   public int getPassengerState ()
   {
      return passengerState;
   }

  /**
   *   Life cycle of the passenger.
   */

   @Override
   public void run ()
   {
	   travelToAirport();
	   departAirport.waitInQueue();
	   departAirport.showDocuments();
	   plane.boardThePlane();
	   plane.waitForEndOfFlight();
	   plane.leaveThePlane();
   }

  /**
   *  Travel to Airport.
   *
   *  Internal operation.
   */

   private void travelToAirport()
   {
      try
      { 
    	  sleep ((long) (1 + 700 * Math.random ()));
      }
      catch (InterruptedException e) {}
      GenericIO.writelnString("Passenger " + passengerId + " is in the airport!");
   }
   
}
